package com.example.studiograficzne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_studio extends AppCompatActivity {


    private TextView lvlTxtView, expTxtView, moneyTxtView, resTxtView;
    private static final String LEVELS = "Levels", USERS = "Users";
    private final String TAG = this.getClass().getName().toUpperCase();

    // Database variables
    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;

    // List for levels
    private final List<Double> lvlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio);


        DatabaseReference userRef = rootRef.child(USERS);
        DatabaseReference lvlRef = rootRef.child(LEVELS);
        Log.v("USERID", userRef.getKey());

        // TextView fields
        lvlTxtView = findViewById(R.id.lvlStarTextView);
        expTxtView = findViewById(R.id.expBarTextView);
        moneyTxtView = findViewById(R.id.moneyBarTextView);
        resTxtView = findViewById(R.id.resBarTextView);

        // Checking if user is logged or not and getting his email
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email = currentUser.getEmail();
        }



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


    private Double checkUserLevel(@NonNull Double exp, Double lvl, List<Double> lvlList) {
        double localLvl = 0, lastLvlValue = lvlList.get(lvlList.size()-1);
        int listSize = lvlList.size();

        for (int i = 0; i < lvlList.size()-1 ; ) {
            // If exp is greater than maximum lvl value in db
            if (exp >= lastLvlValue) {
                updateUserLvl((double) listSize);
                lvlList.clear();
                return (double) listSize;
            }
            if (exp >= lvlList.get(i) && exp < lvlList.get(i + 1)) {
                localLvl = (double) i + 1;
                break;
            }
            else {
                i += 1;
            }
        }

        if (lvl != localLvl) {
            updateUserLvl(localLvl);
        }

        lvlList.clear();
        return localLvl;
    }

    private void updateUserLvl(Double localLvl) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
            rootRef = database.getReference("Users");
            rootRef.child(uid).child("UserGameInfo").child("level").setValue(localLvl);
        }
    }

}
