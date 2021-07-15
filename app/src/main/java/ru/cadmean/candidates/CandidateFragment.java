package ru.cadmean.candidates;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.cadmean.candidates.models.Candidate;
import ru.cadmean.candidates.models.CandidatesViewModel;


public class CandidateFragment extends Fragment {

    @Deprecated static final String ARGUMENT_INDEX = "index";
    static final String ARGUMENT_ID = "id";

    public CandidateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_candidate, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        if (args == null) return;

        String id = args.getString(ARGUMENT_ID);

        CandidatesViewModel model = ViewModelProviders.of(getActivity()).get(CandidatesViewModel.class);

        ArrayList<Candidate> candidates = model.getCandidates().getValue();

        if (candidates == null) return;

        Candidate candidate = null;

        for (Candidate c : candidates) {
            if (c.id.equals(id)) {
                candidate = c;
                break;
            }
        }

        if (candidate == null) return;

        ((TextView)view.findViewById(R.id.surname_text)).setText(candidate.surname);
        ((TextView)view.findViewById(R.id.name_text)).setText(candidate.name + " " + candidate.middleName);
        ((TextView)view.findViewById(R.id.party_text)).setText(candidate.party);
        ((TextView)view.findViewById(R.id.year_text)).setText(getString(R.string.year_label, candidate.year));
        ((TextView)view.findViewById(R.id.residence_text)).setText(getString(R.string.residence_label, candidate.placeOfResidence));
        ((TextView)view.findViewById(R.id.work_text)).setText(getString(R.string.work_label, candidate.work));
        ((TextView)view.findViewById(R.id.position_text)).setText(getString(R.string.work_position_label, candidate.workPosition));
        ((TextView)view.findViewById(R.id.biography_text)).setText(candidate.description);
    }
}
