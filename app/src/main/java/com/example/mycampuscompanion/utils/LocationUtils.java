package com.example.mycampuscompanion.utils;

import com.example.mycampuscompanion.model.entities.PointInteret;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

    /**
     * Liste des points d'intérêt du campus ESATIC et environs
     */
    public static List<PointInteret> getPointsInteretESATIC() {
        List<PointInteret> points = new ArrayList<>();

        // Campus ESATIC - Bâtiments principaux
        points.add(new PointInteret(
                "ESATIC - Administration",
                5.290572,
                -3.9988684,
                "Administration et salles de cours principales",
                "Administration"  // Type spécifique
        ));

        points.add(new PointInteret(
                "ESATIC - Bibliothèque",
                5.290747,
                -3.998308,
                "Bibliothèque universitaire et centre de documentation",
                "Bibliothèque"  // Type spécifique
        ));

        points.add(new PointInteret(
                "ESATIC - Amphithéâtre",
                5.290314,
                -3.998218,
                "Grand amphithéâtre pour les conférences",
                "Amphithéâtre"  // Type spécifique
        ));

        points.add(new PointInteret(
                "ESATIC - M2SIGL",
                5.291084,
                -3.998812,
                "Notre salle de classe M2SIGL",
                "Bâtiment Académique"  // Type spécifique
        ));

        points.add(new PointInteret(
                "ESATIC - Restaurant",
                5.290030,
                -3.998100,
                "Restaurant universitaire et cafétéria",
                "Restaurant"  // Type spécifique
        ));

        points.add(new PointInteret(
                "ESATIC - Terrain de Sport",
                5.290721,
                -3.999347,
                "Installations sportives et terrains",
                "Sport"  // Type spécifique
        ));

        points.add(new PointInteret(
                "ESATIC - Chez Abidal",
                5.290483,
                -3.999202,
                "La boutique d'Abidal",
                "Service"  // Type modifié
        ));

        points.add(new PointInteret(
                "ESATIC - Parking Principal",
                5.290645,
                -3.999052,
                "Parking pour étudiants et personnel",
                "Service"  // Type spécifique
        ));

        points.add(new PointInteret(
                "ESATIC - Résidence Universitaire",
                5.290084,
                -3.998471,
                "Résidence pour étudiants",
                "Résidence"  // Type spécifique
        ));

        return points;
    }
    /**
     * Trouver le point d'intérêt le plus proche d'une position donnée
     */
    public static PointInteret trouverPointLePlusProche(double latitude, double longitude) {
        List<PointInteret> points = getPointsInteretESATIC();

        if (points.isEmpty()) {
            return null;
        }

        PointInteret plusProche = points.get(0);
        double distanceMin = plusProche.distanceEnMetresVers(latitude, longitude);

        for (PointInteret point : points) {
            double distance = point.distanceEnMetresVers(latitude, longitude);
            if (distance < distanceMin) {
                distanceMin = distance;
                plusProche = point;
            }
        }

        return plusProche;
    }

    /**
     * Formater une distance en mètres ou kilomètres
     */
    public static String formaterDistance(double distanceEnMetres) {
        if (distanceEnMetres < 1000) {
            return String.format("%.0f m", distanceEnMetres);
        } else {
            return String.format("%.2f km", distanceEnMetres / 1000);
        }
    }

    /**
     * Calculer la distance entre deux points GPS (formule de Haversine)
     */
    public static double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Rayon de la Terre en mètres

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    /**
     * Obtenir tous les points d'intérêt d'un type spécifique
     */
    public static List<PointInteret> getPointsParType(String type) {
        List<PointInteret> tousLesPoints = getPointsInteretESATIC();
        List<PointInteret> pointsFiltres = new ArrayList<>();

        for (PointInteret point : tousLesPoints) {
            if (point.getType().equalsIgnoreCase(type)) {
                pointsFiltres.add(point);
            }
        }

        return pointsFiltres;
    }

    /**
     * Obtenir les N points d'intérêt les plus proches
     */
    public static List<PointInteret> getPointsLesPlusProches(double latitude, double longitude, int nombre) {
        List<PointInteret> tousLesPoints = getPointsInteretESATIC();

        // Trier par distance
        tousLesPoints.sort((p1, p2) -> {
            double dist1 = p1.distanceEnMetresVers(latitude, longitude);
            double dist2 = p2.distanceEnMetresVers(latitude, longitude);
            return Double.compare(dist1, dist2);
        });

        // Retourner les N premiers
        return tousLesPoints.subList(0, Math.min(nombre, tousLesPoints.size()));
    }
}