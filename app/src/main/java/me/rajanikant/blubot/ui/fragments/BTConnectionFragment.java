package me.rajanikant.blubot.ui.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import me.rajanikant.blubot.R;
import me.rajanikant.blubot.ui.ConnectionFragmentListener;
import me.rajanikant.blubot.ui.adapter.BluetoothDeviceAdapter;

public class BTConnectionFragment extends Fragment {

    private static final String LOGCAT = BTConnectionFragment.class.getName();

    private static final int STATE_DISCOVERY_IN_PROGRESS = 0;
    private static final int STATE_DISCOVERY_FINISHED = 1;

    @BindView(R.id.fragment_bt_connection_list)
    ListView btListView;
    @BindView(R.id.fragment_bt_connection_refresh_button)
    ImageButton refreshButton;
    @BindView(R.id.fragment_bt_connection_refresh_indicator)
    ProgressBar refreshIndicator;

    private ArrayList<BluetoothDevice> mDeviceList;
    private BluetoothDeviceAdapter mDeviceAdapter;
    private ConnectionFragmentListener mListener;

    // Create a BroadcastReceiver for discovery of bluetooth mDeviceList
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null || action.isEmpty())
                return;

            // When discovery finds a device
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array mDeviceAdapter to show in a ListView
                    if (device != null) {
                        mDeviceList.add(device);
                        mDeviceAdapter.notifyDataSetChanged();
                        Log.e(LOGCAT, device.getName() + "\n" + device.getAddress());
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    setViewState(STATE_DISCOVERY_IN_PROGRESS);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    setViewState(STATE_DISCOVERY_FINISHED);
                    break;
                default:
                    Log.e(LOGCAT, "Action : " + action + " is not bluetooth related.");
                    Toast.makeText(getActivity(), "Action : " + action + " is not bluetooth related.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public static BTConnectionFragment newInstance() {
        return new BTConnectionFragment();
    }

    public BTConnectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Initialize DeviceList and DeviceAdapter
        mDeviceList = new ArrayList<>();
        mDeviceAdapter = new BluetoothDeviceAdapter(getActivity(), mDeviceList);

        try {
            mListener = (ConnectionFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ConnectionFragmentListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bt_connection, container, false);
        ButterKnife.bind(this, rootView);

        btListView.setAdapter(mDeviceAdapter);

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        Context context = getContext();
        if (context != null) {
            context.registerReceiver(bluetoothReceiver, filter); // Unregister during onDestroy
        }

        setViewState(STATE_DISCOVERY_FINISHED);

        return rootView;
    }

    private void setViewState(int state) {
        switch (state) {
            case STATE_DISCOVERY_FINISHED: // No discovery in process
                refreshButton.setVisibility(View.VISIBLE);
                refreshIndicator.setVisibility(View.GONE);
                break;
            case STATE_DISCOVERY_IN_PROGRESS: // Discovery in process
                refreshButton.setVisibility(View.GONE);
                refreshIndicator.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        discoverDevices();

        Set<BluetoothDevice> bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        mDeviceAdapter.addAll(bondedDevices);
    }

    @OnClick(R.id.fragment_bt_connection_refresh_button)
    public void onRefreshButtonClicked() {
        discoverDevices();
    }

    @OnItemClick(R.id.fragment_bt_connection_list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setViewState(STATE_DISCOVERY_FINISHED);
        BluetoothDevice device = mDeviceList.get(position);
        mListener.startDeviceConnection(device);
    }

    private void discoverDevices() {
        // Clear old mDeviceList
        mDeviceList.clear();
        mDeviceAdapter.notifyDataSetChanged();

        // Start discovery // Need BLUETOOTH_ADMIN permission
        boolean discoveryStarted = mListener.startBTDeviceDiscovery();
        if (!discoveryStarted)
            Toast.makeText(getActivity(), "Bluetooth discovery failed.", Toast.LENGTH_LONG).show();
        Log.e(LOGCAT, "Discovery started");
        Toast.makeText(getActivity(), "Discovery started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Context context = getContext();
        if (context != null) {
            context.unregisterReceiver(bluetoothReceiver);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
