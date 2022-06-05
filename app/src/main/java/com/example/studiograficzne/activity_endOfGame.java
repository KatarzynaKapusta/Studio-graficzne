package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class activity_endOfGame extends AppCompatActivity {

    private Button backToGameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_game);


        backToGameBtn = findViewById(R.id.backToGameButton);
        backToGameBtn.setOnClickListener(v -> {
            finish();
        });

    }

    @Override
    public void onBackPressed () {}
}