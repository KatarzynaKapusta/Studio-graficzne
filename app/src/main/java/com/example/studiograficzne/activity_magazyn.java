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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class activity_magazyn extends AppCompatActivity {

    //MISSION TIMER
    private static final long START_TIME_IN_MILLIS =60000;
    private TextView mTextViewCountDown;

    private Button collect_rewards_button;
    private Button start_mission_button;

    CountDownTimer mCountDownTimer;
    private boolean mTimeRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mEndTime;

    private final String TAG = this.getClass().getName().toUpperCase();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Missions Mission1 = new Missions();
    FirebaseAuth mAuth;
    UserGameInfo User;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazyn);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Users");
        Log.v("USERID", databaseReference.getKey());

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        start_mission_button = findViewById(R.id.start_mission);
        collect_rewards_button = findViewById(R.id.collect_rewards);

        User = new UserGameInfo();

        databaseReference.addValueEventListener(new ValueEventListener() {
            Double experience, resources;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyId: dataSnapshot.getChildren()) {
                    if (keyId.child("UserInfo").child("email").getValue().equals(email))

                    {
                        experience = keyId.child("UserGameInfo").child("experience").getValue(Double.class);
                        resources = keyId.child("UserGameInfo").child("resources").getValue(Double.class);

                        break;
                    }
                }
                User.setExperience(experience);
                User.setResources(resources);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

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
    }

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

        if (user!=null)
        {
            String uid = user.getUid();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("resources", User.getResources());
            childUpdates.put("experience", User.getExperience());

            databaseReference.child(uid).child("UserGameInfo").updateChildren(childUpdates);
        }
    }
}