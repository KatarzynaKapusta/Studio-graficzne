package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton button1;
    private ImageButton button2;
    private ImageButton button3;
    private ImageButton button4;
    private ImageButton button5;
    private ImageButton button6;
    private Button button7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1= (ImageButton) findViewById(R.id.budowlany);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityBudowlany();
            }
        });

        button2 = (ImageButton) findViewById(R.id.bank);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityBank();
            }
        });

        button3 = (ImageButton) findViewById(R.id.studio);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityStudio();
            }
        });

        button4 = (ImageButton) findViewById(R.id.magazyn);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityMagazyn();
            }
        });

        button5=(ImageButton) findViewById(R.id.meblowy);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityMeblowy();
            }
        });

        button6 = (ImageButton) findViewById(R.id.menu);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityMenu();
            }
        });

        button7 = (Button) findViewById(R.id.button_logowanie);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityLogowanie();
            }
        });

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
}