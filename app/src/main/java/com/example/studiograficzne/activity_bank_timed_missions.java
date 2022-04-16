package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class activity_bank_timed_missions extends AppCompatActivity {

    private Button time_button1;
    private Button time_button2;
    private Button time_button3;

    private Button start_timed_mission_button;
    private Button collect_timed_rewards_button;

    private View layout1;
    private View layout2;
    private View layout3;
    private View layout_m;

    //TIMER

    private TextView mTextViewCountDown;
    CountDownTimer mCountDownTimer;

    private long mStartTimeInMillis;
    private boolean mTimeRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;

//    private final String TAG = this.getClass().getName().toUpperCase();

    //DATABASE
//    FirebaseDatabase firebaseDatabase;
//    DatabaseReference databaseReference;
//    Missions Mission1 = new Missions();
//    FirebaseAuth mAuth;
//    UserGameInfo User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_timed_missions);

        time_button1 = findViewById(R.id.button_1);
        time_button2 = findViewById(R.id.button_2);
        time_button3 = findViewById(R.id.button_3);

        start_timed_mission_button = findViewById(R.id.start_timed_mission);
        collect_timed_rewards_button = findViewById(R.id.collect_timed_rewards);

        layout1 = findViewById(R.id.mission1_layout);
        layout2 = findViewById(R.id.mission2_layout);
        layout3 = findViewById(R.id.mission3_layout);

        layout_m = findViewById(R.id.timed_mission_layout);

        mTextViewCountDown = findViewById(R.id.text_view_countdown_timed);

        time_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time1 = 60000;
                setTime(time1);
                updateLayoutINVIS();
            }
        });

        time_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time2 = 120000;
                setTime(time2);
                updateLayoutINVIS();
            }
        });

        time_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time3 = 180000;
                setTime(time3);
                updateLayoutINVIS();
            }
        });

        start_timed_mission_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTimeRunning){

                }
                else
                {
                    startTimer();
                }
            }
        });

        collect_timed_rewards_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
                updateLayoutVIS();
            }
        });
    }

    private  void setTime(long milliseconds)
    {
        mStartTimeInMillis = milliseconds;
        resetTimer();
    }

    //Mission timer
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

    //Saving timer for running in background
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimeRunning);
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

        mStartTimeInMillis=prefs.getLong("startTimeInMillis", 0);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimeRunning = prefs.getBoolean("timerRunning", false);

        updateLayout();
        updateCountDownText();
        updateButtons();

        if(mTimeRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            updateLayout();

            if(mTimeLeftInMillis < 0) {
                mTimeLeftInMillis =0;
                mTimeRunning = false;
                updateLayout();
                updateCountDownText();
                updateButtons();
            }
            else{
                startTimer();
            }
        }
    }
    //End of saving timer information

    //Layout update
    private void updateLayout() {

        if(mTimeLeftInMillis >=0)
        {
            updateLayoutINVIS();
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
}