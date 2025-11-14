package com.example.applicationrftg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "filmrental.db";
    private static final int DATABASE_VERSION = 1;

    // Table panier
    private static final String TABLE_PANIER = "panier";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FILM_ID = "film_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_RELEASE_YEAR = "release_year";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_LENGTH = "length";
    private static final String COLUMN_RENTAL_DURATION = "rental_duration";
    private static final String COLUMN_RENTAL_RATE = "rental_rate";
    private static final String COLUMN_REPLACEMENT_COST = "replacement_cost";
    private static final String COLUMN_SPECIAL_FEATURES = "special_features";
    private static final String COLUMN_ORIGINAL_LANGUAGE_ID = "original_language_id";
    private static final String COLUMN_LAST_UPDATE = "last_update";

    // Requête de création de la table
    private static final String CREATE_TABLE_PANIER =
        "CREATE TABLE " + TABLE_PANIER + " (" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_FILM_ID + " INTEGER, " +
        COLUMN_TITLE + " TEXT, " +
        COLUMN_DESCRIPTION + " TEXT, " +
        COLUMN_RELEASE_YEAR + " INTEGER, " +
        COLUMN_RATING + " TEXT, " +
        COLUMN_LENGTH + " INTEGER, " +
        COLUMN_RENTAL_DURATION + " INTEGER, " +
        COLUMN_RENTAL_RATE + " REAL, " +
        COLUMN_REPLACEMENT_COST + " REAL, " +
        COLUMN_SPECIAL_FEATURES + " TEXT, " +
        COLUMN_ORIGINAL_LANGUAGE_ID + " INTEGER, " +
        COLUMN_LAST_UPDATE + " TEXT" +
        ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PANIER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANIER);
        onCreate(db);
    }

    // Ajouter un film au panier
    public long ajouterFilm(Film film) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FILM_ID, film.getFilmId());
        values.put(COLUMN_TITLE, film.getTitle());
        values.put(COLUMN_DESCRIPTION, film.getDescription());
        values.put(COLUMN_RELEASE_YEAR, film.getReleaseYear());
        values.put(COLUMN_RATING, film.getRating());
        values.put(COLUMN_LENGTH, film.getLength());
        values.put(COLUMN_RENTAL_DURATION, film.getRentalDuration());
        values.put(COLUMN_RENTAL_RATE, film.getRentalRate());
        values.put(COLUMN_REPLACEMENT_COST, film.getReplacementCost());
        values.put(COLUMN_SPECIAL_FEATURES, film.getSpecialFeatures());
        values.put(COLUMN_ORIGINAL_LANGUAGE_ID, film.getOriginalLanguageId());
        values.put(COLUMN_LAST_UPDATE, film.getLastUpdate());

        long id = db.insert(TABLE_PANIER, null, values);
        db.close();
        return id;
    }

    // Récupérer tous les films du panier
    public List<Film> getTousLesFilms() {
        List<Film> filmList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PANIER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Film film = new Film();
                film.setFilmId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FILM_ID)));
                film.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                film.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                film.setReleaseYear(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RELEASE_YEAR)));
                film.setRating(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RATING)));
                film.setLength(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LENGTH)));
                film.setRentalDuration(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RENTAL_DURATION)));
                film.setRentalRate(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RENTAL_RATE)));
                film.setReplacementCost(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REPLACEMENT_COST)));
                film.setSpecialFeatures(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SPECIAL_FEATURES)));
                film.setOriginalLanguageId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORIGINAL_LANGUAGE_ID)));
                film.setLastUpdate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_UPDATE)));

                filmList.add(film);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return filmList;
    }

    // Supprimer un film par position
    public void supprimerFilmParPosition(int position) {
        List<Film> films = getTousLesFilms();
        if (position >= 0 && position < films.size()) {
            Film film = films.get(position);

            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT " + COLUMN_ID + " FROM " + TABLE_PANIER +
                               " ORDER BY " + COLUMN_ID + " LIMIT 1 OFFSET " + position;
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                db.delete(TABLE_PANIER, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            }

            cursor.close();
            db.close();
        }
    }

    // Vider tout le panier
    public void viderPanier() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANIER, null, null);
        db.close();
    }

    // Compter le nombre de films
    public int compterFilms() {
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_PANIER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }
}
