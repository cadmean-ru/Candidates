package ru.cadmean.candidates;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;

import ru.cadmean.candidates.models.PollingStation;

public class StationsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<PollingStation> stations;

    private OnStationSelectedListener listener;

    StationsAdapter(Context c, ArrayList<PollingStation> stations, OnStationSelectedListener l) {
        this.stations = stations;
        listener = l;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public Object getItem(int i) {
        return stations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        View v = view;
        if (view == null)
            v = inflater.inflate(R.layout.polling_station, viewGroup, false);

        final PollingStation s = stations.get(i);

        ((TextView)v.findViewById(R.id.text_name)).setText(s.name);
        ((TextView)v.findViewById(R.id.text_address)).setText(s.address);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onStationSelected(s);
            }
        });

        return v;
    }

    public interface OnStationSelectedListener {
        void onStationSelected(PollingStation ps);
    }
}
