package com.example.mycampuscompanion.repository;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LocationRepository {

    private static final String TAG = "LocationRepository";

    private Context context;
    private LocationManager locationManager;
    private MutableLiveData<Location> locationLiveData;

    public LocationRepository(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.locationLiveData = new MutableLiveData<>();
    }

    public LiveData<Location> getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "❌ Permission ACCESS_FINE_LOCATION non accordée");
            locationLiveData.postValue(null);
            return locationLiveData;
        }

        Log.d(TAG, "🔍 Récupération de la position...");

        // Vérifier si le GPS est activé
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d(TAG, "GPS activé : " + isGPSEnabled);
        Log.d(TAG, "Network activé : " + isNetworkEnabled);

        if (!isGPSEnabled && !isNetworkEnabled) {
            Log.e(TAG, "❌ GPS et Network désactivés");
            locationLiveData.postValue(null);
            return locationLiveData;
        }

        // Essayer d'abord avec la dernière position connue
        Location lastKnownLocation = null;

        if (isGPSEnabled) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (lastKnownLocation == null && isNetworkEnabled) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (lastKnownLocation != null) {
            Log.d(TAG, "✅ Position trouvée (cache) : " + lastKnownLocation.getLatitude() + ", " + lastKnownLocation.getLongitude());
            locationLiveData.postValue(lastKnownLocation);
            return locationLiveData;
        }

        // Si pas de position en cache, demander une mise à jour
        Log.d(TAG, "⏳ Demande de mise à jour de la position...");

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "✅ Position mise à jour : " + location.getLatitude() + ", " + location.getLongitude());
                locationLiveData.postValue(location);
                locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {
                Log.d(TAG, "Provider activé : " + provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG, "Provider désactivé : " + provider);
            }
        };

        try {
            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0,
                        locationListener
                );
                Log.d(TAG, "📡 Demande GPS envoyée");
            }

            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0,
                        0,
                        locationListener
                );
                Log.d(TAG, "📡 Demande Network envoyée");
            }

            // Timeout après 10 secondes
            new android.os.Handler().postDelayed(() -> {
                if (locationLiveData.getValue() == null) {
                    Log.e(TAG, "⏱️ Timeout : impossible de récupérer la position");
                    locationLiveData.postValue(null);
                    locationManager.removeUpdates(locationListener);
                }
            }, 10000);

        } catch (SecurityException e) {
            Log.e(TAG, "❌ SecurityException", e);
            locationLiveData.postValue(null);
        }

        return locationLiveData;
    }
}