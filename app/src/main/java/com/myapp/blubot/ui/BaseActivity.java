package com.myapp.blubot.ui;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Project: BluBot
 * Author: rk
 * Date: 03/01/18
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mBluetoothAvailable;

    private final BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.i(TAG, "onReceive: Bluetooth state turned off");
                        onBluetoothTurnedOff();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.i(TAG, "onReceive: Bluetooth state turning off");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.i(TAG, "onReceive: Bluetooth state turned on");
                        onBluetoothTurnedOn();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.i(TAG, "onReceive: Bluetooth state turning off");
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise Bluetooth Adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAvailable = mBluetoothAdapter != null;

        if (!mBluetoothAvailable) {
            Log.e(TAG, "onCreate: No Bluetooth Available.");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isBluetoothAvailable()) {
            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBluetoothStateReceiver, filter);
        }
    }

    abstract void onBluetoothTurnedOn();

    abstract void onBluetoothTurnedOff();

    @Override
    protected void onStop() {
        super.onStop();
        if (isBluetoothAvailable())
            unregisterReceiver(mBluetoothStateReceiver);
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public boolean isBluetoothAvailable() {
        return mBluetoothAvailable;
    }

    public boolean isBluetoothEnable() {
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }
}
