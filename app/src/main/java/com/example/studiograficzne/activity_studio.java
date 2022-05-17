package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_studio extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio);

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        Button studio_view_button = findViewById(R.id.studio_view_button);
        Button studio_panel_button = findViewById(R.id.studio_panel_button);

        studio_view_button.setOnClickListener(view -> openActivityStudioView());
        studio_panel_button.setOnClickListener(view -> openActivityStudioPanel());
    }

    public void openActivityStudioView() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_studio_view.class);
        intent.putExtra("email", currentUser.getEmail());
        startActivity(intent);
    }

    public void openActivityStudioPanel() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_studio_panel.class);
        intent.putExtra("email", currentUser.getEmail());
        startActivity(intent);
    }
}
