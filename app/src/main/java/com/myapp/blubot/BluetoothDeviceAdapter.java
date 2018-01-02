package com.myapp.blubot;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Project : BluBot
 * Created by Rajanikant Deshmukh on 11 Aug 2015.
 */
public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {

    public BluetoothDeviceAdapter(Context context, List<BluetoothDevice> objects) {
        super(context, R.layout.item_bluetooth_device, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BluetoothDevice device = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_bluetooth_device, parent, false);

        TextView deviceName = (TextView) convertView.findViewById(R.id.item_bluetooth_device_name);
        TextView deviceSsid = (TextView) convertView.findViewById(R.id.item_bluetooth_device_ssid);

        deviceName.setText(device.getName());
        deviceSsid.setText(device.getAddress());

        return convertView;
    }
}
