package ru.cadmean.candidates;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import ru.cadmean.candidates.models.Post;
import ru.cadmean.candidates.models.PostsViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainRedditPageFragment extends Fragment {

    private static final int PReqCode = 2 ;
    private static final int REQUESCODE = 2 ;
    private FirebaseUser currentUser ;
//    private Dialog popAddPost ;
//    private ImageView popupUserImage,popupPostImage,popupAddBtn;
//    private TextView popupTitle,popupDescription;
//    private ProgressBar popupClickProgress;
//    private Uri pickedImgUri = null;

    private View view;

    private PostsViewModel postsViewModel;


    public MainRedditPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_reddit_page, container, false);
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
                t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                t.add(android.R.id.content, new PostDialogFragment());
                t.addToBackStack(null);
                t.commit();
            }
        });
//        iniPopup();
//        setupPopupImageClick();
//
//        FloatingActionButton fab = view.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popAddPost.show();
//            }
//        });
//
//        postsViewModel = ViewModelProviders.of(getActivity()).get(PostsViewModel.class);
//        postsViewModel.getPosts()
//                .observe(getActivity(), new Observer<ArrayList<Post>>() {
//                    @Override
//                    public void onChanged(ArrayList<Post> posts) {
//
//                    }
//                });
    }

    private void iniPopup() {
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
//        if (currentUser == null) return;
//
//        popAddPost = new Dialog(getActivity());
//
//        popAddPost.setContentView(R.layout.popout_add_post);
//        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
//        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;
//
//        // ini popup widgets
//        popupUserImage = popAddPost.findViewById(R.id.popup_user_image);
//        popupPostImage = popAddPost.findViewById(R.id.popup_img);
//        popupTitle = popAddPost.findViewById(R.id.popup_title);
//        popupDescription = popAddPost.findViewById(R.id.popup_description);
//        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
//        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);
//
//        // load Current user profile photo
//
//        final Uri photoUri = currentUser.getPhotoUrl();
//        if (photoUri != null) {
//            Glide.with(getActivity()).load(currentUser.getPhotoUrl()).into(popupUserImage);
//        }
//
//        // Add post click Listener
//
//        popupAddBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popupAddBtn.setVisibility(View.INVISIBLE);
//                popupClickProgress.setVisibility(View.VISIBLE);
//
//                // we need to test all input fields (Title and description ) and post image
//
//                if (!popupTitle.getText().toString().isEmpty()
//                        && !popupDescription.getText().toString().isEmpty()
//                        && pickedImgUri != null ) {
//
//                    //everything is okey no empty or null value
//                    // TODO Create Post Object and add it to firebase database
//                    // first we need to upload post Image
//                    // access firebase storage
//                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
//                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
//                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    String imageDownlaodLink = uri.toString();
//                                    // create post Object
//                                    Post post = new Post(popupTitle.getText().toString(),
//                                            popupDescription.getText().toString(),
//                                            imageDownlaodLink,
//                                            currentUser.getUid(),
//                                            photoUri == null ? "" : photoUri.toString());
//
//                                    // Add post to firebase database
//                                    addPost(post);
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    // something goes wrong uploading picture
//
//                                    showMessage(e.getMessage());
//                                    popupClickProgress.setVisibility(View.INVISIBLE);
//                                    popupAddBtn.setVisibility(View.VISIBLE);
//                                }
//                            });
//                        }
//                    });
//                }
//                else
//                {
//                    showMessage("something went wrong again... nothing here should be null");
//                    popupAddBtn.setVisibility(View.VISIBLE);
//                    popupClickProgress.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
    }

    private void showMessage(String message) {

        Toast.makeText(getActivity().getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    private void setupPopupImageClick() {
//        popupPostImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // here when image clicked we need to open the gallery
//                // before we open the gallery we need to check if our app have the access to user files
//                // we did this before in register activity I'm just going to copy the code to save time ...
//
//                checkAndRequestForPermission();
//            }
//        });
    }

    private void checkAndRequestForPermission() {


//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//                Toast.makeText(getActivity(),"Please accept for required permission",Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        PReqCode);
//            }
//        }
//        else
//            // everything goes well : we have permission to access user gallery
//            openGallery();
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }



    private void addPost(Post post) {
        postsViewModel.addPost(post);
    }


    // when user picked an image ...
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUESCODE && data != null ) {
//            // the user has successfully picked an image
//            // we need to save its reference to a Uri variable
//            pickedImgUri = data.getData() ;
//            popupPostImage.setImageURI(pickedImgUri);
//        }
    }

}
