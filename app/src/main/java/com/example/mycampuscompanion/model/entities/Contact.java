package com.example.mycampuscompanion.model.entities;

/**
 * Entité représentant un contact de l'annuaire universitaire
 * Pas de Room car données chargées depuis JSON local
 */
public class Contact {

    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String service;
    private String poste;
    private String photoUrl;

    // Constructeur vide
    public Contact() {
    }

    // Constructeur complet
    public Contact(int id, String nom, String prenom, String telephone,
                   String email, String service, String poste, String photoUrl) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.service = service;
        this.poste = poste;
        this.photoUrl = photoUrl;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    // Méthode utilitaire pour afficher le nom complet
    public String getNomComplet() {
        return prenom + " " + nom;
    }
}