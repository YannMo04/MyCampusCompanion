package com.example.mycampuscompanion.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.graphics.Color;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycampuscompanion.R;
import com.example.mycampuscompanion.model.entities.Signalement;
import com.example.mycampuscompanion.utils.Constants;
import com.example.mycampuscompanion.utils.DateUtils;
import com.example.mycampuscompanion.utils.MediaUtils;
import com.example.mycampuscompanion.view.activities.SignalementListActivity;
import com.example.mycampuscompanion.viewmodel.SignalementViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import android.graphics.Color;
import com.example.mycampuscompanion.model.entities.PointInteret;
import com.example.mycampuscompanion.utils.LocationUtils;

public class SignalementFragment extends Fragment {

    private static final String TAG = "SignalementFragment";

    private SignalementViewModel viewModel;

    // Vues
    private AutoCompleteTextView autoCompleteType;
    private TextInputEditText editTextTitre;
    private TextInputEditText editTextDescription;
    private MaterialButton buttonTakePhoto;
    private MaterialButton buttonTakeVideo;
    private MaterialButton buttonGetLocation;
    private MaterialButton buttonSubmit;
    private CardView cardViewMediaPreview;
    private ImageView imageViewPreview;
    private VideoView videoViewPreview;
    private FloatingActionButton fabRemoveMedia;
    private TextView textViewMediaInfo;
    private TextView textViewLocationInfo;

    private MaterialButton buttonViewSignalements;

    // Données
    private String currentPhotoPath;
    private String currentVideoPath;
    private String currentMediaType;
    private double latitude = 0.0;
    private double longitude = 0.0;

    // Launchers
    private ActivityResultLauncher<String> requestCameraPermission;
    private ActivityResultLauncher<String> requestLocationPermission;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<Intent> takeVideoLauncher;

    public SignalementFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLaunchers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signalement, container, false);

        Log.d(TAG, "🚨 SignalementFragment créé");

        initViews(view);
        viewModel = new ViewModelProvider(this).get(SignalementViewModel.class);
        setupListeners();
        setupTypeDropdown();

        return view;
    }

    private void initLaunchers() {
        requestCameraPermission = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Toast.makeText(getContext(), "Permission caméra accordée", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Permission caméra refusée", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestLocationPermission = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Toast.makeText(getContext(), "Permission localisation accordée", Toast.LENGTH_SHORT).show();
                        getCurrentLocation();
                    } else {
                        Toast.makeText(getContext(), "Permission localisation refusée", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Log.d(TAG, "✅ Photo capturée");
                        displayPhotoPreview();
                    }
                }
        );

        takeVideoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Log.d(TAG, "✅ Vidéo capturée");
                        displayVideoPreview();
                    }
                }
        );
    }

    private void initViews(View view) {
        autoCompleteType = view.findViewById(R.id.autoCompleteType);
        editTextTitre = view.findViewById(R.id.editTextTitre);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        buttonTakePhoto = view.findViewById(R.id.buttonTakePhoto);
        buttonTakeVideo = view.findViewById(R.id.buttonTakeVideo);
        buttonGetLocation = view.findViewById(R.id.buttonGetLocation);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        cardViewMediaPreview = view.findViewById(R.id.cardViewMediaPreview);
        imageViewPreview = view.findViewById(R.id.imageViewPreview);
        videoViewPreview = view.findViewById(R.id.videoViewPreview);
        fabRemoveMedia = view.findViewById(R.id.fabRemoveMedia);
        textViewMediaInfo = view.findViewById(R.id.textViewMediaInfo);
        textViewLocationInfo = view.findViewById(R.id.textViewLocationInfo);
        buttonViewSignalements = view.findViewById(R.id.buttonViewSignalements);
    }

    private void setupListeners() {
        buttonTakePhoto.setOnClickListener(v -> checkCameraPermissionAndTakePhoto());
        buttonTakeVideo.setOnClickListener(v -> checkCameraPermissionAndTakeVideo());
        buttonGetLocation.setOnClickListener(v -> checkLocationPermissionAndGetLocation());
        buttonSubmit.setOnClickListener(v -> submitSignalement());
        fabRemoveMedia.setOnClickListener(v -> removeMedia());
        buttonViewSignalements.setOnClickListener(v -> viewSignalements());
    }

    private void setupTypeDropdown() {
        String[] types = getResources().getStringArray(R.array.signalement_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                types
        );
        autoCompleteType.setAdapter(adapter);
    }

    private void checkCameraPermissionAndTakePhoto() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            takePhoto();
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA);
        }
    }
    private void viewSignalements() {
        Intent intent = new Intent(getActivity(), SignalementListActivity.class);
        startActivity(intent);
    }

    private void takePhoto() {
        try {
            File photoFile = MediaUtils.createImageFile(requireContext());
            currentPhotoPath = photoFile.getAbsolutePath();
            currentMediaType = Constants.MEDIA_TYPE_PHOTO;

            Uri photoUri = MediaUtils.getUriForFile(requireContext(), photoFile);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            takePictureLauncher.launch(takePictureIntent);

        } catch (Exception e) {
            Log.e(TAG, "Erreur", e);
            Toast.makeText(getContext(), "Erreur lors de la capture", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkCameraPermissionAndTakeVideo() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            takeVideo();
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA);
        }
    }

    private void takeVideo() {
        try {
            File videoFile = MediaUtils.createVideoFile(requireContext());
            currentVideoPath = videoFile.getAbsolutePath();
            currentMediaType = Constants.MEDIA_TYPE_VIDEO;

            Uri videoUri = MediaUtils.getUriForFile(requireContext(), videoFile);

            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
            takeVideoLauncher.launch(takeVideoIntent);

        } catch (Exception e) {
            Log.e(TAG, "Erreur", e);
            Toast.makeText(getContext(), "Erreur lors de la capture", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayPhotoPreview() {
        cardViewMediaPreview.setVisibility(View.VISIBLE);
        imageViewPreview.setVisibility(View.VISIBLE);
        videoViewPreview.setVisibility(View.GONE);
        imageViewPreview.setImageURI(Uri.parse(currentPhotoPath));

        double sizeMB = MediaUtils.getFileSizeMB(currentPhotoPath);
        textViewMediaInfo.setVisibility(View.VISIBLE);
        textViewMediaInfo.setText(String.format("📷 Photo (%.2f Mo)", sizeMB));
    }

    @SuppressLint("DefaultLocale")
    private void displayVideoPreview() {
        cardViewMediaPreview.setVisibility(View.VISIBLE);
        imageViewPreview.setVisibility(View.GONE);
        videoViewPreview.setVisibility(View.VISIBLE);
        videoViewPreview.setVideoURI(Uri.parse(currentVideoPath));
        videoViewPreview.start();

        double sizeMB = MediaUtils.getFileSizeMB(currentVideoPath);
        textViewMediaInfo.setVisibility(View.VISIBLE);
        textViewMediaInfo.setText(String.format("🎥 Vidéo (%.2f Mo)", sizeMB));
    }

    private void removeMedia() {
        currentPhotoPath = null;
        currentVideoPath = null;
        currentMediaType = null;
        cardViewMediaPreview.setVisibility(View.GONE);
        textViewMediaInfo.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Média supprimé", Toast.LENGTH_SHORT).show();
    }

    private void checkLocationPermissionAndGetLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void getCurrentLocation() {
        Log.d(TAG, "🔵 getCurrentLocation() appelée");

        // Afficher un message de chargement
        textViewLocationInfo.setText("⏳ Recherche de la position GPS en cours...");
        textViewLocationInfo.setBackgroundColor(Color.parseColor("#FFF3E0")); // Orange clair

        // Observer la position
        viewModel.getCurrentLocation().observe(getViewLifecycleOwner(), location -> {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                Log.d(TAG, String.format("✅ Position reçue : Lat=%.6f, Lon=%.6f", latitude, longitude));

                // Trouver le point d'intérêt le plus proche
                PointInteret pointProche = LocationUtils.trouverPointLePlusProche(latitude, longitude);

                String infoPosition;
                if (pointProche != null) {
                    double distance = pointProche.distanceEnMetresVers(latitude, longitude);
                    String distanceFormatee = LocationUtils.formaterDistance(distance);

                    infoPosition = String.format(
                            "📍 Position enregistrée\n" +
                                    "Latitude: %.6f\n" +
                                    "Longitude: %.6f\n\n" +
                                    "📌 Point d'intérêt le plus proche:\n" +
                                    "%s\n" +
                                    "Type: %s\n" +
                                    "Distance: %s",
                            latitude, longitude,
                            pointProche.getNom(),
                            pointProche.getType(),
                            distanceFormatee
                    );

                    Log.d(TAG, String.format("📌 Point le plus proche : %s (%s) à %s",
                            pointProche.getNom(), pointProche.getType(), distanceFormatee));
                } else {
                    infoPosition = String.format(
                            "📍 Position enregistrée\n" +
                                    "Latitude: %.6f\n" +
                                    "Longitude: %.6f",
                            latitude, longitude
                    );
                }

                textViewLocationInfo.setText(infoPosition);
                textViewLocationInfo.setBackgroundColor(Color.parseColor("#C8E6C9")); // Vert clair

                Toast.makeText(getContext(), "✅ Position récupérée avec succès !", Toast.LENGTH_SHORT).show();

            } else {
                Log.e(TAG, "❌ Position null reçue");

                textViewLocationInfo.setText("❌ Impossible de récupérer la position.\n\n" +
                        "Vérifications :\n" +
                        "• GPS activé ?\n" +
                        "• À l'extérieur ou près d'une fenêtre ?\n" +
                        "• Permission accordée ?");
                textViewLocationInfo.setBackgroundColor(Color.parseColor("#FFCDD2")); // Rouge clair

                Toast.makeText(getContext(),
                        "❌ Impossible de récupérer la position. Vérifiez le GPS.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void submitSignalement() {
        // Validation
        String type = autoCompleteType.getText().toString().trim();
        String titre = editTextTitre.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (type.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez sélectionner un type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (titre.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez saisir un titre", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez saisir une description", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créer le signalement
        Signalement signalement = new Signalement();
        signalement.setType(type);
        signalement.setTitre(titre);
        signalement.setDescription(description);
        signalement.setDateCreation(DateUtils.getCurrentDateTime());
        signalement.setStatut(Constants.STATUT_EN_ATTENTE);

        // Ajouter le média
        if (currentPhotoPath != null) {
            signalement.setPhotoPath(currentPhotoPath);
            signalement.setMediaType(Constants.MEDIA_TYPE_PHOTO);
        } else if (currentVideoPath != null) {
            signalement.setVideoPath(currentVideoPath);
            signalement.setMediaType(Constants.MEDIA_TYPE_VIDEO);
        }

        // Ajouter la position
        signalement.setLatitude(latitude);
        signalement.setLongitude(longitude);

        // Sauvegarder
        viewModel.insertSignalement(signalement);

        Toast.makeText(getContext(), "Signalement enregistré avec succès !", Toast.LENGTH_LONG).show();

        // Réinitialiser
        clearForm();
    }

    private void clearForm() {
        autoCompleteType.setText("Problème technique");
        editTextTitre.setText("");
        editTextDescription.setText("");
        removeMedia();
        latitude = 0.0;
        longitude = 0.0;
        textViewLocationInfo.setText("Aucune position enregistrée");
    }
}