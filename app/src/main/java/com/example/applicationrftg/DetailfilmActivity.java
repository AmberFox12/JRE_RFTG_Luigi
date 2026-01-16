package com.example.applicationrftg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailfilmActivity extends AppCompatActivity {

    private TextView filmTitle, filmDescription, filmYear, filmRating, filmLength, filmRentalDuration, filmSpecialFeatures;
    private Button reserveFilmButton, backButton;
    private Film currentFilm;
    private int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailfilm);

        // Récupérer le customerId depuis SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        customerId = preferences.getInt("customerId", -1);

        // Initialiser les vues
        filmTitle = findViewById(R.id.filmTitle);
        filmDescription = findViewById(R.id.filmDescription);
        filmYear = findViewById(R.id.filmYear);
        filmRating = findViewById(R.id.filmRating);
        filmLength = findViewById(R.id.filmLength);
        filmRentalDuration = findViewById(R.id.filmRentalDuration);
        filmSpecialFeatures = findViewById(R.id.filmSpecialFeatures);
        reserveFilmButton = findViewById(R.id.reserveFilmButton);
        backButton = findViewById(R.id.backButton);

        // Récupérer les données passées par l'Intent
        Intent intent = getIntent();
        if (intent != null) {
            // Créer un objet Film avec les données reçues
            currentFilm = new Film();
            currentFilm.setFilmId(intent.getIntExtra("filmId", 0));
            currentFilm.setTitle(intent.getStringExtra("filmTitle"));
            currentFilm.setDescription(intent.getStringExtra("filmDescription"));
            currentFilm.setReleaseYear(intent.getIntExtra("filmYear", 0));
            currentFilm.setRating(intent.getStringExtra("filmRating"));
            currentFilm.setRentalRate(intent.getDoubleExtra("filmPrice", 0.0));
            currentFilm.setLength(intent.getIntExtra("filmLength", 0));
            currentFilm.setRentalDuration(intent.getIntExtra("filmRentalDuration", 0));
            currentFilm.setSpecialFeatures(intent.getStringExtra("filmSpecialFeatures"));

            // Afficher les données
            displayFilmDetails();
        }

        // Configurer le bouton de réservation
        reserveFilmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFilm == null) {
                    return;
                }

                if (customerId <= 0) {
                    Toast.makeText(DetailfilmActivity.this, "Erreur: utilisateur non connecté", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(DetailfilmActivity.this, "Ajout en cours...", Toast.LENGTH_SHORT).show();

                new AddToCartTask((success, message) -> {
                    if (success) {
                        Toast.makeText(DetailfilmActivity.this,
                                currentFilm.getTitle() + " ajouté au panier",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailfilmActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }).execute(customerId, currentFilm.getFilmId());
            }
        });

        // Configurer le bouton retour
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayFilmDetails() {
        filmTitle.setText(currentFilm.getTitle());
        filmDescription.setText(currentFilm.getDescription());
        filmYear.setText(String.valueOf(currentFilm.getReleaseYear()));
        filmRating.setText(currentFilm.getRating());
        filmLength.setText(currentFilm.getLength() + " min");

        if (currentFilm.getRentalDuration() > 0) {
            filmRentalDuration.setText(currentFilm.getRentalDuration() + " jours");
        } else {
            filmRentalDuration.setText("Non spécifié");
        }

        if (currentFilm.getSpecialFeatures() != null && !currentFilm.getSpecialFeatures().isEmpty()) {
            filmSpecialFeatures.setText("Fonctionnalités spéciales: " + currentFilm.getSpecialFeatures());
        } else {
            filmSpecialFeatures.setText("Aucune fonctionnalité spéciale");
        }
    }
}
