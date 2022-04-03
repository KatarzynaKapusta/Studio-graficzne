package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ImageButton button1;
    private ImageButton button2;
    private ImageButton button3;
    private ImageButton button4;
    private ImageButton button5;
    private ImageButton button6;
    private Button button7;
    private Button button8;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1=  findViewById(R.id.budowlany);
        button1.setOnClickListener(view -> openActivityBudowlany());

        button2 = findViewById(R.id.bank);
        button2.setOnClickListener(view -> openActivityBank());

        button3 = findViewById(R.id.studio);
        button3.setOnClickListener(view -> openActivityStudio());

        button4 = findViewById(R.id.magazyn);
        button4.setOnClickListener(view -> openActivityMagazyn());

        button5= findViewById(R.id.meblowy);
        button5.setOnClickListener(view -> openActivityMeblowy());

        button6 =  findViewById(R.id.menu);
        button6.setOnClickListener(view -> openActivityMenu());

        button7 = findViewById(R.id.button_logowanie);
        button7.setOnClickListener(view -> openActivityLogowanie());

        mAuth = FirebaseAuth.getInstance();

        button8 = findViewById(R.id.button_wyloguj);
        button8.setOnClickListener(view -> openActivityWyloguj());

    }
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
    public void openActivityMagazyn(){
        Intent intent = new Intent(this, activity_magazyn.class);
        startActivity(intent);
    }

    public void openActivityMeblowy(){
        Intent intent = new Intent(this, activity_meblowy.class);
        startActivity(intent);
    }

    public void openActivityMenu(){
        Intent intent = new Intent(this, activity_menu.class);
        startActivity(intent);
    }

    public void openActivityLogowanie(){

        Intent intent = new Intent(this, activity_logowanie.class);
        startActivity(intent);
    }

    public void openActivityWyloguj(){
        Toast.makeText(MainActivity.this, "Wylogowano pomyslnie", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,activity_logowanie.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}