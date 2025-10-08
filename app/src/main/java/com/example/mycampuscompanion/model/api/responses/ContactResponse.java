package com.example.mycampuscompanion.model.api.responses;


import com.example.mycampuscompanion.model.entities.Contact;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Classe de réponse pour l'API des contacts (si utilisée)
 */
public class ContactResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Contact> data;

    // Constructeur
    public ContactResponse() {
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

    public List<Contact> getData() {
        return data;
    }

    public void setData(List<Contact> data) {
        this.data = data;
    }
}
