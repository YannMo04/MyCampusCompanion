package com.example.mycampuscompanion.viewmodel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mycampuscompanion.model.entities.Signalement;
import com.example.mycampuscompanion.repository.SignalementRepository;

import java.util.List;

public class SignalementViewModel extends AndroidViewModel {

    private SignalementRepository repository;
    private LiveData<List<Signalement>> allSignalements;

    public SignalementViewModel(@NonNull Application application) {
        super(application);
        repository = new SignalementRepository(application);
        allSignalements = repository.getAllSignalements();
    }

    public LiveData<List<Signalement>> getAllSignalements() {
        return allSignalements;
    }

    public void insertSignalement(Signalement signalement) {
        repository.insertSignalement(signalement);
    }

    public void updateSignalement(Signalement signalement) {
        repository.updateSignalement(signalement);
    }

    public void deleteSignalement(Signalement signalement) {
        repository.deleteSignalement(signalement);
    }

    public void deleteAllSignalement() {
        repository.deleteAllSignalement();
    }


    public LiveData<Location> getCurrentLocation() {
        return repository.getCurrentLocation();
    }
}