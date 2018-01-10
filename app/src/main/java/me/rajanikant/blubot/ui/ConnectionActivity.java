package me.rajanikant.blubot.ui;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import me.rajanikant.blubot.R;
import me.rajanikant.blubot.ui.fragments.BTConnectionFragment;
import me.rajanikant.blubot.ui.fragments.BTDisabledFragment;
import me.rajanikant.blubot.ui.fragments.BTUnavailableFragment;

public class ConnectionActivity extends BaseActivity implements ConnectionFragmentListener {

    private static final String TAG = "ConnectionActivity";

    private static final int STATUS_BLUETOOTH_NA = 0;
    private static final int STATUS_BLUETOOTH_OFF = 1;
    private static final int STATUS_BLUETOOTH_ON = 2;

    private BluetoothDevice mSelectedDevice;

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
    public void startDeviceConnection(BluetoothDevice device) {
        // Save the device in app context
        //BluBot.setCurrentDevice(device);
        mSelectedDevice = device;

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBondStateReceiver, filter);

        // Cancel discovery to make connection faster
        getBluetoothAdapter().cancelDiscovery();

        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                device.createBond();
            } else {
                Method method;
                try {
                    method = device.getClass().getMethod("createBond", (Class[]) null);
                    method.invoke(device, (Object[]) null);
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, "startDeviceConnection: ", e);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "startDeviceConnection: ", e);
                } catch (InvocationTargetException e) {
                    Log.e(TAG, "startDeviceConnection: ", e);
                }
            }
        } else if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            startControlActivity(device);
        }
    }

    private BroadcastReceiver mBondStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);

            if (mSelectedDevice != null && device.getAddress().equals(mSelectedDevice.getAddress())) {
                switch (bondState) {
                    case BluetoothDevice.BOND_BONDED:
                        startControlActivity(device);
                        break;
                    case BluetoothDevice.BOND_BONDING:
                    case BluetoothDevice.BOND_NONE:
                    default:
                }

            }
        }
    };

    private void startControlActivity(BluetoothDevice device) {
        Intent intent = new Intent(this, ControlActivity.class);
        intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Receiver is only registered when any device is selected
        if (mSelectedDevice != null)
            unregisterReceiver(mBondStateReceiver);
    }
}
