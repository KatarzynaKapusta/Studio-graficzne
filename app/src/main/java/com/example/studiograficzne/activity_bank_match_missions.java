package com.example.studiograficzne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class activity_bank_match_missions extends AppCompatActivity {

    private final String TAG = this.getClass().getName().toUpperCase();

    private Button bm11;
    private Button bm12;
    private Button bm13;

    private Button bm14;
    private Button bm15;
    private Button bm16;

    private Button bm17;
    private Button bm18;
    private Button bm19;

    private Button start_matched_mission_button;
    private Button back_to_matched_missions_button;
    private Button collect_match_rewards_button;

    private Button match_button1;
    private Button match_button2;
    private Button match_button3;

    private View layout1;
    private View layout2;
    private View layout3;
    private View layout_m;

    private ImageView Img1Mission1;
    private ImageView Img2Mission1;
    private ImageView Img3Mission1;

    private boolean MissionChosen1 = false;
    private boolean MissionChosen2 = false;
    private boolean MissionChosen3 = false;

    private boolean rewardsCollectedMatch = false;
    private boolean missionMatchStarted = false;

    private long money;
    private long exp;

    private int levelCounter =0;
    //DATABASE
//    FirebaseDatabase firebaseDatabase;
//    DatabaseReference databaseReference;
//    FirebaseAuth mAuth;
    UserGameInfo User;
    int matchedCounter =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_match_missions);

        //Layout
        layout1 = findViewById(R.id.mission1_layout_m);
        layout2 = findViewById(R.id.mission2_layout_m);
        layout3 = findViewById(R.id.mission3_layout_m);

        layout_m = findViewById(R.id.timed_mission_layout);

        //Choice buttons
        match_button1 = findViewById(R.id.mbutton_1);
        match_button2 = findViewById(R.id.mbutton_2);
        match_button3 = findViewById(R.id.mbutton_3);

        //Start mission buttons
        start_matched_mission_button = findViewById(R.id.start_match_mission);
        back_to_matched_missions_button = findViewById(R.id.back_to_match_missions_choice);
        collect_match_rewards_button = findViewById(R.id.collect_match_rewards);

        //Mission 1 - match buttons
        bm11 = findViewById(R.id.no_match_m11);
        bm12 = findViewById(R.id.no_match_m12);
        bm13 = findViewById(R.id.match_m11);

        bm14 = findViewById(R.id.no_match_m13);
        bm15 = findViewById(R.id.no_match_m14);
        bm16 = findViewById(R.id.match_m12);

        bm17 = findViewById(R.id.no_match_m15);
        bm18 = findViewById(R.id.no_match_m16);
        bm19 = findViewById(R.id.match_m13);

        //Mission 1 - images
        Img1Mission1 = findViewById(R.id.img1mission1);
        Img2Mission1 = findViewById(R.id.img2mission1);
        Img3Mission1 = findViewById(R.id.img3mission1);

        BankMatchMission1 m1 = new BankMatchMission1();

        User = new UserGameInfo();
        //Database
//        mAuth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
//        databaseReference = firebaseDatabase.getReference("Users");
//        Log.v("USERID", databaseReference.getKey());
//
//        Intent intent = getIntent();
//        String email = intent.getStringExtra("email");
//
//
//
//        //Reading database
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            Double money, experience;
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot keyId: dataSnapshot.getChildren()) {
//                    if (keyId.child("UserInfo").child("email").getValue().equals(email))
//                    {
//                        experience = keyId.child("UserGameInfo").child("experience").getValue(Double.class);
//                        money = keyId.child("UserGameInfo").child("money").getValue(Double.class);
//
//                        break;
//                    }
//                }
//                User.setExperience(experience);
//                User.setMoney(money);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
        //End of reading database

        //Mission Choice
        match_button1.setOnClickListener(view -> {
            MissionChosen1 = true;
            updateLayoutINVIS();
        });

        match_button2.setOnClickListener(view -> {
            MissionChosen2 =true;
            updateLayoutINVIS()
            ;
        });

        match_button3.setOnClickListener(view -> {
            MissionChosen3 = true;
            updateLayoutINVIS();
        });
        //

        start_matched_mission_button.setOnClickListener(view -> {
            missionMatchStarted = true;
            if(MissionChosen1)
            {
                levelCounter++;
                updateButtonsMission1(levelCounter);
            }
            else if(MissionChosen2)
            {

            }
            else
            {

            }

        });

        collect_match_rewards_button.setOnClickListener(view -> {
            if(MissionChosen1)
            {
                double moneyR = Double.longBitsToDouble(money);
                double expR = Double.longBitsToDouble(exp);
                User.addMatchMissionRewardsBank(moneyR, expR);
                System.out.println(User.getExperience());
                System.out.println(User.getMoney());
            }
            else if(MissionChosen2)
            {

            }
            else
            {

            }
            levelCounter = 0;
            matchedCounter = 0;
            rewardsCollectedMatch = true;
            updateButtonsStart();
            updateLayoutVIS();
        });

        back_to_matched_missions_button.setOnClickListener(view -> {
            MissionChosen1 = false;
            MissionChosen2 = false;
            MissionChosen3=false;
            missionMatchStarted = false;
            updateLayoutVIS();
        });

        //Mission 1 - Level 1
        bm11.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission1(levelCounter);
        });

        bm12.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission1(levelCounter);
        });

        bm13.setOnClickListener(view -> {
            levelCounter++;
            matchedCounter++;
            updateButtonsMission1(levelCounter);
        });

        //Mission 1 - level 2
        bm14.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission1(levelCounter);
        });

        bm15.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission1(levelCounter);
        });

        bm16.setOnClickListener(view -> {
            levelCounter++;
            matchedCounter++;
            updateButtonsMission1(levelCounter);
        });

        //Mission 1 - Level 3
        bm17.setOnClickListener(view -> {
            levelCounter++;
            m1.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m1.getBm_money()) ;
            exp = Double.doubleToLongBits(m1.getBm_exp());
            updateButtonsMission1(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm18.setOnClickListener(view -> {
            levelCounter++;
            m1.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m1.getBm_money()) ;
            exp = Double.doubleToLongBits(m1.getBm_exp());
            updateButtonsMission1(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm19.setOnClickListener(view -> {
            levelCounter++;
            matchedCounter++;
            m1.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m1.getBm_money());
            exp = Double.doubleToLongBits(m1.getBm_exp());
            updateButtonsMission1(levelCounter);
            rewardsCollectedMatch = false;

        });

    }

    private void updateLayoutMatch(){
        if(rewardsCollectedMatch)
        {
            updateLayoutVIS();
        }
        else
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

    private void updateButtonsStart(){
        if(rewardsCollectedMatch)
        {
            back_to_matched_missions_button.setVisibility(View.VISIBLE);
            start_matched_mission_button.setVisibility(View.VISIBLE);
            collect_match_rewards_button.setVisibility(View.INVISIBLE);
        }
        else
        {
            back_to_matched_missions_button.setVisibility(View.INVISIBLE);
            start_matched_mission_button.setVisibility(View.INVISIBLE);
            collect_match_rewards_button.setVisibility(View.VISIBLE);
        }
    }

    private void updateButtonsMission1(int level){
        if(levelCounter==1)
        {
            back_to_matched_missions_button.setVisibility(View.INVISIBLE);
            start_matched_mission_button.setVisibility(View.INVISIBLE);

            Img1Mission1.setVisibility(View.VISIBLE);
            bm11.setVisibility(View.VISIBLE);
            bm12.setVisibility(View.VISIBLE);
            bm13.setVisibility(View.VISIBLE);
        }
        else if(levelCounter==2)
        {
            Img1Mission1.setVisibility(View.INVISIBLE);
            bm11.setVisibility(View.INVISIBLE);
            bm12.setVisibility(View.INVISIBLE);
            bm13.setVisibility(View.INVISIBLE);

            Img2Mission1.setVisibility(View.VISIBLE);
            bm14.setVisibility(View.VISIBLE);
            bm15.setVisibility(View.VISIBLE);
            bm16.setVisibility(View.VISIBLE);
        }
        else if (levelCounter==3)
        {
            Img2Mission1.setVisibility(View.INVISIBLE);
            bm14.setVisibility(View.INVISIBLE);
            bm15.setVisibility(View.INVISIBLE);
            bm16.setVisibility(View.INVISIBLE);

            Img3Mission1.setVisibility(View.VISIBLE);
            bm17.setVisibility(View.VISIBLE);
            bm18.setVisibility(View.VISIBLE);
            bm19.setVisibility(View.VISIBLE);
        }
        else
        {
            Img3Mission1.setVisibility(View.INVISIBLE);
            bm17.setVisibility(View.INVISIBLE);
            bm18.setVisibility(View.INVISIBLE);
            bm19.setVisibility(View.INVISIBLE);

            collect_match_rewards_button.setVisibility(View.VISIBLE);
        }
    }

    //
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefsBankMatch = getSharedPreferences("prefsBankMatch", MODE_PRIVATE);
        SharedPreferences.Editor editorBankMatch = prefsBankMatch.edit();

        editorBankMatch.putLong("moneyRewards", money);
        editorBankMatch.putLong("expRewards", exp);
        editorBankMatch.putBoolean("rewardsCollected", rewardsCollectedMatch);
        editorBankMatch.putBoolean("MissionChosen1", MissionChosen1);
        editorBankMatch.putBoolean("MissionChosen2", MissionChosen2);
        editorBankMatch.putBoolean("MissionChosen3", MissionChosen3);
        //editorBankMatch.putBoolean("MissionStarted", missionMatchStarted);

        editorBankMatch.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefsBankMatch = getSharedPreferences("prefsBankMatch", MODE_PRIVATE);

        money = prefsBankMatch.getLong("moneyRewards", 0);
        exp = prefsBankMatch.getLong("expRewards", 0);
        rewardsCollectedMatch = prefsBankMatch.getBoolean("rewardsCollected", false);
        MissionChosen1 = prefsBankMatch.getBoolean("MissionChosen1", false);
        MissionChosen2 = prefsBankMatch.getBoolean("MissionChosen2", false);
        MissionChosen3 = prefsBankMatch.getBoolean("MissionChosen3", false);
        //missionMatchStarted = prefsBankMatch.getBoolean("MissionStarted", false);

        updateLayoutMatch();
        updateButtonsStart();

    }
}