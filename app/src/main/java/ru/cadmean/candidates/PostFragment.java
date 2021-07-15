package ru.cadmean.candidates;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ru.cadmean.candidates.Utils.Permissions;
import ru.cadmean.candidates.models.Post;
import ru.cadmean.candidates.models.PostsViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {


    public PostFragment() {
        // Required empty public constructor
    }

    private static final int REQUEST_CODE = 2;

    private View view;
    private String uploadedImageLink;
    private EditText editTitle;
    private EditText editDescription;
    private ImageView editImage;
    private Uri pickedImgUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editTitle = view.findViewById(R.id.edit_title);
        editDescription = view.findViewById(R.id.edit_description);
        editImage = view.findViewById(R.id.edit_image);

        view.findViewById(R.id.done_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pickedImgUri != null)
                    publishPostWithPicture();
                else
                    publishPost();
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
    }

    private void publishPostWithPicture() {
        if (pickedImgUri == null) return;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
        final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uploadedImageLink = uri.toString();
                        publishPost();
                    }
                });
            }
        });
    }

    private void publishPost() {
        String title = editTitle.getText().toString();
        String desc = editDescription.getText().toString();

        if (title.isEmpty() || desc.isEmpty()) {
            Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_LONG).show();
            return;
        }

        Post post = new Post(
                ((EditText)view.findViewById(R.id.edit_title)).getText().toString(),
                ((EditText)view.findViewById(R.id.edit_description)).getText().toString(),
                uploadedImageLink,
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        ViewModelProviders.of(getActivity()).get(PostsViewModel.class).addPost(post);

        Navigation.findNavController(view).popBackStack();
    }


    private void pickImage() {
        if (!Permissions.checkAndRequestPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) return;

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null ) {
            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            editImage.setImageURI(pickedImgUri);
        }
    }
}
