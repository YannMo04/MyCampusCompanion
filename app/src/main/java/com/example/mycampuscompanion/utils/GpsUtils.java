package com.example.mycampuscompanion.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

public class GpsUtils {

    /**
     * Vérifier si le GPS est activé
     */
    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null &&
                (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    /**
     * Afficher une boîte de dialogue pour activer le GPS
     */
    public static void showGPSDisabledDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("GPS désactivé")
                .setMessage("Le GPS est désactivé. Voulez-vous l'activer ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                })
                .setNegativeButton("Non", null)
                .show();
    }
}