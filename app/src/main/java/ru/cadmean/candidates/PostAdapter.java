package ru.cadmean.candidates;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ru.cadmean.candidates.models.Post;

public class PostAdapter extends BaseAdapter {

    private ArrayList<Post> posts;
    private LayoutInflater inflater;
    private Context c;

    PostAdapter(Context c, ArrayList<Post> posts) {
        this.c = c;
        this.posts = posts;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int i) {
        return posts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;

        if (v == null)
            v = inflater.inflate(R.layout.row_post_item, viewGroup, false);

        Post post = posts.get(i);

        ((TextView)v.findViewById(R.id.row_post_title)).setText(post.getTitle());
        ((TextView)v.findViewById(R.id.row_post_description)).setText(post.getDescription());

        if (post.getPicture() != null && !post.getPicture().isEmpty()) {
            Uri photoUri = Uri.parse(post.getPicture());
            Glide.with(c).load(photoUri).into((ImageView) v.findViewById(R.id.row_post_image));
        }

//        ((TextView)v.findViewById(R.id.row_post_username)).setText(post.getUserId());

        return v;
    }
}
