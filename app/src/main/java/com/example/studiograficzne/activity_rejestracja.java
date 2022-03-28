package com.example.studiograficzne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_rejestracja extends AppCompatActivity {

    Button button_masz_juz_konto,button_zarejestruj_sie;
    private TextInputLayout login_rejestracja,haslo_rejestracja,haslo_powtorz_rejestracja,email_rejestracja;

   private FirebaseAuth mAuth;
   private ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        login_rejestracja = findViewById(R.id.usrlogin_rejestracja);
        haslo_rejestracja = findViewById(R.id.haslo_rejestracja);
        haslo_powtorz_rejestracja = findViewById(R.id.haslo_powtorzone_rejestracja);
        email_rejestracja = findViewById(R.id.email_rejestracja);
        button_masz_juz_konto =  findViewById(R.id.button_rejestracja_masz_juz_konto);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(activity_rejestracja.this);

        button_zarejestruj_sie = findViewById(R.id.button_zarejestruj_sie);
        button_zarejestruj_sie.setOnClickListener(view -> checkCredentials());

        button_masz_juz_konto.setOnClickListener(view -> openActivityMaszJuzKonto());

        //Sprawdzanie poprawnosci danych
        checkCredentials();

    } // OnCreate end

    private void checkCredentials() {
        String login = login_rejestracja.getEditText().getText().toString();
        String email = email_rejestracja.getEditText().getText().toString();
        String haslo = haslo_rejestracja.getEditText().getText().toString();
        String hasloPowtorz = haslo_powtorz_rejestracja.getEditText().getText().toString();

        if (login.isEmpty())
        {
            showError(login_rejestracja, "Wpisz login");
        }
        else if(email.isEmpty() || !email.contains("@"))
        {
            showError(email_rejestracja, "Niepoprawny adres email");
        }
        else if (haslo.isEmpty() || haslo.length()<6)
        {
            showError(haslo_rejestracja, "Wpisz haslo!");
        }
        else if (hasloPowtorz.isEmpty() || !hasloPowtorz.equals(haslo))
        {
            showError(haslo_powtorz_rejestracja, "Hasla sie roznia");
        }
        //Jesli nie ma bledow, to rejestrujemy uzytkownika
        else
        {
            mLoadingBar.setTitle("Rejestracja");
            mLoadingBar.setMessage("Prosze czekac, sprawdzamy poprawnosc dnaych");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,haslo).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(activity_rejestracja.this, "Zarejestrowano pomyslnie", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity_rejestracja.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    }
                    else
                    {
                        Toast.makeText(activity_rejestracja.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private void showError(TextInputLayout input, String s) {
        login_rejestracja.setError(s);
        input.setError(s);
        input.requestFocus();
    }

    //Implementacja dla MaszJuzKonto()
    private void openActivityMaszJuzKonto() {
        Intent intent = new Intent(this, activity_logowanie.class);
        startActivity(intent);
    }
}