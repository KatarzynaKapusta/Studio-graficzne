package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        Button mainSignInButton = findViewById(R.id.button_logowanie);
        mainSignInButton.setOnClickListener(view -> openActivitySignIn());

        //Logout
        Button mainLogoutButton = findViewById(R.id.button_wyloguj);
        mainLogoutButton.setOnClickListener(view -> openActivityWyloguj());

    } //OnCreate end

    public void openActivityBudowlany(){
        Intent intent = new Intent(this, activity_budowlany.class);
        startActivity(intent);
    }

    public void openActivityBank(){
        Intent intent = new Intent(this, activity_bank.class);
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
        Intent intent = new Intent(this, activity_menu.class);
        startActivity(intent);
    }

    public void openActivitySignIn(){
        Intent intent = new Intent(this, activity_logowanie.class);
        startActivity(intent);
    }

    public void openActivityWyloguj(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "Wylogowano pomyslnie", Toast.LENGTH_SHORT).show();
    }
}