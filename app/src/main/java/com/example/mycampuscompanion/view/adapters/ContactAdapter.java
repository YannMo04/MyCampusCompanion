package com.example.mycampuscompanion.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycampuscompanion.R;
import com.example.mycampuscompanion.model.entities.Contact;
import com.example.mycampuscompanion.utils.PermissionUtils;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter pour afficher les contacts dans un RecyclerView
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private List<Contact> contacts;

    public ContactAdapter(Context context) {
        this.context = context;
        this.contacts = new ArrayList<>();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);

        // Afficher les informations
        holder.textViewNom.setText(contact.getNomComplet());
        holder.textViewService.setText(contact.getService() + " - " + contact.getPoste());
        holder.textViewTelephone.setText("📞 " + contact.getTelephone());

        // Afficher les initiales
        String initiales = getInitials(contact.getPrenom(), contact.getNom());
        holder.textViewInitials.setText(initiales);

        // Bouton Appel
        holder.buttonCall.setOnClickListener(v -> {
            makePhoneCall(contact.getTelephone());
        });

        // Bouton SMS
        holder.buttonSms.setOnClickListener(v -> {
            sendSMS(contact.getTelephone());
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    /**
     * Mettre à jour la liste des contacts
     */
    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    /**
     * Obtenir les initiales d'un nom
     */
    private String getInitials(String prenom, String nom) {
        String initiales = "";
        if (prenom != null && !prenom.isEmpty()) {
            initiales += prenom.charAt(0);
        }
        if (nom != null && !nom.isEmpty()) {
            initiales += nom.charAt(0);
        }
        return initiales.toUpperCase();
    }

    /**
     * Lancer un appel téléphonique
     */
    private void makePhoneCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        // Vérifier la permission
        if (PermissionUtils.isPermissionGranted(context, android.Manifest.permission.CALL_PHONE)) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Permission d'appel requise", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Envoyer un SMS
     */
    private void sendSMS(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", "Bonjour, je vous contacte depuis MyCampus Companion.");

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Aucune application SMS disponible", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ViewHolder pour un contact
     */
    static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView textViewInitials;
        TextView textViewNom;
        TextView textViewService;
        TextView textViewTelephone;
        MaterialButton buttonCall;
        MaterialButton buttonSms;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewInitials = itemView.findViewById(R.id.textViewInitials);
            textViewNom = itemView.findViewById(R.id.textViewNom);
            textViewService = itemView.findViewById(R.id.textViewService);
            textViewTelephone = itemView.findViewById(R.id.textViewTelephone);
            buttonCall = itemView.findViewById(R.id.buttonCall);
            buttonSms = itemView.findViewById(R.id.buttonSms);
        }
    }
}