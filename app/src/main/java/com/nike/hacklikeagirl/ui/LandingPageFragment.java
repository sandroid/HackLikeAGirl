package com.nike.hacklikeagirl.ui;

import com.nike.hacklikeagirl.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 */
public class LandingPageFragment extends Fragment {

    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private Button mVideoButton;


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

        //Find the +1 button
        mVideoButton = (Button) view.findViewById(R.id.plus_one_button);
        mVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenThreadVideoActivity.navigate(getActivity(), "http://cdn-vod-a.sesameonline"
                        + ".net/pd/p/1786071/sp/178607100/serveFlavor/entryId/0_dm4qkb4e/v/2/pv/1/flavorId/0_t7iwesxn/name/a.mp4", true);
            }
        });

        return view;
    }



}
