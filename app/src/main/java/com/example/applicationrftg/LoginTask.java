package com.example.applicationrftg;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginTask extends AsyncTask<String, Void, Integer> {

    private MainActivity mainActivity;

    public LoginTask(MainActivity activity) {
        this.mainActivity = activity;
    }

    @Override
    protected Integer doInBackground(String... params) {
        String email = params[0];
        String password = params[1];

        try {
            // URL de l'API
            URL url = new URL(UrlManager.getURLConnexion() + "/customers/verify");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configuration de la requête POST
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Créer le JSON body
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);

            Log.d("LoginTask", "Sending request to: " + url.toString());
            Log.d("LoginTask", "Request body: " + jsonBody.toString());

            // Envoyer la requête
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(jsonBody.toString());
            writer.flush();
            writer.close();
            outputStream.close();

            // Lire la réponse
            int responseCode = connection.getResponseCode();
            Log.d("LoginTask", "Response Code: " + responseCode);

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
                Log.d("LoginTask", "Response body: " + responseBody);

                JSONObject jsonResponse = new JSONObject(responseBody);
                int customerId = jsonResponse.getInt("customerId");

                Log.d("LoginTask", "Customer ID: " + customerId);
                return customerId;
            } else {
                Log.e("LoginTask", "HTTP Error: " + responseCode);

                // Lire le message d'erreur
                try {
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    errorReader.close();
                    Log.e("LoginTask", "Error response: " + errorResponse.toString());
                } catch (Exception ex) {
                    Log.e("LoginTask", "Could not read error response");
                }

                return -1;
            }

        } catch (Exception e) {
            Log.e("LoginTask", "Error during login: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer customerId) {
        mainActivity.handleLoginResult(customerId);
    }
}