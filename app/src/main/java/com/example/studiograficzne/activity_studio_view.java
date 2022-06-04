package com.example.studiograficzne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Map;

public class activity_studio_view extends AppCompatActivity {

    private TextView lvlTxtView, expTxtView, moneyTxtView, resTxtView;
    private ImageView plant1ImageView, plant2ImageView, plant3ImageView;
    private ImageView table1ImageView, table2ImageView, table3ImageView;
    private ImageView floor1ImageView, floor2ImageView, floor3ImageView;
    private static final String LEVELS = "Levels";
    private static final String USERS = "Users";
    private final String TAG = this.getClass().getName().toUpperCase();
    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;
    private final List<Double> lvlList = new ArrayList<>();
    private UserOwnedItems userOwnedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio_view);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email = currentUser.getEmail();
        }

        DatabaseReference userRef = rootRef.child(USERS);
        DatabaseReference lvlRef = rootRef.child(LEVELS);
        Log.v("USERID", userRef.getKey());

        lvlTxtView = findViewById(R.id.lvlStarTextView);
        expTxtView = findViewById(R.id.expBarTextView);
        moneyTxtView = findViewById(R.id.moneyBarTextView);
        resTxtView = findViewById(R.id.resBarTextView);
        FirebaseUser user = mAuth.getCurrentUser();

        // Plants
        plant1ImageView = findViewById(R.id.studioPlant1);
        plant1ImageView.setVisibility(View.INVISIBLE);

        plant2ImageView = findViewById(R.id.studioPlant2);
        plant2ImageView.setVisibility(View.INVISIBLE);

        plant3ImageView = findViewById(R.id.studioPlant3);
        plant3ImageView.setVisibility(View.INVISIBLE);

        // Tables
        table1ImageView = findViewById(R.id.studioTable1);
        table1ImageView.setVisibility(View.INVISIBLE);

        table2ImageView = findViewById(R.id.studioTable2);
        table2ImageView.setVisibility(View.INVISIBLE);

        table3ImageView = findViewById(R.id.studioTable3);
        table3ImageView.setVisibility(View.INVISIBLE);

        // Floor
        floor1ImageView = findViewById(R.id.floor1ImageView);
        floor1ImageView.setVisibility(View.INVISIBLE);

        floor2ImageView = findViewById(R.id.floor2ImageView);
        floor2ImageView.setVisibility(View.INVISIBLE);

        floor3ImageView = findViewById(R.id.floor3ImageView);
        floor3ImageView.setVisibility(View.INVISIBLE);

        if (user != null) {
            // Read from the database
            userRef.addValueEventListener(new ValueEventListener() {
                Double money, level, resources, experience, exp;
                String moneyString, levelString, resourcesString, experienceString;

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

                            Map<String,Long> m = (Map)keyId.child("UserOwnedItems").getValue();

                            userOwnedItems = new UserOwnedItems(m.get("f1").intValue(), m.get("f2").intValue(),m.get("f3").intValue(),m.get("p1").intValue(),m.get("p2").intValue(),m.get("p3").intValue(),m.get("t1").intValue(),m.get("t2").intValue(),m.get("t3").intValue());
                            break;
                        }
                    }
                    setItemsVisibility(userOwnedItems);

                    //TUTAJ
                    lvlRef.addValueEventListener(new ValueEventListener() {
                        Double exp;
                        String levelString;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot keyId : dataSnapshot.getChildren()) {
                                exp = keyId.getValue(Double.class);
                                lvlList.add(exp);
                            }
                            Double result;
                            result = checkUserLevel(experience, level, lvlList);
                            levelString = String.valueOf(result.intValue());
                            lvlTxtView.setText(levelString);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                    //TUTAJ END

                    moneyTxtView.setText(moneyString);
                    resTxtView.setText(resourcesString);
                    expTxtView.setText(experienceString);

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }

    } // onCreate end

    private void setItemsVisibility(UserOwnedItems userOwnedItems) {

        List<Integer> statuses = new ArrayList<>();
        statuses.add(userOwnedItems.getP1());
        statuses.add(userOwnedItems.getP2());
        statuses.add(userOwnedItems.getP3());
        statuses.add(userOwnedItems.getT1());
        statuses.add(userOwnedItems.getT2());
        statuses.add(userOwnedItems.getT3());
        statuses.add(userOwnedItems.getF1());
        statuses.add(userOwnedItems.getF2());
        statuses.add(userOwnedItems.getF3());

        List<ImageView> views = new ArrayList<>();
        views.add(plant1ImageView);
        views.add(plant2ImageView);
        views.add(plant3ImageView);
        views.add(table1ImageView);
        views.add(table2ImageView);
        views.add(table3ImageView);
        views.add(floor1ImageView);
        views.add(floor2ImageView);
        views.add(floor3ImageView);

        for(int i = 0; i< statuses.size(); i++){
            if(statuses.get(i)==ItemStatus.OWNED.value)
                views.get(i).setVisibility(View.VISIBLE);
            else
                views.get(i).setVisibility(View.INVISIBLE);
        }

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