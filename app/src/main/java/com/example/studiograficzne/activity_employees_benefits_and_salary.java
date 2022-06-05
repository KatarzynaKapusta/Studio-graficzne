package com.example.studiograficzne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
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

public class activity_employees_benefits_and_salary extends AppCompatActivity {

    private TextView lvlTxtView, expTxtView, moneyTxtView, resTxtView;
    private static final String LEVELS = "Levels", USERS = "Users", EMPLOYEES = "Employees";
    private final String TAG = this.getClass().getName().toUpperCase();

    // Database variables
    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;

    // List for levels
    private final List<Double> lvlList = new ArrayList<>();
    // Database variables

    UserGameInfo User;
    UserEmployeesInfo userEmployeesInfo;

    //TIMER
    private TextView mTextViewCountDownStudioSalary1;
    private TextView mTextViewCountDownStudioSalary2;
    private TextView mTextViewCountDownStudioSalary3;

    CountDownTimer mCountDownTimerStudioSalary1;
    CountDownTimer mCountDownTimerStudioSalary2;
    CountDownTimer mCountDownTimerStudioSalary3;

    //    private static final long START_TIME_IN_MILLIS = 86400000;
    private static final long START_TIME_IN_MILLIS = 60000;

    private boolean mTimeRunningStudioSalary1;
    private long mTimeLeftInMillisStudioSalary1 = START_TIME_IN_MILLIS;
    private long mEndTimeStudioSalary1;

    private boolean mTimeRunningStudioSalary2;
    private long mTimeLeftInMillisStudioSalary2 = START_TIME_IN_MILLIS;
    private long mEndTimeStudioSalary2;

    private boolean mTimeRunningStudioSalary3;
    private long mTimeLeftInMillisStudioSalary3 = START_TIME_IN_MILLIS;
    private long mEndTimeStudioSalary3;


    private boolean earningsCollected1;
    private boolean earningsCollected2;
    private boolean earningsCollected3;

    //Buttons
    private Button start_salary_timer_button1;
    private Button collect_benefits_pay_salary_button1;

    private Button start_salary_timer_button2;
    private Button collect_benefits_pay_salary_button2;

    private Button start_salary_timer_button3;
    private Button collect_benefits_pay_salary_button3;

    //Employee earnings
    private Employee emp1;
    private Employee emp2;
    private Employee emp3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees_benefits_and_salary);

        User = new UserGameInfo();
        userEmployeesInfo = new UserEmployeesInfo();
        emp1 = new Employee();
        emp2 = new Employee();
        emp3 = new Employee();

        // Checking if user is logged or not and getting his email
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email = currentUser.getEmail();
        }

        DatabaseReference userRef = rootRef.child(USERS);
        DatabaseReference lvlRef = rootRef.child(LEVELS);
        DatabaseReference empRef = rootRef.child(EMPLOYEES);
        Log.v("USERID", userRef.getKey());

        // TextView fields
        lvlTxtView = findViewById(R.id.lvlStarTextView);
        expTxtView = findViewById(R.id.expBarTextView);
        moneyTxtView = findViewById(R.id.moneyBarTextView);
        resTxtView = findViewById(R.id.resBarTextView);

        //Button fields
        start_salary_timer_button1 =findViewById(R.id.start_salary_timer_button1);
        collect_benefits_pay_salary_button1 = findViewById(R.id.collect_earnings_pay_salary_button1);

        start_salary_timer_button2 =findViewById(R.id.start_salary_timer_button2);
        collect_benefits_pay_salary_button2 = findViewById(R.id.collect_earnings_pay_salary_button2);

        start_salary_timer_button3 =findViewById(R.id.start_salary_timer_button3);
        collect_benefits_pay_salary_button3 = findViewById(R.id.collect_earnings_pay_salary_button3);

        //Timers TextViews
        mTextViewCountDownStudioSalary1 = findViewById(R.id.text_view_countdown_studio_salary1);
        mTextViewCountDownStudioSalary2 = findViewById(R.id.text_view_countdown_studio_salary2);
        mTextViewCountDownStudioSalary3 = findViewById(R.id.text_view_countdown_studio_salary3);

        // Reading information from the database if user is logged
        readFromDatabase(currentUser, userRef, lvlRef, empRef);

        //Employees timers
        start_salary_timer_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                earningsCollected1 = false;
                User.payEmployeesSalary(emp1.getSalary());
                updateDataToFirebase();
                Toast.makeText(activity_employees_benefits_and_salary.this, "Opłacono pracownika",
                        Toast.LENGTH_SHORT).show();
                if(mTimeRunningStudioSalary1){

                }
                else {
                    startTimerStudioSalary1();
                }
            }
        });

        start_salary_timer_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                earningsCollected2 = false;
                User.payEmployeesSalary(emp2.getSalary());
                updateDataToFirebase();
                Toast.makeText(activity_employees_benefits_and_salary.this, "Opłacono pracownika",
                        Toast.LENGTH_SHORT).show();
                if(mTimeRunningStudioSalary2){

                }
                else {
                    startTimerStudioSalary2();
                }
            }
        });

        start_salary_timer_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                earningsCollected3 = false;
                User.payEmployeesSalary(emp3.getSalary());
                updateDataToFirebase();
                Toast.makeText(activity_employees_benefits_and_salary.this, "Opłacono pracownika",
                        Toast.LENGTH_SHORT).show();
                if(mTimeRunningStudioSalary3){

                }
                else {
                    startTimerStudioSalary3();
                }
            }
        });

        //Collecting benefits and paying salary
        collect_benefits_pay_salary_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {

                    User.addEmployeesEarnings(emp1.getAdditional_money1());
                    updateDataToFirebase();
                    Toast.makeText(activity_employees_benefits_and_salary.this, "Przyznano zarobki",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(activity_employees_benefits_and_salary.this, "BŁĄD",
                            Toast.LENGTH_SHORT).show();
                }
                earningsCollected1 = true;
                resetTimerStudioSalary1();
            }
        });

        collect_benefits_pay_salary_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {

                    User.addEmployeesEarnings(emp2.getAdditional_money1());
                    updateDataToFirebase();
                    Toast.makeText(activity_employees_benefits_and_salary.this, "Przyznano zarobki",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(activity_employees_benefits_and_salary.this, "BŁĄD",
                            Toast.LENGTH_SHORT).show();
                }
                earningsCollected2 = true;
                resetTimerStudioSalary2();
            }
        });

        collect_benefits_pay_salary_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {

                    User.addEmployeesEarnings(emp3.getAdditional_money1());
                    updateDataToFirebase();
                    Toast.makeText(activity_employees_benefits_and_salary.this, "Przyznano zarobki",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(activity_employees_benefits_and_salary.this, "BŁĄD",
                            Toast.LENGTH_SHORT).show();
                }
                earningsCollected3 = true;
                resetTimerStudioSalary3();
            }
        });

    }

    //TIMER 1

    private void startTimerStudioSalary1() {
        mEndTimeStudioSalary1 = System.currentTimeMillis() + mTimeLeftInMillisStudioSalary1;
        CountDownTimer mCountDownTimerSalary1 = new CountDownTimer(mTimeLeftInMillisStudioSalary1, 1000) {
            @Override
            public void onTick(long millisUntilFinishedStudioSalary1) {
                mTimeLeftInMillisStudioSalary1 = millisUntilFinishedStudioSalary1;
                updateCountDownTextStudioSalary1();
            }

            @Override
            public void onFinish() {
                mTimeRunningStudioSalary1 = false;
                updateButtonsStudioSalary1();
            }
        }.start();

        mTimeRunningStudioSalary1 = true;
        updateButtonsStudioSalary1();
    }

    private void resetTimerStudioSalary1() {
        mTimeLeftInMillisStudioSalary1 = START_TIME_IN_MILLIS;
        updateCountDownTextStudioSalary1();
        updateButtonsStudioSalary1();
    }

    private void updateCountDownTextStudioSalary1() {
        int hours = (int) (mTimeLeftInMillisStudioSalary1 /1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillisStudioSalary1 /1000) %3600)/ 60;
        int seconds = (int) (mTimeLeftInMillisStudioSalary1 /1000) % 60;

        String timeLeftFormattedStudioSalary1 = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours, minutes, seconds);

        mTextViewCountDownStudioSalary1.setText(timeLeftFormattedStudioSalary1);
    }

    private void updateButtonsStudioSalary1(){
        if(mTimeRunningStudioSalary1) {
            start_salary_timer_button1.setVisibility(View.INVISIBLE);
        }
        else {
            start_salary_timer_button1.setVisibility(View.VISIBLE);

            if(mTimeLeftInMillisStudioSalary1 < 1000) {
                start_salary_timer_button1.setVisibility(View.INVISIBLE);
            }
            else {
                start_salary_timer_button1.setVisibility(View.VISIBLE);
            }

            if(mTimeLeftInMillisStudioSalary1 < START_TIME_IN_MILLIS) {
                collect_benefits_pay_salary_button1.setVisibility(View.VISIBLE);
            }
            else {
                collect_benefits_pay_salary_button1.setVisibility(View.INVISIBLE);
            }
        }
    }
    //END TIMER 1

    //TIMER 2
    private void startTimerStudioSalary2() {
        mEndTimeStudioSalary2 = System.currentTimeMillis() + mTimeLeftInMillisStudioSalary2;
        CountDownTimer mCountDownTimerSalary2 = new CountDownTimer(mTimeLeftInMillisStudioSalary2, 1000) {
            @Override
            public void onTick(long millisUntilFinishedStudioSalary2) {
                mTimeLeftInMillisStudioSalary2 = millisUntilFinishedStudioSalary2;
                updateCountDownTextStudioSalary2();
            }

            @Override
            public void onFinish() {
                mTimeRunningStudioSalary2 = false;
                updateButtonsStudioSalary2();
            }
        }.start();

        mTimeRunningStudioSalary2 = true;
        updateButtonsStudioSalary2();
    }

    private void resetTimerStudioSalary2() {
        mTimeLeftInMillisStudioSalary2 = START_TIME_IN_MILLIS;
        updateCountDownTextStudioSalary2();
        updateButtonsStudioSalary2();
    }

    private void updateCountDownTextStudioSalary2() {
        int hours = (int) (mTimeLeftInMillisStudioSalary2 /1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillisStudioSalary2 /1000) %3600)/ 60;
        int seconds = (int) (mTimeLeftInMillisStudioSalary2 /1000) % 60;

        String timeLeftFormattedStudioSalary2 = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours, minutes, seconds);

        mTextViewCountDownStudioSalary2.setText(timeLeftFormattedStudioSalary2);
    }

    private void updateButtonsStudioSalary2(){
        if(mTimeRunningStudioSalary2) {
            start_salary_timer_button2.setVisibility(View.INVISIBLE);
        }
        else {
            start_salary_timer_button2.setVisibility(View.VISIBLE);

            if(mTimeLeftInMillisStudioSalary2 < 1000) {
                start_salary_timer_button2.setVisibility(View.INVISIBLE);
            }
            else {
                start_salary_timer_button2.setVisibility(View.VISIBLE);
            }

            if(mTimeLeftInMillisStudioSalary2 < START_TIME_IN_MILLIS) {
                collect_benefits_pay_salary_button2.setVisibility(View.VISIBLE);
            }
            else {
                collect_benefits_pay_salary_button2.setVisibility(View.INVISIBLE);
            }
        }
    }
    //END TIMER 2

    //TIMER 3

    private void startTimerStudioSalary3() {
        mEndTimeStudioSalary3 = System.currentTimeMillis() + mTimeLeftInMillisStudioSalary3;
        CountDownTimer mCountDownTimerSalary3 = new CountDownTimer(mTimeLeftInMillisStudioSalary3, 1000) {
            @Override
            public void onTick(long millisUntilFinishedStudioSalary3) {
                mTimeLeftInMillisStudioSalary3 = millisUntilFinishedStudioSalary3;
                updateCountDownTextStudioSalary3();
            }

            @Override
            public void onFinish() {
                mTimeRunningStudioSalary3 = false;
                updateButtonsStudioSalary3();
            }
        }.start();

        mTimeRunningStudioSalary3 = true;
        updateButtonsStudioSalary3();
    }

    private void resetTimerStudioSalary3() {
        mTimeLeftInMillisStudioSalary3 = START_TIME_IN_MILLIS;
        updateCountDownTextStudioSalary3();
        updateButtonsStudioSalary3();
    }

    private void updateCountDownTextStudioSalary3() {
        int hours = (int) (mTimeLeftInMillisStudioSalary3 /1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillisStudioSalary3 /1000) %3600)/ 60;
        int seconds = (int) (mTimeLeftInMillisStudioSalary3 /1000) % 60;

        String timeLeftFormattedStudioSalary3 = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours, minutes, seconds);

        mTextViewCountDownStudioSalary3.setText(timeLeftFormattedStudioSalary3);
    }

    private void updateButtonsStudioSalary3(){
        if(mTimeRunningStudioSalary3) {
            start_salary_timer_button3.setVisibility(View.INVISIBLE);
        }
        else {
            start_salary_timer_button3.setVisibility(View.VISIBLE);

            if(mTimeLeftInMillisStudioSalary3 < 1000) {
                start_salary_timer_button3.setVisibility(View.INVISIBLE);
            }
            else {
                start_salary_timer_button3.setVisibility(View.VISIBLE);
            }

            if(mTimeLeftInMillisStudioSalary3 < START_TIME_IN_MILLIS) {
                collect_benefits_pay_salary_button3.setVisibility(View.VISIBLE);
            }
            else {
                collect_benefits_pay_salary_button3.setVisibility(View.INVISIBLE);
            }
        }
    }

    //END TIMER 3

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefsStudioS = getSharedPreferences("prefsStudioS", MODE_PRIVATE);
        SharedPreferences.Editor editorStudioS = prefsStudioS.edit();

        editorStudioS.putBoolean("timerS1",mTimeRunningStudioSalary1);
        editorStudioS.putBoolean("earningsCollected1",earningsCollected1);

        editorStudioS.putLong("millisLeftStudioS1",mTimeLeftInMillisStudioSalary1);
        editorStudioS.putLong("endTimeStudioS1",mEndTimeStudioSalary1);

        editorStudioS.putBoolean("timerS2",mTimeRunningStudioSalary2);
        editorStudioS.putBoolean("earningsCollected2",earningsCollected2);

        editorStudioS.putLong("millisLeftStudioS2",mTimeLeftInMillisStudioSalary2);
        editorStudioS.putLong("endTimeStudioS2",mEndTimeStudioSalary2);

        editorStudioS.putBoolean("timerS3",mTimeRunningStudioSalary3);
        editorStudioS.putBoolean("earningsCollected3",earningsCollected3);

        editorStudioS.putLong("millisLeftStudioS3",mTimeLeftInMillisStudioSalary3);
        editorStudioS.putLong("endTimeStudioS3",mEndTimeStudioSalary3);

        editorStudioS.apply();

        if(mCountDownTimerStudioSalary1!=null){
            mCountDownTimerStudioSalary1.cancel();
        }

        if(mCountDownTimerStudioSalary2!=null){
            mCountDownTimerStudioSalary2.cancel();
        }

        if(mCountDownTimerStudioSalary3!=null){
            mCountDownTimerStudioSalary3.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefsStudioS = getSharedPreferences("prefsStudioS", MODE_PRIVATE);

        mTimeRunningStudioSalary1 = prefsStudioS.getBoolean("timerS1",false);
        earningsCollected1 = prefsStudioS.getBoolean("earningsCollected1",false);

        mTimeLeftInMillisStudioSalary1 = prefsStudioS.getLong("millisLeftStudioS1", START_TIME_IN_MILLIS);

        updateCountDownTextStudioSalary1();
        updateButtonsStudioSalary1();

        if (mTimeRunningStudioSalary1) {

            mEndTimeStudioSalary1 = prefsStudioS.getLong("endTimeStudioS1",0);
            mTimeLeftInMillisStudioSalary1 = mEndTimeStudioSalary1 - System.currentTimeMillis();
            if(mTimeLeftInMillisStudioSalary1 <0){
                mTimeLeftInMillisStudioSalary1=0;
                mTimeRunningStudioSalary1 = false;

                updateCountDownTextStudioSalary1();
                updateButtonsStudioSalary1();
            }
            else
            {
                startTimerStudioSalary1();
            }
        }

        mTimeRunningStudioSalary2 = prefsStudioS.getBoolean("timerS2",false);
        earningsCollected2 = prefsStudioS.getBoolean("earningsCollected2",false);

        mTimeLeftInMillisStudioSalary2 = prefsStudioS.getLong("millisLeftStudioS2", START_TIME_IN_MILLIS);

        updateCountDownTextStudioSalary2();
        updateButtonsStudioSalary2();

        if (mTimeRunningStudioSalary2) {

            mEndTimeStudioSalary2 = prefsStudioS.getLong("endTimeStudioS2",0);
            mTimeLeftInMillisStudioSalary2 = mEndTimeStudioSalary2 - System.currentTimeMillis();
            if(mTimeLeftInMillisStudioSalary2 <0){
                mTimeLeftInMillisStudioSalary2=0;
                mTimeRunningStudioSalary2 = false;

                updateCountDownTextStudioSalary2();
                updateButtonsStudioSalary2();
            }
            else
            {
                startTimerStudioSalary2();
            }
        }

        mTimeRunningStudioSalary3 = prefsStudioS.getBoolean("timerS3",false);
        earningsCollected3 = prefsStudioS.getBoolean("earningsCollected3",false);

        mTimeLeftInMillisStudioSalary3 = prefsStudioS.getLong("millisLeftStudioS3", START_TIME_IN_MILLIS);

        updateCountDownTextStudioSalary3();
        updateButtonsStudioSalary3();

        if (mTimeRunningStudioSalary3) {

            mEndTimeStudioSalary3 = prefsStudioS.getLong("endTimeStudioS3",0);
            mTimeLeftInMillisStudioSalary3 = mEndTimeStudioSalary3 - System.currentTimeMillis();
            if(mTimeLeftInMillisStudioSalary3 <0){
                mTimeLeftInMillisStudioSalary3=0;
                mTimeRunningStudioSalary3 = false;

                updateCountDownTextStudioSalary3();
                updateButtonsStudioSalary3();
            }
            else
            {
                startTimerStudioSalary3();
            }
        }
    }

    //Updating data to firebase
    private void updateDataToFirebase() {

        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null) {
            Map<String, Object> childUpdates = new HashMap<>();

            childUpdates.put("money", User.getMoney());

            FirebaseDatabase database = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
            rootRef = database.getReference("Users");
            rootRef.child(user.getUid()).child("UserGameInfo").updateChildren(childUpdates);
        }
    }

    private void readFromDatabase(FirebaseUser currentUser, DatabaseReference userRef, DatabaseReference lvlRef, DatabaseReference empRef) {
        if (currentUser != null) {
            // Read from "Users" branch in db
            userRef.addValueEventListener(new ValueEventListener() {
                Double money, level, resources, experience;
                String moneyString, resourcesString, experienceString;
                Boolean isEmployee1Hired, isEmployee2Hired, isEmployee3Hired;

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

                            isEmployee1Hired = keyId.child("UserEmployeesInfo").child("employee1Hired").getValue(Boolean.class);
                            isEmployee2Hired = keyId.child("UserEmployeesInfo").child("employee2Hired").getValue(Boolean.class);
                            isEmployee3Hired = keyId.child("UserEmployeesInfo").child("employee3Hired").getValue(Boolean.class);

                            break;
                        }
                    }

                    // Read from "Employees" branch in db
                    empRef.addValueEventListener(new ValueEventListener() {
                        //List<Double> addExpList = new ArrayList<>();
                        List<Double> addMonList = new ArrayList<>();
                        List<Double> addSalaryList = new ArrayList<>();

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot keyId : dataSnapshot.getChildren()) {
                                //addExpList.add(keyId.child("additional_exp").getValue(Double.class));
                                addMonList.add(keyId.child("additional_money").getValue(Double.class));
                                addSalaryList.add(keyId.child("salary").getValue(Double.class));
                            }

                            //emp1.setAdditional_exp1(addExpList.get(0));
                            emp1.setAdditional_money1(addMonList.get(0));
                            emp1.setSalary(addSalaryList.get(0));

                            //emp2.setAdditional_exp1(addExpList.get(1));
                            emp2.setAdditional_money1(addMonList.get(1));
                            emp2.setSalary(addSalaryList.get(1));

                            //emp3.setAdditional_exp1(addExpList.get(2));
                            emp3.setAdditional_money1(addMonList.get(2));
                            emp3.setSalary(addSalaryList.get(2));
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    }); // End of reading from "Employees" branch

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

                    userEmployeesInfo.setEmployee1Hired(isEmployee1Hired);
                    userEmployeesInfo.setEmployee2Hired(isEmployee2Hired);
                    userEmployeesInfo.setEmployee3Hired(isEmployee3Hired);

                    User.setMoney(money);
                    User.setExperience(experience);
                    User.setResources(resources);
                    User.setLevel(level);

                    checkIfEmployeeIsHired();


                } // End of reading from "Users" branch

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
    }

    private void checkIfEmployeeIsHired() {
        if (User.getLevel() >= 3) {

            if (userEmployeesInfo.getEmployee1Hired()) {
                start_salary_timer_button1.setEnabled(true);
                collect_benefits_pay_salary_button1.setEnabled(true);
            } else {
                        start_salary_timer_button1.setEnabled(false);
                collect_benefits_pay_salary_button1.setEnabled(false);
            }
        }
        if(User.getLevel()>=5){

            if(userEmployeesInfo.getEmployee2Hired())
            {
                start_salary_timer_button2.setEnabled(true);
                collect_benefits_pay_salary_button2.setEnabled(true);
            }
            else {
                start_salary_timer_button2.setEnabled(false);
                collect_benefits_pay_salary_button2.setEnabled(false);
            }
        }
        if(User.getLevel()==10){
            if(userEmployeesInfo.getEmployee3Hired())
            {
                start_salary_timer_button3.setEnabled(true);
                collect_benefits_pay_salary_button3.setEnabled(true);
            }
            else
            {
                start_salary_timer_button3.setEnabled(false);
                collect_benefits_pay_salary_button3.setEnabled(false);
            }
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