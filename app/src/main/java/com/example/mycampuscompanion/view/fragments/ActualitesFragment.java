package com.example.mycampuscompanion.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mycampuscompanion.R;
import com.example.mycampuscompanion.view.adapters.ActualiteAdapter;
import com.example.mycampuscompanion.viewmodel.ActualiteViewModel;

/**
 * Fragment affichant la liste des actualités
 */
public class ActualitesFragment extends Fragment {

    private static final String TAG = "ActualitesFragment";

    private ActualiteViewModel viewModel;
    private ActualiteAdapter adapter;

    // Vues
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private LinearLayout layoutError;
    private Button buttonRetry;
    private TextView textViewEmpty;
    private TextView textViewOffline;

    public ActualitesFragment() {
        // Constructeur vide requis
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actualites, container, false);

        Log.d(TAG, "📰 ActualitesFragment créé");

        // Initialisation des vues
        initViews(view);

        // Initialisation du ViewModel
        initViewModel();

        // Observer les données
        observeData();

        // Charger les actualités au démarrage
        viewModel.refreshActualites();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewActualites);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progressBar = view.findViewById(R.id.progressBar);
        layoutError = view.findViewById(R.id.layoutError);
        buttonRetry = view.findViewById(R.id.buttonRetry);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        textViewOffline = view.findViewById(R.id.textViewOffline);

        // Configuration du RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ActualiteAdapter(getContext());
        recyclerView.setAdapter(adapter);

        // Pull-to-refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Log.d(TAG, "🔄 Pull-to-refresh déclenché");
            viewModel.refreshActualites();
        });

        // Bouton Réessayer
        buttonRetry.setOnClickListener(v -> {
            Log.d(TAG, "🔄 Bouton Réessayer cliqué");
            viewModel.refreshActualites();
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ActualiteViewModel.class);
    }

    private void observeData() {
        // Observer les actualités (depuis le cache SQLite)
        viewModel.getAllActualites().observe(getViewLifecycleOwner(), actualites -> {
            if (actualites != null && !actualites.isEmpty()) {
                Log.d(TAG, "📰 " + actualites.size() + " actualités reçues");
                adapter.setActualites(actualites);
                showContent();
            } else {
                Log.d(TAG, "⚠️ Aucune actualité en cache");
                showEmpty();
            }
        });

        // Observer le statut de chargement
        viewModel.getLoadingStatus().observe(getViewLifecycleOwner(), status -> {
            swipeRefreshLayout.setRefreshing(false);

            switch (status) {
                case "loading":
                    Log.d(TAG, "⏳ Chargement en cours...");
                    showLoading();
                    break;

                case "success":
                    Log.d(TAG, "✅ Chargement réussi");
                    Toast.makeText(getContext(), "✅ Actualités mises à jour", Toast.LENGTH_SHORT).show();
                    hideOfflineBanner();
                    break;

                case "error":
                    Log.e(TAG, "❌ Erreur de chargement");
                    Toast.makeText(getContext(), "❌ Erreur de chargement", Toast.LENGTH_SHORT).show();
                    // Si le cache est vide, afficher l'erreur
                    if (adapter.getItemCount() == 0) {
                        showError();
                    }
                    break;

                case "offline":
                    Log.w(TAG, "📡 Mode hors-ligne");
                    Toast.makeText(getContext(), "📡 Pas de connexion - Mode hors-ligne", Toast.LENGTH_SHORT).show();
                    showOfflineBanner();
                    break;
            }
        });
    }

    /**
     * Afficher le contenu (liste d'actualités)
     */
    private void showContent() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
        textViewEmpty.setVisibility(View.GONE);
    }

    /**
     * Afficher l'état de chargement
     */
    private void showLoading() {
        // Afficher le spinner seulement si la liste est vide
        if (adapter.getItemCount() == 0) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        layoutError.setVisibility(View.GONE);
        textViewEmpty.setVisibility(View.GONE);
    }

    /**
     * Afficher l'état d'erreur
     */
    private void showError() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        textViewEmpty.setVisibility(View.GONE);
    }

    /**
     * Afficher l'état vide
     */
    private void showEmpty() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
        textViewEmpty.setVisibility(View.VISIBLE);
    }

    /**
     * Afficher le bandeau "Mode hors-ligne"
     */
    private void showOfflineBanner() {
        textViewOffline.setVisibility(View.VISIBLE);
    }

    /**
     * Cacher le bandeau "Mode hors-ligne"
     */
    private void hideOfflineBanner() {
        textViewOffline.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "🔄 Fragment repris - Rafraîchissement des actualités");
        // Optionnel : Rafraîchir à chaque retour sur le fragment
        // viewModel.refreshActualites();
    }
}