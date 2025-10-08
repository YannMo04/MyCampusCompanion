package com.example.mycampuscompanion.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * Entité représentant une actualité du campus
 * Stockée en cache SQLite via Room
 */
@Entity(tableName = "actualites")


public class Actualite {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String titre;
    private String description;
    private String contenu;
    private String imageUrl;
    private String datePublication;
    private String auteur;

    // Constructeur vide (requis par Room)
    public Actualite() {
    }

    // Constructeur complet
    public Actualite(String titre, String description, String contenu,
                     String imageUrl, String datePublication, String auteur) {
        this.titre = titre;
        this.description = description;
        this.contenu = contenu;
        this.imageUrl = imageUrl;
        this.datePublication = datePublication;
        this.auteur = auteur;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(String datePublication) {
        this.datePublication = datePublication;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }
}