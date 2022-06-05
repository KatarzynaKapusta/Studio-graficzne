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

public class fragment_info_improvements extends Fragment {

    private static final String USERS = "Users";
    private final String TAG = this.getClass().getName().toUpperCase();

    // Database variables
    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;

    UserGameInfo userGameInfo;
    UserOwnedUpgrades userOwnedUpgrades;

    // List for levels
    private final List<Double> lvlList = new ArrayList<>();

    private TextView comp1, comp2, comp3, card1, card2, card3, tab1,tab2,tab3;

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

        //readFromDatabase(currentUser, userRef);

        return v;
    }

    private void readFromDatabase(FirebaseUser currentUser, DatabaseReference userRef) {
        if (currentUser != null) {
            // Read from "Users" branch in db
            userRef.addValueEventListener(new ValueEventListener() {
                Double money, level, resources, experience;
                String moneyString, resourcesString, experienceString;
                Boolean isEmployee1Hired, isEmployee2Hired, isEmployee3Hired;
                Integer f1, f2, f3, t1,t2,t3,p1,p2,p3, studio_level,
                        card_lvl1, card_lvl2, card_lvl3, pc_lvl1, pc_lvl2, pc_lvl3, t_lvl1, t_lvl2, t_lvl3;

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

                            studio_level = keyId.child("UserStudioInfo").child("studioLevel").getValue(Integer.class);

                            isEmployee1Hired = keyId.child("UserEmployeesInfo").child("employee1Hired").getValue(Boolean.class);
                            isEmployee2Hired = keyId.child("UserEmployeesInfo").child("employee2Hired").getValue(Boolean.class);
                            isEmployee3Hired = keyId.child("UserEmployeesInfo").child("employee3Hired").getValue(Boolean.class);

                            f1 = keyId.child("UserOwnedItems").child("f1").getValue(Integer.class);
                            f2 = keyId.child("UserOwnedItems").child("f2").getValue(Integer.class);
                            f3 = keyId.child("UserOwnedItems").child("f3").getValue(Integer.class);

                            p1 = keyId.child("UserOwnedItems").child("p1").getValue(Integer.class);
                            p2 = keyId.child("UserOwnedItems").child("p2").getValue(Integer.class);
                            p3 = keyId.child("UserOwnedItems").child("p3").getValue(Integer.class);

                            t1 = keyId.child("UserOwnedItems").child("t1").getValue(Integer.class);
                            t2 = keyId.child("UserOwnedItems").child("t2").getValue(Integer.class);
                            t3 = keyId.child("UserOwnedItems").child("t3").getValue(Integer.class);

                            card_lvl1 = keyId.child("UserOwnedUpgrades").child("card_lvl1").getValue(Integer.class);
                            card_lvl2 = keyId.child("UserOwnedUpgrades").child("card_lvl2").getValue(Integer.class);
                            card_lvl3 = keyId.child("UserOwnedUpgrades").child("card_lvl3").getValue(Integer.class);

                            pc_lvl1 = keyId.child("UserOwnedUpgrades").child("pc_lvl1").getValue(Integer.class);
                            pc_lvl2 = keyId.child("UserOwnedUpgrades").child("pc_lvl2").getValue(Integer.class);
                            pc_lvl3 = keyId.child("UserOwnedUpgrades").child("pc_lvl3").getValue(Integer.class);

                            t_lvl1 = keyId.child("UserOwnedUpgrades").child("t_lvl1").getValue(Integer.class);
                            t_lvl2 = keyId.child("UserOwnedUpgrades").child("t_lvl2").getValue(Integer.class);
                            t_lvl3 = keyId.child("UserOwnedUpgrades").child("t_lvl3").getValue(Integer.class);


                            break;
                        }
                    }

//                    userEmployeesInfo.setEmployee1Hired(isEmployee1Hired);
//                    userEmployeesInfo.setEmployee2Hired(isEmployee2Hired);
//                    userEmployeesInfo.setEmployee3Hired(isEmployee3Hired);
//
//                    setUserOwnedUpgrades(card_lvl1, card_lvl2, card_lvl3, pc_lvl1, pc_lvl2, pc_lvl3, t_lvl1, t_lvl2,t_lvl3);
//                    setUserOwnedItems(f1,f2,f3,p1,p2,p3,t1,t2,t3);
//
//                    checkIfEmployeeIsHired();
//                    checkIfDeskIsOwned();
//                    checkIfFloorIsOwned();
//                    checkIfPlantIsOwned();
//
//                    checkIfComputerIsOwned();
//                    checkIfGraphicCardIsOwned();
//                    checkIfTabletIsOwned();
//
//                    addImprovements();
//                    addFurniture();
//
//                    numbOfEmployees.setText(String.valueOf(numberOfEmployees));
//                    numbOfImprovements.setText(String.valueOf(numberOfImprovements));
//                    studioLevel.setText(String.valueOf(studio_level));
//
//                    numbOfDesks.setText(String.valueOf(desksCounter));
//                    numbOfPlants.setText(String.valueOf(plantsCounter));
//                    numbOfFloors.setText(String.valueOf(floorsCounter));
//
//                    numbOfComputers.setText(String.valueOf(computersCounter));
//                    numbOfTablets.setText(String.valueOf(tabletsCounter));
//                    numbOfGraphicCards.setText(String.valueOf(graphicCardsCounter));
                } // End of reading from "Users" branch

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
    }

}