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

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import ru.cadmean.candidates.models.Post;
import ru.cadmean.candidates.models.PostsViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscussionFragment extends Fragment {


    public DiscussionFragment() {
        // Required empty public constructor
    }

    private View view;
    private FirebaseAuth auth;

    private ListView redditList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_discussion, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            Navigation.findNavController(view).navigate(R.id.action_nav_discussion_to_login2Fragment);
            return;
        }

        redditList = view.findViewById(R.id.reddit_list);

        ViewModelProviders.of(getActivity()).get(PostsViewModel.class).getPosts().observe(getActivity(), new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(ArrayList<Post> posts) {
                if (getActivity() == null || posts == null) return;

                if (posts.size() == 0) {
                    //TODO: add no posts message
                    return;
                }

                redditList.setAdapter(new PostAdapter(getActivity(), posts));
            }
        });


        view.findViewById(R.id.new_post_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_discussion_to_postFragment);
            }
        });
    }
}
