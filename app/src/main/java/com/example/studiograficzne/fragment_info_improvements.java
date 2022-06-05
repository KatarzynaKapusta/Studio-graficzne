package com.example.studiograficzne;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class fragment_info_improvements extends Fragment {

    private static final String USERS = "Users";
    private final String TAG = this.getClass().getName().toUpperCase();

    // Database variables
    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;

    UserGameInfo userGameInfo;
    UserOwnedUpgrades userOwnedUpgrades;
    UserOwnedItems userOwnedItems;

    // List for levels
    private final List<Double> lvlList = new ArrayList<>();

    private  TextView comp1, comp2, comp3, card1, card2, card3, tab1, tab2, tab3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_info_improvements, container, false);

        comp1 = v.findViewById(R.id.comp_level1);
        comp2 = v.findViewById(R.id.comp_level2);
        comp3 = v.findViewById(R.id.comp_level3);

        card1 = v.findViewById(R.id.card_level1);
        card2 = v.findViewById(R.id.card_level2);
        card3 = v.findViewById(R.id.card_level3);

        tab1 = v.findViewById(R.id.tablet_level1);
        tab2 = v.findViewById(R.id.tablet_level2);
        tab3 = v.findViewById(R.id.tablet_level3);

        // Checking if user is logged or not and getting his email
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email = currentUser.getEmail();
        }

        DatabaseReference userRef = rootRef.child(USERS);
        Log.v("USERID", userRef.getKey());

        readFromDatabase(currentUser, userRef);

        return v;
    }

    private void readFromDatabase(FirebaseUser currentUser, DatabaseReference userRef) {
        if (currentUser != null) {
            // Read from "Users" branch in db
            userRef.addValueEventListener(new ValueEventListener() {
                Double money, level, resources, experience;
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

                            Map<String,Long> m = (Map)keyId.child("UserOwnedItems").getValue();
                            Map<String,Long> u = (Map)keyId.child("UserOwnedUpgrades").getValue();

                            userOwnedItems = new UserOwnedItems(m.get("f1").intValue(), m.get("f2").intValue(),m.get("f3").intValue(),m.get("p1").intValue(),m.get("p2").intValue(),m.get("p3").intValue(),m.get("t1").intValue(),m.get("t2").intValue(),m.get("t3").intValue());
                            userOwnedUpgrades = new UserOwnedUpgrades(u.get("card_lvl1").intValue(), u.get("card_lvl2").intValue(),u.get("card_lvl3").intValue(),u.get("pc_lvl1").intValue(),u.get("pc_lvl2").intValue(),u.get("pc_lvl3").intValue(),u.get("t_lvl1").intValue(),u.get("t_lvl2").intValue(),u.get("t_lvl3").intValue());


                            break;
                        }
                    }
                    checkIfUpgradeIsOwned(comp1, comp2, comp3, userOwnedUpgrades.getPc_lvl1(),userOwnedUpgrades.getPc_lvl2(), userOwnedUpgrades.getPc_lvl3());
                    checkIfUpgradeIsOwned(card1, card2, card3, userOwnedUpgrades.getCard_lvl1(), userOwnedUpgrades.getCard_lvl2(), userOwnedUpgrades.getCard_lvl3());
                    checkIfUpgradeIsOwned(tab1, tab2, tab3, userOwnedUpgrades.getT_lvl1(), userOwnedUpgrades.getT_lvl2(), userOwnedUpgrades.getT_lvl3());
                } // End of reading from "Users" branch

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
    }

    private void checkIfUpgradeIsOwned(TextView t1, TextView t2, TextView t3, Integer up1, Integer up2, Integer up3){
        if(up1==1)
        {
            t1.setText("TAK");
        }
        else
        {
            t1.setText("NIE");
        }
        if(up2==1)
        {
            t2.setText("TAK");
        }
        else
        {
            t2.setText("NIE");
        }
        if(up3==1)
        {
            t3.setText("TAK");
        }
        else
        {
            t3.setText("NIE");
        }
    }


}