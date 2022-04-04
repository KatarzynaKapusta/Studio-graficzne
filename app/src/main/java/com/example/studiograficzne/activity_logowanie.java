package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;

public class activity_logowanie extends AppCompatActivity {

    Button createAccountButton, forgotPasswordButton, SignInButton;
    private TextInputLayout emailInput, passwordInput;

    private static final String TAG = "EmailPassword";

    //Declare auth
    private FirebaseAuth mAuth;

    //Loading bar
    ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logowanie);

        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.logEmailInput);
        passwordInput = findViewById(R.id.logPasswordInput);
        SignInButton = findViewById(R.id.logSignInButton);

        SignInButton.setOnClickListener(view -> checkCredentials());

        createAccountButton = findViewById(R.id.logCreateAccountButton);
        createAccountButton.setOnClickListener(view -> openActivityAlreadyHaveAccount());

        forgotPasswordButton = findViewById(R.id.logForgotPasswordButton);

        mLoadingBar = new ProgressDialog(activity_logowanie.this);

    } //OnCreate end

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(activity_logowanie.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
        // [END sign_in_with_email]
    }

    private void checkCredentials() {
        String email = emailInput.getEditText().getText().toString();
        String password = passwordInput.getEditText().getText().toString();
        boolean flag = false;

        if (email.isEmpty() || !email.contains("@")) {
            showError(emailInput, "Niepoprawny adres email");
            flag = true;
        }

        if (password.isEmpty() || password.length() < 6 ) {
            showError(passwordInput, "Niepoprawne haslo");
            flag = true;
        }

        //If no errors call signIn()
        if(!flag) {
            mLoadingBar.setTitle("Logowanie");
            mLoadingBar.setMessage("Proszę czekać, sprawdzam poprawność dnaych");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            //call SignIn()
            signIn(email,password);
        }
    }

    private void showError(TextInputLayout input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    //If user already have an account, open registration activity
    private void openActivityAlreadyHaveAccount() {
        Intent intent = new Intent(this, activity_rejestracja.class);
        startActivity(intent);
    }

    private void reload() { }

    private void updateUI(@Nullable FirebaseUser user) {
        mLoadingBar.dismiss();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}