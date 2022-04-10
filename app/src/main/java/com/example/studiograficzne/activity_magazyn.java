package com.example.studiograficzne;

import android.os.Bundle;
import android.widget.Button;
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

public class activity_magazyn extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Missions Mission1 = new Missions();
    UserGameInfo User;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazyn);

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
        //Odczytać baze danych w mainie
        //User = getIntent().getParcelableExtra("user");
        //User = new UserGameInfo();

        Button rewardsButton = findViewById(R.id.collect_rewards);
        rewardsButton.setOnClickListener(view -> {

            FirebaseUser user = mAuth.getCurrentUser();
            //User.setUID(user.getUid());

            if(user!=null)
            {
                User.addMissionRewards(Mission1.getM_resources(), Mission1.getM_money(), Mission1.getM_experience());
//                databaseReference = firebaseDatabase.getReference("Users");
//                addDatatoFirebase(User.getResources(), User.getMoney(), User.getExperience());
            }
            else
            {
                Toast.makeText(activity_magazyn.this, "BŁĄD",
                        Toast.LENGTH_SHORT).show();
            }

            System.out.println(User.getExperience());
            System.out.println(User.getMoney());
            System.out.println(User.getResources());
        });
    }

    private void addDatatoFirebase(double res, double money, double exp) {
        // below 3 lines of code is used to set
        // data in our object class.
        FirebaseUser user = mAuth.getCurrentUser();

        User.setResources(res);
        User.setMoney(money);
        User.setExperience(exp);
        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.child(user.getUid()).child("UserGameInfo").child("resources").setValue(User.getResources());
                databaseReference.child(user.getUid()).child("UserGameInfo").child("money").setValue(User.getMoney());
                databaseReference.child(user.getUid()).child("UserGameInfo").child("experience").setValue(User.getExperience());

                // after adding this data we are showing toast message.
                Toast.makeText(activity_magazyn.this, "Przyznano Nagrody", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(activity_magazyn.this, "Nie przyznano nagród " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}