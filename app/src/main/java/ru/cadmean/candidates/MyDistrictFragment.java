package ru.cadmean.candidates;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import ru.cadmean.candidates.models.Candidate;
import ru.cadmean.candidates.models.CandidatesViewModel;
import ru.cadmean.candidates.models.PollingStation;
import ru.cadmean.candidates.models.PollingStationViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyDistrictFragment extends Fragment {


    public MyDistrictFragment() {
        // Required empty public constructor
    }

    private View view;
    private LinearLayout candidatesLayout;
    private ProgressBar candidatesProgressbar;

    private LinearLayout stationsLayout;
    private ProgressBar stationsProgressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_district, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        candidatesLayout = view.findViewById(R.id.candidates_layout);
        candidatesProgressbar = view.findViewById(R.id.candidates_progressbar);

        stationsLayout = view.findViewById(R.id.stations_layout);
        stationsProgressbar = view.findViewById(R.id.stations_progressbar);

        int district = getActivity().getPreferences(Context.MODE_PRIVATE).getInt("district", 0);

        if (district == 0) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(getString(R.string.message_select_district))
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Navigation.findNavController(view).navigate(R.id.action_nav_district_to_nav_map);
                        }
                    })
                    .create()
                    .show();
            return;
        } else {
            ((TextView) view.findViewById(R.id.district_title)).setText(getString(R.string.title_district, district));
        }

        final CandidatesViewModel model = ViewModelProviders.of(getActivity()).get(CandidatesViewModel.class);
        model.getCandidates(district).observe(getActivity(), new Observer<ArrayList<Candidate>>() {
            @Override
            public void onChanged(ArrayList<Candidate> candidates) {
                if (getActivity() == null || candidates == null) return;

                if (candidates.size() == 0) {
                    showNoCandidates();
                    return;
                }

                showCandidates(candidates);
            }
        });

        final PollingStationViewModel model1 = ViewModelProviders.of(getActivity()).get(PollingStationViewModel.class);
        model1.getStations(district).observe(getActivity(), new Observer<ArrayList<PollingStation>>() {
            @Override
            public void onChanged(ArrayList<PollingStation> stations) {
                if (getActivity() == null || stations == null) return;

                if (stations.size() == 0) {
                    showNoStations();
                    return;
                }

                showStations(stations);
            }
        });
    }

    private void showNoCandidates() {
        candidatesProgressbar.setVisibility(View.GONE);
        TextView text = new TextView(getContext());
        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        text.setText(getString(R.string.no_candidates_in_district));
        candidatesLayout.removeAllViews();
        candidatesLayout.addView(text);
    }

    private void showCandidates(ArrayList<Candidate> candidates) {
        candidatesProgressbar.setVisibility(View.GONE);
        candidatesLayout.removeAllViews();

        LayoutInflater inflater = getActivity().getLayoutInflater();

        int size;
        if (candidates.size() < 5) {
            view.findViewById(R.id.more_candidates).setVisibility(View.GONE);
            size = candidates.size();
        } else {
            size = 5;
        }

        for (int i = 0; i < size; ++i) {
            final Candidate candidate = candidates.get(i);
            View v = inflater.inflate(R.layout.candidate, candidatesLayout, false);
            ((TextView)v.findViewById(R.id.name_text)).setText(String.format("%s %s %s", candidate.surname, candidate.name, candidate.middleName));
            ((TextView)v.findViewById(R.id.desc_text)).setText(candidate.shortDescription);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString(CandidateFragment.ARGUMENT_ID, candidate.id);
                    Navigation.findNavController(view).navigate(R.id.action_nav_district_to_nav_candidate, args);
                }
            });
            candidatesLayout.addView(v);
        }
    }

    private void showNoStations() {
        stationsProgressbar.setVisibility(View.GONE);
        TextView text = new TextView(getContext());
        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        text.setText(getString(R.string.no_stations_in_district));
        stationsLayout.removeAllViews();
        stationsLayout.addView(text);
    }

    private void showStations(ArrayList<PollingStation> stations) {
        stationsProgressbar.setVisibility(View.GONE);
        stationsLayout.removeAllViews();

        int size;
        if (stations.size() < 5) {
            view.findViewById(R.id.more_polling).setVisibility(View.GONE);
            size = stations.size();
        } else {
            size = 5;
        }

        StationsAdapter adapter = new StationsAdapter(getActivity(), stations, new StationsAdapter.OnStationSelectedListener() {
            @Override
            public void onStationSelected(PollingStation ps) {
                Bundle b = new Bundle();
                b.putString(StationFragment.ARGUMENT_STATION_ID, ps.id);
                Navigation.findNavController(view).navigate(R.id.action_nav_district_to_stationFragment, b);
            }
        });

        for (int i = 0; i < size; ++i) {
            stationsLayout.addView(adapter.getView(i, null, stationsLayout));
        }
    }
}
