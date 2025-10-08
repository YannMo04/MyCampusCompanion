package com.example.mycampuscompanion.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entité représentant un signalement d'incident
 */
@Entity(tableName = "signalements")
public class Signalement {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String type;              // Type de signalement
    private String titre;             // Titre du signalement
    private String description;       // Description détaillée
    private String photoPath;         // Chemin vers la photo
    private String videoPath;         // Chemin vers la vidéo
    private String mediaType;         // "PHOTO" ou "VIDEO"
    private double latitude;          // Latitude GPS
    private double longitude;         // Longitude GPS
    private String dateCreation;      // Date de création
    private String statut;            // "EN_ATTENTE" ou "TRAITE"

    // Constructeur vide requis par Room
    public Signalement() {
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    // Méthodes utilitaires
    public boolean isPhoto() {
        return "PHOTO".equals(mediaType);
    }

    public boolean isVideo() {
        return "VIDEO".equals(mediaType);
    }
}