package com.example.mycampuscompanion.model.api;

import com.example.mycampuscompanion.model.api.responses.NewsApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface définissant les endpoints de l'API REST
 */
public interface ApiService {

    /**
     * Récupérer les actualités depuis NewsAPI
     * GET /top-headlines?country=fr&category=technology&apiKey=XXX
     */
    @GET("top-headlines")
    Call<NewsApiResponse> getTopHeadlines(
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apiKey
    );

    /**
     * Alternative : Récupérer des posts depuis JSONPlaceholder (API de test)
     * GET /posts
     */
    // @GET("posts")
    // Call<List<Actualite>> getPosts();
}