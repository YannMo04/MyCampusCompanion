package com.example.mycampuscompanion.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mycampuscompanion.model.entities.Actualite;
import com.example.mycampuscompanion.repository.ActualiteRepository;

import java.util.List;

/**
 * ViewModel pour le module Actualités
 */
public class ActualiteViewModel extends AndroidViewModel {

    private ActualiteRepository repository;
    private LiveData<List<Actualite>> allActualites;
    private MutableLiveData<String> loadingStatus;

    public ActualiteViewModel(@NonNull Application application) {
        super(application);
        repository = new ActualiteRepository(application);
        allActualites = repository.getAllActualites();
        loadingStatus = new MutableLiveData<>();
    }

    /**
     * Obtenir toutes les actualités (depuis le cache SQLite)
     */
    public LiveData<List<Actualite>> getAllActualites() {
        return allActualites;
    }

    /**
     * Rafraîchir les actualités depuis l'API
     */
    public void refreshActualites() {
        loadingStatus = repository.refreshActualites();
    }

    /**
     * Obtenir le statut de chargement
     */
    public LiveData<String> getLoadingStatus() {
        return loadingStatus;
    }
}