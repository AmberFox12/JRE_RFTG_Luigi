package com.example.applicationrftg;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetCartTask extends AsyncTask<Integer, Void, List<Rental>> {

    public interface Callback {
        void onCartLoaded(List<Rental> rentals);
    }

    private Callback callback;

    public GetCartTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected List<Rental> doInBackground(Integer... params) {
        int customerId = params[0];
        List<Rental> rentals = new ArrayList<>();

        try {
            URL url = new URL(UrlManager.getURLConnexion() + "/cart/" + customerId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.e30.jg2m4pLbAlZv1h5uPQ6fU38X23g65eXMX8q-SXuIPDg");
            connection.setRequestProperty("Accept", "application/json");

            Log.d("GetCartTask", "GET " + url);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonRental = jsonArray.getJSONObject(i);
                    Rental rental = new Rental();
                    rental.setRentalId(jsonRental.optInt("rentalId"));
                    rental.setInventoryId(jsonRental.optInt("inventoryId"));
                    rental.setCustomerId(jsonRental.optInt("customerId"));
                    rental.setStatusId(jsonRental.optInt("statusId"));

                    // Parser le film depuis inventory
                    if (jsonRental.has("inventory") && !jsonRental.isNull("inventory")) {
                        JSONObject inventoryJson = jsonRental.getJSONObject("inventory");
                        if (inventoryJson.has("film") && !inventoryJson.isNull("film")) {
                            JSONObject filmJson = inventoryJson.getJSONObject("film");
                            Film film = new Film();
                            film.setFilmId(filmJson.optInt("filmId"));
                            film.setTitle(filmJson.optString("title"));
                            film.setReleaseYear(filmJson.optInt("releaseYear"));
                            film.setRentalRate(filmJson.optDouble("rentalRate", 0.0));
                            film.setRating(filmJson.optString("rating"));
                            rental.setFilm(film);
                        }
                    }
                    rentals.add(rental);
                }
            }
        } catch (Exception e) {
            Log.e("GetCartTask", "Error: " + e.getMessage(), e);
        }
        return rentals;
    }

    @Override
    protected void onPostExecute(List<Rental> rentals) {
        if (callback != null) {
            callback.onCartLoaded(rentals);
        }
    }
}