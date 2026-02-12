package com.example.applicationrftg;

/* Liste déroulante serveur + token JWT */
public class UrlManager {
    private static String URLConnexion = "";
    private static String token = "";

    public static String getURLConnexion() {
        return URLConnexion;
    }

    public static void setURLConnexion(String url) {
        URLConnexion = url;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String t) {
        token = t;
    }

    public static String getAuthHeader() {
        return "Bearer " + token;
    }
}
