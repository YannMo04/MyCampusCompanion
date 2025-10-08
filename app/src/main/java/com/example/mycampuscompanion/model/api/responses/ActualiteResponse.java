package com.example.mycampuscompanion.model.api.responses;


import com.example.mycampuscompanion.model.entities.Actualite;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Classe de réponse pour l'API des actualités
 * Utilisée si l'API retourne un format enveloppé
 */
public class ActualiteResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Actualite> data;

    // Constructeur
    public ActualiteResponse() {
    }

    // Getters et Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Actualite> getData() {
        return data;
    }

    public void setData(List<Actualite> data) {
        this.data = data;
    }
}