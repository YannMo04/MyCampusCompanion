package com.example.mycampuscompanion.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mycampuscompanion.model.database.dao.ActualiteDao;
import com.example.mycampuscompanion.model.database.dao.SignalementDao;
import com.example.mycampuscompanion.model.entities.Actualite;
import com.example.mycampuscompanion.model.entities.Signalement;

@Database(
        entities = {Actualite.class, Signalement.class},
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "mycampus_companion.db";
    private static volatile AppDatabase instance;

    public abstract ActualiteDao actualiteDao();
    public abstract SignalementDao signalementDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}