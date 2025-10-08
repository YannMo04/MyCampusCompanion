package com.example.mycampuscompanion.view.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mycampuscompanion.R;
import com.example.mycampuscompanion.view.fragments.ActualitesFragment;
import com.example.mycampuscompanion.view.fragments.AnnuaireFragment;
import com.example.mycampuscompanion.view.fragments.CarteFragment;
import com.example.mycampuscompanion.view.fragments.SignalementFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Activité principale avec BottomNavigationView
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser les vues
        initViews();

        // Charger le fragment par défaut (Actualités)
        if (savedInstanceState == null) {
            loadFragment(new ActualitesFragment());
        }
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_actualites) {
                fragment = new ActualitesFragment();
            } else if (itemId == R.id.nav_annuaire) {
                fragment = new AnnuaireFragment();
            } else if (itemId == R.id.nav_carte) {
                fragment = new CarteFragment();
            } else if (itemId == R.id.nav_signalement) {
                fragment = new SignalementFragment();
            }

            return loadFragment(fragment);
        });
    }

    /**
     * Charger un fragment dans le conteneur
     */
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}