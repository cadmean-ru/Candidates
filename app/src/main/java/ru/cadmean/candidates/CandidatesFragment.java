package ru.cadmean.candidates;


import android.os.Bundle;

import androidx.annotation.NonNull;
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

import ru.cadmean.candidates.models.Candidate;
import ru.cadmean.candidates.models.CandidatesViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class CandidatesFragment extends Fragment {


    public CandidatesFragment() {
        // Required empty public constructor
    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_candidates, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final CandidatesViewModel model = ViewModelProviders.of(getActivity()).get(CandidatesViewModel.class);
        model.getCandidates().observe(getActivity(), new Observer<ArrayList<Candidate>>() {
            @Override
            public void onChanged(ArrayList<Candidate> candidates) {
                if (getActivity() == null || candidates == null) return;

                view.findViewById(R.id.candidates_progressbar).setVisibility(View.GONE);
                ListView lv = view.findViewById(R.id.candidates_list);
                lv.setAdapter(new CandidateAdapter(getActivity(), candidates, new CandidateAdapter.OnCandidateSelectedListener() {
                    @Override
                    public void onCandidateSelected(int index, Candidate c) {
                        Bundle args = new Bundle();
                        args.putString(CandidateFragment.ARGUMENT_ID, c.id);
                        Navigation.findNavController(view).navigate(R.id.action_nav_candidates_to_nav_candidate, args);
                    }
                }));
            }
        });
    }
}
