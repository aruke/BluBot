package com.myapp.blubot.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.myapp.blubot.ConnectionFragmentListener;
import com.myapp.blubot.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BTDisabledFragment extends Fragment {

    private static final int REQUEST_ENABLE_BT = 1123;
    private ConnectionFragmentListener mListener;

    @Bind(R.id.fragment_bt_disabled_on_button)
    Button turnBTOnButton;

    public static BTDisabledFragment newInstance() {
        return new BTDisabledFragment();
    }

    public BTDisabledFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bt_disabled, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.fragment_bt_disabled_on_button)
    public void turnBTOn(View view)
    {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        Log.e("BT DISABLED", "starting bluetooth enable intent");
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT)
        {
            // If user replies affirmative to bluetooth request then start discovery else finish
            if(resultCode==Activity.RESULT_OK) {
                Log.e("BT DISABLED", "Yes bluetooth is on and im passing control to activity");
                Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
                mListener.onBluetoothTurnedOn();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ConnectionFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ConnectionFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
