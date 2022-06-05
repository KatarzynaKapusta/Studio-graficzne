package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;

public class activity_endOfGame extends AppCompatActivity {

    private Button backToGameBtn;
    private Button showStatsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_game);


        backToGameBtn = findViewById(R.id.backToGameButton);
        backToGameBtn.setOnClickListener(v -> {
            finish();
        });

        showStatsBtn = findViewById(R.id.showStatisticsButton);
        showStatsBtn.setOnClickListener(v -> {
            openActivityStudioStatistics();
            finish();
        });

    }

    private void openActivityStudioStatistics() {
        Intent intent = new Intent(this, activity_studio_panel_info.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed () {}
}