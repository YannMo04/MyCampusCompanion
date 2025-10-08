package com.example.mycampuscompanion.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycampuscompanion.R;
import com.example.mycampuscompanion.model.entities.Signalement;
import com.example.mycampuscompanion.view.adapters.SignalementAdapter;
import com.example.mycampuscompanion.viewmodel.SignalementViewModel;
import com.google.android.material.appbar.MaterialToolbar;

public class SignalementListActivity extends AppCompatActivity {

    private static final String TAG = "SignalementListActivity";

    private SignalementViewModel viewModel;
    private RecyclerView recyclerView;
    private SignalementAdapter adapter;
    private LinearLayout linearLayoutEmpty;  // ✅ CORRIGÉ
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signalement_list);

        Log.d(TAG, "📋 SignalementListActivity créée");

        initViews();
        viewModel = new ViewModelProvider(this).get(SignalementViewModel.class);
        setupRecyclerView();
        observeData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerViewSignalements);
        linearLayoutEmpty = findViewById(R.id.linearLayoutEmpty);  // ✅ CORRIGÉ

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mes Signalements");
        }

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new SignalementAdapter(this, signalement -> {
            Log.d(TAG, "Signalement cliqué : " + signalement.getTitre());
            openSignalementDetail(signalement);  // ✅ AJOUTÉ
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    private void openSignalementDetail(Signalement signalement) {
        Intent intent = new Intent(this, SignalementDetailActivity.class);

        // Passer les données
        intent.putExtra("type", signalement.getType());
        intent.putExtra("statut", signalement.getStatut());
        intent.putExtra("titre", signalement.getTitre());
        intent.putExtra("description", signalement.getDescription());
        intent.putExtra("dateCreation", signalement.getDateCreation());
        intent.putExtra("latitude", signalement.getLatitude());
        intent.putExtra("longitude", signalement.getLongitude());
        intent.putExtra("photoPath", signalement.getPhotoPath());
        intent.putExtra("videoPath", signalement.getVideoPath());
        intent.putExtra("mediaType", signalement.getMediaType());

        startActivity(intent);
    }

    private void observeData() {
        viewModel.getAllSignalements().observe(this, signalements -> {
            if (signalements != null && !signalements.isEmpty()) {
                Log.d(TAG, "✅ " + signalements.size() + " signalements chargés");
                adapter.setSignalements(signalements);
                recyclerView.setVisibility(View.VISIBLE);
                linearLayoutEmpty.setVisibility(View.GONE);  // ✅ CORRIGÉ
            } else {
                Log.d(TAG, "⚠️ Aucun signalement");
                recyclerView.setVisibility(View.GONE);
                linearLayoutEmpty.setVisibility(View.VISIBLE);  // ✅ CORRIGÉ
            }
        });
    }
}