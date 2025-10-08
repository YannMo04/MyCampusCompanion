package com.example.mycampuscompanion.model.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mycampuscompanion.model.entities.Signalement;

import java.util.List;

@Dao
public interface SignalementDao {

    @Query("SELECT * FROM signalements ORDER BY dateCreation DESC")
    LiveData<List<Signalement>> getAllSignalements();

    @Query("SELECT * FROM signalements WHERE id = :signalementId")
    LiveData<Signalement> getSignalementById(int signalementId);

    @Query("SELECT * FROM signalements WHERE statut = :statut ORDER BY dateCreation DESC")
    LiveData<List<Signalement>> getSignalementsByStatut(String statut);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSignalement(Signalement signalement);

    @Update
    void updateSignalement(Signalement signalement);

    @Delete
    void deleteSignalement(Signalement signalement);

    @Query("DELETE FROM signalements")
    void deleteAllSignalements();
}