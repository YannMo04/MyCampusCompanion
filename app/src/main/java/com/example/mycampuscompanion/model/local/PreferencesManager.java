package com.example.mycampuscompanion.model.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mycampuscompanion.utils.Constants;

/**
 * Gestionnaire des préférences partagées
 * TODO: Utiliser EncryptedSharedPreferences pour les données sensibles (Phase sécurité)
 */
public class PreferencesManager {

    private static PreferencesManager instance;
    private final SharedPreferences sharedPreferences;

    private PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(
                Constants.PREFS_NAME,
                Context.MODE_PRIVATE
        );
        // TODO: Remplacer par EncryptedSharedPreferences dans la phase sécurité
    }

    /**
     * Obtenir l'instance (Singleton)
     */
    public static synchronized PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Sauvegarder une valeur String
     */
    public void saveString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * Récupérer une valeur String
     */
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * Sauvegarder une valeur boolean
     */
    public void saveBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    /**
     * Récupérer une valeur boolean
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Vérifier si c'est le premier lancement
     */
    public boolean isFirstLaunch() {
        return getBoolean(Constants.PREF_FIRST_LAUNCH, true);
    }

    /**
     * Marquer que l'application a déjà été lancée
     */
    public void setFirstLaunchDone() {
        saveBoolean(Constants.PREF_FIRST_LAUNCH, false);
    }

    /**
     * Effacer toutes les préférences
     */
    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }
}
