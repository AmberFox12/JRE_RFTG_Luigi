package com.example.applicationrftg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PanierActivity  extends AppCompatActivity {

    private ListView listePanier;
    private TextView totalPanier;
    private Button backButton;
    private PanierAdapter panierAdapter;
    private List<Film> filmsPanier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        // Initialiser le PanierManager avec la base de données
        PanierManager.getInstance().initialiser(this);

        // Initialiser les vues
        listePanier = findViewById(R.id.listePanier);
        totalPanier = findViewById(R.id.totalPanier);
        backButton = findViewById(R.id.backButton);

        // Récupérer les films du panier
        filmsPanier = PanierManager.getInstance().getFilmsPanier();

        // Créer l'adapter
        panierAdapter = new PanierAdapter(this, filmsPanier, this);
        listePanier.setAdapter(panierAdapter);

        // Mettre à jour le compteur
        updateTotal();

        // Configurer le bouton retour
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Rafraîchir la liste quand on revient sur la page
        panierAdapter.notifyDataSetChanged();
        updateTotal();
    }

    public void updateTotal() {
        int nombreFilms = PanierManager.getInstance().getNombreFilms();
        totalPanier.setText(nombreFilms + " film(s) sélectionné(s)");
    }

    public void validerPanier(View view) {
        int nombreFilms = PanierManager.getInstance().getNombreFilms();

        if (nombreFilms == 0) {
            Toast.makeText(this, "Votre panier est vide", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,
                "Réservation validée ! " + nombreFilms + " film(s) réservé(s)",
                Toast.LENGTH_LONG).show();

            // Vider le panier après validation
            PanierManager.getInstance().viderPanier();
            panierAdapter.notifyDataSetChanged();
            updateTotal();
        }
    }
}
