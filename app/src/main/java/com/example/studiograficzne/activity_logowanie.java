package com.example.studiograficzne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class activity_logowanie extends AppCompatActivity {

    Button button_nie_masz_konta, zapomnianeHaslo, zalogujSie, check;
    private TextInputLayout emailLogowanie,hasloLogowanie;
    private FirebaseAuth mAuth;
    ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logowanie);

        mAuth = FirebaseAuth.getInstance();
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

        mLoadingBar = new ProgressDialog(activity_logowanie.this);

//        check = findViewById(R.id.checkButton);
//        check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                checkIfEmailExist(view);
//            }
//        });

    } //OnCreate end

//    public void checkIfEmailExist(View v)
//    {
//        mAuth.fetchSignInMethodsForEmail(emailLogowanie.getEditText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
//            @Override
//            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
//
//                boolean check = !task.getResult().getSignInMethods().isEmpty();
//                if(!check)
//                {
//                    Toast.makeText(activity_logowanie.this, "Podany uzytkownik nie istnieje", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(activity_logowanie.this, "Podany email zostal juz uzyty", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    @Override
    protected void onStart() {

        if(mAuth.getCurrentUser() != null) //user is login
        {
            Toast.makeText(activity_logowanie.this, "TAK Zalogowany", Toast.LENGTH_SHORT).show();
            finish();
        }

        else {
            Toast.makeText(activity_logowanie.this, "NIE jestes zalogowany", Toast.LENGTH_SHORT).show();
        }
        super.onStart();
    }

    private void checkCredentials() {
        String email = emailLogowanie.getEditText().getText().toString();
        String haslo = hasloLogowanie.getEditText().getText().toString();
        boolean flag = false;
        if(email.isEmpty() || !email.contains("@"))
        {
            showError(emailLogowanie, "Niepoprawny adres email");
            flag = true;
        }
        if (haslo.isEmpty() || haslo.length()<6)
        {
            showError(hasloLogowanie, "Niepoprawne haslo");
            flag = true;
        }
        if(!flag)
        {
            mLoadingBar.setTitle("Logowanie");
            mLoadingBar.setMessage("Prosze czekac, sprawdzamy poprawnosc danych");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.signInWithEmailAndPassword(email, haslo).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        mLoadingBar.dismiss();
                        Toast.makeText(activity_logowanie.this, "Zalogowano pomyslnie!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity_logowanie.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            });
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