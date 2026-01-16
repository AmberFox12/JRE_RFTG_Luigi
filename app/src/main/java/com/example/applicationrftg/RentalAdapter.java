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

import java.util.List;

public class RentalAdapter extends ArrayAdapter<Rental> {

    private Context context;
    private List<Rental> rentalList;
    private PanierActivity panierActivity;

    public RentalAdapter(Context context, List<Rental> rentalList, PanierActivity activity) {
        super(context, 0, rentalList);
        this.context = context;
        this.rentalList = rentalList;
        this.panierActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout rowView = (LinearLayout) convertView;

        if (rowView == null) {
            rowView = new LinearLayout(context);
            rowView.setOrientation(LinearLayout.HORIZONTAL);
            rowView.setWeightSum(8.5f);
            rowView.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));

            TextView filmTitle = createTextView(3, true);
            filmTitle.setTag("title");

            TextView filmYear = createTextView(1.5f, false);
            filmYear.setTag("year");

            TextView filmRating = createTextView(1.5f, false);
            filmRating.setTag("rating");

            Button removeButton = createButton(2.5f);
            removeButton.setText("Retirer");
            removeButton.setTag("removeButton");

            rowView.addView(filmTitle);
            rowView.addView(filmYear);
            rowView.addView(filmRating);
            rowView.addView(removeButton);
        }

        final Rental rental = rentalList.get(position);
        Film film = rental.getFilm();

        TextView titleTextView = (TextView) rowView.findViewWithTag("title");
        TextView yearTextView = (TextView) rowView.findViewWithTag("year");
        TextView ratingTextView = (TextView) rowView.findViewWithTag("rating");
        Button removeButton = (Button) rowView.findViewWithTag("removeButton");

        if (film != null) {
            titleTextView.setText(film.getTitle());
            yearTextView.setText(String.valueOf(film.getReleaseYear()));
            ratingTextView.setText(film.getRating());
        } else {
            titleTextView.setText("Film inconnu");
            yearTextView.setText("-");
            ratingTextView.setText("-");
        }

        removeButton.setOnClickListener(v -> {
            panierActivity.removeItem(rental.getRentalId());
        });

        return rowView;
    }

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

    private int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}