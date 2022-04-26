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

public class activity_magazyn_timed_missions extends AppCompatActivity {

    private Button time_button1;
    private Button time_button2;
    private Button time_button3;

    private Button start_timed_mission_button;
    private Button collect_timed_rewards_button;
    private Button back_to_missions_button;

    private View layout1;
    private View layout2;
    private View layout3;
    private View layout_m;

    private boolean rewardsCollectedStorage;

    //Timer
    private TextView mTextViewCountDownStorage;
    CountDownTimer mCountDownTimerStorage;

    private long mStartTimeInMillisStorage;
    private boolean mTimeRunningStorage;
    private long mTimeLeftInMillisStorage;
    private long mEndTimeStorage;

    //Database
    private final String TAG = this.getClass().getName().toUpperCase();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    UserGameInfo User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazyn_timed_missions);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Users");
        Log.v("USERID", databaseReference.getKey());

        time_button1 = findViewById(R.id.storage_button_1);
        time_button2 = findViewById(R.id.storage_button_2);
        time_button3 = findViewById(R.id.storage_button_3);

        start_timed_mission_button = findViewById(R.id.start_timed_mission_storage);
        collect_timed_rewards_button = findViewById(R.id.collect_timed_rewards_storage);
        back_to_missions_button = findViewById(R.id.back_to_missions_choice_storage);

        layout1 = findViewById(R.id.mission1_layout_storage);
        layout2 = findViewById(R.id.mission2_layout_storage);
        layout3 = findViewById(R.id.mission3_layout_storage);

        layout_m = findViewById(R.id.storage_timed_mission_layout);

        mTextViewCountDownStorage = findViewById(R.id.text_view_countdown_timed_storage);

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


        //Mission Choice
        time_button1.setOnClickListener(view -> {
            long time1 = 60000;
            setTimeStorage(time1);
            updateLayoutINVIS();
        });

        time_button2.setOnClickListener(view -> {
            long time2 = 120000;
            setTimeStorage(time2);
            updateLayoutINVIS();
        });

        time_button3.setOnClickListener(view -> {
            long time3 = 180000;
            setTimeStorage(time3);
            updateLayoutINVIS();

        });

        //Mission
        start_timed_mission_button.setOnClickListener(view -> {
            rewardsCollectedStorage = false;
            if (mTimeRunningStorage) {

            } else {
                startTimerStorage();
            }
        });

        collect_timed_rewards_button.setOnClickListener(view -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                if (mStartTimeInMillisStorage == 60000) {
                    StorageTimedMission1 m1 = new StorageTimedMission1();
                    User.addTimedMissionRewardsStorage(m1.getRes_1(), m1.getExp_1());
                    updateDataToFirebase();

                    Toast.makeText(activity_magazyn_timed_missions.this, "Przyznano nagrody",
                            Toast.LENGTH_SHORT).show();


                } else if (mStartTimeInMillisStorage == 120000) {
                    StorageTimedMission2 m2 = new StorageTimedMission2();
                    User.addTimedMissionRewardsStorage(m2.getRes_2(), m2.getExp_2());
                    updateDataToFirebase();

                    Toast.makeText(activity_magazyn_timed_missions.this, "Przyznano nagrody",
                            Toast.LENGTH_SHORT).show();

                } else {
                    StorageTimedMission3 m3 = new StorageTimedMission3();
                    User.addTimedMissionRewardsStorage(m3.getRes_3(), m3.getExp_3());
                    updateDataToFirebase();

                    Toast.makeText(activity_magazyn_timed_missions.this, "Przyznano nagrody",
                            Toast.LENGTH_SHORT).show();

                }
            }
            else
            {
                Toast.makeText(activity_magazyn_timed_missions.this, "BŁĄD",
                        Toast.LENGTH_SHORT).show();
            }

            rewardsCollectedStorage = true;
            resetTimerStorage();
            updateLayoutVIS();

        });

        back_to_missions_button.setOnClickListener(view -> {
            rewardsCollectedStorage=true;
            updateLayoutVIS();
        });

    }

    private void setTimeStorage(long milliseconds){
        mStartTimeInMillisStorage = milliseconds;
        resetTimerStorage();
    }

    private void startTimerStorage() {
        mEndTimeStorage = System.currentTimeMillis() + mTimeLeftInMillisStorage;
        CountDownTimer mCountDownTimerStorage = new CountDownTimer(mTimeLeftInMillisStorage,1000) {
            @Override
            public void onTick(long millisUntilFinishedStorage) {
                mTimeLeftInMillisStorage = millisUntilFinishedStorage;
                updateCountDownTextStorage();
            }

            @Override
            public void onFinish() {
                mTimeRunningStorage = false;
                updateButtonsStorage();
            }
        }.start();

        mTimeRunningStorage = true;
        updateButtonsStorage();
    }

    private void resetTimerStorage(){
        mTimeLeftInMillisStorage = mStartTimeInMillisStorage;
        updateCountDownTextStorage();
        updateButtonsStorage();
    }

    private void updateCountDownTextStorage(){
        int minutes = (int) (mTimeLeftInMillisStorage/1000) /60;
        int seconds = (int) (mTimeLeftInMillisStorage/1000) %60;

        String timeLeftFormattedStorage = String.format(Locale.getDefault(),"%02d:%02d", minutes,seconds);
        mTextViewCountDownStorage.setText(timeLeftFormattedStorage);
    }

    private void updateButtonsStorage(){
        if(mTimeRunningStorage) {
            start_timed_mission_button.setVisibility(View.INVISIBLE);
            back_to_missions_button.setVisibility(View.INVISIBLE);
        }
        else {
            start_timed_mission_button.setVisibility(View.VISIBLE);
            back_to_missions_button.setVisibility(View.VISIBLE);
            if(mTimeLeftInMillisStorage < 1000) {
                start_timed_mission_button.setVisibility(View.INVISIBLE);
                back_to_missions_button.setVisibility(View.INVISIBLE);
            }
            else {
                start_timed_mission_button.setVisibility(View.VISIBLE);
            }

            if(mTimeLeftInMillisStorage < mStartTimeInMillisStorage) {
                collect_timed_rewards_button.setVisibility(View.VISIBLE);
            }
            else {
                collect_timed_rewards_button.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void updateLayoutStorage() {
        if(mTimeRunningStorage)
        {
            updateLayoutINVIS();
        }
        else
        {
            if(rewardsCollectedStorage)
            {
                updateLayoutVIS();
            }
            else
            {
                updateLayoutINVIS();
            }
        }
    }

    private void updateLayoutINVIS(){
        layout1.setVisibility(View.INVISIBLE);
        layout2.setVisibility(View.INVISIBLE);
        layout3.setVisibility(View.INVISIBLE);
        layout_m.setVisibility(View.VISIBLE);
    }

    private void updateLayoutVIS(){
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.VISIBLE);
        layout3.setVisibility(View.VISIBLE);
        layout_m.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefsStorage = getSharedPreferences("prefsStorage", MODE_PRIVATE);
        SharedPreferences.Editor editorStorage = prefsStorage.edit();

        editorStorage.putLong("startTimeInMillisStorage", mStartTimeInMillisStorage);
        editorStorage.putLong("millisLeftStorage", mTimeLeftInMillisStorage);
        editorStorage.putBoolean("timerRunningStorage", mTimeRunningStorage);
        editorStorage.putBoolean("rewardsCollectedStorage", rewardsCollectedStorage);
        editorStorage.putLong("endTimeStorage", mEndTimeStorage);

        editorStorage.apply();

        if(mCountDownTimerStorage!=null)
        {
            mCountDownTimerStorage.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefsStorage = getSharedPreferences("prefsStorage", MODE_PRIVATE);

        mStartTimeInMillisStorage = prefsStorage.getLong("startTimeInMillisStorage", 30000);
        mTimeLeftInMillisStorage = prefsStorage.getLong("millisLeftStorage", mStartTimeInMillisStorage);

        mTimeRunningStorage = prefsStorage.getBoolean("timerRunningStorage", false);
        rewardsCollectedStorage = prefsStorage.getBoolean("rewardsCollectedStorage", false);

        updateCountDownTextStorage();
        updateButtonsStorage();
        updateLayoutStorage();

        if (mTimeRunningStorage) {
            mEndTimeStorage = prefsStorage.getLong("endTimeStorage", 0);
            mTimeLeftInMillisStorage = mEndTimeStorage - System.currentTimeMillis();

            if (mTimeLeftInMillisStorage < 0) {
                mTimeLeftInMillisStorage = 0;
                mTimeRunningStorage = false;
                updateLayoutStorage();
                updateCountDownTextStorage();
                updateButtonsStorage();
            } else {
                startTimerStorage();
            }
        }
    }

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
