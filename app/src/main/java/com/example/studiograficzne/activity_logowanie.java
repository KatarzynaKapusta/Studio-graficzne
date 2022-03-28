package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class activity_logowanie extends AppCompatActivity {

    Button button_nie_masz_konta, zapomnianeHaslo, zalogujSie;
    private TextInputLayout emailLogowanie,hasloLogowanie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logowanie);

        emailLogowanie = findViewById(R.id.email_logowanie);
        hasloLogowanie = findViewById(R.id.haslo_logowanie);
        zalogujSie = findViewById(R.id.button_zalogujSie);

        zalogujSie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });

        button_nie_masz_konta = findViewById(R.id.button_przejdz_do_rejestracji_z_logowania);
        button_nie_masz_konta.setOnClickListener(view -> openActivityNieMaszKonta());

        zapomnianeHaslo = findViewById(R.id.button_zapomnianeHaslo);


    } //OnCreate end

    private void checkCredentials() {
        String email = emailLogowanie.getEditText().getText().toString();
        String haslo = hasloLogowanie.getEditText().getText().toString();

        if(email.isEmpty() || !email.contains("@"))
        {
            showError(emailLogowanie, "Niepoprawny adres email");
        }
        else if (haslo.isEmpty() || haslo.length()<6)
        {
            showError(hasloLogowanie, "Niepoprawne haslo");
        }
        else
        {
            Toast.makeText(this, "Metoda login wywolana", Toast.LENGTH_SHORT).show();
        }

    }

    private void showError(TextInputLayout input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    //Funkcje dodatkowe
    private void openActivityNieMaszKonta() {
        Intent intent = new Intent(this, activity_rejestracja.class);
        startActivity(intent);
    }
}