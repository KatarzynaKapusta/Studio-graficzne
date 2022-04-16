package com.example.studiograficzne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class activity_bank_timed_missions extends AppCompatActivity {

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

    private boolean rewardsCollected;

    //TIMER

    private TextView mTextViewCountDown;
    CountDownTimer mCountDownTimer;

    private long mStartTimeInMillis;
    private boolean mTimeRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;

    private final String TAG = this.getClass().getName().toUpperCase();

    //DATABASE
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    UserGameInfo User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_timed_missions);

        time_button1 = findViewById(R.id.button_1);
        time_button2 = findViewById(R.id.button_2);
        time_button3 = findViewById(R.id.button_3);

        start_timed_mission_button = findViewById(R.id.start_timed_mission);
        collect_timed_rewards_button = findViewById(R.id.collect_timed_rewards);
        back_to_missions_button = findViewById(R.id.back_to_missions_choice);

        layout1 = findViewById(R.id.mission1_layout);
        layout2 = findViewById(R.id.mission2_layout);
        layout3 = findViewById(R.id.mission3_layout);

        layout_m = findViewById(R.id.timed_mission_layout);

        mTextViewCountDown = findViewById(R.id.text_view_countdown_timed);

        mStartTimeInMillis = -1;
        mTimeLeftInMillis =-1;

        //Database
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Users");
        Log.v("USERID", databaseReference.getKey());

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        User = new UserGameInfo();

        //Reading database
        databaseReference.addValueEventListener(new ValueEventListener() {
            Double money, experience;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyId: dataSnapshot.getChildren()) {
                    if (keyId.child("UserInfo").child("email").getValue().equals(email))
                    {
                        experience = keyId.child("UserGameInfo").child("experience").getValue(Double.class);
                        money = keyId.child("UserGameInfo").child("money").getValue(Double.class);

                        break;
                    }
                }
                User.setExperience(experience);
                User.setMoney(money);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        //End of reading database

        //Mission choice
        time_button1.setOnClickListener(view -> {
            rewardsCollected = false;
            long time1 = 60000;
            setTime(time1);
            updateLayoutINVIS();
        });

        time_button2.setOnClickListener(view -> {
            rewardsCollected = false;
            long time2 = 120000;
            setTime(time2);
            updateLayoutINVIS();
        });

        time_button3.setOnClickListener(view -> {
            rewardsCollected = false;
            long time3 = 180000;
            setTime(time3);
            updateLayoutINVIS();
        });
        //End of mission choice

        //Chosen timed mission
        start_timed_mission_button.setOnClickListener(view -> {
            rewardsCollected = false;
            if(mTimeRunning){

            }
            else
            {
                startTimer();
            }
        });

        collect_timed_rewards_button.setOnClickListener(view -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                if (mStartTimeInMillis == 60000) {
                    BankTimedMission1 m1 = new BankTimedMission1();
                    User.addTimedMissionRewardsBank(m1.getMoney_1(), m1.getExp_1());
                    updateDataToFirebase();

                    Toast.makeText(activity_bank_timed_missions.this, "Przyznano nagrody",
                            Toast.LENGTH_SHORT).show();

                    rewardsCollected =true;
                } else if (mStartTimeInMillis == 120000) {
                    BankTimedMission2 m2 = new BankTimedMission2();
                    User.addTimedMissionRewardsBank(m2.getMoney_2(), m2.getExp_2());
                    updateDataToFirebase();

                    Toast.makeText(activity_bank_timed_missions.this, "Przyznano nagrody",
                            Toast.LENGTH_SHORT).show();
                    rewardsCollected =true;
                } else {
                    BankTimedMission3 m3 = new BankTimedMission3();
                    User.addTimedMissionRewardsBank(m3.getMoney_3(), m3.getExp_3());
                    updateDataToFirebase();

                    Toast.makeText(activity_bank_timed_missions.this, "Przyznano nagrody",
                            Toast.LENGTH_SHORT).show();
                    rewardsCollected =true;
                }
            }
            else
            {
                Toast.makeText(activity_bank_timed_missions.this, "BŁĄD",
                        Toast.LENGTH_SHORT).show();
            }
            resetTimer();
            updateLayoutVIS();
        });

        back_to_missions_button.setOnClickListener(view -> updateLayoutVIS());
    }

    //Mission timer
    private  void setTime(long milliseconds)
    {
        mStartTimeInMillis = milliseconds;
        resetTimer();
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        CountDownTimer mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
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
        mTimeLeftInMillis = mStartTimeInMillis;
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
            start_timed_mission_button.setVisibility(View.INVISIBLE);
        }
        else {
            start_timed_mission_button.setVisibility(View.VISIBLE);
            if(mTimeLeftInMillis < 1000) {
                start_timed_mission_button.setVisibility(View.INVISIBLE);
            }
            else {
                start_timed_mission_button.setVisibility(View.VISIBLE);
            }

            if(mTimeLeftInMillis < mStartTimeInMillis) {
                collect_timed_rewards_button.setVisibility(View.VISIBLE);
            }
            else {
                collect_timed_rewards_button.setVisibility(View.INVISIBLE);
            }
        }
    }
    //Mission timer end

    private void updateLayout() {
        if(mTimeRunning)
        {
            updateLayoutINVIS();
        }
        else
        {
            if(rewardsCollected)
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
    //End of Layout update

    //Saving timer for running in background
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimeRunning);
        editor.putBoolean("rewardsCollected", rewardsCollected);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if(mCountDownTimer !=null){
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis=prefs.getLong("startTimeInMillis", 10000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);

        mTimeRunning = prefs.getBoolean("timerRunning", false);
        rewardsCollected = prefs.getBoolean("rewardsCollected", false);

        updateCountDownText();
        updateButtons();
        updateLayout();

        if (mTimeRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimeRunning = false;
                updateLayout();
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }
    }
    //End of saving timer information

    //Updating data to firebase
    private void updateDataToFirebase() {

        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null) {
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("experience", User.getExperience());
            childUpdates.put("money", User.getMoney());

            databaseReference.child(user.getUid()).child("UserGameInfo").updateChildren(childUpdates);
        }
    }
}