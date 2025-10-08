package com.example.mycampuscompanion.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Factory pour CarteViewModel
 */
public class CarteViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public CarteViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CarteViewModel.class)) {
            return (T) new CarteViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}