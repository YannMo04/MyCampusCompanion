package com.example.mycampuscompanion.view.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mycampuscompanion.R;
import com.example.mycampuscompanion.utils.DateUtils;

/**
 * Activité affichant le détail d'une actualité
 */
public class ActualiteDetailActivity extends AppCompatActivity {

    private ImageView imageViewActualite;
    private TextView textViewTitre;
    private TextView textViewDate;
    private TextView textViewAuteur;
    private TextView textViewContenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualite_detail);

        // Activer le bouton retour
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Détail de l'actualité");
        }

        // Initialiser les vues
        initViews();

        // Récupérer les données de l'Intent
        loadDataFromIntent();
    }

    private void initViews() {
        imageViewActualite = findViewById(R.id.imageViewActualite);
        textViewTitre = findViewById(R.id.textViewTitre);
        textViewDate = findViewById(R.id.textViewDate);
        textViewAuteur = findViewById(R.id.textViewAuteur);
        textViewContenu = findViewById(R.id.textViewContenu);
    }

    private void loadDataFromIntent() {
        // Récupérer les données passées par l'Intent
        String titre = getIntent().getStringExtra("actualite_titre");
        String description = getIntent().getStringExtra("actualite_description");
        String contenu = getIntent().getStringExtra("actualite_contenu");
        String imageUrl = getIntent().getStringExtra("actualite_imageUrl");
        String date = getIntent().getStringExtra("actualite_date");
        String auteur = getIntent().getStringExtra("actualite_auteur");

        // Afficher le titre
        if (titre != null) {
            textViewTitre.setText(titre);
        }

        // Afficher la date
        if (date != null) {
            String dateFormatted = DateUtils.formatDateForDisplay(date);
            textViewDate.setText("📅 " + dateFormatted);
        }

        // Afficher l'auteur
        if (auteur != null && !auteur.isEmpty()) {
            textViewAuteur.setText("✍️ " + auteur);
        } else {
            textViewAuteur.setText("✍️ Auteur inconnu");
        }

        // Afficher le contenu
        String contenuFinal = contenu;
        if (contenuFinal == null || contenuFinal.isEmpty()) {
            contenuFinal = description;
        }
        if (contenuFinal != null && !contenuFinal.isEmpty()) {
            textViewContenu.setText(contenuFinal);
        } else {
            textViewContenu.setText("Contenu non disponible.");
        }

        // Charger l'image
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_actualites)
                    .error(R.drawable.ic_actualites)
                    .centerCrop()
                    .into(imageViewActualite);
        } else {
            imageViewActualite.setImageResource(R.drawable.ic_actualites);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gérer le clic sur le bouton retour
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}