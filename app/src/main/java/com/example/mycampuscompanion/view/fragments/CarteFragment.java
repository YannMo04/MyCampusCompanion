package com.example.mycampuscompanion.view.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycampuscompanion.R;
import com.example.mycampuscompanion.model.entities.PointInteret;
import com.example.mycampuscompanion.utils.Constants;
import com.example.mycampuscompanion.utils.GpsUtils;
import com.example.mycampuscompanion.utils.LocationUtils;
import com.example.mycampuscompanion.viewmodel.SignalementViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

public class CarteFragment extends Fragment {

    private static final String TAG = "CarteFragment";

    private MapView mapView;
    private IMapController mapController;
    private MyLocationNewOverlay myLocationOverlay;
    private SignalementViewModel viewModel;

    // Vues
    private FloatingActionButton fabMyLocation;
    private FloatingActionButton fabShowAllPoints;
    private FloatingActionButton fabZoomIn;
    private FloatingActionButton fabZoomOut;
    private TextView textViewInfo;

    // Position actuelle
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;

    // Launcher pour permission
    private ActivityResultLauncher<String> requestLocationPermission;

    public CarteFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuration OSMDroid
        Configuration.getInstance().load(
                requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext())
        );
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());

        // Initialiser le launcher de permission
        requestLocationPermission = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Toast.makeText(getContext(), "Permission accordée", Toast.LENGTH_SHORT).show();
                        enableMyLocation();
                    } else {
                        Toast.makeText(getContext(), "Permission refusée", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carte, container, false);

        Log.d(TAG, "🗺️ CarteFragment créé");

        // Initialiser les vues
        initViews(view);

        // Initialiser le ViewModel
        viewModel = new ViewModelProvider(this).get(SignalementViewModel.class);

        // Configurer la carte
        setupMap();

        // Ajouter les points d'intérêt
        addPointsOfInterest();

        // Activer ma position si permission accordée
        enableMyLocation();

        return view;
    }

    private void initViews(View view) {
        mapView = view.findViewById(R.id.mapView);
        fabMyLocation = view.findViewById(R.id.fabMyLocation);
        fabShowAllPoints = view.findViewById(R.id.fabShowAllPoints);
        fabZoomIn = view.findViewById(R.id.fabZoomIn);
        fabZoomOut = view.findViewById(R.id.fabZoomOut);
        textViewInfo = view.findViewById(R.id.textViewInfo);

        // Listeners
        fabMyLocation.setOnClickListener(v -> goToMyLocation());
        fabShowAllPoints.setOnClickListener(v -> showAllPoints());
        fabZoomIn.setOnClickListener(v -> mapView.getController().zoomIn());
        fabZoomOut.setOnClickListener(v -> mapView.getController().zoomOut());
    }

    private void setupMap() {
        // Configurer la carte
        mapView.setTileSource(TileSourceFactory.MAPNIK); // Style OpenStreetMap standard
        mapView.setMultiTouchControls(true); // Activer le pinch-to-zoom
        mapView.setBuiltInZoomControls(false); // Désactiver les contrôles par défaut

        // Controller pour manipuler la carte
        mapController = mapView.getController();
        mapController.setZoom(15.0); // Niveau de zoom initial

        // Centrer sur ESATIC
        GeoPoint esaticPoint = new GeoPoint(Constants.ESATIC_LATITUDE, Constants.ESATIC_LONGITUDE);
        mapController.setCenter(esaticPoint);

        Log.d(TAG, "✅ Carte OpenStreetMap configurée");
    }

    private void addPointsOfInterest() {
        List<PointInteret> points = LocationUtils.getPointsInteretESATIC();

        Log.d(TAG, "📍 Ajout de " + points.size() + " points d'intérêt");

        int count = 0;
        for (PointInteret point : points) {
            GeoPoint geoPoint = new GeoPoint(point.getLatitude(), point.getLongitude());

            Marker marker = new Marker(mapView);
            marker.setPosition(geoPoint);
            marker.setTitle(point.getNom());
            marker.setSnippet(point.getDescription() + "\nType: " + point.getType());
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

            // Icône selon le type
            Drawable icon = getIconeParType(point.getType());
            if (icon != null) {
                marker.setIcon(icon);
            }

            // Click sur le marker
            marker.setOnMarkerClickListener((m, mv) -> {
                // Trouver le point proche
                PointInteret pointProche = LocationUtils.trouverPointLePlusProche(
                        m.getPosition().getLatitude(),
                        m.getPosition().getLongitude()
                );

                String message = m.getTitle();
                if (pointProche != null && pointProche.getNom().equals(m.getTitle())) {
                    message += "\n" + pointProche.getDescription();
                    message += "\nType: " + pointProche.getType();
                }

                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                // Centrer sur le marker
                mapController.animateTo(m.getPosition());

                return true;
            });

            mapView.getOverlays().add(marker);
            count++;
        }

        mapView.invalidate();

        // Mettre à jour l'info
        textViewInfo.setText(count + " points d'intérêt");

        Log.d(TAG, "✅ " + count + " marqueurs ajoutés");
    }

    private int getCouleurParType(String type) {
        switch (type.toLowerCase()) {
            case "bâtiment académique":
                return Color.RED;
            case "service":
                return Color.BLUE;
            case "sport":
                return Color.GREEN;
            case "résidence":
                return Color.parseColor("#FF9800"); // Orange
            case "santé":
                return Color.parseColor("#E91E63"); // Rose
            case "banque":
                return Color.parseColor("#FFC107"); // Jaune
            case "restaurant":
                return Color.parseColor("#9C27B0"); // Violet
            case "transport":
                return Color.CYAN;
            default:
                return Color.GRAY;
        }
    }

    /**
     * Obtenir l'icône selon le type de point d'intérêt
     */
    /**
     * Obtenir l'icône selon le type de point d'intérêt
     */
    private Drawable getIconeParType(String type) {
        int iconRes;

        switch (type.toLowerCase()) {
            case "administration":
            case "bâtiment académique":
                iconRes = R.drawable.poi_academic;
                break;

            case "bibliothèque":
                iconRes = R.drawable.poi_library;
                break;

            case "amphithéâtre":
                iconRes = R.drawable.poi_amphitheater;
                break;

            case "laboratoire":
                iconRes = R.drawable.poi_lab;
                break;

            case "restaurant":
            case "cafétéria":
                iconRes = R.drawable.poi_restaurant;
                break;

            case "sport":
                iconRes = R.drawable.poi_sport;
                break;

            case "service":
            case "parking":
                iconRes = R.drawable.poi_service;
                break;

            case "résidence":
                iconRes = R.drawable.poi_residence;
                break;

            default:
                iconRes = R.drawable.poi_default;
                break;
        }

        return ContextCompat.getDrawable(requireContext(), iconRes);
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Créer l'overlay de position
            myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), mapView);
            myLocationOverlay.enableMyLocation();
            myLocationOverlay.enableFollowLocation(); // Suivre la position

            mapView.getOverlays().add(myLocationOverlay);

            Log.d(TAG, "✅ Ma position activée sur la carte");
        } else {
            Log.w(TAG, "⚠️ Permission non accordée");
        }
    }

    private void goToMyLocation() {
        Log.d(TAG, "🔵 goToMyLocation() appelée");

        // Vérifier la permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }

        // Vérifier le GPS
        if (!GpsUtils.isGPSEnabled(requireContext())) {
            GpsUtils.showGPSDisabledDialog(requireContext());
            return;
        }

        // Récupérer la position
        viewModel.getCurrentLocation().observe(getViewLifecycleOwner(), location -> {
            if (location != null) {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();

                GeoPoint myPosition = new GeoPoint(currentLatitude, currentLongitude);
                mapController.animateTo(myPosition);
                mapController.setZoom(17.0);

                // Trouver le point le plus proche
                PointInteret pointProche = LocationUtils.trouverPointLePlusProche(currentLatitude, currentLongitude);
                if (pointProche != null) {
                    double distance = pointProche.distanceEnMetresVers(currentLatitude, currentLongitude);
                    String distanceFormatee = LocationUtils.formaterDistance(distance);

                    Toast.makeText(getContext(),
                            String.format("📍 Vous êtes ici !\nPrès de : %s (%s)",
                                    pointProche.getNom(), distanceFormatee),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "📍 Vous êtes ici !", Toast.LENGTH_SHORT).show();
                }

                Log.d(TAG, String.format("✅ Centré sur position : %.6f, %.6f",
                        currentLatitude, currentLongitude));
            } else {
                Toast.makeText(getContext(), "Impossible de récupérer la position", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAllPoints() {
        // Recentrer sur ESATIC et zoom out
        GeoPoint esaticPoint = new GeoPoint(Constants.ESATIC_LATITUDE, Constants.ESATIC_LONGITUDE);
        mapController.animateTo(esaticPoint);
        mapController.setZoom(14.0);

        Toast.makeText(getContext(), "🗺️ Vue d'ensemble affichée", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mapView != null) {
            mapView.onDetach();
        }
    }
}