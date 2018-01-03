package com.myapp.blubot.ui.fragments;

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
import android.widget.ListView;
import android.widget.Toast;

import com.myapp.blubot.R;
import com.myapp.blubot.ui.BluetoothDeviceAdapter;
import com.myapp.blubot.ui.ConnectionActivity;
import com.myapp.blubot.ui.ConnectionFragmentListener;

import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class BTConnectionFragment extends Fragment {

    private static final String LOGCAT = BTConnectionFragment.class.getName();

    static ArrayList<BluetoothDevice> devices = new ArrayList<>();
    private BluetoothDeviceAdapter adapter;

    @BindView(R.id.fragment_bt_connection_list)
    ListView btListView;

    private ConnectionFragmentListener mListener;
    private ConnectionActivity mConnectionActivity;

    public static BTConnectionFragment newInstance() {
        return new BTConnectionFragment();
    }

    public BTConnectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bt_connection, container, false);
        ButterKnife.bind(this, rootView);

        adapter = new BluetoothDeviceAdapter(getActivity(), devices);
        btListView.setAdapter(adapter);

        mConnectionActivity = (ConnectionActivity) getActivity();

        Set<BluetoothDevice> pairedDevices = mConnectionActivity.getBluetoothAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            adapter.addAll(pairedDevices);
            adapter.notifyDataSetChanged();
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        discoverDevices();
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(bluetoothReceiver);
    }

    @OnItemClick(R.id.fragment_bt_connection_list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothDevice device = devices.get(position);
        mListener.saveDeviceStartControl(device);
    }

    private void discoverDevices() {
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(bluetoothReceiver, filter); // Unregister during onDestroy

        // Clear old devices
        devices.clear();

        // Start discovery // Need BLUETOOTH_ADMIN permission
        boolean discoveryStarted = mListener.startBTDeviceDiscovery();
        if (!discoveryStarted)
            Toast.makeText(getActivity(), "Bluetooth discovery failed.", Toast.LENGTH_LONG).show();
        Log.e(LOGCAT, "Discovery started");
        Toast.makeText(getActivity(), "Discovery started", Toast.LENGTH_SHORT).show();
    }

    // Create a BroadcastReceiver for discovery of bluetooth devices
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                devices.add(device);
                adapter.notifyDataSetChanged();
                android.util.Log.e(LOGCAT, device.getName() + "\n" + device.getAddress());
            } else {
                android.util.Log.e(LOGCAT, "Action : " + action + " is not bluetooth related.");
                Toast.makeText(getActivity(), "Action : " + action + " is not bluetooth related.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ConnectionFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ConnectionFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
