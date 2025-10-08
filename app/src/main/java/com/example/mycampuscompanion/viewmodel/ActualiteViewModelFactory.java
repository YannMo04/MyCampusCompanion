package com.example.mycampuscompanion.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Factory pour créer ActualiteViewModel avec des paramètres personnalisés
 */
public class ActualiteViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public ActualiteViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ActualiteViewModel.class)) {
            return (T) new ActualiteViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
