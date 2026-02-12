package com.example.applicationrftg;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ListefilmsTask extends AsyncTask<URL,Integer,String> {
    private volatile ListefilmsActivity screen;  //référence à l'écran

    public ListefilmsTask(ListefilmsActivity s) {
        this.screen = s;
    }
    @Override
    protected void onPreExecute() {
        //prétraitement de l'appel
    }

    @Override
    protected String doInBackground(URL...urls) {
        String sResultatAppel = null;
        URL urlAAppeler = urls[0];
        sResultatAppel = appelerServiceRestHttp(urlAAppeler);
        return sResultatAppel;
    }

    @Override
    protected void onPostExecute(String resultat) {
        System.out.println(">>>onPostExecute / resultat=" + resultat);
        this.screen.mettreAJourActivityApresAppelRest(resultat);
    }

    private String appelerServiceRestHttp(URL urlAAppeler) {
        HttpURLConnection urlConnection = null;
        int responseCode = -1;
        String sResultatAppel = "";
        try {
            urlConnection = (HttpURLConnection) urlAAppeler.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("User-Agent", System.getProperty("http.agent"));
            urlConnection.setRequestProperty("Authorization", UrlManager.getAuthHeader());

            responseCode = urlConnection.getResponseCode();
            Log.d("mydebug", ">>>Code de réponse HTTP : " + responseCode);

            if (responseCode >= 200 && responseCode < 300) {
                // MÉTHODE DE LECTURE - Plus efficace
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                java.util.Scanner scanner = new java.util.Scanner(in, "UTF-8").useDelimiter("\\A");
                sResultatAppel = scanner.hasNext() ? scanner.next() : "";
                scanner.close();
                in.close();

                Log.d("mydebug", ">>> Réponse HTTP " + responseCode + " - Longueur : " + sResultatAppel.length());
                Log.d("mydebug", ">>> Début réponse : " + sResultatAppel.substring(0, Math.min(200, sResultatAppel.length())));

            } else {
                // Lire le message d'erreur
                InputStream errorStream = urlConnection.getErrorStream();
                if (errorStream != null) {
                    java.util.Scanner scanner = new java.util.Scanner(errorStream, "UTF-8").useDelimiter("\\A");
                    String errorMsg = scanner.hasNext() ? scanner.next() : "";
                    scanner.close();
                    errorStream.close();
                    Log.e("mydebug", ">>> ERREUR HTTP " + responseCode + " : " + errorMsg);
                    sResultatAppel = "ERREUR " + responseCode + ": " + errorMsg;
                }
            }

        } catch (IOException ioe) {
            Log.e("mydebug", ">>>Pour appelerServiceRestHttp - IOException ioe =" + ioe.toString());
            sResultatAppel = "ERREUR IOException: " + ioe.getMessage();
        } catch (Exception e) {
            Log.e("mydebug", ">>>Pour appelerServiceRestHttp - Exception=" + e.toString());
            sResultatAppel = "ERREUR Exception: " + e.getMessage();
        }

        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        return sResultatAppel;
    }
}

