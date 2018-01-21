package me.rajanikant.blubot.ui.fragments;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.rajanikant.blubot.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BTDisabledFragment extends Fragment {

    private static final int REQUEST_ENABLE_BT = 1123;

    @BindView(R.id.fragment_bt_disabled_on_button)
    Button turnBTOnButton;

    public static BTDisabledFragment newInstance() {
        return new BTDisabledFragment();
    }

    public BTDisabledFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bt_disabled, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.fragment_bt_disabled_on_button)
    public void turnBluetoothOn() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }
}
