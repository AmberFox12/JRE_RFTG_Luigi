package com.example.applicationrftg;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailfilm);

        // Initialiser le PanierManager avec la base de données
        PanierManager.getInstance().initialiser(this);

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
                if (currentFilm != null) {
                    PanierManager.getInstance().ajouterFilm(currentFilm);
                    Toast.makeText(DetailfilmActivity.this,
                        currentFilm.getTitle() + " ajouté au panier (" +
                        PanierManager.getInstance().getNombreFilms() + " film(s))",
                        Toast.LENGTH_SHORT).show();
                }
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
