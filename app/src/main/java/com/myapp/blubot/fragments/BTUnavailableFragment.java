package com.myapp.blubot.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myapp.blubot.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BTUnavailableFragment extends Fragment {

    @BindView(R.id.fragment_bt_unavailable_github_link)
    TextView githubLink;

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
        View rootView = inflater.inflate(R.layout.fragment_bt_unavailable, container, false);
        ButterKnife.bind(this, rootView);
        githubLink.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        return rootView;
    }

    @OnClick(R.id.fragment_bt_unavailable_github_link)
    void onGithubLinkClicked(View view)
    {
        String url = getString(R.string.github_repo_url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
