package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class activity_rejestracja extends AppCompatActivity {

    private Button button_masz_juz_konto;
    TextInputLayout login_rejestracja,haslo_rejestracja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        button_masz_juz_konto = (Button) findViewById(R.id.button_rejestracja_masz_juz_konto);
        button_masz_juz_konto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityMaszJuzKonto();
            }
        });
    }

    private void openActivityMaszJuzKonto() {
        Intent intent = new Intent(this, activity_logowanie.class);
        startActivity(intent);
    }
}