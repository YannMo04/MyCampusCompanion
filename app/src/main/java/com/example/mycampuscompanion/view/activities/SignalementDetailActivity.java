package com.example.mycampuscompanion.view.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycampuscompanion.R;
import com.example.mycampuscompanion.model.entities.PointInteret;
import com.example.mycampuscompanion.utils.Constants;
import com.example.mycampuscompanion.utils.DateUtils;
import com.example.mycampuscompanion.utils.LocationUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class SignalementDetailActivity extends AppCompatActivity {

    private static final String TAG = "SignalementDetail";

    // Vues
    private MaterialToolbar toolbar;
    private TextView textViewType;
    private TextView textViewStatut;
    private TextView textViewTitre;
    private TextView textViewDescription;
    private TextView textViewDate;
    private TextView textViewLocation;
    private MaterialCardView cardViewMedia;
    private ImageView imageViewPhoto;
    private VideoView videoViewVideo;
    private MaterialButton buttonViewMap;

    // Données
    private String type;
    private String statut;
    private String titre;
    private String description;
    private String dateCreation;
    private double latitude;
    private double longitude;
    private String photoPath;
    private String videoPath;
    private String mediaType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signalement_detail);

        Log.d(TAG, "📄 SignalementDetailActivity créée");

        // Récupérer les données de l'Intent
        getDataFromIntent();

        // Initialiser les vues
        initViews();

        // Afficher les données
        displayData();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
            statut = intent.getStringExtra("statut");
            titre = intent.getStringExtra("titre");
            description = intent.getStringExtra("description");
            dateCreation = intent.getStringExtra("dateCreation");
            latitude = intent.getDoubleExtra("latitude", 0.0);
            longitude = intent.getDoubleExtra("longitude", 0.0);
            photoPath = intent.getStringExtra("photoPath");
            videoPath = intent.getStringExtra("videoPath");
            mediaType = intent.getStringExtra("mediaType");

            Log.d(TAG, "📦 Données reçues : " + titre);
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        textViewType = findViewById(R.id.textViewType);
        textViewStatut = findViewById(R.id.textViewStatut);
        textViewTitre = findViewById(R.id.textViewTitre);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewDate = findViewById(R.id.textViewDate);
        textViewLocation = findViewById(R.id.textViewLocation);
        cardViewMedia = findViewById(R.id.cardViewMedia);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        videoViewVideo = findViewById(R.id.videoViewVideo);
        buttonViewMap = findViewById(R.id.buttonViewMap);

        // Configurer la toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Détail du signalement");
        }

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void displayData() {
        // Type
        textViewType.setText(type);

        // Statut
        textViewStatut.setText(statut);
        if (Constants.STATUT_EN_ATTENTE.equals(statut)) {
            textViewStatut.setBackgroundColor(Color.parseColor("#FF9800")); // Orange
        } else if (Constants.STATUT_TRAITE.equals(statut)) {
            textViewStatut.setBackgroundColor(Color.parseColor("#4CAF50")); // Vert
        }

        // Titre
        textViewTitre.setText(titre);

        // Description
        textViewDescription.setText(description);

        // Date
        String dateFormatted = DateUtils.formatDateForDisplay(dateCreation);
        textViewDate.setText("📅 Créé le : " + dateFormatted);

        // Localisation
        if (latitude != 0.0 && longitude != 0.0) {
            PointInteret pointProche = LocationUtils.trouverPointLePlusProche(latitude, longitude);

            String infoLocation;
            if (pointProche != null) {
                double distance = pointProche.distanceEnMetresVers(latitude, longitude);
                String distanceFormatee = LocationUtils.formaterDistance(distance);

                infoLocation = String.format(
                        "📍 Position : %.6f, %.6f\n" +
                                "📌 Proche de : %s\n" +
                                "Type : %s\n" +
                                "Distance : %s",
                        latitude, longitude,
                        pointProche.getNom(),
                        pointProche.getType(),
                        distanceFormatee
                );
            } else {
                infoLocation = String.format("📍 Position : %.6f, %.6f", latitude, longitude);
            }

            textViewLocation.setText(infoLocation);
            buttonViewMap.setVisibility(View.VISIBLE);
            buttonViewMap.setOnClickListener(v -> openMap());
        } else {
            textViewLocation.setText("📍 Position : Non localisé");
            buttonViewMap.setVisibility(View.GONE);
        }

        // Média (photo ou vidéo) - VERSION AMÉLIORÉE
        if (Constants.MEDIA_TYPE_PHOTO.equals(mediaType) && photoPath != null && !photoPath.isEmpty()) {
            cardViewMedia.setVisibility(View.VISIBLE);
            imageViewPhoto.setVisibility(View.VISIBLE);
            videoViewVideo.setVisibility(View.GONE);
            imageViewPhoto.setImageURI(Uri.parse(photoPath));
            Log.d(TAG, "📷 Photo affichée : " + photoPath);

        } else if (Constants.MEDIA_TYPE_VIDEO.equals(mediaType) && videoPath != null && !videoPath.isEmpty()) {
            cardViewMedia.setVisibility(View.VISIBLE);
            imageViewPhoto.setVisibility(View.GONE);
            videoViewVideo.setVisibility(View.VISIBLE);

            Uri videoUri = Uri.parse(videoPath);
            videoViewVideo.setVideoURI(videoUri);

            // Ajouter des contrôles de lecture
            android.widget.MediaController mediaController = new android.widget.MediaController(this);
            mediaController.setAnchorView(videoViewVideo);
            videoViewVideo.setMediaController(mediaController);

            // Configuration au chargement de la vidéo
            videoViewVideo.setOnPreparedListener(mp -> {
                // Obtenir les dimensions de la vidéo
                int videoWidth = mp.getVideoWidth();
                int videoHeight = mp.getVideoHeight();

                Log.d(TAG, "🎥 Dimensions vidéo : " + videoWidth + "x" + videoHeight);

                // Calculer le ratio pour adapter la hauteur
                if (videoWidth > 0 && videoHeight > 0) {
                    float aspectRatio = (float) videoHeight / (float) videoWidth;

                    // Obtenir la largeur de l'écran
                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                    int padding = (int) (32 * getResources().getDisplayMetrics().density); // 16dp de chaque côté
                    int targetWidth = screenWidth - padding;

                    // Calculer la hauteur proportionnelle
                    int targetHeight = (int) (targetWidth * aspectRatio);

                    // Limiter la hauteur maximale
                    int maxHeight = (int) (500 * getResources().getDisplayMetrics().density);
                    if (targetHeight > maxHeight) {
                        targetHeight = maxHeight;
                    }

                    // Appliquer les nouvelles dimensions
                    android.view.ViewGroup.LayoutParams params = videoViewVideo.getLayoutParams();
                    params.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = targetHeight;
                    videoViewVideo.setLayoutParams(params);

                    Log.d(TAG, "📐 Hauteur vidéo ajustée : " + targetHeight + "px");
                }

                // Lancer la lecture automatiquement
                videoViewVideo.start();
                Log.d(TAG, "🎥 Vidéo en lecture");
            });

            // Gérer les erreurs
            videoViewVideo.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "❌ Erreur lecture vidéo : " + what + ", " + extra);
                return true;
            });

            Log.d(TAG, "🎥 Vidéo configurée : " + videoPath);
        }
    }

    /**
     * Ouvrir la position sur Google Maps
     */
    private void openMap() {
        String uri = String.format("geo:%f,%f?q=%f,%f(Signalement: %s)",
                latitude, longitude, latitude, longitude, titre);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Fallback : ouvrir dans le navigateur
            String urlMaps = String.format("https://www.google.com/maps?q=%f,%f", latitude, longitude);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlMaps));
            startActivity(webIntent);
        }
    }
}