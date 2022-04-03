package com.example.studiograficzne;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.annotations.Nullable;

public class activity_logowanie extends AppCompatActivity {

    Button createAccountButton, forgotPasswordButton, SignInButton, check;
    private TextInputLayout emailInput, passwordInput;

    private static final String TAG = "EmailPassword";
    //declare auth
    private FirebaseAuth mAuth;

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

        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });

        createAccountButton = findViewById(R.id.logCreateAccountButton);
        createAccountButton.setOnClickListener(view -> openActivityNieMaszKonta());

        forgotPasswordButton = findViewById(R.id.logForgotPasswordButton);

        mLoadingBar = new ProgressDialog(activity_logowanie.this);

//        check = findViewById(R.id.checkButton);
//        check.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                String email1 = emailInput.getEditText().getText().toString().trim();
//                if(!email1.isEmpty())
//                checkIfEmailExist(view);
//            }
//        });

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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mLoadingBar.dismiss();
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            mLoadingBar.dismiss();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(activity_logowanie.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    /////STARE/////
    public void checkIfEmailExist(View v)
    {
        mAuth.fetchSignInMethodsForEmail(emailInput.getEditText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                boolean check = !task.getResult().getSignInMethods().isEmpty();
                if(!check)
                {
                    Toast.makeText(activity_logowanie.this, "Podany uzytkownik nie istnieje", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(activity_logowanie.this, "Podany email zostal juz uzyty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    @Override
//    protected void onStart() {
//
//        if(mAuth.getCurrentUser() != null) //user is login
//        {
//            Toast.makeText(activity_logowanie.this, "TAK Zalogowany", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//
//        else {
//            Toast.makeText(activity_logowanie.this, "NIE jestes zalogowany", Toast.LENGTH_SHORT).show();
//        }
//        super.onStart();
//    }

    public void getEmailCredentials() {
        String email = emailInput.getEditText().getText().toString();
        String password = passwordInput.getEditText().getText().toString();
        // [START auth_email_cred]
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        // [END auth_email_cred]
    }

    private void checkCredentials() {
        String email = emailInput.getEditText().getText().toString();
        String password = passwordInput.getEditText().getText().toString();
        boolean flag = false;
        if(email.isEmpty() || !email.contains("@"))
        {
            showError(emailInput, "Niepoprawny adres email");
            flag = true;
        }
        if (password.isEmpty() || password.length() <6 )
        {
            showError(passwordInput, "Niepoprawne haslo");
            flag = true;
        }
        if(!flag)
        {
            mLoadingBar.setTitle("Logowanie");
            mLoadingBar.setMessage("Prosze czekac, sprawdzamy poprawnosc danych");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            signIn(email,password);
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

    private void reload() { }

    private void updateUI(@Nullable FirebaseUser user) {
        // No-op
    }
}