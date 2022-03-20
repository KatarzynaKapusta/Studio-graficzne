package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class activity_logowanie extends AppCompatActivity {

    private Button button_nie_masz_konta;
    TextInputLayout login_logowanie,haslo_logowanie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logowanie);

        button_nie_masz_konta = (Button) findViewById(R.id.button_przejdz_do_rejestracji_z_logowania);
        button_nie_masz_konta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityNieMaszKonta();
            }
        });
    }

    private void openActivityNieMaszKonta() {
        Intent intent = new Intent(this, activity_rejestracja.class);
        startActivity(intent);
    }
}