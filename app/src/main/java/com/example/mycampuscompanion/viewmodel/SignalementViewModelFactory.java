package com.example.mycampuscompanion.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Factory pour SignalementViewModel
 */
public class SignalementViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public SignalementViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignalementViewModel.class)) {
            return (T) new SignalementViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}