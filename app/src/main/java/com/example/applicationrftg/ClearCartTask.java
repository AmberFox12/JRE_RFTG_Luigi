package com.example.applicationrftg;

import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class ClearCartTask extends AsyncTask<Integer, Void, Boolean> {

    public interface Callback {
        void onResult(boolean success);
    }

    private Callback callback;

    public ClearCartTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        int customerId = params[0];

        try {
            URL url = new URL(UrlManager.getURLConnexion() + "/cart/clear/" + customerId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", UrlManager.getAuthHeader());

            Log.d("ClearCartTask", "DELETE " + url);

            int responseCode = connection.getResponseCode();
            Log.d("ClearCartTask", "Response: " + responseCode);
            return responseCode == HttpURLConnection.HTTP_OK;

        } catch (Exception e) {
            Log.e("ClearCartTask", "Error: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (callback != null) {
            callback.onResult(success);
        }
    }
}