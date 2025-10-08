package com.example.mycampuscompanion.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Utilitaire pour gérer les permissions runtime
 */
public class PermissionUtils {

    /**
     * Vérifier si une permission est accordée
     */
    public static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Demander une permission
     */
    public static void requestPermission(Activity activity, String permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    /**
     * Vérifier et demander la permission CALL_PHONE
     */
    public static boolean checkAndRequestCallPermission(Activity activity) {
        if (!isPermissionGranted(activity, Manifest.permission.CALL_PHONE)) {
            requestPermission(activity, Manifest.permission.CALL_PHONE,
                    Constants.PERMISSION_REQUEST_CALL_PHONE);
            return false;
        }
        return true;
    }

    /**
     * Vérifier et demander la permission CAMERA
     */
    public static boolean checkAndRequestCameraPermission(Activity activity) {
        if (!isPermissionGranted(activity, Manifest.permission.CAMERA)) {
            requestPermission(activity, Manifest.permission.CAMERA,
                    Constants.PERMISSION_REQUEST_CAMERA);
            return false;
        }
        return true;
    }

    /**
     * Vérifier et demander la permission LOCATION
     */
    public static boolean checkAndRequestLocationPermission(Activity activity) {
        if (!isPermissionGranted(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION,
                    Constants.PERMISSION_REQUEST_LOCATION);
            return false;
        }
        return true;
    }

    /**
     * Vérifier si l'utilisateur a refusé définitivement une permission
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }
}
