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
import java.util.Map;

public class activity_magazyn_match_missions extends AppCompatActivity {

    private final String TAG = this.getClass().getName().toUpperCase();

    //Mission 1 - buttons
    private Button sm11, sm12, sm13, sm14, sm15, sm16, sm17, sm18, sm19;

    //Mission 2 - buttons
    private Button sm211, sm212, sm213, sm214, sm215, sm216;
    private Button sm221, sm222, sm223, sm224, sm225, sm226;
    private Button sm231, sm232, sm233, sm234, sm235, sm236;

    //Mission 3 - buttons
    private Button sm311, sm312, sm313, sm314, sm315, sm316, sm317, sm318, sm319;
    private Button sm321, sm322, sm323, sm324, sm325, sm326, sm327, sm328, sm329;
    private Button sm331, sm332, sm333, sm334, sm335, sm336, sm337, sm338, sm339;

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

    private boolean SMissionChosen1 = false;
    private boolean SMissionChosen2 = false;
    private boolean SMissionChosen3 = false;

    private boolean rewardsCollectedMatchStorage = false;

    private long res, exp;

    private int levelCounterStorage =0, matchedCounterStorage =0;

    //TextView Rewards
    private TextView Experience;
    private TextView vExperience;
    private TextView Resources;
    private TextView vResources;
    private TextView RewardsAcquired;

    //DATABASE
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    UserGameInfo User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazyn_match_missions);

        //Rewards TextViews
        Experience = findViewById(R.id.rewards_exp_sm);
        vExperience = findViewById(R.id.rewards_exp_smv);
        Resources = findViewById(R.id.rewards_res_sm);
        vResources = findViewById(R.id.rewards_res_smv);
        RewardsAcquired = findViewById(R.id.rewards_acquired_sm);

        //Layout
        layout1 = findViewById(R.id.mission1_layout_s);
        layout2 = findViewById(R.id.mission2_layout_s);
        layout3 = findViewById(R.id.mission3_layout_s);

        layout_m = findViewById(R.id.s_match_mission_layout);

        //Choice buttons
        match_button1 = findViewById(R.id.sbutton_1);
        match_button2 = findViewById(R.id.sbutton_2);
        match_button3 = findViewById(R.id.sbutton_3);

        //Start mission buttons
        start_matched_mission_button = findViewById(R.id.s_start_match_mission);
        back_to_matched_missions_button = findViewById(R.id.s_back_to_match_missions_choice);
        collect_match_rewards_button = findViewById(R.id.s_collect_match_rewards);

        //Mission 1 - match buttons
        sm11 = findViewById(R.id.no_match_s11);
        sm12 = findViewById(R.id.no_match_s12);
        sm13 = findViewById(R.id.match_s11);

        sm14 = findViewById(R.id.no_match_s13);
        sm15 = findViewById(R.id.no_match_s14);
        sm16 = findViewById(R.id.match_s12);

        sm17 = findViewById(R.id.no_match_s15);
        sm18 = findViewById(R.id.no_match_s16);
        sm19 = findViewById(R.id.match_s13);

        //Mission 1 - images
        Img1Mission1 = findViewById(R.id.sm1_img11);
        Img2Mission1 = findViewById(R.id.sm1_img12);
        Img3Mission1 = findViewById(R.id.sm1_img13);


        StorageMatchMission1 m1 = new StorageMatchMission1();
        StorageMatchMission2 m2 = new StorageMatchMission2();
        StorageMatchMission3 m3 = new StorageMatchMission3();


        User = new UserGameInfo();

        //Mission 2 - match buttons
        sm211 = findViewById(R.id.match_s211);
        sm212 = findViewById(R.id.no_match_s212);
        sm213 = findViewById(R.id.no_match_s213);
        sm214 = findViewById(R.id.no_match_s214);
        sm215 = findViewById(R.id.no_match_s215);
        sm216 = findViewById(R.id.no_match_s216);

        sm221 = findViewById(R.id.match_s221);
        sm222 = findViewById(R.id.no_match_s222);
        sm223 = findViewById(R.id.no_match_s223);
        sm224 = findViewById(R.id.no_match_s224);
        sm225 = findViewById(R.id.no_match_s225);
        sm226 = findViewById(R.id.no_match_s226);

        sm231 = findViewById(R.id.match_s231);
        sm232 = findViewById(R.id.no_match_s232);
        sm233 = findViewById(R.id.no_match_s233);
        sm234 = findViewById(R.id.no_match_s234);
        sm235 = findViewById(R.id.no_match_s235);
        sm236 = findViewById(R.id.no_match_s236);

        //Mission 2 - images
        Img1Mission2 = findViewById(R.id.sm2_img21);
        Img2Mission2 = findViewById(R.id.sm2_img22);
        Img3Mission2 = findViewById(R.id.sm2_img23);

        //Mission 3 - match buttons
        sm311 = findViewById(R.id.match_s311);
        sm312 = findViewById(R.id.no_match_s312);
        sm313 = findViewById(R.id.no_match_s313);
        sm314 = findViewById(R.id.no_match_s314);
        sm315 = findViewById(R.id.no_match_s315);
        sm316 = findViewById(R.id.no_match_s316);
        sm317 = findViewById(R.id.no_match_s317);
        sm318 = findViewById(R.id.no_match_s318);
        sm319 = findViewById(R.id.no_match_s319);

        sm321 = findViewById(R.id.match_s321);
        sm322 = findViewById(R.id.no_match_s322);
        sm323 = findViewById(R.id.no_match_s323);
        sm324 = findViewById(R.id.no_match_s324);
        sm325 = findViewById(R.id.no_match_s325);
        sm326 = findViewById(R.id.no_match_s326);
        sm327 = findViewById(R.id.no_match_s327);
        sm328 = findViewById(R.id.no_match_s328);
        sm329 = findViewById(R.id.no_match_s329);

        sm331 = findViewById(R.id.match_s331);
        sm332 = findViewById(R.id.no_match_s332);
        sm333 = findViewById(R.id.no_match_s333);
        sm334 = findViewById(R.id.no_match_s334);
        sm335 = findViewById(R.id.no_match_s335);
        sm336 = findViewById(R.id.no_match_s336);
        sm337 = findViewById(R.id.no_match_s337);
        sm338 = findViewById(R.id.no_match_s338);
        sm339 = findViewById(R.id.no_match_s339);

        //Mission 3 - images
        Img1Mission3 = findViewById(R.id.sm3_img31);
        Img2Mission3 = findViewById(R.id.sm3_img32);
        Img3Mission3 = findViewById(R.id.sm3_img33);

        //Database
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Users");
        Log.v("USERID", databaseReference.getKey());

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");



        //Reading database
        databaseReference.addValueEventListener(new ValueEventListener() {
            Double res, experience;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyId: dataSnapshot.getChildren()) {
                    if (keyId.child("UserInfo").child("email").getValue().equals(email))
                    {
                        experience = keyId.child("UserGameInfo").child("experience").getValue(Double.class);
                        res = keyId.child("UserGameInfo").child("resources").getValue(Double.class);

                        break;
                    }
                }
                User.setExperience(experience);
                User.setResources(res);
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
            SMissionChosen1 = true;
            SMissionChosen2 = false;
            SMissionChosen3 = false;
            updateLayoutINVIS();
        });

        match_button2.setOnClickListener(view -> {
            SMissionChosen2 =true;
            SMissionChosen1 = false;
            SMissionChosen3 = false;
            updateLayoutINVIS()
            ;
        });

        match_button3.setOnClickListener(view -> {
            SMissionChosen3 = true;
            SMissionChosen2 = false;
            SMissionChosen1 = false;
            updateLayoutINVIS();
        });
        //

        start_matched_mission_button.setOnClickListener(view -> {
            if(SMissionChosen1)
            {
                levelCounterStorage++;
                updateButtonsMission1(levelCounterStorage);
            }
            else if(SMissionChosen2)
            {
                levelCounterStorage++;
                updateButtonsMission2(levelCounterStorage);
            }
            else
            {
                levelCounterStorage++;
                updateButtonsMission3(levelCounterStorage);
            }

        });

        collect_match_rewards_button.setOnClickListener(view -> {
            if(SMissionChosen1)
            {
                double resR=(double)(res);
                double expR=(double)(exp);
                User.addMatchMissionRewardsStorage(resR, expR);
                System.out.println(User.getExperience());
                System.out.println(User.getResources());
            }
            else if(SMissionChosen2)
            {
                double resR=(double)(res);
                double expR=(double)(exp);
                User.addMatchMissionRewardsStorage(resR, expR);
                System.out.println(User.getExperience());
                System.out.println(User.getResources());
            }
            else
            {
                double resR=(double)(res);
                double expR=(double)(exp);
                User.addMatchMissionRewardsStorage(resR, expR);
                System.out.println(User.getExperience());
                System.out.println(User.getResources());
            }
            updateDataToFirebase();

            Toast.makeText(activity_magazyn_match_missions.this, "Przyznano nagrody",
                    Toast.LENGTH_SHORT).show();
            levelCounterStorage = 0;
            matchedCounterStorage = 0;
            rewardsCollectedMatchStorage = true;

            updateTextView();
            updateButtonsStart();
            updateLayoutVIS();
        });

        back_to_matched_missions_button.setOnClickListener(view -> {
            SMissionChosen1 = false;
            SMissionChosen2 = false;
            SMissionChosen3 =false;
            updateLayoutVIS();
        });

        //////////////////////////////////////////////////////
        //Mission 1 - Level 1
        sm11.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission1(levelCounterStorage);
        });

        sm12.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission1(levelCounterStorage);
        });

        sm13.setOnClickListener(view -> {
            levelCounterStorage++;
            matchedCounterStorage++;
            updateButtonsMission1(levelCounterStorage);
        });

        //Mission 1 - level 2
        sm14.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission1(levelCounterStorage);
        });

        sm15.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission1(levelCounterStorage);
        });

        sm16.setOnClickListener(view -> {
            levelCounterStorage++;
            matchedCounterStorage++;
            updateButtonsMission1(levelCounterStorage);
        });

        //Mission 1 - Level 3
        sm17.setOnClickListener(view -> {
            levelCounterStorage++;
            m1.HowManyMatched(matchedCounterStorage);
            res = (long)(m1.getSm_res()) ;
            exp = (long)(m1.getSm_exp());


            updateButtonsMission1(levelCounterStorage);
        });

        sm18.setOnClickListener(view -> {
            levelCounterStorage++;
            m1.HowManyMatched(matchedCounterStorage);
            res = (long)(m1.getSm_res()) ;
            exp = (long)(m1.getSm_exp());


            updateButtonsMission1(levelCounterStorage);
        });

        sm19.setOnClickListener(view -> {
            levelCounterStorage++;
            matchedCounterStorage++;
            m1.HowManyMatched(matchedCounterStorage);
            res = (long)(m1.getSm_res()) ;
            exp = (long)(m1.getSm_exp());


            updateButtonsMission1(levelCounterStorage);
        });

        ////////////////////////////////////////
        //Mission 2 - level 1
        sm211.setOnClickListener(view -> {
            levelCounterStorage++;
            matchedCounterStorage++;
            updateButtonsMission2(levelCounterStorage);

        });

        sm212.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission2(levelCounterStorage);
        });

        sm213.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission2(levelCounterStorage);
        });

        sm214.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission2(levelCounterStorage);
        });

        sm215.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission2(levelCounterStorage);
        });

        sm216.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission2(levelCounterStorage);
        });

        //Mission 2 - level 2
        sm221.setOnClickListener(view -> {
            levelCounterStorage++;
            matchedCounterStorage++;
            updateButtonsMission2(levelCounterStorage);
        });

        sm222.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission2(levelCounterStorage);
        });

        sm223.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission2(levelCounterStorage);
        });

        sm224.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission2(levelCounterStorage);
        });

        sm225.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission2(levelCounterStorage);
        });

        sm226.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission2(levelCounterStorage);
        });

        //Mission 2 - level 3
        sm231.setOnClickListener(view -> {
            levelCounterStorage++;
            matchedCounterStorage++;
            m2.HowManyMatched(matchedCounterStorage);
            res = (long)(m2.getSm_res());
            exp = (long)(m2.getSm_exp());

            updateButtonsMission2(levelCounterStorage);

        });

        sm232.setOnClickListener(view -> {
            levelCounterStorage++;
            m2.HowManyMatched(matchedCounterStorage);
            res = (long)(m2.getSm_res());
            exp = (long)(m2.getSm_exp());
            updateButtonsMission2(levelCounterStorage);


        });

        sm233.setOnClickListener(view -> {
            levelCounterStorage++;
            m2.HowManyMatched(matchedCounterStorage);
            res = (long)(m2.getSm_res());
            exp = (long)(m2.getSm_exp());
            updateButtonsMission2(levelCounterStorage);
        });

        sm234.setOnClickListener(view -> {
            levelCounterStorage++;
            m2.HowManyMatched(matchedCounterStorage);
            res = (long)(m2.getSm_res());
            exp = (long)(m2.getSm_exp());
            updateButtonsMission2(levelCounterStorage);
        });

        sm235.setOnClickListener(view -> {
            levelCounterStorage++;
            m2.HowManyMatched(matchedCounterStorage);
            res = (long)(m2.getSm_res());
            exp = (long)(m2.getSm_exp());
            updateButtonsMission2(levelCounterStorage);


        });

        sm236.setOnClickListener(view -> {
            levelCounterStorage++;
            m2.HowManyMatched(matchedCounterStorage);
            res = (long)(m2.getSm_res());
            exp = (long)(m2.getSm_exp());
            updateButtonsMission2(levelCounterStorage);
        });

        ///////////////////
        //Mission  3 - level 1

        sm311.setOnClickListener(view -> {
            levelCounterStorage++;
            matchedCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm312.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm313.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm314.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm315.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm316.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm317.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm318.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm319.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        //Mission 3 - level 2
        sm321.setOnClickListener(view -> {
            levelCounterStorage++;
            matchedCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm322.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm323.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm324.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm325.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm326.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm327.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm328.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        sm329.setOnClickListener(view -> {
            levelCounterStorage++;
            updateButtonsMission3(levelCounterStorage);
        });

        //Mission  3 - level 3

        sm331.setOnClickListener(view -> {
            levelCounterStorage++;
            matchedCounterStorage++;
            m3.HowManyMatched(matchedCounterStorage);
            res = (long)(m3.getSm_res());
            exp = (long)(m3.getSm_exp());
            updateButtonsMission3(levelCounterStorage);


        });

        sm332.setOnClickListener(view -> {
            levelCounterStorage++;
            m3.HowManyMatched(matchedCounterStorage);
            res = (long)(m3.getSm_res());
            exp = (long)(m3.getSm_exp());
            updateButtonsMission3(levelCounterStorage);

        });

        sm333.setOnClickListener(view -> {
            levelCounterStorage++;
            m3.HowManyMatched(matchedCounterStorage);
            res = (long)(m3.getSm_res());
            exp = (long)(m3.getSm_exp());
            updateButtonsMission3(levelCounterStorage);


        });

        sm334.setOnClickListener(view -> {
            levelCounterStorage++;
            m3.HowManyMatched(matchedCounterStorage);
            res = (long)(m3.getSm_res());
            exp = (long)(m3.getSm_exp());
            updateButtonsMission3(levelCounterStorage);


        });

        sm335.setOnClickListener(view -> {
            levelCounterStorage++;
            m3.HowManyMatched(matchedCounterStorage);
            res = (long)(m3.getSm_res());
            exp = (long)(m3.getSm_exp());
            updateButtonsMission3(levelCounterStorage);


        });

        sm336.setOnClickListener(view -> {
            levelCounterStorage++;
            m3.HowManyMatched(matchedCounterStorage);
            res = (long)(m3.getSm_res());
            exp = (long)(m3.getSm_exp());
            updateButtonsMission3(levelCounterStorage);


        });

        sm337.setOnClickListener(view -> {
            levelCounterStorage++;
            m3.HowManyMatched(matchedCounterStorage);
            res = (long)(m3.getSm_res());
            exp = (long)(m3.getSm_exp());
            updateButtonsMission3(levelCounterStorage);
        });

        sm338.setOnClickListener(view -> {
            levelCounterStorage++;
            m3.HowManyMatched(matchedCounterStorage);
            res = (long)(m3.getSm_res());
            exp = (long)(m3.getSm_exp());
            updateButtonsMission3(levelCounterStorage);


        });

        sm339.setOnClickListener(view -> {
            levelCounterStorage++;
            m3.HowManyMatched(matchedCounterStorage);
            res = (long)(m3.getSm_res());
            exp = (long)(m3.getSm_exp());
            updateButtonsMission3(levelCounterStorage);

        });

    }

    private void updateTextView(){
        if(rewardsCollectedMatchStorage)
        {
            RewardsAcquired.setVisibility(View.INVISIBLE);

            vResources.setVisibility(View.INVISIBLE);
            Resources.setVisibility(View.INVISIBLE);
            vExperience.setVisibility(View.INVISIBLE);
            Experience.setVisibility(View.INVISIBLE);
        }
        else
        {
            RewardsAcquired.setVisibility(View.VISIBLE);

            vResources.setVisibility(View.VISIBLE);
            Resources.setVisibility(View.VISIBLE);
            vExperience.setVisibility(View.VISIBLE);
            Experience.setVisibility(View.VISIBLE);
        }
    }

    private void setTextView(long resRew, long expRew){
        String expString = String.valueOf(resRew);
        String resString = String.valueOf(expRew);
        vExperience.setText(expString);
        vResources.setText(resString);
    }

    private void updateLayoutMatch(){
        if(rewardsCollectedMatchStorage)
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
        if(rewardsCollectedMatchStorage)
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

    private void updateButtonsMission1(int level) {
        if(level==1)
        {
            back_to_matched_missions_button.setVisibility(View.INVISIBLE);
            start_matched_mission_button.setVisibility(View.INVISIBLE);

            Img1Mission1.setVisibility(View.VISIBLE);
            sm11.setVisibility(View.VISIBLE);
            sm12.setVisibility(View.VISIBLE);
            sm13.setVisibility(View.VISIBLE);
        }
        else if(level==2)
        {
            Img1Mission1.setVisibility(View.INVISIBLE);
            sm11.setVisibility(View.INVISIBLE);
            sm12.setVisibility(View.INVISIBLE);
            sm13.setVisibility(View.INVISIBLE);

            Img2Mission1.setVisibility(View.VISIBLE);
            sm14.setVisibility(View.VISIBLE);
            sm15.setVisibility(View.VISIBLE);
            sm16.setVisibility(View.VISIBLE);
        }
        else if (level==3)
        {
            Img2Mission1.setVisibility(View.INVISIBLE);
            sm14.setVisibility(View.INVISIBLE);
            sm15.setVisibility(View.INVISIBLE);
            sm16.setVisibility(View.INVISIBLE);

            Img3Mission1.setVisibility(View.VISIBLE);
            sm17.setVisibility(View.VISIBLE);
            sm18.setVisibility(View.VISIBLE);
            sm19.setVisibility(View.VISIBLE);
        }
        else
        {
            Img3Mission1.setVisibility(View.INVISIBLE);
            sm17.setVisibility(View.INVISIBLE);
            sm18.setVisibility(View.INVISIBLE);
            sm19.setVisibility(View.INVISIBLE);

            rewardsCollectedMatchStorage = false;
            updateTextView();
            setTextView(res, exp);
            collect_match_rewards_button.setVisibility(View.VISIBLE);
        }
    }

    private void updateButtonsMission2(int level) {
        if(level==1) {
            back_to_matched_missions_button.setVisibility(View.INVISIBLE);
            start_matched_mission_button.setVisibility(View.INVISIBLE);

            Img1Mission2.setVisibility(View.VISIBLE);
            sm211.setVisibility(View.VISIBLE);
            sm212.setVisibility(View.VISIBLE);
            sm213.setVisibility(View.VISIBLE);
            sm214.setVisibility(View.VISIBLE);
            sm215.setVisibility(View.VISIBLE);
            sm216.setVisibility(View.VISIBLE);
        }
        else if (level==2) {
            Img1Mission2.setVisibility(View.INVISIBLE);
            sm211.setVisibility(View.INVISIBLE);
            sm212.setVisibility(View.INVISIBLE);
            sm213.setVisibility(View.INVISIBLE);
            sm214.setVisibility(View.INVISIBLE);
            sm215.setVisibility(View.INVISIBLE);
            sm216.setVisibility(View.INVISIBLE);

            Img2Mission2.setVisibility(View.VISIBLE);
            sm221.setVisibility(View.VISIBLE);
            sm222.setVisibility(View.VISIBLE);
            sm223.setVisibility(View.VISIBLE);
            sm224.setVisibility(View.VISIBLE);
            sm225.setVisibility(View.VISIBLE);
            sm226.setVisibility(View.VISIBLE);
        }
        else if(level==3) {
            Img2Mission2.setVisibility(View.INVISIBLE);
            sm221.setVisibility(View.INVISIBLE);
            sm222.setVisibility(View.INVISIBLE);
            sm223.setVisibility(View.INVISIBLE);
            sm224.setVisibility(View.INVISIBLE);
            sm225.setVisibility(View.INVISIBLE);
            sm226.setVisibility(View.INVISIBLE);

            Img3Mission2.setVisibility(View.VISIBLE);
            sm231.setVisibility(View.VISIBLE);
            sm232.setVisibility(View.VISIBLE);
            sm233.setVisibility(View.VISIBLE);
            sm234.setVisibility(View.VISIBLE);
            sm235.setVisibility(View.VISIBLE);
            sm236.setVisibility(View.VISIBLE);
        }
        else {
            Img3Mission2.setVisibility(View.INVISIBLE);
            sm231.setVisibility(View.INVISIBLE);
            sm232.setVisibility(View.INVISIBLE);
            sm233.setVisibility(View.INVISIBLE);
            sm234.setVisibility(View.INVISIBLE);
            sm235.setVisibility(View.INVISIBLE);
            sm236.setVisibility(View.INVISIBLE);

            rewardsCollectedMatchStorage = false;
            updateTextView();
            setTextView(res, exp);
            collect_match_rewards_button.setVisibility(View.VISIBLE);
        }
    }

    private void updateButtonsMission3(int level) {
        if(level==1) {
            back_to_matched_missions_button.setVisibility(View.INVISIBLE);
            start_matched_mission_button.setVisibility(View.INVISIBLE);

            Img1Mission3.setVisibility(View.VISIBLE);
            sm311.setVisibility(View.VISIBLE);
            sm312.setVisibility(View.VISIBLE);
            sm313.setVisibility(View.VISIBLE);
            sm314.setVisibility(View.VISIBLE);
            sm315.setVisibility(View.VISIBLE);
            sm316.setVisibility(View.VISIBLE);
            sm317.setVisibility(View.VISIBLE);
            sm318.setVisibility(View.VISIBLE);
            sm319.setVisibility(View.VISIBLE);
        }
        else if (level==2) {
            Img1Mission3.setVisibility(View.INVISIBLE);
            sm311.setVisibility(View.INVISIBLE);
            sm312.setVisibility(View.INVISIBLE);
            sm313.setVisibility(View.INVISIBLE);
            sm314.setVisibility(View.INVISIBLE);
            sm315.setVisibility(View.INVISIBLE);
            sm316.setVisibility(View.INVISIBLE);
            sm317.setVisibility(View.INVISIBLE);
            sm318.setVisibility(View.INVISIBLE);
            sm319.setVisibility(View.INVISIBLE);

            Img2Mission3.setVisibility(View.VISIBLE);
            sm321.setVisibility(View.VISIBLE);
            sm322.setVisibility(View.VISIBLE);
            sm323.setVisibility(View.VISIBLE);
            sm324.setVisibility(View.VISIBLE);
            sm325.setVisibility(View.VISIBLE);
            sm326.setVisibility(View.VISIBLE);
            sm327.setVisibility(View.VISIBLE);
            sm328.setVisibility(View.VISIBLE);
            sm329.setVisibility(View.VISIBLE);
        }
        else if(level==3) {
            Img2Mission3.setVisibility(View.INVISIBLE);
            sm321.setVisibility(View.INVISIBLE);
            sm322.setVisibility(View.INVISIBLE);
            sm323.setVisibility(View.INVISIBLE);
            sm324.setVisibility(View.INVISIBLE);
            sm325.setVisibility(View.INVISIBLE);
            sm326.setVisibility(View.INVISIBLE);
            sm327.setVisibility(View.INVISIBLE);
            sm328.setVisibility(View.INVISIBLE);
            sm329.setVisibility(View.INVISIBLE);

            Img3Mission3.setVisibility(View.VISIBLE);
            sm331.setVisibility(View.VISIBLE);
            sm332.setVisibility(View.VISIBLE);
            sm333.setVisibility(View.VISIBLE);
            sm334.setVisibility(View.VISIBLE);
            sm335.setVisibility(View.VISIBLE);
            sm336.setVisibility(View.VISIBLE);
            sm337.setVisibility(View.VISIBLE);
            sm338.setVisibility(View.VISIBLE);
            sm339.setVisibility(View.VISIBLE);
        }
        else {
            Img3Mission3.setVisibility(View.INVISIBLE);
            sm331.setVisibility(View.INVISIBLE);
            sm332.setVisibility(View.INVISIBLE);
            sm333.setVisibility(View.INVISIBLE);
            sm334.setVisibility(View.INVISIBLE);
            sm335.setVisibility(View.INVISIBLE);
            sm336.setVisibility(View.INVISIBLE);
            sm337.setVisibility(View.INVISIBLE);
            sm338.setVisibility(View.INVISIBLE);
            sm339.setVisibility(View.INVISIBLE);

            rewardsCollectedMatchStorage = false;
            updateTextView();
            setTextView(res, exp);
            collect_match_rewards_button.setVisibility(View.VISIBLE);
        }
    }


    //
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefsStorageMatch = getSharedPreferences("prefsStorageMatch", MODE_PRIVATE);
        SharedPreferences.Editor editorStorageMatch = prefsStorageMatch.edit();

        editorStorageMatch.putLong("resourcesRewards", res);
        editorStorageMatch.putLong("expRewards", exp);
        editorStorageMatch.putBoolean("rewardsCollectedMatchStorage", rewardsCollectedMatchStorage);
        editorStorageMatch.putBoolean("SMissionChosen1", SMissionChosen1);
        editorStorageMatch.putBoolean("SMissionChosen2", SMissionChosen2);
        editorStorageMatch.putBoolean("SMissionChosen3", SMissionChosen3);

        editorStorageMatch.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefsStorageMatch = getSharedPreferences("prefsStorageMatch", MODE_PRIVATE);

        res = prefsStorageMatch.getLong("resourcesRewards", 0);
        exp = prefsStorageMatch.getLong("expRewards", 0);

        rewardsCollectedMatchStorage = prefsStorageMatch.getBoolean("rewardsCollectedMatchStorage", false);
        SMissionChosen1 = prefsStorageMatch.getBoolean("SMissionChosen1", false);
        SMissionChosen2 = prefsStorageMatch.getBoolean("SMissionChosen2", false);
        SMissionChosen3 = prefsStorageMatch.getBoolean("SMissionChosen3", false);

        updateTextView();
        setTextView(res, exp);
        updateLayoutMatch();
        updateButtonsStart();

    }

    //Updating data to firebase
    private void updateDataToFirebase() {

        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null) {
            databaseOperations.updateDatabase(user.getUid(),databaseReference,"experience",User.getExperience());
            databaseOperations.updateDatabase(user.getUid(),databaseReference,"resources",User.getResources());
//            Map<String, Object> childUpdates = new HashMap<>();
//            childUpdates.put("experience", User.getExperience());
//            childUpdates.put("resources", User.getResources());
//
//            databaseReference.child(user.getUid()).child("UserGameInfo").updateChildren(childUpdates);
        }
    }
}