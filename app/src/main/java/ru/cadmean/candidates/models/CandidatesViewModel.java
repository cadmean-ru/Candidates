package ru.cadmean.candidates.models;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CandidatesViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Candidate>> candidates;

    public MutableLiveData<ArrayList<Candidate>> getCandidates() {
        if (candidates == null) {
            candidates = new MutableLiveData<>();
        }
        loadCandidates();
        return candidates;
    }

    public MutableLiveData<ArrayList<Candidate>> getCandidates(int district) {
        if (candidates == null) {
            candidates = new MutableLiveData<>();
        }
        loadCandidates(district);
        return candidates;
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void loadCandidates() {
        db.collection("candidates")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) return;

                        ArrayList<Candidate> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            list.add(new Candidate(doc.getId(), doc.getData()));
                        }

                        candidates.setValue(list);
                    }
                });
    }

    private void loadCandidates(int district) {
        db.collection("candidates")
                .whereEqualTo("district", district)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Candidate> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            list.add(new Candidate(doc.getId(), doc.getData()));
                        }

                        candidates.setValue(list);
                    }
                });
    }
}
