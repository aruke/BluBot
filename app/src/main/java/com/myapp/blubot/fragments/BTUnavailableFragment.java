package com.myapp.blubot.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myapp.blubot.R;

public class BTUnavailableFragment extends Fragment {

    public static BTUnavailableFragment newInstance() {

        return new BTUnavailableFragment();
    }

    public BTUnavailableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bt_unavailable, container, false);
    }
}
