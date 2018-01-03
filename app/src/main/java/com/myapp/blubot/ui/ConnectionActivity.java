package com.myapp.blubot.ui;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.myapp.blubot.BluBot;
import com.myapp.blubot.R;
import com.myapp.blubot.ui.fragments.BTConnectionFragment;
import com.myapp.blubot.ui.fragments.BTDisabledFragment;
import com.myapp.blubot.ui.fragments.BTUnavailableFragment;

public class ConnectionActivity extends BaseActivity implements ConnectionFragmentListener {

    private static final int STATUS_BLUETOOTH_NA = 0;
    private static final int STATUS_BLUETOOTH_OFF = 1;
    private static final int STATUS_BLUETOOTH_ON = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        // Check if bluetooth is available
        if (!isBluetoothAvailable())
            setFragmentView(STATUS_BLUETOOTH_NA);
        else if (!isBluetoothEnable())
            setFragmentView(STATUS_BLUETOOTH_OFF);
        else
            setFragmentView(STATUS_BLUETOOTH_ON);
    }

    private void setFragmentView(int status) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (status) {
            case STATUS_BLUETOOTH_NA:
                transaction.replace(R.id.activity_connection_layout, BTUnavailableFragment.newInstance()).commit();
                return;
            case STATUS_BLUETOOTH_OFF:
                transaction.replace(R.id.activity_connection_layout, BTDisabledFragment.newInstance()).commit();
                return;
            case STATUS_BLUETOOTH_ON:
                transaction.replace(R.id.activity_connection_layout, BTConnectionFragment.newInstance()).commitAllowingStateLoss();
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
    void onBluetoothTurnedOff() {
        setFragmentView(STATUS_BLUETOOTH_OFF);
    }

    @Override
    public boolean startBTDeviceDiscovery() {
        Log.e("ConnectionActivity", "Discovery started");
        return getBluetoothAdapter().startDiscovery();
    }

    @Override
    public void saveDeviceStartControl(BluetoothDevice device) {
        // Save the device in app context
        BluBot.setCurrentDevice(device);

        // Start Control activity
        Intent intent = new Intent(getApplicationContext(), ControlActivity.class);
        startActivity(intent);
    }
}
