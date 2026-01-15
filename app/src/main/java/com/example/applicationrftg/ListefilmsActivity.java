package com.example.applicationrftg;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListefilmsActivity extends AppCompatActivity {

    private String listeFilmsResultat = "";
    private ListView listView;
    private FilmFormatageDonnées filmFormatageDonnées;
    private List<Film> filmList;
    private List<Film> filmListFiltree;
    private TextView textListeFilm;
    private EditText searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("mydebug", ">>>Demarrage de liste filmActivity");
        setContentView(R.layout.activity_listefilms);

        // Initialiser le PanierManager avec la base de données
        PanierManager.getInstance().initialiser(this);

        // Initialiser les vues
        listView = findViewById(R.id.truefilmliste);
        textListeFilm = findViewById(R.id.textlistefilm);
        searchField = findViewById(R.id.searchField);

        // Initialiser la liste de films
        filmList = new ArrayList<>();
        filmListFiltree = new ArrayList<>();

        // Créer l'adapter avec la liste filtrée
        filmFormatageDonnées = new FilmFormatageDonnées(this, filmListFiltree);
        listView.setAdapter(filmFormatageDonnées);

        // Configurer le champ de recherche
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrerFilms(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Appeler le service web
        URL urlAAppeler = null;
        try {
            urlAAppeler = new URL("http://10.0.2.2:8180/films");
            Log.d("mydebug", ">>> onCreate - URL à appeler : " + urlAAppeler.toString());
            new ListefilmsTask(this).execute(urlAAppeler, null, null);

        } catch (MalformedURLException mue) {
            Log.d("mydebug", ">>>Pour AppelerServiceRestGETTask - Mal-formedURLException mue=" + mue.toString());
            textListeFilm.setText("Erreur: URL invalide");
        } finally {
            urlAAppeler = null;
        }
    }

    public void ConnectToPanierPage(View view) {
        Intent intent = new Intent(this, PanierActivity.class);
        startActivity(intent);
    }

    public void ConnectToDetailsPage(View view) {
        Intent intent = new Intent(this, DetailfilmActivity.class);
        startActivity(intent);
    }

    public void mettreAJourActivityApresAppelRest(String resultatAppelRest) {
        listeFilmsResultat = resultatAppelRest;
        Log.d("mydebug", ">>>Pour AppelServiceRestActivity - Longueur réponse: " +
                (listeFilmsResultat != null ? listeFilmsResultat.length() : 0));

        if (listeFilmsResultat != null && !listeFilmsResultat.isEmpty()) {
            // Parser le JSON et remplir la liste
            parseJsonAndDisplayFilms(listeFilmsResultat);
        } else {
            textListeFilm.setText("Aucun film trouvé");
            Log.e("mydebug", ">>>Résultat vide ou null");
        }
    }

    private void parseJsonAndDisplayFilms(String jsonString) {
        try {
            JSONArray filmsArray = new JSONArray(jsonString);
            filmList.clear();

            Log.d("mydebug", ">>>Nombre de films reçus: " + filmsArray.length());

            for (int i = 0; i < filmsArray.length(); i++) {
                JSONObject filmJson = filmsArray.getJSONObject(i);

                Film film = new Film();
                film.setFilmId(filmJson.getInt("filmId"));
                film.setTitle(filmJson.getString("title"));
                film.setDescription(filmJson.optString("description", "Pas de description"));
                film.setReleaseYear(filmJson.optInt("releaseYear", 0));
                film.setOriginalLanguageId(filmJson.optInt("originalLanguageId", 0));
                film.setRentalDuration(filmJson.optInt("rentalDuration", 0));
                film.setRentalRate(filmJson.optDouble("rentalRate", 0.0));
                film.setLength(filmJson.optInt("length", 0));
                film.setReplacementCost(filmJson.optDouble("replacementCost", 0.0));
                film.setRating(filmJson.optString("rating", "N/A"));
                film.setSpecialFeatures(filmJson.optString("specialFeatures", ""));
                film.setLastUpdate(filmJson.optString("lastUpdate", ""));

                filmList.add(film);
            }

            // Initialiser la liste filtrée avec tous les films
            filtrerFilms(searchField.getText().toString());

            Log.d("mydebug", ">>>Films parsés avec succès: " + filmList.size());

        } catch (JSONException e) {
            Log.e("mydebug", ">>>Erreur lors du parsing JSON: " + e.toString());
            textListeFilm.setText("Erreur lors du chargement");
            Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void filtrerFilms(String texteRecherche) {
        filmListFiltree.clear();

        if (texteRecherche == null || texteRecherche.trim().isEmpty()) {
            // Si le champ est vide, afficher tous les films
            filmListFiltree.addAll(filmList);
        } else {
            // Filtrer les films dont le titre contient le texte de recherche
            String rechercheLowerCase = texteRecherche.toLowerCase().trim();
            for (Film film : filmList) {
                if (film.getTitle().toLowerCase().contains(rechercheLowerCase)) {
                    filmListFiltree.add(film);
                }
            }
        }

        // Mettre à jour l'adapter
        filmFormatageDonnées.notifyDataSetChanged();

        // Mettre à jour le texte
        textListeFilm.setText(filmListFiltree.size() + " film(s) disponible(s)");

        Log.d("mydebug", ">>>Films filtrés: " + filmListFiltree.size() + " sur " + filmList.size());
    }

    private void chargerStockPourTousLesFilms() {
        Log.d("mydebug", ">>>Chargement du stock pour " + filmList.size() + " films");
        for (Film film : filmList) {
            new CheckAvailabilityTask(this).execute(film.getFilmId());
        }
    }

    public void handleAvailabilityResult(int filmId, int availableCount) {
        Log.d("mydebug", ">>>Stock reçu pour film " + filmId + ": " + availableCount);

        // Trouver le film dans la liste et mettre à jour son stock
        for (Film film : filmList) {
            if (film.getFilmId() == filmId) {
                film.setAvailableCount(availableCount);
                break;
            }
        }

        // Rafraîchir l'adapter pour afficher le nouveau stock
        filmFormatageDonnées.notifyDataSetChanged();
    }
}