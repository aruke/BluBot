package me.rajanikant.blubot.ui.fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.rajanikant.blubot.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BTUnavailableFragment extends Fragment {

    @BindView(R.id.fragment_bt_unavailable_github_link)
    TextView gitHubLink;
    @BindView(R.id.fragment_bt_unavailable_email)
    TextView emailLink;

    public static BTUnavailableFragment newInstance() {
        return new BTUnavailableFragment();
    }

    public BTUnavailableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bt_unavailable, container, false);
        ButterKnife.bind(this, rootView);
        gitHubLink.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        emailLink.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        return rootView;
    }

    @OnClick(R.id.fragment_bt_unavailable_github_link)
    void onGitHubLinkClicked() {
        String url = getString(R.string.github_repo_url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @OnClick(R.id.fragment_bt_unavailable_email)
    void onEmailDeveloperClicked() {
        String devEmail = getString(R.string.developer_email);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{devEmail});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }
}
