package com.myapp.blubot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.myapp.blubot.fragments.BTConnectionFragment;
import com.myapp.blubot.fragments.BTDisabledFragment;
import com.myapp.blubot.fragments.BTUnavailableFragment;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ConnectionActivity extends AppCompatActivity implements ConnectionFragmentListener{

    private static final int STATUS_BLUETOOTH_NA = 0;
    private static final int STATUS_BLUETOOTH_OFF = 1;
    private static final int STATUS_BLUETOOTH_ON = 2;

    BluetoothAdapter mBluetoothAdapter;

    private static final UUID DEVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard //SerialPortService ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        // Check if bluetooth is available
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            setFragmentView(STATUS_BLUETOOTH_NA);
            return;
        }

        // Check if bluetooth is off
        if (!mBluetoothAdapter.isEnabled())
        {
            // If Bluetooth is disabled then prompt user to start it
            setFragmentView(STATUS_BLUETOOTH_OFF);
            return;
        }

        // If everything is OK start searching for bluetooth devices
        setFragmentView(STATUS_BLUETOOTH_ON);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register for broadcasts on BluetoothAdapter state change
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister broadcast listeners
        unregisterReceiver(mReceiver);
    }

    private void setFragmentView(int status)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (status)
        {
            case STATUS_BLUETOOTH_NA:
                transaction.replace(R.id.activity_connection_layout, BTUnavailableFragment.newInstance()).commit();
                return;
            case STATUS_BLUETOOTH_OFF:
                transaction.replace(R.id.activity_connection_layout, BTDisabledFragment.newInstance()).commit();
                return;
            case STATUS_BLUETOOTH_ON:
                transaction.replace(R.id.activity_connection_layout, BTConnectionFragment.newInstance(null)).commit();
                return;
            default:
                throw new RuntimeException("Invalid fragment status.");
        }
    }

    @Override
    public void onBluetoothTurnedOn() {
        Log.e("ConnectionActivity", "Im activity im changing view to enabled bt view");
        setFragmentView(STATUS_BLUETOOTH_ON);
    }

    @Override
    public boolean startBTDeviceDiscovery() {
        Log.e("ConnectionActivity", "Discovery started");
        return mBluetoothAdapter.startDiscovery();
    }

    @Override
    public void saveDeviceStartControl(BluetoothDevice device) {
        // Save the device in app context
        BluBot.setCurrentDevice(device);

        // Start Control activity
        Intent intent = new Intent(getApplicationContext(), ControlActivity.class);
        startActivity(intent);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        setFragmentView(STATUS_BLUETOOTH_OFF);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Toast.makeText(ConnectionActivity.this, "Turning Bluetooth off...", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        setFragmentView(STATUS_BLUETOOTH_ON);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Toast.makeText(ConnectionActivity.this, "Turning Bluetooth on...", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };
}
