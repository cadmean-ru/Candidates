package ru.cadmean.candidates.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import ru.cadmean.candidates.R;

public abstract class Permissions {
    public static boolean checkAndRequestPermission(Activity context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                new AlertDialog.Builder(context)
                        .setMessage(R.string.explain_location)
                        .setNeutralButton("OK", null)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(context,
                        new String[] {permission},
                        69);
            }

            return false;
        }

        return true;
    }
}
