package com.example.applicationrftg;

import android.content.Context;
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

public class PanierAdapter extends ArrayAdapter<Film> {

    private Context context;
    private List<Film> filmList;
    private PanierActivity panierActivity;

    public PanierAdapter(Context context, List<Film> filmList, PanierActivity activity) {
        super(context, 0, filmList);
        this.context = context;
        this.filmList = filmList;
        this.panierActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout rowView = (LinearLayout) convertView;

        // Créer la vue si elle n'existe pas
        if (rowView == null) {
            // Créer le LinearLayout principal
            rowView = new LinearLayout(context);
            rowView.setOrientation(LinearLayout.HORIZONTAL);
            rowView.setWeightSum(8.5f);
            rowView.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));

            // Créer les TextViews
            TextView filmTitle = createTextView(3, true);
            filmTitle.setTag("title");

            TextView filmYear = createTextView(1.5f, false);
            filmYear.setTag("year");

            TextView filmRating = createTextView(1.5f, false);
            filmRating.setTag("rating");

            // Créer le bouton "Retirer"
            Button removeButton = createButton(2.5f);
            removeButton.setText("Retirer");
            removeButton.setTag("removeButton");

            // Ajouter les vues au LinearLayout
            rowView.addView(filmTitle);
            rowView.addView(filmYear);
            rowView.addView(filmRating);
            rowView.addView(removeButton);
        }

        // Récupérer le film à cette position
        final Film film = filmList.get(position);
        final int currentPosition = position;

        // Remplir les TextViews avec les données du film
        TextView titleTextView = (TextView) rowView.findViewWithTag("title");
        TextView yearTextView = (TextView) rowView.findViewWithTag("year");
        TextView ratingTextView = (TextView) rowView.findViewWithTag("rating");
        Button removeButton = (Button) rowView.findViewWithTag("removeButton");

        titleTextView.setText(film.getTitle());
        yearTextView.setText(String.valueOf(film.getReleaseYear()));
        ratingTextView.setText(film.getRating());

        // Configurer le bouton pour retirer le film du panier
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PanierManager.getInstance().retirerFilm(currentPosition);
                notifyDataSetChanged();
                panierActivity.updateTotal();
                Toast.makeText(context,
                    film.getTitle() + " retiré du panier",
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
