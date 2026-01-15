package com.example.applicationrftg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser les vues
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        errorMessage = findViewById(R.id.errorMessage);

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