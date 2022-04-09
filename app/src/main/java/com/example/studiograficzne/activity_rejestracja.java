package com.example.studiograficzne;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.textfield.TextInputLayout;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.annotations.Nullable;


//// Nowe//
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_rejestracja extends AppCompatActivity {

    private TextInputEditText nicknameTextInputEditText, emailTextInputEditText, passwordTextInputEditText, confirmPasswordTextInputEditText;
    private Button registerButton, alreadySignedUpButton;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    private static final String USERS = "users";
    private String TAG = "activity_rejestracja";

    private String nickname, email, password, confirmPassword;

    protected User_info userInfo;
    protected UserGameInfo userGameInfo;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);

        nicknameTextInputEditText = findViewById(R.id.regLoginInput);
        emailTextInputEditText = findViewById(R.id.regEmailInput);
        passwordTextInputEditText = findViewById(R.id.regPasswordInput);
        confirmPasswordTextInputEditText = findViewById(R.id.regConfirmPasswordInput);

        registerButton = findViewById(R.id.regSignUpButton);
        alreadySignedUpButton =  findViewById(R.id.regAlreadySignedUpButton);

        database = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
        mAuth = FirebaseAuth.getInstance();
        userGameInfo = new UserGameInfo();

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Insert data into firebase database
                //If no errors register user
                if(!checkCredentials()) {
                    nickname = nicknameTextInputEditText.getText().toString();
                    email = emailTextInputEditText.getText().toString();
                    password = passwordTextInputEditText.getText().toString();
                    confirmPassword = confirmPasswordTextInputEditText.getText().toString();
                    userInfo = new User_info(nickname, email);
                    registerUser();
                }
                else {
                    Log.w(TAG, "checkCredentials function returned false");
                    Toast.makeText(activity_rejestracja.this, "Invalid data.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        alreadySignedUpButton.setOnClickListener(view -> openAlreadySignedUpActivity());

    } // OnCreate end

    public void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(activity_rejestracja.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /** Basic data validation
     * */
    private boolean checkCredentials() {
        nickname = nicknameTextInputEditText.getText().toString();
        email = emailTextInputEditText.getText().toString();
        password = passwordTextInputEditText.getText().toString();
        confirmPassword = confirmPasswordTextInputEditText.getText().toString();

        boolean flag = false;

        if (nickname.isEmpty() || nickname.length() < 2) {
            showError(nicknameTextInputEditText, getString(R.string.enterCorrectNickname));
            flag = true;
        } else {
            showError(nicknameTextInputEditText, null);
        }

        if (email.isEmpty() || !email.contains("@")) {
            showError(emailTextInputEditText, getString(R.string.invalidEmailAddreess));
            flag = true;
        } else {
            showError(emailTextInputEditText, null);
        }

        if (password.isEmpty() || password.length() < 6) {
            showError(passwordTextInputEditText, getString(R.string.atLeast6CharInPasswd));
            flag = true;
        } else {
            showError(passwordTextInputEditText, null);
        }

        if (confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
            showError(confirmPasswordTextInputEditText, getString(R.string.passwDoNotMatch));
            flag = true;
        } else {
            showError(confirmPasswordTextInputEditText, null);
        }

        return flag;
    }

    private void showError(TextInputEditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    /**
     * adding user information to database and redirect to login screen
     * @param currentUser
     */
    public void updateUI(FirebaseUser currentUser) {
        FirebaseUser user2 = mAuth.getCurrentUser();
        String uid = user2.getUid();
        mDatabase = database.getReference("Users");

        String keyid = mDatabase.push().getKey();
        mDatabase.child(uid).child("UserInfo").setValue(userInfo); //adding user info to database
        mDatabase.child(uid).child("UserGameInfo").setValue(userGameInfo);
        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }

    //Implementation of AlreadySignedUp()
    private void openAlreadySignedUpActivity() {
        Intent intent = new Intent(this, activity_logowanie.class);
        startActivity(intent);
    }

}





































//    Button signUpButton, alreadySignedUpButton;
//    private TextInputLayout nicknameInput, emailInput, passwordInput, confirmPasswordInput;
//
//    private static final String TAG = "EmailPassword";
//
//    //Declarate auth
//    private FirebaseAuth mAuth;
//
//    //Loading bar
//    private ProgressDialog mLoadingBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_rejestracja);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        nicknameInput = findViewById(R.id.regLoginInput);
//        emailInput = findViewById(R.id.regEmailInput);
//        passwordInput = findViewById(R.id.regPasswordInput);
//        confirmPasswordInput = findViewById(R.id.regConfirmPasswordInput);
//        alreadySignedUpButton =  findViewById(R.id.regAlreadySignedUpButton);
//        signUpButton = findViewById(R.id.regSignUpButton);
//        mLoadingBar = new ProgressDialog(activity_rejestracja.this);
//
//        //Data validation
//        signUpButton.setOnClickListener(view -> checkCredentials());
//        alreadySignedUpButton.setOnClickListener(view -> openAlreadySignedUpActivity());
//
//    } //OnCreate end
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            reload();
//        }
//        else {
//            Toast.makeText(activity_rejestracja.this, "Nie jesteś zalogowany",
//                    Toast.LENGTH_SHORT).show();
//        }
//    } //OnStart end
//
//    private void createAccount(String email, String password, String nickname) {
//        // [START create_user_with_email]
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        // Sign in success, update UI with the signed-in user's information
//                        Log.d(TAG, "createUserWithEmail:success");
//                        FirebaseUser user = mAuth.getCurrentUser();
//
//                        //Enter user data into the firebase realitme database
//                        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(nickname);
//
//                        //Extracting User reference from Database for registered users
//                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("users");
//
//                        if(user != null){
//                            referenceProfile.child(user.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful())
//                                    {
//
//                                        Toast.makeText(activity_rejestracja.this, "JESTEŚ KUL",
//                                                Toast.LENGTH_SHORT).show();
////                                    updateUI(user);
////                                    Intent intent = new Intent(activity_rejestracja.this, MainActivity.class);
//                                    } else
//                                    {
//                                        Toast.makeText(activity_rejestracja.this, "BŁĄD",
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//
//                                }
//                            });
//                        }
//                        else {
//                            Toast.makeText(activity_rejestracja.this, "AHA",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//
//
//                        updateUI(user);
//
//                    }else {
//                        // If sign in fails, display a message to the user.
//                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                        Toast.makeText(activity_rejestracja.this, "Authentication failed.",
//                                Toast.LENGTH_SHORT).show();
//                        updateUI(null);
//                    }
//                });
//        // [END create_user_with_email]
//    }
//
//    //Basic data validation
//    private void checkCredentials() {
//        String nickname = nicknameInput.getEditText().getText().toString().trim();
//        String email = emailInput.getEditText().getText().toString().trim();
//        String password = passwordInput.getEditText().getText().toString();
//        String confirmPassword = confirmPasswordInput.getEditText().getText().toString();
//        boolean flag = false;
//
//        if (nickname.isEmpty() || nickname.length() < 2) {
//            showError(nicknameInput, "Wpisz poprawną nazwę użytkownia");
//            flag = true;
//        } else {
//            showError(nicknameInput, null);
//        }
//
//        if (email.isEmpty() || !email.contains("@")) {
//            showError(emailInput, "Niepoprawny adres email");
//            flag = true;
//        } else {
//            showError(emailInput, null);
//        }
//
//        if (password.isEmpty() || password.length() < 6) {
//            showError(passwordInput, "Hasło musi zawierać przynajmniej 6 znakow");
//            flag = true;
//        } else {
//            showError(passwordInput, null);
//        }
//
//        if (confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
//            showError(confirmPasswordInput, "Hasła nie pasują");
//            flag = true;
//        } else {
//            showError(confirmPasswordInput, null);
//        }
//
//        //If no errors call createAccount()
//        if (!flag) {
//            mLoadingBar.setTitle("Rejestracja");
//            mLoadingBar.setMessage("Proszę czekać, sprawdzam poprawność dnaych");
//            mLoadingBar.setCanceledOnTouchOutside(false);
//            mLoadingBar.show();
//
//            //call createAccount()
//            createAccount(email,password,nickname);
//        }
//    }
//
//
//
//    private void showError(TextInputLayout input, String s) {
//        input.setError(s);
//        input.requestFocus();
//    }
//
//    //Implementation of AlreadySignedUp()
//    private void openAlreadySignedUpActivity() {
//        Intent intent = new Intent(this, activity_logowanie.class);
//        startActivity(intent);
//    }
//
//    private void updateUI(@Nullable FirebaseUser user) {
//        mLoadingBar.dismiss();
//        if (user != null) {
//            findViewById(R.id.mainSignInButton).setVisibility(View.GONE);
//            findViewById(R.id.mainLogoutButton).setVisibility(View.VISIBLE);
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        } else {
////            findViewById(R.id.mainSignInButton).setVisibility(View.VISIBLE);
////            findViewById(R.id.mainLogoutButton).setVisibility(View.GONE);
//        }
//
//    }
//
//    private void reload() {
//        Toast.makeText(activity_rejestracja.this, "Jesteś zalogowany",
//                Toast.LENGTH_SHORT).show();
//    }
//}