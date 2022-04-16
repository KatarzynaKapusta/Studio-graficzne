package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //Budowlany
        ImageButton budowlanyButton = findViewById(R.id.budowlany);
        budowlanyButton.setOnClickListener(view -> openActivityBudowlany());

        //Bank
        ImageButton bankButton = findViewById(R.id.bank);
        bankButton.setOnClickListener(view -> openActivityBank());

        //Studio
        ImageButton studioButton = findViewById(R.id.studio);
        studioButton.setOnClickListener(view -> openActivityStudio());

        //Storage
        ImageButton storageButton = findViewById(R.id.magazyn);
        storageButton.setOnClickListener(view -> openActivityStorage());

        //Furniture
        ImageButton furnitureStoreButton = findViewById(R.id.meblowy);
        furnitureStoreButton.setOnClickListener(view -> openActivityFurnitureStore());

        //Menu
        ImageButton menuButton = findViewById(R.id.menu);
        menuButton.setOnClickListener(view -> openActivityMenu());

        //Sign in
        Button mainSignInButton = findViewById(R.id.mainSignInButton);
        if (isRegistered()) {
            mainSignInButton.setVisibility(View.INVISIBLE);
        }
        mainSignInButton.setOnClickListener(view -> openActivitySignIn());

        //Logout
        Button mainLogoutButton = findViewById(R.id.mainLogoutButton);
        if (!isRegistered()) {
            mainSignInButton.setVisibility(View.INVISIBLE);
        }
        mainLogoutButton.setOnClickListener(view -> openActivityWyloguj());

        //Displaying user's login
        FirebaseUser user = mAuth.getCurrentUser();
        TextView textViewUserEmail =  findViewById(R.id.textViewUserEmail);
        if(user != null){
            String uid = user.getUid();
            textViewUserEmail.setText(uid);
        }
        else {
            textViewUserEmail.setText(R.string.strNotLogged);
        }

    } //OnCreate end

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(isRegistered()){
            Toast.makeText(MainActivity.this, "Jesteś zalogowany",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(MainActivity.this, "Nie jesteś zalogowany",
                    Toast.LENGTH_SHORT).show();
            openActivitySignIn();
        }
    } //OnStart end

    public boolean isRegistered() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
            return true;
        else
            return false;
    }

    public void openActivityBudowlany(){
        Intent intent = new Intent(this, activity_budowlany.class);
        startActivity(intent);
    }

    public void openActivityBank(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_bank.class);
        intent.putExtra("email", currentUser.getEmail());
        startActivity(intent);
    }

    public void openActivityStudio(){
        Intent intent = new Intent(this, activity_studio.class);
        startActivity(intent);
    }

    public void openActivityStorage(){
        Intent intent = new Intent(this, activity_magazyn.class);
        startActivity(intent);
    }

    public void openActivityFurnitureStore(){
        Intent intent = new Intent(this, activity_meblowy.class);
        startActivity(intent);
    }

    public void openActivityMenu(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_menu.class);
        intent.putExtra("email", currentUser.getEmail());
        startActivity(intent);
    }

    public void openActivitySignIn(){
        Intent intent = new Intent(this, activity_logowanie.class);
        startActivity(intent);
    }

    public void openActivityWyloguj(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "Wylogowano pomyślnie", Toast.LENGTH_SHORT).show();
        reload();
    }

    private void reload() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}