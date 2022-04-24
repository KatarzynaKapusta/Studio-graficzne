package com.example.studiograficzne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class activity_bank_match_missions extends AppCompatActivity {

    private final String TAG = this.getClass().getName().toUpperCase();

    //Mission 1 - buttons
    private Button bm11, bm12, bm13, bm14, bm15, bm16, bm17, bm18, bm19;

    //Mission 2 - buttons
    private Button bm211, bm212, bm213,bm214,bm215,bm216;
    private Button bm221, bm222, bm223,bm224,bm225,bm226;
    private Button bm231, bm232, bm233,bm234,bm235,bm236;

    //Mission 3 - buttons
    private Button bm311, bm312,bm313, bm314,bm315,bm316, bm317, bm318, bm319;
    private Button bm321, bm322,bm323, bm324,bm325,bm326, bm327, bm328, bm329;
    private Button bm331, bm332,bm333, bm334,bm335,bm336, bm337, bm338, bm339;

    //Start Mission Buttons
    private Button start_matched_mission_button;
    private Button back_to_matched_missions_button;
    private Button collect_match_rewards_button;

    //Mission choice buttons
    private Button match_button1, match_button2, match_button3;

    private View layout1, layout2, layout3, layout_m;

    //Mission 1 - images
    private ImageView Img1Mission1, Img2Mission1, Img3Mission1;

    //Mission 2 - images
    private ImageView Img1Mission2, Img2Mission2, Img3Mission2;

    //Mission 3 - images
    private ImageView Img1Mission3, Img2Mission3, Img3Mission3;

    private boolean MissionChosen1 = false;
    private boolean MissionChosen2 = false;
    private boolean MissionChosen3 = false;

    private boolean rewardsCollectedMatch = false;

    private long money, exp;

    private int levelCounter =0, matchedCounter =0;
    //DATABASE
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    UserGameInfo User;

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

        //Mission 2 - match buttons
        bm211 = findViewById(R.id.match_m211);
        bm212 = findViewById(R.id.no_match_m212);
        bm213 = findViewById(R.id.no_match_m213);
        bm214 = findViewById(R.id.no_match_m214);
        bm215 = findViewById(R.id.no_match_m215);
        bm216 = findViewById(R.id.no_match_m216);

        bm221 = findViewById(R.id.match_m221);
        bm222 = findViewById(R.id.no_match_m222);
        bm223 = findViewById(R.id.no_match_m223);
        bm224 = findViewById(R.id.no_match_m224);
        bm225 = findViewById(R.id.no_match_m225);
        bm226 = findViewById(R.id.no_match_m226);

        bm231 = findViewById(R.id.match_m231);
        bm232 = findViewById(R.id.no_match_m232);
        bm233 = findViewById(R.id.no_match_m233);
        bm234 = findViewById(R.id.no_match_m234);
        bm235 = findViewById(R.id.no_match_m235);
        bm236 = findViewById(R.id.no_match_m236);

        //Mission 2 - images
        Img1Mission2 = findViewById(R.id.img3mission2);
        Img2Mission2 = findViewById(R.id.img2mission2);
        Img3Mission2 = findViewById(R.id.img1mission2);

        //Mission 3 - match buttons
        bm311 = findViewById(R.id.match_m311);
        bm312 = findViewById(R.id.no_match_m312);
        bm313 = findViewById(R.id.no_match_m313);
        bm314 = findViewById(R.id.no_match_m314);
        bm315 = findViewById(R.id.no_match_m315);
        bm316 = findViewById(R.id.no_match_m316);
        bm317 = findViewById(R.id.no_match_m317);
        bm318 = findViewById(R.id.no_match_m318);
        bm319 = findViewById(R.id.no_match_m319);

        bm321 = findViewById(R.id.match_m321);
        bm322 = findViewById(R.id.no_match_m322);
        bm323 = findViewById(R.id.no_match_m323);
        bm324 = findViewById(R.id.no_match_m324);
        bm325 = findViewById(R.id.no_match_m325);
        bm326 = findViewById(R.id.no_match_m326);
        bm327 = findViewById(R.id.no_match_m327);
        bm328 = findViewById(R.id.no_match_m328);
        bm329 = findViewById(R.id.no_match_m329);

        bm331 = findViewById(R.id.match_m331);
        bm332 = findViewById(R.id.no_match_m332);
        bm333 = findViewById(R.id.no_match_m333);
        bm334 = findViewById(R.id.no_match_m334);
        bm335 = findViewById(R.id.no_match_m335);
        bm336 = findViewById(R.id.no_match_m336);
        bm337 = findViewById(R.id.no_match_m337);
        bm338 = findViewById(R.id.no_match_m338);
        bm339 = findViewById(R.id.no_match_m339);

        //Mission 3 - images
        Img1Mission3 = findViewById(R.id.img1mission3);
        Img2Mission3 = findViewById(R.id.img2mission3);
        Img3Mission3 = findViewById(R.id.img3mission3);

        BankMatchMission1 m1 = new BankMatchMission1();
        BankMatchMission2 m2 = new BankMatchMission2();
        BankMatchMission3 m3 = new BankMatchMission3();


        User = new UserGameInfo();
        //Database
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Users");
        Log.v("USERID", databaseReference.getKey());

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");



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

        //Mission Choice
        match_button1.setOnClickListener(view -> {
            MissionChosen1 = true;
            MissionChosen2 = false;
            MissionChosen3 = false;
            updateLayoutINVIS();
        });

        match_button2.setOnClickListener(view -> {
            MissionChosen2 =true;
            MissionChosen1 = false;
            MissionChosen3 = false;
            updateLayoutINVIS()
            ;
        });

        match_button3.setOnClickListener(view -> {
            MissionChosen3 = true;
            MissionChosen2 = false;
            MissionChosen1 = false;
            updateLayoutINVIS();
        });
        //

        start_matched_mission_button.setOnClickListener(view -> {
            if(MissionChosen1)
            {
                levelCounter++;
                updateButtonsMission1(levelCounter);
            }
            else if(MissionChosen2)
            {
                levelCounter++;
                updateButtonsMission2(levelCounter);
            }
            else
            {
                levelCounter++;
                updateButtonsMission3(levelCounter);
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
                double moneyR=Double.longBitsToDouble(money);
                double expR=Double.longBitsToDouble(exp);
                User.addMatchMissionRewardsBank(moneyR, expR);
                System.out.println(User.getExperience());
                System.out.println(User.getMoney());
            }
            else
            {
                double moneyR=Double.longBitsToDouble(money);
                double expR=Double.longBitsToDouble(exp);
                User.addMatchMissionRewardsBank(moneyR, expR);
                System.out.println(User.getExperience());
                System.out.println(User.getMoney());
                updateDataToFirebase();
            }
            updateDataToFirebase();

            Toast.makeText(activity_bank_match_missions.this, "Przyznano nagrody",
                    Toast.LENGTH_SHORT).show();
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
            updateLayoutVIS();
        });

        //////////////////////////////////////////////////////
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

        ////////////////////////////////////////
        //Mission 2 - level 1
        bm211.setOnClickListener(view -> {
            levelCounter++;
            matchedCounter++;
            updateButtonsMission2(levelCounter);

        });

        bm212.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission2(levelCounter);
        });

        bm213.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission2(levelCounter);
        });

        bm214.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission2(levelCounter);
        });

        bm215.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission2(levelCounter);
        });

        bm216.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission2(levelCounter);
        });

        //Mission 2 - level 2
        bm221.setOnClickListener(view -> {
            levelCounter++;
            matchedCounter++;
            updateButtonsMission2(levelCounter);
        });

        bm222.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission2(levelCounter);
        });

        bm223.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission2(levelCounter);
        });

        bm224.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission2(levelCounter);
        });

        bm225.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission2(levelCounter);
        });

        bm226.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission2(levelCounter);
        });

        //Mission 2 - level 3
        bm231.setOnClickListener(view -> {
            levelCounter++;
            matchedCounter++;
            m2.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m2.getBm_money()) ;
            exp = Double.doubleToLongBits(m2.getBm_exp());
            updateButtonsMission2(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm232.setOnClickListener(view -> {
            levelCounter++;
            m2.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m2.getBm_money()) ;
            exp = Double.doubleToLongBits(m2.getBm_exp());
            updateButtonsMission2(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm233.setOnClickListener(view -> {
            levelCounter++;
            m2.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m2.getBm_money()) ;
            exp = Double.doubleToLongBits(m2.getBm_exp());
            updateButtonsMission2(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm234.setOnClickListener(view -> {
            levelCounter++;
            m2.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m2.getBm_money()) ;
            exp = Double.doubleToLongBits(m2.getBm_exp());
            updateButtonsMission2(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm235.setOnClickListener(view -> {
            levelCounter++;
            m2.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m2.getBm_money()) ;
            exp = Double.doubleToLongBits(m2.getBm_exp());
            updateButtonsMission2(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm236.setOnClickListener(view -> {
            levelCounter++;
            m2.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m2.getBm_money()) ;
            exp = Double.doubleToLongBits(m2.getBm_exp());
            updateButtonsMission2(levelCounter);
            rewardsCollectedMatch = false;
        });

        ///////////////////
        //Mission  3 - level 1

        bm311.setOnClickListener(view -> {
            levelCounter++;
            matchedCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm312.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm313.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm314.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm315.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm316.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm317.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm318.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm319.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        //Mission 3 - level 2
        bm321.setOnClickListener(view -> {
            levelCounter++;
            matchedCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm322.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm323.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm324.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm325.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm326.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm327.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm328.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        bm329.setOnClickListener(view -> {
            levelCounter++;
            updateButtonsMission3(levelCounter);
        });

        //Mission  3 - level 3

        bm331.setOnClickListener(view -> {
            levelCounter++;
            matchedCounter++;
            m3.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m3.getBm_money()) ;
            exp = Double.doubleToLongBits(m3.getBm_exp());
            updateButtonsMission3(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm332.setOnClickListener(view -> {
            levelCounter++;
            m3.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m3.getBm_money()) ;
            exp = Double.doubleToLongBits(m3.getBm_exp());
            updateButtonsMission3(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm333.setOnClickListener(view -> {
            levelCounter++;
            m3.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m3.getBm_money()) ;
            exp = Double.doubleToLongBits(m3.getBm_exp());
            updateButtonsMission3(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm334.setOnClickListener(view -> {
            levelCounter++;
            m3.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m3.getBm_money()) ;
            exp = Double.doubleToLongBits(m3.getBm_exp());
            updateButtonsMission3(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm335.setOnClickListener(view -> {
            levelCounter++;
            m3.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m3.getBm_money()) ;
            exp = Double.doubleToLongBits(m3.getBm_exp());
            updateButtonsMission3(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm336.setOnClickListener(view -> {
            levelCounter++;
            m3.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m3.getBm_money()) ;
            exp = Double.doubleToLongBits(m3.getBm_exp());
            updateButtonsMission3(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm337.setOnClickListener(view -> {
            levelCounter++;
            m3.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m3.getBm_money()) ;
            exp = Double.doubleToLongBits(m3.getBm_exp());
            updateButtonsMission3(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm338.setOnClickListener(view -> {
            levelCounter++;
            m3.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m3.getBm_money()) ;
            exp = Double.doubleToLongBits(m3.getBm_exp());
            updateButtonsMission3(levelCounter);
            rewardsCollectedMatch = false;
        });

        bm339.setOnClickListener(view -> {
            levelCounter++;
            m3.HowManyMatched(matchedCounter);
            money = Double.doubleToLongBits(m3.getBm_money()) ;
            exp = Double.doubleToLongBits(m3.getBm_exp());
            updateButtonsMission3(levelCounter);
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
        if(level==1)
        {
            back_to_matched_missions_button.setVisibility(View.INVISIBLE);
            start_matched_mission_button.setVisibility(View.INVISIBLE);

            Img1Mission1.setVisibility(View.VISIBLE);
            bm11.setVisibility(View.VISIBLE);
            bm12.setVisibility(View.VISIBLE);
            bm13.setVisibility(View.VISIBLE);
        }
        else if(level==2)
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
        else if (level==3)
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

    private void updateButtonsMission2(int level) {
        if(level==1) {
            back_to_matched_missions_button.setVisibility(View.INVISIBLE);
            start_matched_mission_button.setVisibility(View.INVISIBLE);

            Img1Mission2.setVisibility(View.VISIBLE);
            bm211.setVisibility(View.VISIBLE);
            bm212.setVisibility(View.VISIBLE);
            bm213.setVisibility(View.VISIBLE);
            bm214.setVisibility(View.VISIBLE);
            bm215.setVisibility(View.VISIBLE);
            bm216.setVisibility(View.VISIBLE);
        }
        else if (level==2) {
            Img1Mission2.setVisibility(View.INVISIBLE);
            bm211.setVisibility(View.INVISIBLE);
            bm212.setVisibility(View.INVISIBLE);
            bm213.setVisibility(View.INVISIBLE);
            bm214.setVisibility(View.INVISIBLE);
            bm215.setVisibility(View.INVISIBLE);
            bm216.setVisibility(View.INVISIBLE);

            Img2Mission2.setVisibility(View.VISIBLE);
            bm221.setVisibility(View.VISIBLE);
            bm222.setVisibility(View.VISIBLE);
            bm223.setVisibility(View.VISIBLE);
            bm224.setVisibility(View.VISIBLE);
            bm225.setVisibility(View.VISIBLE);
            bm226.setVisibility(View.VISIBLE);
        }
        else if(level==3) {
            Img2Mission2.setVisibility(View.INVISIBLE);
            bm221.setVisibility(View.INVISIBLE);
            bm222.setVisibility(View.INVISIBLE);
            bm223.setVisibility(View.INVISIBLE);
            bm224.setVisibility(View.INVISIBLE);
            bm225.setVisibility(View.INVISIBLE);
            bm226.setVisibility(View.INVISIBLE);

            Img3Mission2.setVisibility(View.VISIBLE);
            bm231.setVisibility(View.VISIBLE);
            bm232.setVisibility(View.VISIBLE);
            bm233.setVisibility(View.VISIBLE);
            bm234.setVisibility(View.VISIBLE);
            bm235.setVisibility(View.VISIBLE);
            bm236.setVisibility(View.VISIBLE);
        }
        else {
            Img3Mission2.setVisibility(View.INVISIBLE);
            bm231.setVisibility(View.INVISIBLE);
            bm232.setVisibility(View.INVISIBLE);
            bm233.setVisibility(View.INVISIBLE);
            bm234.setVisibility(View.INVISIBLE);
            bm235.setVisibility(View.INVISIBLE);
            bm236.setVisibility(View.INVISIBLE);

            collect_match_rewards_button.setVisibility(View.VISIBLE);
        }
    }

    private void updateButtonsMission3(int level)
    {
        if(level==1) {
            back_to_matched_missions_button.setVisibility(View.INVISIBLE);
            start_matched_mission_button.setVisibility(View.INVISIBLE);

            Img1Mission3.setVisibility(View.VISIBLE);
            bm311.setVisibility(View.VISIBLE);
            bm312.setVisibility(View.VISIBLE);
            bm313.setVisibility(View.VISIBLE);
            bm314.setVisibility(View.VISIBLE);
            bm315.setVisibility(View.VISIBLE);
            bm316.setVisibility(View.VISIBLE);
            bm317.setVisibility(View.VISIBLE);
            bm318.setVisibility(View.VISIBLE);
            bm319.setVisibility(View.VISIBLE);
        }
        else if (level==2) {
            Img1Mission3.setVisibility(View.INVISIBLE);
            bm311.setVisibility(View.INVISIBLE);
            bm312.setVisibility(View.INVISIBLE);
            bm313.setVisibility(View.INVISIBLE);
            bm314.setVisibility(View.INVISIBLE);
            bm315.setVisibility(View.INVISIBLE);
            bm316.setVisibility(View.INVISIBLE);
            bm317.setVisibility(View.INVISIBLE);
            bm318.setVisibility(View.INVISIBLE);
            bm319.setVisibility(View.INVISIBLE);

            Img2Mission3.setVisibility(View.VISIBLE);
            bm321.setVisibility(View.VISIBLE);
            bm322.setVisibility(View.VISIBLE);
            bm323.setVisibility(View.VISIBLE);
            bm324.setVisibility(View.VISIBLE);
            bm325.setVisibility(View.VISIBLE);
            bm326.setVisibility(View.VISIBLE);
            bm327.setVisibility(View.VISIBLE);
            bm328.setVisibility(View.VISIBLE);
            bm329.setVisibility(View.VISIBLE);
        }
        else if(level==3) {
            Img2Mission3.setVisibility(View.INVISIBLE);
            bm321.setVisibility(View.INVISIBLE);
            bm322.setVisibility(View.INVISIBLE);
            bm323.setVisibility(View.INVISIBLE);
            bm324.setVisibility(View.INVISIBLE);
            bm325.setVisibility(View.INVISIBLE);
            bm326.setVisibility(View.INVISIBLE);
            bm327.setVisibility(View.INVISIBLE);
            bm328.setVisibility(View.INVISIBLE);
            bm329.setVisibility(View.INVISIBLE);

            Img3Mission3.setVisibility(View.VISIBLE);
            bm331.setVisibility(View.VISIBLE);
            bm332.setVisibility(View.VISIBLE);
            bm333.setVisibility(View.VISIBLE);
            bm334.setVisibility(View.VISIBLE);
            bm335.setVisibility(View.VISIBLE);
            bm336.setVisibility(View.VISIBLE);
            bm337.setVisibility(View.VISIBLE);
            bm338.setVisibility(View.VISIBLE);
            bm339.setVisibility(View.VISIBLE);
        }
        else {
            Img3Mission3.setVisibility(View.INVISIBLE);
            bm331.setVisibility(View.INVISIBLE);
            bm332.setVisibility(View.INVISIBLE);
            bm333.setVisibility(View.INVISIBLE);
            bm334.setVisibility(View.INVISIBLE);
            bm335.setVisibility(View.INVISIBLE);
            bm336.setVisibility(View.INVISIBLE);
            bm337.setVisibility(View.INVISIBLE);
            bm338.setVisibility(View.INVISIBLE);
            bm339.setVisibility(View.INVISIBLE);

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

        updateLayoutMatch();
        updateButtonsStart();

    }

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