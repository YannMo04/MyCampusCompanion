package com.example.mycampuscompanion.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utilitaire pour formater et manipuler les dates
 */
public class DateUtils {

    // Formats de date
    private static final String FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String FORMAT_DISPLAY = "dd/MM/yyyy à HH:mm";
    private static final String FORMAT_DATE_ONLY = "dd/MM/yyyy";

    /**
     * Obtenir la date actuelle au format String
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_API, Locale.FRANCE);
        return sdf.format(new Date());
    }

    /**
     * Formater une date pour l'affichage
     */
    public static String formatDateForDisplay(String dateString) {
        try {
            SimpleDateFormat apiFormat = new SimpleDateFormat(FORMAT_API, Locale.FRANCE);
            SimpleDateFormat displayFormat = new SimpleDateFormat(FORMAT_DISPLAY, Locale.FRANCE);
            Date date = apiFormat.parse(dateString);
            return displayFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;  // Retourner la date brute en cas d'erreur
        }
    }

    /**
     * Formater une date (date uniquement, sans heure)
     */
    public static String formatDateOnly(String dateString) {
        try {
            SimpleDateFormat apiFormat = new SimpleDateFormat(FORMAT_API, Locale.FRANCE);
            SimpleDateFormat displayFormat = new SimpleDateFormat(FORMAT_DATE_ONLY, Locale.FRANCE);
            Date date = apiFormat.parse(dateString);
            return displayFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }
}
