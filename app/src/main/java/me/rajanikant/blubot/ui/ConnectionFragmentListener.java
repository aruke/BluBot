package me.rajanikant.blubot.ui;

import android.bluetooth.BluetoothDevice;

/**
 * Project : BluBot
 * Created by Rajanikant Deshmukh on 11 Aug 2015.
 */
public interface ConnectionFragmentListener {

    boolean startBTDeviceDiscovery();

    void saveDeviceStartControl(BluetoothDevice device);
}
