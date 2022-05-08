package com.example.studiograficzne;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    //MISSION TIMER
    private static final long START_TIME_IN_MILLIS =10000;
    private TextView mTextViewCountDown;
    private TextView lvlTxtView, expTxtView, moneyTxtView, resTxtView;

    private Button collect_rewards_button;
    private Button start_mission_button;

    CountDownTimer mCountDownTimer;
    private boolean mTimeRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mEndTime;

    private final String TAG = this.getClass().getName().toUpperCase();

    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;
    private final List<Double> lvlList = new ArrayList<>();

    Missions Mission1 = new Missions();
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

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        start_mission_button = findViewById(R.id.start_mission);
        collect_rewards_button = findViewById(R.id.collect_rewards);

        // TextView fields
        lvlTxtView = findViewById(R.id.lvlStarTextView);
        expTxtView = findViewById(R.id.expBarTextView);
        moneyTxtView = findViewById(R.id.moneyBarTextView);
        resTxtView = findViewById(R.id.resBarTextView);

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

        start_mission_button.setOnClickListener(view -> {
            if(mTimeRunning){

            }
            else {
                startTimer();
            }
        });

        collect_rewards_button.setOnClickListener(view -> {
            FirebaseUser user = mAuth.getCurrentUser();

            if(user!=null)
            {
                User.addMissionRewardsStorage(Mission1.getM_resources(), Mission1.getM_experience());
                updateDataToFirebase();

            }
            else
            {
                Toast.makeText(activity_magazyn.this, "BŁĄD",
                        Toast.LENGTH_SHORT).show();
            }
            resetTimer();
            System.out.println(User.getExperience());
            System.out.println(User.getResources());
        });


    } // End of OnCreate()

    //Mission timer
    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimeRunning = false;
                updateButtons();
            }
        }.start();

        mTimeRunning = true;
        updateButtons();
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        updateButtons();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis /1000) / 60;
        int seconds = (int) (mTimeLeftInMillis /1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateButtons(){
        if(mTimeRunning) {
            start_mission_button.setVisibility(View.INVISIBLE);
        }
        else {
            start_mission_button.setVisibility(View.VISIBLE);

            if(mTimeLeftInMillis < 1000) {
                start_mission_button.setVisibility(View.INVISIBLE);
            }
            else {
                start_mission_button.setVisibility(View.VISIBLE);
            }

            if(mTimeLeftInMillis < START_TIME_IN_MILLIS) {
                collect_rewards_button.setVisibility(View.VISIBLE);
            }
            else {
                collect_rewards_button.setVisibility(View.INVISIBLE);
            }
        }
    }
    //Mission timer end

    //Saving timer for running in background
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimeRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimeRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateButtons();

        if(mTimeRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if(mTimeLeftInMillis < 0) {
                mTimeLeftInMillis =0;
                mTimeRunning = false;
                updateCountDownText();
                updateButtons();
            }
            else{
                startTimer();
            }
        }
    }
    //End of saving timer information

    //Updating data to firebase
    private void updateDataToFirebase() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null) {
            String uid = user.getUid();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("resources", User.getResources());
            childUpdates.put("experience", User.getExperience());

            rootRef.child(uid).child("UserGameInfo").updateChildren(childUpdates);
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