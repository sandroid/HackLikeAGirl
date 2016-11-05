package com.nike.hacklikeagirl.ui;

import com.nike.hacklikeagirl.R;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 */
public class LandingPageFragment extends Fragment {

    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private ImageButton mVideoButton;
    private ImageButton mVideoButton2;
    private ImageButton mVideoButton3;


    public LandingPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LandingPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LandingPageFragment newInstance() {
        LandingPageFragment fragment = new LandingPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing_page, container, false);

        getActivity().setTitle(getString(R.string.app_name));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color
                .parseColor("#bedb39")));

        mVideoButton = (ImageButton) view.findViewById(R.id.video1);
        mVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenThreadVideoActivity.navigate(getActivity(), "http://cdn-vod-a.sesameonline"
                        + ".net/pd/p/1786071/sp/178607100/serveFlavor/entryId/0_dm4qkb4e/v/2/pv/1/flavorId/0_t7iwesxn/name/a.mp4", true);
            }
        });

        mVideoButton2 = (ImageButton) view.findViewById(R.id.video2);
        mVideoButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenThreadVideoActivity.navigate(getActivity(), "http://video.cdn.schooltube.com/09d9f05c-d4df-424f-ba13-0898da2f157f/v/11/cf/bd/cc/f5/af/11cfbdcc-f5af-8be7-e6c7-9b6bafe576ca.mp4", true);
            }
        });

        mVideoButton3 = (ImageButton) view.findViewById(R.id.video3);
        mVideoButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenThreadVideoActivity.navigate(getActivity(), "http://video.cdn.schooltube.com/09d9f05c-d4df-424f-ba13-0898da2f157f/v/11/cf/bd/cc/f5/af/11cfbdcc-f5af-8be7-e6c7-9b6bafe576ca.mp4", true);
            }
        });


        return view;
    }



}
