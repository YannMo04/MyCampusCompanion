package com.example.mycampuscompanion.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mycampuscompanion.model.entities.Contact;
import com.example.mycampuscompanion.repository.ContactRepository;

import java.util.List;

/**
 * ViewModel pour le module Annuaire
 */
public class AnnuaireViewModel extends AndroidViewModel {

    private ContactRepository repository;
    private LiveData<List<Contact>> allContacts;

    public AnnuaireViewModel(@NonNull Application application) {
        super(application);
        repository = new ContactRepository(application);
        allContacts = repository.loadContacts();
    }

    /**
     * Obtenir tous les contacts
     */
    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }
}