package com.myapp.blubot;

/**
 * Project : BluBot
 * Created by Rajanikant Deshmukh on 11 Aug 2015.
 */
public interface ConnectionFragmentListener {

    void onBluetoothTurnedOn();
    boolean startBTDeviceDiscovery();
}
