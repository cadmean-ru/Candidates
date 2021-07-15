package ru.cadmean.candidates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.cadmean.candidates.models.Candidate;

class CandidateAdapter extends BaseAdapter {

    private Context c;
    private ArrayList<Candidate> candidates;
    private LayoutInflater inflater;

    private OnCandidateSelectedListener listener;

    CandidateAdapter(Context c, ArrayList<Candidate> candidates, OnCandidateSelectedListener listener) {
        this.c = c;
        this.candidates = candidates;
        this.listener = listener;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return candidates.size();
    }

    @Override
    public Candidate getItem(int i) {
        return candidates.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View oldView, ViewGroup viewGroup) {
        View v = oldView;

        if (v == null) {
            v = inflater.inflate(R.layout.candidate, viewGroup, false);
        }

        final Candidate candidate = candidates.get(i);

        ((TextView)v.findViewById(R.id.name_text)).setText(String.format("%s %s %s", candidate.surname, candidate.name, candidate.middleName));
        ((TextView)v.findViewById(R.id.desc_text)).setText(candidate.shortDescription);

        final int index = i;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCandidateSelected(index, candidate);
            }
        });

        return v;
    }

    interface OnCandidateSelectedListener {
        void onCandidateSelected(int index, Candidate c);
    }
}
