package com.example.mycampuscompanion.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Factory pour AnnuaireViewModel
 */
public class AnnuaireViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public AnnuaireViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AnnuaireViewModel.class)) {
            return (T) new AnnuaireViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}