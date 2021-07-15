package ru.cadmean.candidates;

import android.content.Context;
import android.os.Bundle;
import com.bumptech.glide.Glide;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private static boolean firstCreated = true;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser ;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_map,
                R.id.nav_district,
                R.id.nav_list,
                R.id.nav_candidates,
                R.id.nav_discussion,
                R.id.nav_about
        ).setDrawerLayout(drawer)
        .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (firstCreated && navController.getCurrentDestination().getId() == R.id.nav_map && getPreferences(Context.MODE_PRIVATE).getInt("district", 0) != 0)
            navController.navigate(R.id.action_nav_map_to_nav_district);

        updateNavHeader();

        firstCreated = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void updateNavHeader() {
        View header = navigationView.getHeaderView(0);

        TextView navUsername = header.findViewById(R.id.nav_username);
        TextView navUserMail = header.findViewById(R.id.nav_email);
        ImageView navUserPhoto = header.findViewById(R.id.nav_user_photo);

        if (currentUser == null) {
            navUsername.setText("Not logged in");
            return;
        }

        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

        header.findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });

        if (currentUser.getPhotoUrl() == null) return;

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
    }
}
