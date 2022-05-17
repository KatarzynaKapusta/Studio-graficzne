package com.example.studiograficzne;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class activity_magazyn extends AppCompatActivity {

    FirebaseAuth mAuth;
    private TextView lvlTxtView;
    private TextView expTxtView;
    private TextView moneyTxtView;
    private TextView resTxtView;

    private final String TAG = this.getClass().getName().toUpperCase();
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;
    private final List<Double> lvlList = new ArrayList<>();

    UserGameInfo User;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazyn);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email = currentUser.getEmail();
        }

        DatabaseReference userRef = rootRef.child("Users");
        DatabaseReference lvlRef = rootRef.child("Levels");
        Log.v("USERID", userRef.getKey());

        // TextView fields
        lvlTxtView = findViewById(R.id.lvlStarTextView);
        expTxtView = findViewById(R.id.expBarTextView);
        moneyTxtView = findViewById(R.id.moneyBarTextView);
        resTxtView = findViewById(R.id.resBarTextView);

        Button go_to_missions_button1 = findViewById(R.id.storage_timed_missions_button);
        Button go_to_missions_button2 = findViewById(R.id.storage_match_missions_button);

        go_to_missions_button1.setOnClickListener(view -> openActivityStorageTimedMissions());
        go_to_missions_button2.setOnClickListener(view -> openActivityStorageMatchMissions());

        User = new UserGameInfo();

        // Reading information from the database if user is logged
        if (currentUser != null) {
            userRef.addValueEventListener(new ValueEventListener() {
                Double money, level, resources, experience, result;
                String moneyString, resourcesString, experienceString;

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot keyId : dataSnapshot.getChildren()) {
                        if (keyId.child("UserInfo").child("email").getValue().equals(email)) {
                            money = keyId.child("UserGameInfo").child("money").getValue(Double.class);
                            moneyString = String.valueOf(money.intValue());
                            level = keyId.child("UserGameInfo").child("level").getValue(Double.class);
                            resources = keyId.child("UserGameInfo").child("resources").getValue(Double.class);
                            resourcesString = String.valueOf(resources.intValue());
                            experience = keyId.child("UserGameInfo").child("experience").getValue(Double.class);
                            experienceString = String.valueOf(experience.intValue());
                            break;
                        }
                    }

                    // Read from "Levels" branch in db
                    lvlRef.addValueEventListener(new ValueEventListener() {
                        Double exp;
                        String levelString;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot keyId : dataSnapshot.getChildren()) {
                                exp = keyId.getValue(Double.class);
                                lvlList.add(exp);
                            }

                            // Checking if level from db is correct and replacing it (if not correct)
                            result = checkUserLevel(experience, level, lvlList);
                            levelString = String.valueOf(result.intValue());
                            lvlTxtView.setText(levelString);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    }); // End of reading from "Levels" branch

                    moneyTxtView.setText(moneyString);
                    resTxtView.setText(resourcesString);
                    expTxtView.setText(experienceString);

                    User.setExperience(experience);
                    User.setResources(resources);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
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