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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class activity_studio_panel extends AppCompatActivity {


    FirebaseAuth mAuth;
    private TextView lvlTxtView;
    private TextView expTxtView;
    private TextView moneyTxtView;
    private TextView resTxtView;

    private final String TAG = this.getClass().getName().toUpperCase();
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;
    private final List<Double> lvlList = new ArrayList<>();
    private final List<Double> improvementsList = new ArrayList<>();

    UserGameInfo User;

    private Button collect_earnings_button;
    private Button start_timer_earnings_button;
    private Button employees_panel_button;
    private Button items_panel_button;

    //TIMER

    private TextView mTextViewCountDownStudioPanel;
    CountDownTimer mCountDownTimerStudioPanel;

//    private static final long START_TIME_IN_MILLIS = 86400000;
    private static final long START_TIME_IN_MILLIS = 60000;
    private boolean mTimeRunningStudioPanel;
    private long mTimeLeftInMillisStudioPanel = START_TIME_IN_MILLIS;
    private long mEndTimeStudioPanel;

    private boolean earningsCollected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio_panel);

        UserStudioInfo Studio = new UserStudioInfo();
        mTextViewCountDownStudioPanel = findViewById(R.id.text_view_countdown_earnings);

        collect_earnings_button = findViewById(R.id.collect_earnings_button);
        start_timer_earnings_button = findViewById(R.id.start_earnings_button);
        employees_panel_button = findViewById(R.id.open_studio_employees_button);
        items_panel_button = findViewById(R.id.open_studio_items_button);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email = currentUser.getEmail();
        }

        DatabaseReference userRef = rootRef.child("Users");
        DatabaseReference lvlRef = rootRef.child("Levels");
        DatabaseReference improvementsRef = rootRef.child("Improvements");
        Log.v("USERID", userRef.getKey());

        // TextView fields
        lvlTxtView = findViewById(R.id.lvlStarTextView);
        expTxtView = findViewById(R.id.expBarTextView);
        moneyTxtView = findViewById(R.id.moneyBarTextView);
        resTxtView = findViewById(R.id.resBarTextView);

        User = new UserGameInfo();

        // Reading information from the database if user is logged
        if (currentUser != null) {
            userRef.addValueEventListener(new ValueEventListener() {
                Double money, level, resources, experience, result, studio_earnings;
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


                    // Read from "Improvements" branch in db
                    improvementsRef.addValueEventListener(new ValueEventListener() {
                        Double addition,level;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot keyId : dataSnapshot.getChildren()) {
                                addition = keyId.getValue(Double.class);
                                lvlList.add(addition);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    }); // End of reading from "Improvements" branch

                    moneyTxtView.setText(moneyString);
                    resTxtView.setText(resourcesString);
                    expTxtView.setText(experienceString);

                    User.setExperience(experience);
                    User.setResources(resources);
                    User.setMoney(money);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }

        collect_earnings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    User.addStudioEarnings(Studio.getEarningsSM());
                    updateDataToFirebase();
                    Toast.makeText(activity_studio_panel.this, "Przyznano zarobki",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(activity_studio_panel.this, "BŁĄD",
                            Toast.LENGTH_SHORT).show();
                }
                earningsCollected = true;
                resetTimerStudioPanel();
            }
        });

        start_timer_earnings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                earningsCollected = false;
                mTextViewCountDownStudioPanel.setVisibility(View.VISIBLE);
                if(mTimeRunningStudioPanel){

                }
                else {
                    startTimerStudioPanel();
                }
            }
        });

        employees_panel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityStudioPanelEmployees();
            }
        });

        items_panel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityStudioPanelItems();
            }
        });

    }

    public void openActivityStudioPanelEmployees() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_studio_panel_employees.class);
        intent.putExtra("email", currentUser.getEmail());
        startActivity(intent);
    }

    public void openActivityStudioPanelItems() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = new Intent(this, activity_studio_panel_info.class);
        intent.putExtra("email", currentUser.getEmail());
        startActivity(intent);
    }


    //TIMER

    private void startTimerStudioPanel() {
        mEndTimeStudioPanel = System.currentTimeMillis() + mTimeLeftInMillisStudioPanel;
        CountDownTimer mCountDownTimerBank = new CountDownTimer(mTimeLeftInMillisStudioPanel, 1000) {
            @Override
            public void onTick(long millisUntilFinishedStudioPanel) {
                mTimeLeftInMillisStudioPanel = millisUntilFinishedStudioPanel;
                updateCountDownTextStudioPanel();
            }

            @Override
            public void onFinish() {
                mTimeRunningStudioPanel = false;
                updateButtonsStudioPanel();
            }
        }.start();

        mTimeRunningStudioPanel = true;
        updateButtonsStudioPanel();
    }

    private void resetTimerStudioPanel() {
        mTimeLeftInMillisStudioPanel = START_TIME_IN_MILLIS;
        updateCountDownTextStudioPanel();
        updateButtonsStudioPanel();
    }

    private void updateCountDownTextStudioPanel() {
        int hours = (int) (mTimeLeftInMillisStudioPanel /1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillisStudioPanel /1000) %3600)/ 60;
        int seconds = (int) (mTimeLeftInMillisStudioPanel /1000) % 60;

        String timeLeftFormattedStudioPanel = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours, minutes, seconds);

        mTextViewCountDownStudioPanel.setText(timeLeftFormattedStudioPanel);
    }

    private void updateButtonsStudioPanel(){
        if(mTimeRunningStudioPanel) {
            start_timer_earnings_button.setVisibility(View.INVISIBLE);
        }
        else {
            start_timer_earnings_button.setVisibility(View.VISIBLE);

            if(mTimeLeftInMillisStudioPanel < 1000) {
                start_timer_earnings_button.setVisibility(View.INVISIBLE);
            }
            else {
                start_timer_earnings_button.setVisibility(View.VISIBLE);
            }

            if(mTimeLeftInMillisStudioPanel < START_TIME_IN_MILLIS) {
                collect_earnings_button.setVisibility(View.VISIBLE);
            }
            else {
                collect_earnings_button.setVisibility(View.INVISIBLE);
            }
        }
    }

    //Saving timer for running in background
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefsStudioPanel = getSharedPreferences("prefsStudioPanel", MODE_PRIVATE);
        SharedPreferences.Editor editorStudioPanel = prefsStudioPanel.edit();

        editorStudioPanel.putLong("millisLeftStudioPanel", mTimeLeftInMillisStudioPanel);
        editorStudioPanel.putBoolean("timerRunningStudioPanel", mTimeRunningStudioPanel);
        editorStudioPanel.putBoolean("earningsCollectedStudioPanel", earningsCollected);
        editorStudioPanel.putLong("endTimeStudioPanel", mEndTimeStudioPanel);

//        editorStudioPanel.putLong("monRew", mon);
//        editorStudioPanel.putLong("expRew", exp);

        editorStudioPanel.apply();

        if(mCountDownTimerStudioPanel !=null){
            mCountDownTimerStudioPanel.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefsStudioPanel = getSharedPreferences("prefsStudioPanel", MODE_PRIVATE);

        mTimeLeftInMillisStudioPanel = prefsStudioPanel.getLong("millisLeftStudioPanel",START_TIME_IN_MILLIS );
        mTimeRunningStudioPanel = prefsStudioPanel.getBoolean("timerRunningStudioPanel", false);
        earningsCollected = prefsStudioPanel.getBoolean("rewardsCollectedStudioPanel", false);

//        mon= prefsStudioPanel.getLong("monRew",0);
//        exp= prefsStudioPanel.getLong("expRew",0);

        updateCountDownTextStudioPanel();
        updateButtonsStudioPanel();

        if (mTimeRunningStudioPanel) {
            mEndTimeStudioPanel = prefsStudioPanel.getLong("endTimeStudioPanel", 0);
            mTimeLeftInMillisStudioPanel = mEndTimeStudioPanel - System.currentTimeMillis();

            if (mTimeLeftInMillisStudioPanel < 0) {
                mTimeLeftInMillisStudioPanel = 0;
                mTimeRunningStudioPanel = false;
                updateCountDownTextStudioPanel();
                updateButtonsStudioPanel();
            } else {
                startTimerStudioPanel();
            }
        }
    }

    //Updating data to firebase
    private void updateDataToFirebase() {

        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null) {
            Map<String, Object> childUpdates = new HashMap<>();
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