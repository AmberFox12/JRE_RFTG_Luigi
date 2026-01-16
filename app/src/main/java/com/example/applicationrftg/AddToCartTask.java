package com.example.applicationrftg;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddToCartTask extends AsyncTask<Integer, Void, Boolean> {

    public interface Callback {
        void onResult(boolean success, String message);
    }

    private Callback callback;
    private String message = "";

    public AddToCartTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        int customerId = params[0];
        int filmId = params[1];

        try {
            URL url = new URL(UrlManager.getURLConnexion() + "/cart/add");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.e30.jg2m4pLbAlZv1h5uPQ6fU38X23g65eXMX8q-SXuIPDg");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONObject body = new JSONObject();
            body.put("customerId", customerId);
            body.put("filmId", filmId);

            Log.d("AddToCartTask", "POST " + url + " - Body: " + body);

            OutputStream os = connection.getOutputStream();
            os.write(body.toString().getBytes("UTF-8"));
            os.close();

            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    responseCode == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            message = jsonResponse.optString("message", "");

            Log.d("AddToCartTask", "Response: " + responseCode + " - " + response);
            return responseCode == HttpURLConnection.HTTP_OK;

        } catch (Exception e) {
            Log.e("AddToCartTask", "Error: " + e.getMessage(), e);
            message = "Erreur de connexion";
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (callback != null) {
            callback.onResult(success, message);
        }
    }
}
