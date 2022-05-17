package com.example.studiograficzne;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_magazyn extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazyn);

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        Button go_to_missions_button1 = findViewById(R.id.storage_timed_missions_button);
        Button go_to_missions_button2 = findViewById(R.id.storage_match_missions_button);

        go_to_missions_button1.setOnClickListener(view -> openActivityStorageTimedMissions());
        go_to_missions_button2.setOnClickListener(view -> openActivityStorageMatchMissions());

    }

    private void openActivityStorageMatchMissions() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_magazyn_match_missions.class);
        intent.putExtra("email", currentUser.getEmail());
        startActivity(intent);
    }

    private void openActivityStorageTimedMissions() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_magazyn_timed_missions.class);
        intent.putExtra("email", currentUser.getEmail());
        startActivity(intent);
    }

}