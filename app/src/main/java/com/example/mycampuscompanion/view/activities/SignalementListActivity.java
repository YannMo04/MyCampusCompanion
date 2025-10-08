package com.example.mycampuscompanion.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycampuscompanion.R;
import com.example.mycampuscompanion.model.entities.Signalement;
import com.example.mycampuscompanion.view.adapters.SignalementAdapter;
import com.example.mycampuscompanion.viewmodel.SignalementViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.io.File;

public class SignalementListActivity extends AppCompatActivity implements SignalementAdapter.OnSignalementClickListener {

    private static final String TAG = "SignalementListActivity";

    private SignalementViewModel viewModel;
    private RecyclerView recyclerView;
    private SignalementAdapter adapter;
    private LinearLayout linearLayoutEmpty;
    private MaterialToolbar toolbar;
    private MaterialButton buttonReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signalement_list);

        Log.d(TAG, "📋 SignalementListActivity créée");

        initViews();
        viewModel = new ViewModelProvider(this).get(SignalementViewModel.class);
        setupRecyclerView();
        observeData();
        setupSwipeToDelete();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerViewSignalements);
        linearLayoutEmpty = findViewById(R.id.linearLayoutEmpty);
        buttonReset = findViewById(R.id.buttonReset);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mes Signalements");
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        // Bouton de réinitialisation
        if (buttonReset != null) {
            buttonReset.setOnClickListener(v -> showResetConfirmationDialog());
        }
    }

    private void setupRecyclerView() {
        adapter = new SignalementAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSignalementClick(Signalement signalement) {
        Log.d(TAG, "Signalement cliqué : " + signalement.getTitre());
        openSignalementDetail(signalement);
    }

    @Override
    public void onDeleteClick(Signalement signalement, int position) {
        // Afficher dialogue de confirmation
        new AlertDialog.Builder(this)
                .setTitle("🗑️ Supprimer le signalement")
                .setMessage("Voulez-vous vraiment supprimer ce signalement ?\n\n" +
                        "Titre : " + signalement.getTitre())
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    deleteSignalement(signalement, position);
                })
                .setNegativeButton("Annuler", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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

    /**
     * Supprimer un signalement
     */
    private void deleteSignalement(Signalement signalement, int position) {
        // Supprimer le fichier photo si existe
        if (signalement.getPhotoPath() != null && !signalement.getPhotoPath().isEmpty()) {
            File photoFile = new File(signalement.getPhotoPath());
            if (photoFile.exists()) {
                boolean deleted = photoFile.delete();
                Log.d(TAG, "Photo supprimée : " + deleted);
            }
        }

        // Supprimer le fichier vidéo si existe
        if (signalement.getVideoPath() != null && !signalement.getVideoPath().isEmpty()) {
            File videoFile = new File(signalement.getVideoPath());
            if (videoFile.exists()) {
                boolean deleted = videoFile.delete();
                Log.d(TAG, "Vidéo supprimée : " + deleted);
            }
        }

        // Supprimer de la base de données
        viewModel.deleteSignalement(signalement);

        // Supprimer de l'adapter avec animation
        adapter.removeItem(position);

        Toast.makeText(this, "✅ Signalement supprimé", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "🗑️ Signalement supprimé : " + signalement.getTitre());
    }

    /**
     * Configuration du swipe pour supprimer
     */
    private void setupSwipeToDelete() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Signalement signalement = adapter.getSignalementAt(position);

                // Afficher le dialogue de confirmation
                onDeleteClick(signalement, position);

                // Annuler le swipe visuellement (le dialogue gérera la suppression)
                adapter.notifyItemChanged(position);
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Dialogue de confirmation pour réinitialisation totale
     */
    private void showResetConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("⚠️ Supprimer tous les signalements")
                .setMessage("Voulez-vous vraiment supprimer TOUS les signalements ?\n\n" +
                        "Cette action est irréversible et supprimera également les photos et vidéos associées.")
                .setPositiveButton("Supprimer tout", (dialog, which) -> {
                    viewModel.deleteAllSignalement();
                    Toast.makeText(this,
                            "✅ Tous les signalements ont été supprimés",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "🗑️ Tous les signalements supprimés");
                })
                .setNegativeButton("Annuler", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void observeData() {
        viewModel.getAllSignalements().observe(this, signalements -> {
            if (signalements != null && !signalements.isEmpty()) {
                Log.d(TAG, "✅ " + signalements.size() + " signalements chargés");
                adapter.setSignalements(signalements);
                recyclerView.setVisibility(View.VISIBLE);
                linearLayoutEmpty.setVisibility(View.GONE);

                // Activer le bouton reset
                if (buttonReset != null) {
                    buttonReset.setEnabled(true);
                    buttonReset.setAlpha(1.0f);
                }
            } else {
                Log.d(TAG, "⚠️ Aucun signalement");
                recyclerView.setVisibility(View.GONE);
                linearLayoutEmpty.setVisibility(View.VISIBLE);

                // Désactiver le bouton reset
                if (buttonReset != null) {
                    buttonReset.setEnabled(false);
                    buttonReset.setAlpha(0.5f);
                }
            }
        });
    }
}