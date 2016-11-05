package com.nike.hacklikeagirl.ui;


import com.nike.hacklikeagirl.R;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

public class ThreadVideoFragment extends Fragment {

    private VideoView mVideoView;
    private MediaController mController;

    private static final String ARG_VIDEO_URL = "videoUrl";

    private String mVideoUrl;

    public static ThreadVideoFragment newInstance(String videoUrl) {
        ThreadVideoFragment fragment = new ThreadVideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VIDEO_URL, videoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    public ThreadVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVideoUrl = getArguments().getString(ARG_VIDEO_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_thread_video, container, false);

        mVideoView = (VideoView) view.findViewById(R.id.product_content_video_view);
        mController = new MediaController(getActivity());
        mVideoView.setMediaController(mController);
        mController.setMediaPlayer(mVideoView);
        mVideoView.setVisibility(View.VISIBLE);
        mVideoView.requestFocus();
        mVideoView.setVideoURI(Uri.parse(mVideoUrl));
        mVideoView.start();

        return view;
    }

}