package com.example.applicationrftg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Spinner spinnerURLs;
    private TextView errorMessage;

    // Liste des URLs prédéfinies
    private final String[] urlOptions = {
        "http://10.0.2.2:8180",      // Émulateur Android -> localhost
        "http://192.168.1.100:8180", // IP locale réseau
        "http://localhost:8180"      // Localhost direct
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser les vues
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        errorMessage = findViewById(R.id.errorMessage);
        spinnerURLs = findViewById(R.id.spinnerURLs);

        // Configurer le Spinner avec les URLs
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, urlOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerURLs.setAdapter(adapter);

        // Listener pour le changement de sélection
        spinnerURLs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedUrl = urlOptions[position];
                UrlManager.setURLConnexion(selectedUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Pré-remplir pour les tests
        emailField.setText("MARY.SMITH@peachcustomer.org");
        passwordField.setText("12345");
    }

    public void ConnectToHomePage(View view) {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Validation des champs
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage.setText("Veuillez remplir tous les champs");
            errorMessage.setVisibility(View.VISIBLE);
            return;
        }

        // Cacher le message d'erreur
        errorMessage.setVisibility(View.GONE);

        // Appeler l'API de connexion
        new LoginTask(this).execute(email, password);
    }

    public void handleLoginResult(Integer customerId) {
        if (customerId != null && customerId > 0) {
            // Connexion réussie
            Toast.makeText(this, "Connexion réussie ! Bienvenue", Toast.LENGTH_SHORT).show();

            // Sauvegarder l'ID du customer dans SharedPreferences
            SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("customerId", customerId);
            editor.apply();

            // Rediriger vers la liste des films
            Intent intent = new Intent(this, ListefilmsActivity.class);
            intent.putExtra("customerId", customerId);
            startActivity(intent);
            finish(); // Fermer l'écran de connexion
        } else {
            // Connexion échouée
            errorMessage.setText("Email ou mot de passe incorrect");
            errorMessage.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Échec de la connexion", Toast.LENGTH_SHORT).show();
        }
    }
}