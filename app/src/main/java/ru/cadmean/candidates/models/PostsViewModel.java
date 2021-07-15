package ru.cadmean.candidates.models;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PostsViewModel extends ViewModel {

    private ArrayList<Post> posts;
    private MutableLiveData<ArrayList<Post>> mutablePosts;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String COLLECTION_NAME = "posts";

    public PostsViewModel() {
        db.collection(COLLECTION_NAME)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null)
                            setPosts(queryDocumentSnapshots);
                    }
                });
    }

    public MutableLiveData<ArrayList<Post>> getPosts() {
        if (posts == null)
            posts = new ArrayList<>();
        if (mutablePosts == null)
            mutablePosts = new MutableLiveData<>();
        loadPosts();
        return mutablePosts;
    }

    private void loadPosts() {
        db.collection(COLLECTION_NAME).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        setPosts(queryDocumentSnapshots);
                    }
                });
    }

    public void addPost(Post post) {
        db.collection(COLLECTION_NAME).add(post);
        posts.add(post);
        mutablePosts.setValue(posts);
    }

    private void setPosts(QuerySnapshot documentSnapshots) {
        ArrayList<Post> list = new ArrayList<>();
        for (DocumentSnapshot doc : documentSnapshots) {
            list.add(new Post(doc.getId(), doc.getData()));
        }
        posts = list;
        mutablePosts.setValue(posts);
    }
}
