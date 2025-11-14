package com.example.applicationrftg;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FilmFormatageDonnées extends ArrayAdapter<Film> {

    private Context context;
    private List<Film> filmList;

    public FilmFormatageDonnées(Context context, List<Film> filmList) {
        super(context, 0, filmList);
        this.context = context;
        this.filmList = filmList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout rowView = (LinearLayout) convertView;

        // Créer la vue si elle n'existe pas
        if (rowView == null) {
            // Créer le LinearLayout principal
            rowView = new LinearLayout(context);
            rowView.setOrientation(LinearLayout.HORIZONTAL);
            rowView.setWeightSum(10);
            rowView.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));

            // Ajouter l'effet de sélection
            int[] attrs = new int[]{android.R.attr.selectableItemBackground};
            android.content.res.TypedArray typedArray = context.obtainStyledAttributes(attrs);
            int backgroundResource = typedArray.getResourceId(0, 0);
            rowView.setBackgroundResource(backgroundResource);
            typedArray.recycle();

            // Créer les 3 TextViews avec des proportions ajustées
            TextView filmTitle = createTextView(3, true);
            filmTitle.setTag("title");

            TextView filmYear = createTextView(1.5f, false);
            filmYear.setTag("year");

            TextView filmRating = createTextView(1.5f, false);
            filmRating.setTag("rating");

            // Créer les boutons "Détails" et "Réserver"
            Button detailButton = createButton(2);
            detailButton.setText("Détails");
            detailButton.setTag("detailButton");

            Button reserveButton = createButton(2);
            reserveButton.setText("Réserver");
            reserveButton.setTag("reserveButton");

            // Ajouter les vues au LinearLayout
            rowView.addView(filmTitle);
            rowView.addView(filmYear);
            rowView.addView(filmRating);
            rowView.addView(detailButton);
            rowView.addView(reserveButton);
        }

        // Récupérer le film à cette position
        final Film film = filmList.get(position);

        // Remplir les TextViews avec les données du film
        TextView titleTextView = (TextView) rowView.findViewWithTag("title");
        TextView yearTextView = (TextView) rowView.findViewWithTag("year");
        TextView ratingTextView = (TextView) rowView.findViewWithTag("rating");
        Button detailButton = (Button) rowView.findViewWithTag("detailButton");
        Button reserveButton = (Button) rowView.findViewWithTag("reserveButton");

        titleTextView.setText(film.getTitle());
        yearTextView.setText(String.valueOf(film.getReleaseYear()));
        ratingTextView.setText(film.getRating());

        // Créer l'intent pour naviguer vers les détails
        final Intent detailIntent = new Intent(context, DetailfilmActivity.class);
        detailIntent.putExtra("filmId", film.getFilmId());
        detailIntent.putExtra("filmTitle", film.getTitle());
        detailIntent.putExtra("filmDescription", film.getDescription());
        detailIntent.putExtra("filmYear", film.getReleaseYear());
        detailIntent.putExtra("filmRating", film.getRating());
        detailIntent.putExtra("filmPrice", film.getRentalRate());
        detailIntent.putExtra("filmLength", film.getLength());
        detailIntent.putExtra("filmRentalDuration", film.getRentalDuration());
        detailIntent.putExtra("filmSpecialFeatures", film.getSpecialFeatures());

        // Configurer le clic sur toute la ligne pour ouvrir les détails
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(detailIntent);
            }
        });

        // Configurer le bouton pour ouvrir la page de détails
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(detailIntent);
            }
        });
        // Configurer le bouton pour ajouter le film au panier
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajouter le film au panier
                PanierManager.getInstance().ajouterFilm(film);

                // Afficher une confirmation
                Toast.makeText(context,
                    film.getTitle() + " ajouté au panier (" +
                    PanierManager.getInstance().getNombreFilms() + " film(s))",
                    Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }

    // Méthode helper pour créer un TextView
    private TextView createTextView(float weight, boolean bold) {
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            0,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            weight
        );
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(dpToPx(2), dpToPx(4), dpToPx(2), dpToPx(4));
        textView.setTextSize(12);

        if (bold) {
            textView.setTypeface(null, Typeface.BOLD);
        }

        return textView;
    }

    // Méthode helper pour créer un bouton
    private Button createButton(float weight) {
        Button button = new Button(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            0,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            weight
        );
        params.setMargins(dpToPx(2), 0, dpToPx(2), 0);
        button.setLayoutParams(params);
        button.setPadding(dpToPx(2), dpToPx(4), dpToPx(2), dpToPx(4));
        button.setTextSize(10);
        button.setAllCaps(false);
        return button;
    }

    // Convertir dp en pixels
    private int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}