package ru.cadmean.candidates.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PollingStationViewModel extends ViewModel {
    private MutableLiveData<ArrayList<PollingStation>> stations;
    private MutableLiveData<ArrayList<PollingStation>> stationsByDistrict;

    public MutableLiveData<ArrayList<PollingStation>> getStations() {
        if (stations == null)
            stations = new MutableLiveData<>();
        loadStations();
        return stations;
    }

    public MutableLiveData<ArrayList<PollingStation>> getStations(int district) {
        if (stationsByDistrict == null)
            stationsByDistrict = new MutableLiveData<>();
        loadStations(district);
        return stationsByDistrict;
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void loadStations() {
        db.collection("pollings")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<PollingStation> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            list.add(new PollingStation(doc.getId(), doc.getData()));
                        }
                        stations.setValue(list);
                    }
                });
    }

    private void loadStations(int district) {
        db.collection("pollings")
                .whereEqualTo("district", district)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<PollingStation> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            list.add(new PollingStation(doc.getId(), doc.getData()));
                        }
                        stationsByDistrict.setValue(list);
                    }
                });
    }
}
