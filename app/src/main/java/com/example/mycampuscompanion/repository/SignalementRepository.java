package com.example.mycampuscompanion.repository;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.mycampuscompanion.model.database.AppDatabase;
import com.example.mycampuscompanion.model.database.dao.SignalementDao;
import com.example.mycampuscompanion.model.entities.Signalement;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SignalementRepository {

    private static final String TAG = "SignalementRepository";

    private SignalementDao signalementDao;
    private LiveData<List<Signalement>> allSignalements;
    private Executor executor;
    private LocationRepository locationRepository;

    public SignalementRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        signalementDao = database.signalementDao();
        allSignalements = signalementDao.getAllSignalements();
        executor = Executors.newSingleThreadExecutor();
        locationRepository = new LocationRepository(context);
    }

    public LiveData<List<Signalement>> getAllSignalements() {
        return allSignalements;
    }

    public void insertSignalement(Signalement signalement) {
        executor.execute(() -> {
            try {
                long id = signalementDao.insertSignalement(signalement);
                Log.d(TAG, "✅ Signalement inséré avec l'ID : " + id);
            } catch (Exception e) {
                Log.e(TAG, "❌ Erreur lors de l'insertion du signalement", e);
            }
        });
    }

    public void updateSignalement(Signalement signalement) {
        executor.execute(() -> {
            signalementDao.updateSignalement(signalement);
            Log.d(TAG, "✅ Signalement mis à jour");
        });
    }

    public void deleteSignalement(Signalement signalement) {
        executor.execute(() -> {
            signalementDao.deleteSignalement(signalement);
            Log.d(TAG, "✅ Signalement supprimé");
        });
    }

    public LiveData<Location> getCurrentLocation() {
        return locationRepository.getCurrentLocation();
    }
}