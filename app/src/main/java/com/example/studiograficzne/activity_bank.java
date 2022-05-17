package com.example.studiograficzne;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_bank extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        Button go_to_missions_button1 = findViewById(R.id.bank_timed_missions_button);
        Button go_to_missions_button2 = findViewById(R.id.bank_match_missions_button);

        go_to_missions_button1.setOnClickListener(view -> openActivityBankTimedMissions());
        go_to_missions_button2.setOnClickListener(view -> openActivityBankMatchMissions());
    }

    public void openActivityBankMatchMissions() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_bank_match_missions.class);
        intent.putExtra("email", currentUser.getEmail());
        startActivity(intent);
    }

    public void openActivityBankTimedMissions() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_bank_timed_missions.class);
        intent.putExtra("email", currentUser.getEmail());
        startActivity(intent);
    }
}
