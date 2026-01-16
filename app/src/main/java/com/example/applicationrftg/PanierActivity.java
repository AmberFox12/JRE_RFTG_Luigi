package com.example.applicationrftg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PanierActivity extends AppCompatActivity {

    private ListView listePanier;
    private TextView totalPanier;
    private Button backButton;
    private RentalAdapter rentalAdapter;
    private List<Rental> rentals;
    private int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        // Récupérer le customerId depuis SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        customerId = preferences.getInt("customerId", -1);

        // Initialiser les vues
        listePanier = findViewById(R.id.listePanier);
        totalPanier = findViewById(R.id.totalPanier);
        backButton = findViewById(R.id.backButton);

        // Initialiser la liste
        rentals = new ArrayList<>();
        rentalAdapter = new RentalAdapter(this, rentals, this);
        listePanier.setAdapter(rentalAdapter);

        // Configurer le bouton retour
        backButton.setOnClickListener(v -> finish());

        // Charger le panier depuis l'API
        loadCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }

    private void loadCart() {
        if (customerId <= 0) {
            Toast.makeText(this, "Erreur: utilisateur non connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        new GetCartTask(cartRentals -> {
            rentals.clear();
            rentals.addAll(cartRentals);
            rentalAdapter.notifyDataSetChanged();
            updateTotal();
        }).execute(customerId);
    }

    public void updateTotal() {
        int nombreFilms = rentals.size();
        totalPanier.setText(nombreFilms + " film(s) sélectionné(s)");
    }

    public void removeItem(int rentalId) {
        new RemoveFromCartTask(success -> {
            if (success) {
                Toast.makeText(this, "Film retiré du panier", Toast.LENGTH_SHORT).show();
                loadCart();
            } else {
                Toast.makeText(this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
            }
        }).execute(rentalId);
    }

    public void validerPanier(View view) {
        if (rentals.isEmpty()) {
            Toast.makeText(this, "Votre panier est vide", Toast.LENGTH_SHORT).show();
            return;
        }

        new CheckoutCartTask((success, itemsCount) -> {
            if (success) {
                Toast.makeText(this,
                        "Réservation validée ! " + itemsCount + " film(s) réservé(s)",
                        Toast.LENGTH_LONG).show();
                loadCart();
            } else {
                Toast.makeText(this, "Erreur lors de la validation", Toast.LENGTH_SHORT).show();
            }
        }).execute(customerId);
    }

    public void viderPanier(View view) {
        if (rentals.isEmpty()) {
            Toast.makeText(this, "Votre panier est déjà vide", Toast.LENGTH_SHORT).show();
            return;
        }

        new ClearCartTask(success -> {
            if (success) {
                Toast.makeText(this, "Panier vidé", Toast.LENGTH_SHORT).show();
                loadCart();
            } else {
                Toast.makeText(this, "Erreur lors du vidage du panier", Toast.LENGTH_SHORT).show();
            }
        }).execute(customerId);
    }
}