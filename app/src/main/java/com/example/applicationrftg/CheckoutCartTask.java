package com.example.applicationrftg;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckoutCartTask extends AsyncTask<Integer, Void, Integer> {

    public interface Callback {
        void onResult(boolean success, int itemsCount);
    }

    private Callback callback;

    public CheckoutCartTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        int customerId = params[0];

        try {
            URL url = new URL(UrlManager.getURLConnexion() + "/cart/checkout");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.e30.jg2m4pLbAlZv1h5uPQ6fU38X23g65eXMX8q-SXuIPDg");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONObject body = new JSONObject();
            body.put("customerId", customerId);

            Log.d("CheckoutCartTask", "POST " + url + " - Body: " + body);

            OutputStream os = connection.getOutputStream();
            os.write(body.toString().getBytes("UTF-8"));
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.optInt("itemsCount", 0);
            }
            return -1;

        } catch (Exception e) {
            Log.e("CheckoutCartTask", "Error: " + e.getMessage(), e);
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer itemsCount) {
        if (callback != null) {
            callback.onResult(itemsCount >= 0, itemsCount);
        }
    }
}
