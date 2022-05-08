package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_nazwij_studio extends AppCompatActivity {

    private Button setStudioNameButton;
    private TextInputEditText studioNameInput;
    private String studioName;

    //Declare auth
    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nazwij_studio);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        studioNameInput = findViewById(R.id.studioNameInput);
        setStudioNameButton = findViewById(R.id.setStudioNameButton);

        setStudioNameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Insert data into firebase database
                    studioName = studioNameInput.getText().toString();
                    updateStudioName(studioName);
            }
        });

    } // OnCreate end

    private void updateStudioName(String studioName) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
            rootRef = database.getReference("Users");
            rootRef.child(uid).child("UserStudioInfo").child("studioName").setValue(studioName);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}