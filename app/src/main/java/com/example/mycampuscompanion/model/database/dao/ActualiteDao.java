package com.example.mycampuscompanion.model.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mycampuscompanion.model.entities.Actualite;

import java.util.List;
@Dao
public abstract class ActualiteDao {

    @Query("SELECT * FROM actualites ORDER BY datePublication DESC")
    public abstract LiveData<List<Actualite>> getAllActualites();


    @Query("SELECT * FROM actualites WHERE id = :actualiteId")
    abstract LiveData<Actualite> getActualiteById(int actualiteId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertActualite(Actualite actualite);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertActualites(List<Actualite> actualites);


    @Update
    abstract void updateActualite(Actualite actualite);


    @Delete
    abstract void deleteActualite(Actualite actualite);


    @Query("DELETE FROM actualites")
    public abstract void deleteAllActualites();


    @Query("SELECT COUNT(*) FROM actualites")
    abstract int getActualitesCount();
}
