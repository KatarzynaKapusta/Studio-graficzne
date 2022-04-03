package com.example.studiograficzne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class activity_rejestracja extends AppCompatActivity {

    Button signUpButton, alreadySignedUpButton;
    private TextInputLayout nicknameInput, emailInput, passwordInput, confirmPasswordInput;

    private static final String TAG = "EmailPassword";
    //declare auth
    private FirebaseAuth mAuth;

    private ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        nicknameInput = findViewById(R.id.regLoginInput);
        emailInput = findViewById(R.id.regEmailInput);
        passwordInput = findViewById(R.id.regPasswordInput);
        confirmPasswordInput = findViewById(R.id.regConfirmPasswordInput);
        alreadySignedUpButton =  findViewById(R.id.regAlreadySignedUpButton);
        signUpButton = findViewById(R.id.regSignUpButton);

        mLoadingBar = new ProgressDialog(activity_rejestracja.this);

        //Sprawdzanie poprawnosci danych
        signUpButton.setOnClickListener(view -> checkCredentials());
        alreadySignedUpButton.setOnClickListener(view -> openAlreadySignedUpActivity());


    } //OnCreate end

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
        else {
            Toast.makeText(activity_rejestracja.this, "Nie jestes zalogowany",
                    Toast.LENGTH_SHORT).show();
        }
    } //OnStart end

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mLoadingBar.dismiss();
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            mLoadingBar.dismiss();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(activity_rejestracja.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    /////STARE GUNWO//////
    private void checkCredentials() {
        String nickname = nicknameInput.getEditText().getText().toString().trim();
        String email = emailInput.getEditText().getText().toString().trim();
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

            createAccount(email,password);
        }
    }

    private void showError(TextInputLayout input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    //Implementacja dla AlreadySignedUp()
    private void openAlreadySignedUpActivity() {
        Intent intent = new Intent(this, activity_logowanie.class);
        startActivity(intent);
    }

    private void updateUI(@Nullable FirebaseUser user) {
        // No-op
    }

    private void reload() {
        Toast.makeText(activity_rejestracja.this, "Jestes zalogowany",
                Toast.LENGTH_SHORT).show();
    }
}