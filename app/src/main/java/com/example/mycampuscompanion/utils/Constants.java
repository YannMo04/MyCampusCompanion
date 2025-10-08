package com.example.mycampuscompanion.utils;

public class Constants {

    public static final String BASE_URL = "https://newsapi.org/v2/";
    public static final String NEWS_API_KEY = "b1390513817743eabb79beba17a6593b";
    public static final String NEWS_COUNTRY = "";
    public static final String NEWS_CATEGORY = "technology";

    // Timeouts réseau (en secondes)
    public static final int CONNECT_TIMEOUT = 30;
    public static final int READ_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 30;

    // Permissions
    public static final int PERMISSION_REQUEST_CALL_PHONE = 100;
    public static final int PERMISSION_REQUEST_CAMERA = 101;
    public static final int PERMISSION_REQUEST_LOCATION = 102;
    public static final int PERMISSION_REQUEST_READ_MEDIA = 103;

    // Intent Request Codes
    public static final int REQUEST_IMAGE_CAPTURE = 200;
    public static final int REQUEST_VIDEO_CAPTURE = 201;
    public static final int REQUEST_PICK_IMAGE = 202;

    // Fichiers média
    public static final int MAX_VIDEO_DURATION_SECONDS = 30;
    public static final int MAX_IMAGE_SIZE_KB = 500;

    // ========== COORDONNÉES ESATIC (Abidjan-Cocody) ========== //
    // Centre du campus ESATIC
    public static final double ESATIC_LATITUDE = 5.290473;
    public static final double ESATIC_LONGITUDE = -3.9991667;

    // Point d'intérêt principal (Administration)
    public static final double POI_LATITUDE = 5.290473;
    public static final double POI_LONGITUDE = -3.9991667;
    public static final String POI_NAME = "ESATIC - Administration";

    // Préférences partagées
    public static final String PREFS_NAME = "MyCampusPrefs";
    public static final String PREF_FIRST_LAUNCH = "first_launch";
    public static final String PREF_USER_TOKEN = "user_token";

    // Types de média
    public static final String MEDIA_TYPE_PHOTO = "PHOTO";
    public static final String MEDIA_TYPE_VIDEO = "VIDEO";

    // Statuts de signalement
    public static final String STATUT_EN_ATTENTE = "EN_ATTENTE";
    public static final String STATUT_TRAITE = "TRAITE";

    // Types de points d'intérêt
    public static final String TYPE_BATIMENT_ACADEMIQUE = "Bâtiment Académique";
    public static final String TYPE_SERVICE = "Service";
    public static final String TYPE_SPORT = "Sport";
    public static final String TYPE_RESIDENCE = "Résidence";
    public static final String TYPE_SANTE = "Santé";
    public static final String TYPE_BANQUE = "Banque";
    public static final String TYPE_RESTAURANT = "Restaurant";
    public static final String TYPE_TRANSPORT = "Transport";
    public static final String TYPE_POINT_REPERE = "Point de Repère";

    // Empêcher l'instanciation
    private Constants() {
        throw new AssertionError("Cannot instantiate Constants class");
    }
}