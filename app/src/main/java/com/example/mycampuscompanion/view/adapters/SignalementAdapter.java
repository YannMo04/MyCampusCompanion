package com.example.mycampuscompanion.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycampuscompanion.R;
import com.example.mycampuscompanion.model.entities.PointInteret;
import com.example.mycampuscompanion.model.entities.Signalement;
import com.example.mycampuscompanion.utils.Constants;
import com.example.mycampuscompanion.utils.DateUtils;
import com.example.mycampuscompanion.utils.LocationUtils;

import java.util.ArrayList;
import java.util.List;

public class SignalementAdapter extends RecyclerView.Adapter<SignalementAdapter.SignalementViewHolder> {

    private Context context;
    private List<Signalement> signalements;
    private OnSignalementClickListener listener;

    public interface OnSignalementClickListener {
        void onSignalementClick(Signalement signalement);
        void onDeleteClick(Signalement signalement, int position); // AJOUTÉ
    }

    public SignalementAdapter(Context context, OnSignalementClickListener listener) {
        this.context = context;
        this.signalements = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public SignalementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_signalement, parent, false);
        return new SignalementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignalementViewHolder holder, int position) {
        Signalement signalement = signalements.get(position);

        // Type
        holder.textViewType.setText(signalement.getType());

        // Titre
        holder.textViewTitre.setText(signalement.getTitre());

        // Description
        holder.textViewDescription.setText(signalement.getDescription());

        // Date
        String dateFormatted = DateUtils.formatDateForDisplay(signalement.getDateCreation());
        holder.textViewDate.setText("📅 " + dateFormatted);

        // Statut
        holder.textViewStatut.setText(signalement.getStatut());
        if (Constants.STATUT_EN_ATTENTE.equals(signalement.getStatut())) {
            holder.textViewStatut.setBackgroundColor(Color.parseColor("#FF9800"));
        } else if (Constants.STATUT_TRAITE.equals(signalement.getStatut())) {
            holder.textViewStatut.setBackgroundColor(Color.parseColor("#4CAF50"));
        }

        // Localisation
        if (signalement.getLatitude() != 0.0 && signalement.getLongitude() != 0.0) {
            PointInteret pointProche = LocationUtils.trouverPointLePlusProche(
                    signalement.getLatitude(),
                    signalement.getLongitude()
            );

            if (pointProche != null) {
                double distance = pointProche.distanceEnMetresVers(
                        signalement.getLatitude(),
                        signalement.getLongitude()
                );
                String distanceFormatee = LocationUtils.formaterDistance(distance);
                holder.textViewLocation.setText(String.format("📍 %s (%s)",
                        pointProche.getNom(), distanceFormatee));
            } else {
                holder.textViewLocation.setText(String.format("📍 %.4f, %.4f",
                        signalement.getLatitude(), signalement.getLongitude()));
            }
        } else {
            holder.textViewLocation.setText("📍 Non localisé");
        }

        // Média preview (Photo ou Vidéo)
        if (Constants.MEDIA_TYPE_PHOTO.equals(signalement.getMediaType()) &&
                signalement.getPhotoPath() != null && !signalement.getPhotoPath().isEmpty()) {

            // C'est une PHOTO
            holder.frameLayoutMedia.setVisibility(View.VISIBLE);
            holder.imageViewPreview.setImageURI(Uri.parse(signalement.getPhotoPath()));
            holder.linearLayoutVideoOverlay.setVisibility(View.GONE);

        } else if (Constants.MEDIA_TYPE_VIDEO.equals(signalement.getMediaType()) &&
                signalement.getVideoPath() != null && !signalement.getVideoPath().isEmpty()) {

            // C'est une VIDÉO - afficher un indicateur
            holder.frameLayoutMedia.setVisibility(View.VISIBLE);
            holder.imageViewPreview.setImageResource(android.R.color.black);
            holder.linearLayoutVideoOverlay.setVisibility(View.VISIBLE);

        } else {
            // Pas de média
            holder.frameLayoutMedia.setVisibility(View.GONE);
        }

        // Click sur l'item entier
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSignalementClick(signalement);
            }
        });

        // Click sur le bouton supprimer (AJOUTÉ)
        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(signalement, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return signalements.size();
    }

    public void setSignalements(List<Signalement> signalements) {
        this.signalements = signalements;
        notifyDataSetChanged();
    }

    // AJOUTÉ : Méthode pour supprimer un item avec animation
    public void removeItem(int position) {
        signalements.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, signalements.size());
    }

    // AJOUTÉ : Méthode pour récupérer un signalement à une position
    public Signalement getSignalementAt(int position) {
        return signalements.get(position);
    }

    static class SignalementViewHolder extends RecyclerView.ViewHolder {

        TextView textViewType;
        TextView textViewTitre;
        TextView textViewDescription;
        TextView textViewDate;
        TextView textViewStatut;
        TextView textViewLocation;
        FrameLayout frameLayoutMedia;
        ImageView imageViewPreview;
        LinearLayout linearLayoutVideoOverlay;
        ImageButton buttonDelete; // AJOUTÉ

        public SignalementViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewTitre = itemView.findViewById(R.id.textViewTitre);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewStatut = itemView.findViewById(R.id.textViewStatut);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            frameLayoutMedia = itemView.findViewById(R.id.frameLayoutMedia);
            imageViewPreview = itemView.findViewById(R.id.imageViewPreview);
            linearLayoutVideoOverlay = itemView.findViewById(R.id.linearLayoutVideoOverlay);
            buttonDelete = itemView.findViewById(R.id.buttonDelete); // AJOUTÉ
        }
    }
}