package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_rejestracja extends AppCompatActivity {

    Button button_masz_juz_konto,button_zarejestruj_sie;
    TextInputLayout login_rejestracja,haslo_rejestracja,email_rejestracja;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        login_rejestracja = findViewById(R.id.login_rejestracja);
        haslo_rejestracja = findViewById(R.id.haslo_rejestracja);
        email_rejestracja = findViewById(R.id.email_rejestracja);
        button_zarejestruj_sie = findViewById(R.id.button_zarejestruj_sie);
        button_masz_juz_konto =  findViewById(R.id.button_rejestracja_masz_juz_konto);

        //Konto zalozone:
        button_masz_juz_konto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityMaszJuzKonto();
            }
        });

        //Zapisywanie danych w FireBase on button Click
        button_zarejestruj_sie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                //Bierzemy wszystkie wartosci z pol tekstowych
                String login = login_rejestracja.getEditText().getText().toString();
                String email = email_rejestracja.getEditText().getText().toString();
                String haslo = haslo_rejestracja.getEditText().getText().toString();

                UserHelperClass helperClass = new UserHelperClass(login, email, haslo);

                reference.setValue(helperClass);

            }
        });
    } // OnCreate end

    //Implementacja dla MaszJuzKonto()
    private void openActivityMaszJuzKonto() {
        Intent intent = new Intent(this, activity_logowanie.class);
        startActivity(intent);
    }
}