package com.myapp.blubot;

import android.app.Application;
import android.bluetooth.BluetoothDevice;

/**
 * Project : BluBot
 * Created by Rajanikant Deshmukh on 11 Aug 2015.
 */
public class BluBot extends Application{

    private static BluetoothDevice currentDevice = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void setCurrentDevice(BluetoothDevice currentDevice) {
        BluBot.currentDevice = currentDevice;
    }

    public static BluetoothDevice getCurrentDevice() {
        return currentDevice;
    }
}
