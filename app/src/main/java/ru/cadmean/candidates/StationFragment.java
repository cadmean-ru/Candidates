package ru.cadmean.candidates;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ru.cadmean.candidates.models.PollingStation;
import ru.cadmean.candidates.models.PollingStationViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class StationFragment extends Fragment implements OnMapReadyCallback {

    static final String ARGUMENT_STATION_ID = "station_id";

    public StationFragment() {
        // Required empty public constructor
    }

    private View view;
    private GoogleMap map;

    private PollingStation station;

    private ProgressBar stationProgressbar;
    private TextView stationName;
    private TextView stationAddress;
    private TextView stationDescription;
    private FloatingActionButton directionsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_station, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        stationName = view.findViewById(R.id.station_name);
        stationAddress = view.findViewById(R.id.station_address);
        stationDescription = view.findViewById(R.id.station_description);
        stationProgressbar = view.findViewById(R.id.station_progressbar);
        directionsButton = view.findViewById(R.id.station_directions_button);

        SupportMapFragment mapFragment = new SupportMapFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.station_map_frame, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

        Bundle args = getArguments();
        if (args == null) return;

        final String id = args.getString(ARGUMENT_STATION_ID);

        PollingStationViewModel model = ViewModelProviders.of(getActivity()).get(PollingStationViewModel.class);
        model.getStations().observe(getActivity(), new Observer<ArrayList<PollingStation>>() {
            @Override
            public void onChanged(ArrayList<PollingStation> stations) {
                for (PollingStation s : stations) {
                    if (s.id.equals(id)) {
                        station = s;
                        showStationData();
                        addMarker();
                        break;
                    }
                }
            }
        });
    }

    private Marker stationPosition;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (stationPosition == null) addMarker();
    }

    private void addMarker() {
        if (station == null || map == null) return;
        LatLng position = new LatLng(station.coordinates.getLatitude(), station.coordinates.getLongitude());
        stationPosition = map.addMarker(new MarkerOptions().position(position));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15f));
    }


    private void showStationData() {
        stationProgressbar.setVisibility(View.GONE);
        stationName.setText(station.name);
        stationAddress.setText(station.address);
        stationDescription.setText(station.description);
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + station.address));
                startActivity(mapIntent);
            }
        });
    }
}
