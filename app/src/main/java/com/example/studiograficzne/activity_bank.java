package com.example.studiograficzne;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class activity_bank extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        Button go_to_missions_button1 = findViewById(R.id.timed_missions_button);
        Button go_to_missions_button2 = findViewById(R.id.match_missions_button);

        go_to_missions_button1.setOnClickListener(view -> openActivityTimedMissions());

        go_to_missions_button2.setOnClickListener(view -> openActivityMatchMissions());
    }

    private void openActivityTimedMissions() {
        Intent intent = new Intent(this, activity_bank_timed_missions.class);
        startActivity(intent);
    }

    private void openActivityMatchMissions() {
        Intent intent = new Intent(this, activity_bank_match_missions.class);
        startActivity(intent);
    }
}
