package com.example.mycampuscompanion.model.api.responses;

import com.example.mycampuscompanion.model.entities.Actualite;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Classe de réponse pour NewsAPI
 * Format retourné par l'API :
 * {
 *   "status": "ok",
 *   "totalResults": 38,
 *   "articles": [...]
 * }
 */
public class NewsApiResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("articles")
    private List<Article> articles;

    // Constructeur
    public NewsApiResponse() {
    }

    // Getters
    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<Article> getArticles() {
        return articles;
    }

    /**
     * Classe interne pour représenter un article NewsAPI
     */
    public static class Article {
        @SerializedName("title")
        private String title;

        @SerializedName("description")
        private String description;

        @SerializedName("content")
        private String content;

        @SerializedName("url")
        private String url;

        @SerializedName("urlToImage")
        private String urlToImage;

        @SerializedName("publishedAt")
        private String publishedAt;

        @SerializedName("author")
        private String author;

        // Getters
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getContent() { return content; }
        public String getUrl() { return url; }
        public String getUrlToImage() { return urlToImage; }
        public String getPublishedAt() { return publishedAt; }
        public String getAuthor() { return author; }

        /**
         * Convertir un Article NewsAPI en Actualite (notre entité)
         */
        public Actualite toActualite() {
            Actualite actualite = new Actualite();
            actualite.setTitre(title);
            actualite.setDescription(description);
            actualite.setContenu(content != null ? content : description);
            actualite.setImageUrl(urlToImage);
            actualite.setDatePublication(publishedAt);
            actualite.setAuteur(author != null ? author : "Auteur inconnu");
            return actualite;
        }
    }
}