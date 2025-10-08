package com.example.mycampuscompanion.view.fragments;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycampuscompanion.R;
import com.example.mycampuscompanion.utils.PermissionUtils;
import com.example.mycampuscompanion.view.adapters.ContactAdapter;
import com.example.mycampuscompanion.viewmodel.AnnuaireViewModel;

/**
 * Fragment affichant l'annuaire des contacts
 */
public class AnnuaireFragment extends Fragment {

    private AnnuaireViewModel viewModel;
    private ContactAdapter adapter;
    private RecyclerView recyclerView;
    private TextView textViewEmpty;

    // Nouveau système de permissions
    private ActivityResultLauncher<String> requestPermissionLauncher;

    public AnnuaireFragment() {
        // Constructeur vide requis
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialiser le launcher de permission AVANT onCreateView
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Toast.makeText(getContext(),
                                "Permission d'appel accordée",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(),
                                "Permission refusée. Vous ne pourrez pas passer d'appels.",
                                Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_annuaire, container, false);

        // Initialisation des vues
        initViews(view);

        // Initialisation du ViewModel
        initViewModel();

        // Demander la permission d'appel
        checkCallPermission();

        // Observer les données
        observeData();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewContacts);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContactAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(AnnuaireViewModel.class);
    }

    private void observeData() {
        viewModel.getAllContacts().observe(getViewLifecycleOwner(), contacts -> {
            if (contacts != null && !contacts.isEmpty()) {
                adapter.setContacts(contacts);
                recyclerView.setVisibility(View.VISIBLE);
                textViewEmpty.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                textViewEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Vérifier et demander la permission d'appel (nouveau système)
     */
    private void checkCallPermission() {
        if (!PermissionUtils.isPermissionGranted(requireContext(), Manifest.permission.CALL_PHONE)) {
            // Utiliser le nouveau launcher au lieu de requestPermissions()
            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
        }
    }
}