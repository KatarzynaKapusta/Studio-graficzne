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

import java.util.ArrayList;
import java.util.List;


public class fragment_info_statistics extends Fragment {

    private static final String USERS = "Users";
    private final String TAG = this.getClass().getName().toUpperCase();

    // Database variables
    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;

    // List for levels
    private final List<Double> lvlList = new ArrayList<>();
    // Database variables

    UserGameInfo userGameInfo;
    UserEmployeesInfo userEmployeesInfo;
    UserOwnedItems userOwnedItems;
    UserStudioInfo userStudioInfo;

    //Employee earnings
    private Employee emp1;
    private Employee emp2;
    private Employee emp3;

    private int numberOfImprovements = 0;
    private int numberOfFurniture = 0;
    private int numberOfEmployees = 0;

    private int computersCounter =0;
    private int graphicCardsCounter =0;
    private int tabletsCounter =0;

    private int plantsCounter =0;
    private int desksCounter =0;
    private int floorsCounter =0;

    private TextView numbOfDesks, numbOfPlants, numbOfFloors;
    private TextView numbOfTablets, numbOfComputers, numbOfGraphicCards;
    private TextView studioLevel, numbOfEmployees, numbOfImprovements, numbOfFurniture;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info_statistics, container, false);

        numbOfFurniture = v.findViewById(R.id.number_of_furniture);
        numbOfEmployees = v.findViewById(R.id.number_of_employees);
        numbOfImprovements = v.findViewById(R.id.number_of_improvements);
        studioLevel = v.findViewById(R.id.studio_level);

        numbOfFloors = v.findViewById(R.id.number_of_floors);
        numbOfDesks = v.findViewById(R.id.number_of_desks);
        numbOfPlants = v.findViewById(R.id.number_of_plants);

        numbOfComputers = v.findViewById(R.id.number_of_computers);
        numbOfGraphicCards = v.findViewById(R.id.number_of_graphic_cards);
        numbOfTablets = v.findViewById(R.id.number_of_tablets);


        userGameInfo = new UserGameInfo();
        userStudioInfo = new UserStudioInfo();
        userOwnedItems = new UserOwnedItems();
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
                int f1, f2, f3, t1,t2,t3,p1,p2,p3;

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

                            f1 = keyId.child("UserOwnedItems").child("f1").getValue(Integer.class);
                            f2 = keyId.child("UserOwnedItems").child("f2").getValue(Integer.class);
                            f3 = keyId.child("UserOwnedItems").child("f3").getValue(Integer.class);

                            p1 = keyId.child("UserOwnedItems").child("p1").getValue(Integer.class);
                            p2 = keyId.child("UserOwnedItems").child("p2").getValue(Integer.class);
                            p3 = keyId.child("UserOwnedItems").child("p3").getValue(Integer.class);

                            t1 = keyId.child("UserOwnedItems").child("t1").getValue(Integer.class);
                            t2 = keyId.child("UserOwnedItems").child("t2").getValue(Integer.class);
                            t3 = keyId.child("UserOwnedItems").child("t3").getValue(Integer.class);

                            break;
                        }
                    }

                    userEmployeesInfo.setEmployee1Hired(isEmployee1Hired);
                    userEmployeesInfo.setEmployee2Hired(isEmployee2Hired);
                    userEmployeesInfo.setEmployee3Hired(isEmployee3Hired);

                    setUserOwnedItems(f1,f2,f3,p1,p2,p3,t1,t2,t3);
                    checkIfDeskIsOwned();
                    checkIfFloorIsOwned();
                    checkIfPlantIsOwned();
                    addFurniture();



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

    private void checkIfEmployeeIsHired(){
        if(userEmployeesInfo.getEmployee1Hired()){
            numberOfEmployees++;
        }
        if(userEmployeesInfo.getEmployee2Hired()){
            numberOfEmployees++;
        }
        if(userEmployeesInfo.getEmployee3Hired()){
            numberOfEmployees++;
        }
    }

    private void addFurniture(){
        numberOfFurniture = plantsCounter + floorsCounter + desksCounter;
    }

    private void addImprovements(){

    }

    private void setUserOwnedItems(int f1, int f2, int f3, int p1, int p2, int p3, int t1, int t2, int t3){
        userOwnedItems.setF1(f1);
        userOwnedItems.setF2(f2);
        userOwnedItems.setF3(f3);

        userOwnedItems.setP1(p1);
        userOwnedItems.setP2(p2);
        userOwnedItems.setP3(p3);

        userOwnedItems.setT1(t1);
        userOwnedItems.setT2(t2);
        userOwnedItems.setT3(t3);
    }

    private void checkIfPlantIsOwned(){
        if(userOwnedItems.getP1()==1){
            plantsCounter++;
        }
        if(userOwnedItems.getP2()==1){
            plantsCounter++;
        }
        if(userOwnedItems.getP3()==1)
        {
            plantsCounter++;
        }
    }

    private void checkIfFloorIsOwned(){
        if(userOwnedItems.getF1()==1){
            floorsCounter++;
        }
        if(userOwnedItems.getF2()==1){
            floorsCounter++;
        }
        if(userOwnedItems.getF3()==1)
        {
            floorsCounter++;
        }
    }

    private void checkIfDeskIsOwned(){
        if(userOwnedItems.getT1()==1){
            desksCounter++;
        }
        if(userOwnedItems.getT2()==1){
            desksCounter++;
        }
        if(userOwnedItems.getT3()==1)
        {
            desksCounter++;
        }
    }

    private void checkIfComputerIsOwned(){

    }

    private void checkIfTabletIsOwned(){

    }

    private void checkIfGraphicCardIsOwned(){

    }
}