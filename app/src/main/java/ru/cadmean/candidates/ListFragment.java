package ru.cadmean.candidates;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ru.cadmean.candidates.models.PollingStation;
import ru.cadmean.candidates.models.PollingStationViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {


    public ListFragment() {
        // Required empty public constructor
    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ViewModelProviders.of(getActivity()).get(PollingStationViewModel.class).getStations().observe(getActivity(), new Observer<ArrayList<PollingStation>>() {
            @Override
            public void onChanged(ArrayList<PollingStation> stations) {
                if (getActivity() == null || stations == null) return;

                view.findViewById(R.id.list_progressbar).setVisibility(View.GONE);
                ((ListView)view.findViewById(R.id.stations_list)).setAdapter(new StationsAdapter(getActivity(), stations, new StationsAdapter.OnStationSelectedListener() {
                    @Override
                    public void onStationSelected(PollingStation ps) {
                        Bundle b = new Bundle();
                        b.putString(StationFragment.ARGUMENT_STATION_ID, ps.id);
                        Navigation.findNavController(view).navigate(R.id.action_nav_list_to_stationFragment, b);
                    }
                }));
            }
        });
    }
}
