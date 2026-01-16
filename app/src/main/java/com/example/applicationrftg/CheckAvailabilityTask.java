package com.example.applicationrftg;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckAvailabilityTask extends AsyncTask<Integer, Void, Integer> {

    // Interface pour le callback
    public interface AvailabilityCallback {
        void onAvailabilityChecked(int filmId, int availableCount);
    }

    private AvailabilityCallback callback;
    private int filmId;

    public CheckAvailabilityTask(AvailabilityCallback callback) {
        this.callback = callback;
    }

    // Constructeur pour compatibilité avec ListefilmsActivity
    public CheckAvailabilityTask(ListefilmsActivity activity) {
        this.callback = activity::handleAvailabilityResult;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        filmId = params[0];

        try {
            // URL de l'API
            URL url = new URL(UrlManager.getURLConnexion() + "/inventories/available/film/" + filmId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configuration de la requête GET
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.e30.jg2m4pLbAlZv1h5uPQ6fU38X23g65eXMX8q-SXuIPDg");

            Log.d("CheckAvailability", "Sending request to: " + url.toString());

            // Lire la réponse
            int responseCode = connection.getResponseCode();
            Log.d("CheckAvailability", "Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parser la réponse JSON
                String responseBody = response.toString();
                Log.d("CheckAvailability", "Response body for film " + filmId + ": " + responseBody);

                // La réponse est un tableau JSON d'objets Inventory
                // Chaque objet a: inventoryId, film (objet complet), storeId, lastUpdate
                // Chaque élément du tableau = 1 DVD disponible
                JSONArray jsonArray = new JSONArray(responseBody);
                int availableCount = jsonArray.length();

                Log.d("CheckAvailability", "Film " + filmId + " has " + availableCount + " copies available");
                return availableCount;
            } else {
                Log.e("CheckAvailability", "HTTP Error: " + responseCode + " for film " + filmId);

                if (responseCode == 403) {
                    Log.e("CheckAvailability", "403 FORBIDDEN - L'endpoint nécessite probablement une authentification");
                }

                // Lire le message d'erreur
                try {
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    errorReader.close();
                    Log.e("CheckAvailability", "Error response body: " + errorResponse.toString());
                } catch (Exception ex) {
                    Log.e("CheckAvailability", "Could not read error response: " + ex.getMessage());
                }

                return 0; // En cas d'erreur, on considère qu'il n'y a pas de stock
            }

        } catch (Exception e) {
            Log.e("CheckAvailability", "Error checking availability for film " + filmId + ": " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    protected void onPostExecute(Integer availableCount) {
        if (callback != null) {
            callback.onAvailabilityChecked(filmId, availableCount);
        }
    }
}