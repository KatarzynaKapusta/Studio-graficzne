package com.example.studiograficzne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;

import android.util.Log;

public class activity_rejestracja extends AppCompatActivity {

    Button signUpButton, alreadySignedUpButton;
    private TextInputLayout nicknameInput, emailInput, passwordInput, confirmPasswordInput;

    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        nicknameInput = findViewById(R.id.regLoginInput);
        emailInput = findViewById(R.id.regEmailInput);
        passwordInput = findViewById(R.id.regPasswordInput);
        confirmPasswordInput = findViewById(R.id.regConfirmPasswordInput);

        alreadySignedUpButton =  findViewById(R.id.regAlreadySignedUpButton);
        signUpButton = findViewById(R.id.regSignUpButton);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(activity_rejestracja.this);

        //Sprawdzanie poprawnosci danych
        signUpButton.setOnClickListener(view -> checkCredentials());

        alreadySignedUpButton.setOnClickListener(view -> openAlreadySingUpActivity());


    } // OnCreate end

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void checkCredentials() {
        String nickname = nicknameInput.getEditText().getText().toString();
        String email = emailInput.getEditText().getText().toString();
        String password = passwordInput.getEditText().getText().toString();
        String confirmPassword = confirmPasswordInput.getEditText().getText().toString();

        boolean flag = false;

        if(nickname.isEmpty() || nickname.length() < 2)
        {
            showError(nicknameInput, "Wpisz poprawny login");
            flag = true;
        }

        if(email.isEmpty() || !email.contains("@"))
        {
            showError(emailInput, "Niepoprawny adres email");
            flag = true;
        }

        if(password.isEmpty() || password.length() < 6)
        {
            showError(passwordInput, "Haslo musi zawierac przynajmniej 6 znakow");
            flag = true;
        }

        if(confirmPassword.isEmpty() || !confirmPassword.equals(password))
        {
            showError(confirmPasswordInput, "Wpisz poprawne haslo");
            flag = true;
        }

        //Jesli nie ma bledow, to rejestrujemy uzytkownika
        if (!flag)
        {
            mLoadingBar.setTitle("Rejestracja");
            mLoadingBar.setMessage("Prosze czekac, sprawdzamy poprawnosc dnaych");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        mLoadingBar.dismiss();
                        Toast.makeText(activity_rejestracja.this, "Zarejestrowano pomyslnie", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity_rejestracja.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        input.setError(s);
        input.requestFocus();
    }

    //Implementacja dla AlreadySignedUp()
    private void openAlreadySingUpActivity() {
        Intent intent = new Intent(this, activity_logowanie.class);
        startActivity(intent);
    }

    private void updateUI(@Nullable FirebaseUser user) {
        // No-op
    }

    private void reload() { }
}