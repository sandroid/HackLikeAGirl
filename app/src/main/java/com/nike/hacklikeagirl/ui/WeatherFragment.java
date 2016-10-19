package com.nike.hacklikeagirl.ui;


import com.nike.hacklikeagirl.R;
import com.nike.hacklikeagirl.network.WeatherNao;
import com.nike.mynike.model.generated.Weather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rx.Observable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    private static final String ZIP_CODE = "zip_code";

    private String mZipCode;


    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String zipCode) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ZIP_CODE, zipCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mZipCode = getArguments().getString(ZIP_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        String zipCode = getArguments().getString(ZIP_CODE);
        TextView weather = (TextView)view.findViewById(R.id.weather);
        Observable<Weather> weatherObservable =  WeatherNao.getWeather(getActivity(),
                zipCode);
        weather.setText(weatherObservable.toString());

        return view;

    }

}
