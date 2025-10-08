package com.example.mycampuscompanion.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mycampuscompanion.model.api.RetrofitClient;
import com.example.mycampuscompanion.model.api.responses.NewsApiResponse;
import com.example.mycampuscompanion.model.database.AppDatabase;
import com.example.mycampuscompanion.model.database.dao.ActualiteDao;
import com.example.mycampuscompanion.model.entities.Actualite;
import com.example.mycampuscompanion.utils.Constants;
import com.example.mycampuscompanion.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository pour gérer les actualités
 * Pattern: Cache-First (SQLite puis API)
 */
public class ActualiteRepository {

    private static final String TAG = "ActualiteRepository";

    private ActualiteDao actualiteDao;
    private LiveData<List<Actualite>> allActualites;
    private Context context;
    private Executor executor;

    public ActualiteRepository(Context context) {
        this.context = context;
        AppDatabase database = AppDatabase.getInstance(context);
        actualiteDao = database.actualiteDao();
        allActualites = actualiteDao.getAllActualites();
        executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Récupérer toutes les actualités (du cache SQLite)
     */
    public LiveData<List<Actualite>> getAllActualites() {
        return allActualites;
    }

    /**
     * Rafraîchir les actualités depuis l'API
     */
    public MutableLiveData<String> refreshActualites() {
        MutableLiveData<String> statusLiveData = new MutableLiveData<>();

        // Vérifier la connexion Internet
        if (!NetworkUtils.isNetworkAvailable(context)) {
            Log.w(TAG, "Pas de connexion Internet");
            statusLiveData.postValue("offline");
            return statusLiveData;
        }

        Log.d(TAG, "🌐 Appel API pour récupérer les actualités...");
        statusLiveData.postValue("loading");

        // Appel API avec Retrofit
        Call<NewsApiResponse> call = RetrofitClient.getApiService().getTopHeadlines(
                Constants.NEWS_COUNTRY,
                Constants.NEWS_CATEGORY,
                Constants.NEWS_API_KEY
        );

        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NewsApiResponse newsResponse = response.body();

                    if ("ok".equals(newsResponse.getStatus())) {
                        Log.d(TAG, "✅ API Success : " + newsResponse.getTotalResults() + " articles");

                        // Convertir les articles en Actualites
                        List<Actualite> actualites = new ArrayList<>();
                        for (NewsApiResponse.Article article : newsResponse.getArticles()) {
                            actualites.add(article.toActualite());
                        }

                        // Sauvegarder dans SQLite (background thread)
                        saveActualitesToCache(actualites);

                        statusLiveData.postValue("success");
                    } else {
                        Log.e(TAG, "❌ API Error : Status = " + newsResponse.getStatus());
                        statusLiveData.postValue("error");
                    }
                } else {
                    Log.e(TAG, "❌ Response Error : Code = " + response.code());
                    statusLiveData.postValue("error");
                }
            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                Log.e(TAG, "❌ API Call Failed", t);
                statusLiveData.postValue("error");
            }
        });

        return statusLiveData;
    }

    /**
     * Sauvegarder les actualités dans le cache SQLite
     */
    private void saveActualitesToCache(List<Actualite> actualites) {
        executor.execute(() -> {
            try {
                // Supprimer les anciennes actualités
                actualiteDao.deleteAllActualites();

                // Insérer les nouvelles
                actualiteDao.insertActualites(actualites);

                Log.d(TAG, "💾 " + actualites.size() + " actualités sauvegardées dans SQLite");
            } catch (Exception e) {
                Log.e(TAG, "❌ Erreur lors de la sauvegarde dans SQLite", e);
            }
        });
    }
}