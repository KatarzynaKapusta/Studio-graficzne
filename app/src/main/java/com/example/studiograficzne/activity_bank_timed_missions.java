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

    private boolean rewardsCollectedBank;

    //TIMER

    private TextView mTextViewCountDownBank;
    CountDownTimer mCountDownTimerBank;

    private long mStartTimeInMillisBank;
    private boolean mTimeRunningBank;
    private long mTimeLeftInMillisBank;
    private long mEndTimeBank;

    private final String TAG = this.getClass().getName().toUpperCase();

    //TextView Rewards
    private TextView Experience;
    private TextView vExperience;
    private TextView Money;
    private TextView vMoney;
    private TextView RewardsAcquiredBank;
    private long mon, exp;
    BankTimedMission1 m1;
    BankTimedMission2 m2;
    BankTimedMission3 m3;

    //DATABASE
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    // Resources bars & database
    private TextView lvlTxtView, expTxtView, moneyTxtView, resTxtView;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;
    private final List<Double> lvlList = new ArrayList<>();

    UserGameInfo User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_timed_missions);

        //Rewards TextViews
        Experience = findViewById(R.id.rewards_exp_bt);
        vExperience = findViewById(R.id.rewards_exp_btv);
        Money = findViewById(R.id.rewards_mon_bt);
        vMoney = findViewById(R.id.rewards_mon_btv);
        RewardsAcquiredBank = findViewById(R.id.rewards_acquired_bt);

        // Getting email from current user if loogged
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email = currentUser.getEmail();
        }

        // Db refereces
        DatabaseReference userRef = rootRef.child("Users");
        DatabaseReference lvlRef = rootRef.child("Levels");

        // Resources bars
        lvlTxtView = findViewById(R.id.lvlStarTextView);
        expTxtView = findViewById(R.id.expBarTextView);
        moneyTxtView = findViewById(R.id.moneyBarTextView);
        resTxtView = findViewById(R.id.resBarTextView);


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

        mTextViewCountDownBank = findViewById(R.id.text_view_countdown_timed);

        //Database
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Users");
        Log.v("USERID", databaseReference.getKey());

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        User = new UserGameInfo();
        m1 = new BankTimedMission1();
        m2 = new BankTimedMission2();
        m3 = new BankTimedMission3();

        // Reading from database
        if(currentUser!=null) {
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
                    User.setMoney(money);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
        //End of reading database

        //Mission choice
        time_button1.setOnClickListener(view -> {
            long time1 = 60000;
            setTimeBank(time1);
            updateLayoutINVIS();
        });

        time_button2.setOnClickListener(view -> {
            long time2 = 120000;
            setTimeBank(time2);
            updateLayoutINVIS();
        });

        time_button3.setOnClickListener(view -> {
            long time3 = 180000;
            setTimeBank(time3);
            updateLayoutINVIS();
        });
        //End of mission choice

        //Chosen timed mission
        start_timed_mission_button.setOnClickListener(view -> {
            rewardsCollectedBank = false;
            mTextViewCountDownBank.setVisibility(View.VISIBLE);
            if(mTimeRunningBank){

            }
            else {
                startTimerBank();
            }
        });

        collect_timed_rewards_button.setOnClickListener(view -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                if (mStartTimeInMillisBank == 60000) {
                    User.addTimedMissionRewardsBank(m1.getMoney_1(), m1.getExp_1());
                    updateDataToFirebase();

                    Toast.makeText(activity_bank_timed_missions.this, "Przyznano nagrody",
                            Toast.LENGTH_SHORT).show();

                } else if (mStartTimeInMillisBank == 120000) {
                    User.addTimedMissionRewardsBank(m2.getMoney_2(), m2.getExp_2());
                    updateDataToFirebase();

                    Toast.makeText(activity_bank_timed_missions.this, "Przyznano nagrody",
                            Toast.LENGTH_SHORT).show();
                } else {
                    User.addTimedMissionRewardsBank(m3.getMoney_3(), m3.getExp_3());
                    updateDataToFirebase();

                    Toast.makeText(activity_bank_timed_missions.this, "Przyznano nagrody",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(activity_bank_timed_missions.this, "BŁĄD",
                        Toast.LENGTH_SHORT).show();
            }

            mTextViewCountDownBank.setVisibility(View.INVISIBLE);
            rewardsCollectedBank = true;
            resetTimerBank();
            updateLayoutVIS();
            updateTextViewINVIS();
        });

        back_to_missions_button.setOnClickListener(view -> {
            rewardsCollectedBank=true;
            updateLayoutVIS();
        });
    }

    //Mission timer
    private void setTimeBank(long milliseconds)
    {
        mStartTimeInMillisBank = milliseconds;
        setTextView();
        resetTimerBank();
    }

    private void startTimerBank() {
        mEndTimeBank = System.currentTimeMillis() + mTimeLeftInMillisBank;
        CountDownTimer mCountDownTimerBank = new CountDownTimer(mTimeLeftInMillisBank, 1000) {
            @Override
            public void onTick(long millisUntilFinishedBank) {
                mTimeLeftInMillisBank = millisUntilFinishedBank;
                updateCountDownTextBank();
            }

            @Override
            public void onFinish() {
                mTimeRunningBank = false;
                updateButtonsBank();
                updateTextViewVIS();
            }
        }.start();

        mTimeRunningBank = true;
        updateButtonsBank();
    }

    private void resetTimerBank() {
        mTimeLeftInMillisBank = mStartTimeInMillisBank;
        updateCountDownTextBank();
        updateButtonsBank();
    }

    private void updateCountDownTextBank() {
        int minutes = (int) (mTimeLeftInMillisBank /1000) / 60;
        int seconds = (int) (mTimeLeftInMillisBank /1000) % 60;

        String timeLeftFormattedBank = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        mTextViewCountDownBank.setText(timeLeftFormattedBank);
    }

    private void updateButtonsBank(){
        if(mTimeRunningBank) {
            start_timed_mission_button.setVisibility(View.INVISIBLE);
            back_to_missions_button.setVisibility(View.INVISIBLE);
            mTextViewCountDownBank.setVisibility(View.VISIBLE);
        }
        else {
            start_timed_mission_button.setVisibility(View.VISIBLE);
            back_to_missions_button.setVisibility(View.VISIBLE);
            mTextViewCountDownBank.setVisibility(View.INVISIBLE);

            if(mTimeLeftInMillisBank < 1000) {
                start_timed_mission_button.setVisibility(View.INVISIBLE);
                back_to_missions_button.setVisibility(View.INVISIBLE);
                mTextViewCountDownBank.setVisibility(View.VISIBLE);
            }
            else {
                start_timed_mission_button.setVisibility(View.VISIBLE);
                mTextViewCountDownBank.setVisibility(View.VISIBLE);
            }

            if(mTimeLeftInMillisBank < mStartTimeInMillisBank) {
                collect_timed_rewards_button.setVisibility(View.VISIBLE);
                mTextViewCountDownBank.setVisibility(View.INVISIBLE);
            }
            else {
                collect_timed_rewards_button.setVisibility(View.INVISIBLE);
                mTextViewCountDownBank.setVisibility(View.VISIBLE);
            }
        }
    }
    //Mission timer end

    //Layout update
    private void updateLayoutBank() {
        if(mTimeRunningBank) {
            updateLayoutINVIS();
            updateTextViewINVIS();
        }
        else {
            updateTextViewINVIS();
            if(rewardsCollectedBank) {
                updateLayoutVIS();
                updateTextViewINVIS();
            }
            else {
                updateLayoutINVIS();
                updateTextViewVIS();
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

    private void updateTextViewVIS(){
        RewardsAcquiredBank.setVisibility(View.VISIBLE);

        vMoney.setVisibility(View.VISIBLE);
        Money.setVisibility(View.VISIBLE);
        vExperience.setVisibility(View.VISIBLE);
        Experience.setVisibility(View.VISIBLE);
    }

    private void updateTextViewINVIS(){
        RewardsAcquiredBank.setVisibility(View.INVISIBLE);

        vMoney.setVisibility(View.INVISIBLE);
        Money.setVisibility(View.INVISIBLE);
        vExperience.setVisibility(View.INVISIBLE);
        Experience.setVisibility(View.INVISIBLE);
    }
    //End of Layout update

    private void setTextView(){

        if (mStartTimeInMillisBank == 60000) {
            mon = (long)(m1.getMoney_1());
            exp = (long)(m1.getExp_1());
        } else if (mStartTimeInMillisBank == 120000) {
            mon = (long)(m2.getMoney_2());
            exp = (long)(m2.getExp_2());
        } else {
            mon = (long)(m3.getMoney_3());
            exp = (long)(m3.getExp_3());
        }
        String expString = String.valueOf(mon);
        String resString = String.valueOf(exp);

        vExperience.setText(expString);
        vMoney.setText(resString);
    }

    //Saving timer for running in background
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefsBank = getSharedPreferences("prefsBank", MODE_PRIVATE);
        SharedPreferences.Editor editorBank = prefsBank.edit();

        editorBank.putLong("startTimeInMillisBank", mStartTimeInMillisBank);
        editorBank.putLong("millisLeftBank", mTimeLeftInMillisBank);
        editorBank.putBoolean("timerRunningBank", mTimeRunningBank);
        editorBank.putBoolean("rewardsCollectedBank", rewardsCollectedBank);
        editorBank.putLong("endTimeBank", mEndTimeBank);

        editorBank.putLong("monRew", mon);
        editorBank.putLong("expRew", exp);

        editorBank.apply();

        if(mCountDownTimerBank !=null){
            mCountDownTimerBank.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefsBank = getSharedPreferences("prefsBank", MODE_PRIVATE);

        mStartTimeInMillisBank =prefsBank.getLong("startTimeInMillisBank", 30000);
        mTimeLeftInMillisBank = prefsBank.getLong("millisLeftBank", mStartTimeInMillisBank);

        mTimeRunningBank = prefsBank.getBoolean("timerRunningBank", false);
        rewardsCollectedBank = prefsBank.getBoolean("rewardsCollectedBank", false);

        mon= prefsBank.getLong("monRew",0);
        exp= prefsBank.getLong("expRew",0);

        updateCountDownTextBank();
        updateButtonsBank();
        updateLayoutBank();
        setTextView();

        if (mTimeRunningBank) {
            mEndTimeBank = prefsBank.getLong("endTimeBank", 0);
            mTimeLeftInMillisBank = mEndTimeBank - System.currentTimeMillis();

            if (mTimeLeftInMillisBank < 0) {
                mTimeLeftInMillisBank = 0;
                mTimeRunningBank = false;
                updateLayoutBank();
                updateCountDownTextBank();
                updateButtonsBank();
            } else {
                startTimerBank();
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

            rootRef.child(user.getUid()).child("UserGameInfo").updateChildren(childUpdates);
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