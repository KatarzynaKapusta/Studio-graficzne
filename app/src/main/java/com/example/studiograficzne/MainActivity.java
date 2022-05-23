package com.example.studiograficzne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

        // Checking if user is logged or not and getting his email
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email = currentUser.getEmail();
            Toast.makeText(MainActivity.this, "You are logged in",
                    Toast.LENGTH_SHORT).show();
        }

        DatabaseReference userRef = rootRef.child(USERS);
        DatabaseReference lvlRef = rootRef.child(LEVELS);
        Log.v("USERID", userRef.getKey());

        // TextView fields
        lvlTxtView = findViewById(R.id.lvlStarTextView);
        expTxtView = findViewById(R.id.expBarTextView);
        moneyTxtView = findViewById(R.id.moneyBarTextView);
        resTxtView = findViewById(R.id.resBarTextView);

        // All buildings buttons
        // Budowlany
        ImageButton budowlanyButton = findViewById(R.id.budowlany);
        budowlanyButton.setOnClickListener(view -> openActivityBudowlany());

        // Bank
        ImageButton bankButton = findViewById(R.id.bank);
        bankButton.setOnClickListener(view -> openActivityBank());

        // Studio
        ImageButton studioButton = findViewById(R.id.studio);
        studioButton.setOnClickListener(view -> openActivityStudio());

        // Storage
        ImageButton storageButton = findViewById(R.id.magazyn);
        storageButton.setOnClickListener(view -> openActivityStorage());

        // Furniture
        ImageButton furnitureStoreButton = findViewById(R.id.meblowy);
        furnitureStoreButton.setOnClickListener(view -> openActivityFurnitureStore());

        // Menu tablet
        ImageButton menuButton = findViewById(R.id.menu);
        menuButton.setOnClickListener(view -> openActivityMenu());

        // Logout button
        Button mainLogoutButton = findViewById(R.id.mainLogoutButton);
        if (isLogged()) {
            mainLogoutButton.setOnClickListener(view -> openActivityWyloguj());
        }

        // Displaying user's id
        TextView textViewUserEmail =  findViewById(R.id.textViewUserEmail);
        if (currentUser != null){
            String uid = currentUser.getUid();
            textViewUserEmail.setText(uid);
        }
        else {
            textViewUserEmail.setText(R.string.strNotLogged);
        }

        // Reading information from the database if user is logged
        if (currentUser != null) {
            // Read from "Users" branch in db
            userRef.addValueEventListener(new ValueEventListener() {
                Double money, level, resources, experience;
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
                    }); // End of reading from "Levels" branch

                    moneyTxtView.setText(moneyString);
                    resTxtView.setText(resourcesString);
                    expTxtView.setText(experienceString);

                } // End of reading from "Users" branch

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
    } // End of OnCreate()

    @Override
    public void onStart() {
        super.onStart();

        // If user is not logged open sign in activity
        if (!isLogged()){
            Toast.makeText(MainActivity.this, "You are not logged in",
                    Toast.LENGTH_SHORT).show();
            openActivitySignIn();
        }

    } // End of OnStart()

    public boolean isLogged() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    private void reload() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private Double checkUserLevel(@NonNull Double exp, Double lvl, List<Double> lvlList) {
        double localLvl = 0, lastLvlValue = lvlList.get(lvlList.size()-1);
        int listSize = lvlList.size();
        System.out.println("wywołuję sprawdzanie levela usera");
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
            databaseOperations.updateDatabase(uid,rootRef,"level",localLvl);
//            rootRef.child(uid).child("UserGameInfo").child("level").setValue(localLvl);
        }
    }

    public void openActivityBudowlany(){
        Intent intent = new Intent(this, activity_budowlany.class);
        startActivity(intent);
    }

    public void openActivityBank(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_bank.class);
        intent.putExtra("email", currentUser != null ? currentUser.getEmail() : null);
        startActivity(intent);
    }

    public void openActivityStudio(){
        Intent intent = new Intent(this, activity_studio.class);
        startActivity(intent);
    }

    public void openActivityStorage(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_magazyn.class);
        intent.putExtra("email", currentUser != null ? currentUser.getEmail() : null);
        startActivity(intent);
    }

    public void openActivityFurnitureStore(){
        Intent intent = new Intent(this, activity_meblowy.class);
        startActivity(intent);
    }

    public void openActivityMenu(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_menu.class);
        intent.putExtra("email", currentUser != null ? currentUser.getEmail() : null);
        startActivity(intent);
    }

    public void openActivitySignIn(){
        Intent intent = new Intent(this, activity_logowanie.class);
        startActivity(intent);
    }

    public void openActivityWyloguj(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, R.string.loggedOutSucc, Toast.LENGTH_SHORT).show();
        reload();
    }

} // End of MainActivity