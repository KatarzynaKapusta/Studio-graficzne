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
    private static final String LEVELS = "Levels";
    private static final String USERS = "Users";
    private FirebaseAuth mAuth;
    private String email;
    private final String TAG = this.getClass().getName().toUpperCase();
    private final List<Double> lvlList = new ArrayList<>();
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private FirebaseDatabase database;

//    LevelsInfo LvlInfo = new LevelsInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //Budowlany
        ImageButton budowlanyButton = findViewById(R.id.budowlany);
        budowlanyButton.setOnClickListener(view -> openActivityBudowlany());

        //Bank
        ImageButton bankButton = findViewById(R.id.bank);
        bankButton.setOnClickListener(view -> openActivityBank());

        //Studio
        ImageButton studioButton = findViewById(R.id.studio);
        studioButton.setOnClickListener(view -> openActivityStudio());

        //Storage
        ImageButton storageButton = findViewById(R.id.magazyn);
        storageButton.setOnClickListener(view -> openActivityStorage());

        //Furniture
        ImageButton furnitureStoreButton = findViewById(R.id.meblowy);
        furnitureStoreButton.setOnClickListener(view -> openActivityFurnitureStore());

        //Menu
        ImageButton menuButton = findViewById(R.id.menu);
        menuButton.setOnClickListener(view -> openActivityMenu());

        //Logout
        Button mainLogoutButton = findViewById(R.id.mainLogoutButton);
        if (isRegistered()) {
            mainLogoutButton.setOnClickListener(view -> openActivityWyloguj());
        }


        //Displaying user's id
        FirebaseUser user = mAuth.getCurrentUser();
        TextView textViewUserEmail =  findViewById(R.id.textViewUserEmail);
        if(user != null){
            String uid = user.getUid();
            textViewUserEmail.setText(uid);
        } else {
            textViewUserEmail.setText(R.string.strNotLogged);
        }

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
                            moneyString = String.valueOf(money);
                            level = keyId.child("UserGameInfo").child("level").getValue(Double.class);
                            resources = keyId.child("UserGameInfo").child("resources").getValue(Double.class);
                            resourcesString = String.valueOf(resources.intValue());
                            experience = keyId.child("UserGameInfo").child("experience").getValue(Double.class);
                            experienceString = String.valueOf(experience.intValue());
                            break;
                        }
                    }

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
    } //OnCreate end

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (isRegistered()){
            Toast.makeText(MainActivity.this, "Jesteś zalogowany",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Nie jesteś zalogowany",
                    Toast.LENGTH_SHORT).show();
            openActivitySignIn();
        }
    } //OnStart end

    public boolean isRegistered() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            return true;
        else
            return false;
    }

    public void openActivityBudowlany(){
        Intent intent = new Intent(this, activity_budowlany.class);
        startActivity(intent);
    }

    public void openActivityBank(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_bank.class);
        intent.putExtra("email", currentUser.getEmail());
        startActivity(intent);
    }

    public void openActivityStudio(){
        Intent intent = new Intent(this, activity_studio.class);
        startActivity(intent);
    }

    public void openActivityStorage(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_magazyn.class);
        intent.putExtra("email", currentUser.getEmail());
        startActivity(intent);
    }

    public void openActivityFurnitureStore(){
        Intent intent = new Intent(this, activity_meblowy.class);
        startActivity(intent);
    }

    public void openActivityMenu(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_menu.class);
        intent.putExtra("email", currentUser.getEmail());
        startActivity(intent);
    }

    public void openActivitySignIn(){
        Intent intent = new Intent(this, activity_logowanie.class);
        startActivity(intent);
    }

    public void openActivityWyloguj(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "Wylogowano pomyślnie", Toast.LENGTH_SHORT).show();
        reload();
    }

    private void reload() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private Double checkUserLevel(@NonNull Double exp, Double lvl, List<Double> lvlList) {
        double localLvl = 0;
        for (int i = 0; i < lvlList.size()-1 ; )
        {
            if (exp >= lvlList.get(i) && exp < lvlList.get(i+1))
            {
                double d = i;
                localLvl = d + 1;
                break;
            }
            else {
                i+=1;
            }
        }

        if(lvl != localLvl)
        {
            updateUserLvl(localLvl);
        }

        lvlList.clear();
        return localLvl;
    }

    private void updateUserLvl(Double localLvl) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            database = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
            rootRef = database.getReference("Users");

            String keyid = rootRef.push().getKey();
            rootRef.child(uid).child("UserGameInfo").child("level").setValue(localLvl);
        }
    }


} //End of all