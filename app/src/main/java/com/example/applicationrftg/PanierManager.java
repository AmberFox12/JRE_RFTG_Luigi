package com.example.applicationrftg;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class PanierManager {
    private static PanierManager instance;
    private DatabaseHelper databaseHelper;
    private List<Film> filmsPanier;

    private PanierManager() {
        filmsPanier = new ArrayList<>();
    }

    public static PanierManager getInstance() {
        if (instance == null) {
            instance = new PanierManager();
        }
        return instance;
    }

    public void initialiser(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
            // Charger les films depuis la base de données
            filmsPanier = databaseHelper.getTousLesFilms();
        }
    }

    public void ajouterFilm(Film film) {
        if (databaseHelper != null) {
            databaseHelper.ajouterFilm(film);
            filmsPanier = databaseHelper.getTousLesFilms();
        } else {
            filmsPanier.add(film);
        }
    }

    public void retirerFilm(Film film) {
        filmsPanier.remove(film);
        if (databaseHelper != null) {
            // Reconstruire la base de données
            databaseHelper.viderPanier();
            for (Film f : filmsPanier) {
                databaseHelper.ajouterFilm(f);
            }
        }
    }

    public void retirerFilm(int position) {
        if (position >= 0 && position < filmsPanier.size()) {
            if (databaseHelper != null) {
                databaseHelper.supprimerFilmParPosition(position);
                filmsPanier = databaseHelper.getTousLesFilms();
            } else {
                filmsPanier.remove(position);
            }
        }
    }

    public List<Film> getFilmsPanier() {
        if (databaseHelper != null) {
            filmsPanier = databaseHelper.getTousLesFilms();
        }
        return filmsPanier;
    }

    public int getNombreFilms() {
        if (databaseHelper != null) {
            return databaseHelper.compterFilms();
        }
        return filmsPanier.size();
    }

    public void viderPanier() {
        if (databaseHelper != null) {
            databaseHelper.viderPanier();
        }
        filmsPanier.clear();
    }

    public double calculerTotal() {
        double total = 0.0;
        for (Film film : filmsPanier) {
            total += film.getRentalRate();
        }
        return total;
    }
}
