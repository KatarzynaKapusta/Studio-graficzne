package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class activity_bank_timed_missions extends AppCompatActivity {

    //MISSION TIMER
    private static final long START_TIME_IN_MILLIS =60000;

    private TextView mTextViewCountDown1;
    private TextView mTextViewCountDown2;
    private TextView mTextViewCountDown3;

    private Button collect_rewards1;
    private Button collect_rewards2;
    private Button collect_rewards3;


    private Button start_mission1;
    private Button start_mission2;
    private Button start_mission3;

    CountDownTimer mCountDownTimer;
    private boolean mTimeRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_timed_missions);

        mTextViewCountDown1 = findViewById(R.id.text_view_countdown1);
        mTextViewCountDown2 = findViewById(R.id.text_view_countdown2);
        mTextViewCountDown3 = findViewById(R.id.text_view_countdown3);

        start_mission1 = findViewById(R.id.mission1_button);
        start_mission2 = findViewById(R.id.mission2_button);
        start_mission3 = findViewById(R.id.mission3_button);

        collect_rewards1 = findViewById(R.id.collect_rewards1);
        collect_rewards2 = findViewById(R.id.collect_rewards2);
        collect_rewards3 = findViewById(R.id.collect_rewards3);

        start_mission1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_mission2.setVisibility(View.INVISIBLE);
                start_mission3.setVisibility(View.INVISIBLE);
                mTextViewCountDown2.setVisibility(View.INVISIBLE);
                mTextViewCountDown3.setVisibility(View.INVISIBLE);
                if(mTimeRunning){

                }
                else {
                    startTimer();
                }
            }
        });

        collect_rewards1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FirebaseUser user = mAuth.getCurrentUser();

//                if(user!=null)
//                {
//                    User.addMissionRewardsStorage(Mission1.getM_resources(), Mission1.getM_experience());
//                    updateDataToFirebase();
//                }
//                else
//                {
//                    Toast.makeText(activity_magazyn.this, "BŁĄD",
//                            Toast.LENGTH_SHORT).show();
//                }
                start_mission2.setVisibility(View.VISIBLE);
                start_mission3.setVisibility(View.VISIBLE);
                mTextViewCountDown2.setVisibility(View.VISIBLE);
                mTextViewCountDown3.setVisibility(View.VISIBLE);
                resetTimer();
//                System.out.println(User.getExperience());
//                System.out.println(User.getResources());
            }
        });

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
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        updateButtons();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis /1000) / 60;
        int seconds = (int) (mTimeLeftInMillis /1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        mTextViewCountDown1.setText(timeLeftFormatted);
    }

    private void updateButtons(){
        if(mTimeRunning) {
            start_mission1.setVisibility(View.INVISIBLE);
        }
        else {
            start_mission1.setVisibility(View.VISIBLE);

            if(mTimeLeftInMillis < 1000) {
                start_mission1.setVisibility(View.INVISIBLE);
            }
            else {
                start_mission1.setVisibility(View.VISIBLE);
            }

            if(mTimeLeftInMillis < START_TIME_IN_MILLIS) {
                collect_rewards1.setVisibility(View.VISIBLE);
            }
            else {
                collect_rewards1.setVisibility(View.INVISIBLE);
            }
        }
    }

    //Saving timer for running in background
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs1 = getSharedPreferences("prefs1", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = prefs1.edit();

        editor1.putLong("millisLeft", mTimeLeftInMillis);
        editor1.putBoolean("timerRunning", mTimeRunning);
        editor1.putLong("endTime", mEndTime);

        editor1.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs1 = getSharedPreferences("prefs1", MODE_PRIVATE);

        mTimeLeftInMillis = prefs1.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimeRunning = prefs1.getBoolean("timerRunning", false);

        updateCountDownText();
        updateButtons();

        if(mTimeRunning) {
            mEndTime = prefs1.getLong("endTime", 0);
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
}