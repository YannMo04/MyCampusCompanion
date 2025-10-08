package com.example.mycampuscompanion.viewmodel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mycampuscompanion.model.entities.PointInteret;
import com.example.mycampuscompanion.repository.LocationRepository;

/**
 * ViewModel pour le module Carte
 */
public class CarteViewModel extends AndroidViewModel {

    private LocationRepository repository;
    private MutableLiveData<Location> currentLocation;
    private PointInteret defaultPOI;

    public CarteViewModel(@NonNull Application application) {
        super(application);

    }

    /**
     * Obtenir la position actuelle
     */
    public MutableLiveData<Location> getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Obtenir le POI par défaut
     */
    public PointInteret getDefaultPOI() {
        return defaultPOI;
    }
}