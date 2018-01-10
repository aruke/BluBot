package me.rajanikant.blubot;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Project : BluBot
 * Created by Rajanikant Deshmukh on 11 Aug 2015.
 */
public class BluBot extends Application {

    //SerialPortService ID;
    private static final UUID DEVICE_UUID = UUID.randomUUID();

    private static BluetoothDevice currentDevice = null;
    private static OutputStream outputStream = null;
    private static InputStream inputStream = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void setCurrentDevice(BluetoothDevice currentDevice) {
        BluBot.currentDevice = currentDevice;
        connectToDevice();
    }

    private static void connectToDevice() {
        BluetoothSocket mmSocket;
        try {
            mmSocket = currentDevice.createRfcommSocketToServiceRecord(DEVICE_UUID);
            mmSocket.connect();

            outputStream = mmSocket.getOutputStream();
            inputStream = mmSocket.getInputStream();

        } catch (IOException e) {
            android.util.Log.e("BluBot", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static InputStream getInputStream() {
        return inputStream;
    }

    public static OutputStream getOutputStream() {
        return outputStream;
    }
}
