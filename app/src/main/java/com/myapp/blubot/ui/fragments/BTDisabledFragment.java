package com.myapp.blubot.ui.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.myapp.blubot.R;
import com.myapp.blubot.ui.ConnectionFragmentListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BTDisabledFragment extends Fragment {

    private static final int REQUEST_ENABLE_BT = 1123;

    private ConnectionFragmentListener mListener;

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
    public void turnBTOn() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        Log.e("BT DISABLED", "starting bluetooth enable intent");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            // If user replies affirmative to bluetooth request then start discovery else finish
//            if (resultCode == Activity.RESULT_OK) {
//                Log.e("BT DISABLED", "Yes bluetooth is on and im passing control to activity");
//                mListener.onBluetoothTurnedOn();
//            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        try {
//            mListener = (ConnectionFragmentListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implement ConnectionFragmentListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
