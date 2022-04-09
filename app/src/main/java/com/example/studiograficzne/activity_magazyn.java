package com.example.studiograficzne;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class activity_magazyn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazyn);

        UserGameInfo User1 = getIntent().getParcelableExtra("user");
        Missions Mission1 = new Missions();

        Button rewardsButton = findViewById(R.id.collect_rewards);
        rewardsButton.setOnClickListener(view -> {


            User1.addMissionRewards(Mission1.getM_resources(), Mission1.getM_money(), Mission1.getM_experience());
            System.out.println(User1.getExperience());
            System.out.println(User1.getMoney());
            System.out.println(User1.getResources());
        });
    }

}