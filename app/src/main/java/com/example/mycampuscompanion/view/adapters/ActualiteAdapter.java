package com.example.mycampuscompanion.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mycampuscompanion.R;
import com.example.mycampuscompanion.model.entities.Actualite;
import com.example.mycampuscompanion.utils.DateUtils;
import com.example.mycampuscompanion.view.activities.ActualiteDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter pour afficher les actualités dans un RecyclerView
 */
public class ActualiteAdapter extends RecyclerView.Adapter<ActualiteAdapter.ActualiteViewHolder> {

    private Context context;
    private List<Actualite> actualites;

    public ActualiteAdapter(Context context) {
        this.context = context;
        this.actualites = new ArrayList<>();
    }

    @NonNull
    @Override
    public ActualiteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_actualite, parent, false);
        return new ActualiteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActualiteViewHolder holder, int position) {
        Actualite actualite = actualites.get(position);

        // Afficher le titre
        holder.textViewTitre.setText(actualite.getTitre());

        // Afficher la description
        if (actualite.getDescription() != null && !actualite.getDescription().isEmpty()) {
            holder.textViewDescription.setText(actualite.getDescription());
            holder.textViewDescription.setVisibility(View.VISIBLE);
        } else {
            holder.textViewDescription.setVisibility(View.GONE);
        }

        // Afficher la date (formatée)
        String dateFormatted = DateUtils.formatDateForDisplay(actualite.getDatePublication());
        holder.textViewDate.setText("📅 " + dateFormatted);

        // Afficher l'auteur
        String auteur = actualite.getAuteur() != null ? actualite.getAuteur() : "Auteur inconnu";
        holder.textViewAuteur.setText("✍️ " + auteur);

        // Charger l'image avec Glide
        if (actualite.getImageUrl() != null && !actualite.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(actualite.getImageUrl())
                    .placeholder(R.drawable.ic_actualites)
                    .error(R.drawable.ic_actualites)
                    .centerCrop()
                    .into(holder.imageViewActualite);
        } else {
            holder.imageViewActualite.setImageResource(R.drawable.ic_actualites);
        }

        // Clic sur l'item → Ouvrir la page de détail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActualiteDetailActivity.class);
            intent.putExtra("actualite_id", actualite.getId());
            intent.putExtra("actualite_titre", actualite.getTitre());
            intent.putExtra("actualite_description", actualite.getDescription());
            intent.putExtra("actualite_contenu", actualite.getContenu());
            intent.putExtra("actualite_imageUrl", actualite.getImageUrl());
            intent.putExtra("actualite_date", actualite.getDatePublication());
            intent.putExtra("actualite_auteur", actualite.getAuteur());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return actualites.size();
    }

    /**
     * Mettre à jour la liste des actualités
     */
    public void setActualites(List<Actualite> actualites) {
        this.actualites = actualites;
        notifyDataSetChanged();
        // TODO: Utiliser DiffUtil pour optimiser (Phase Performance)
    }

    /**
     * ViewHolder pour une actualité
     */
    static class ActualiteViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewActualite;
        TextView textViewTitre;
        TextView textViewDescription;
        TextView textViewDate;
        TextView textViewAuteur;

        public ActualiteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewActualite = itemView.findViewById(R.id.imageViewActualite);
            textViewTitre = itemView.findViewById(R.id.textViewTitre);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewAuteur = itemView.findViewById(R.id.textViewAuteur);
        }
    }
}