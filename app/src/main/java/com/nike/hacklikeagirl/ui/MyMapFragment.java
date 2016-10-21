package com.nike.hacklikeagirl.ui;

import com.nike.hacklikeagirl.R;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static com.nike.hacklikeagirl.R.id.map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MyMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyMapFragment newInstance(String param1, String param2) {
        MyMapFragment fragment = new MyMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mymap, container, false);
        FragmentManager fragmentManager = getChildFragmentManager();
        SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager
                .findFragmentById(map);
        supportMapFragment.getMapAsync(this);

        return view;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In
     * this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted
     * to install
     * it inside the SupportMapFragment. This method will only be triggered once the user
     * has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Geocoder geocoder = new Geocoder(getActivity());
        double latitude = 0;
        double longitude = 0;

        try {
            List<Address> geoResults = geocoder.getFromLocationName("<address goes here>", 1);
            while (geoResults.size()==0) {
                geoResults = geocoder.getFromLocationName("308 SW 2nd Ave., Portland, OR", 1);
            }
            if (geoResults.size()>0) {
                Address addr = geoResults.get(0);
                latitude = addr.getLatitude();
                longitude = addr.getLongitude();
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng hackathon = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(hackathon).title(getResources()
                .getString(R.string.map_marker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hackathon));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }
}
