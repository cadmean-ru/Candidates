package ru.cadmean.candidates;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

import ru.cadmean.candidates.models.Post;
import ru.cadmean.candidates.models.PostsViewModel;


public class PostDialogFragment extends Fragment {


    public PostDialogFragment() {
    }

    private View view;

    private String picture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_dialog, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        (view.findViewById(R.id.done_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishPost();
            }
        });
    }

    private void publishPost() {
        Post post = new Post(
                ((EditText)view.findViewById(R.id.edit_title)).getText().toString(),
                ((EditText)view.findViewById(R.id.edit_description)).getText().toString(),
                picture,
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        ViewModelProviders.of(getActivity()).get(PostsViewModel.class).addPost(post);
    }
}
