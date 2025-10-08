package com.example.mycampuscompanion.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mycampuscompanion.model.entities.Contact;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository pour gérer les contacts de l'annuaire
 */
public class ContactRepository {

    private static final String TAG = "ContactRepository";
    private Context context;
    private MutableLiveData<List<Contact>> contactsLiveData;

    public ContactRepository(Context context) {
        this.context = context;
        contactsLiveData = new MutableLiveData<>();
    }

    /**
     * Charger les contacts depuis le fichier JSON
     */
    public LiveData<List<Contact>> loadContacts() {
        Log.d(TAG, "Début du chargement des contacts");

        // Charger dans un thread séparé
        new Thread(() -> {
            try {
                Log.d(TAG, "Lecture du fichier JSON...");

                // Lire le fichier JSON depuis assets
                String json = loadJSONFromAsset();

                if (json == null) {
                    Log.e(TAG, "Fichier JSON null");
                    contactsLiveData.postValue(new ArrayList<>());
                    return;
                }

                Log.d(TAG, "JSON chargé, parsing...");

                // Parser avec Gson
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Contact>>(){}.getType();
                List<Contact> contacts = gson.fromJson(json, listType);

                if (contacts == null) {
                    Log.e(TAG, "Parsing JSON a retourné null");
                    contacts = new ArrayList<>();
                }

                Log.d(TAG, "Nombre de contacts chargés : " + contacts.size());

                // Publier les données sur le thread principal
                contactsLiveData.postValue(contacts);

            } catch (Exception e) {
                Log.e(TAG, "Erreur lors du chargement des contacts", e);
                e.printStackTrace();
                // En cas d'erreur, retourner une liste vide
                contactsLiveData.postValue(new ArrayList<>());
            }
        }).start();

        return contactsLiveData;
    }

    /**
     * Lire le fichier JSON depuis assets
     */
    private String loadJSONFromAsset() {
        String json = null;
        try {
            Log.d(TAG, "Ouverture du fichier contacts.json...");
            InputStream inputStream = context.getAssets().open("contacts.json");

            int size = inputStream.available();
            Log.d(TAG, "Taille du fichier : " + size + " bytes");

            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer, StandardCharsets.UTF_8);
            Log.d(TAG, "Fichier JSON lu avec succès");

        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de la lecture du fichier JSON", e);
            e.printStackTrace();
            return null;
        }
        return json;
    }
}