package com.example.studiograficzne;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.UserManager;
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
    UserOwnedUpgrades userOwnedUpgrades;

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
        userOwnedUpgrades = new UserOwnedUpgrades();


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
        readFromDatabase(currentUser, userRef);

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

                    userEmployeesInfo.setEmployee1Hired(isEmployee1Hired);
                    userEmployeesInfo.setEmployee2Hired(isEmployee2Hired);
                    userEmployeesInfo.setEmployee3Hired(isEmployee3Hired);

                    setUserOwnedUpgrades(card_lvl1, card_lvl2, card_lvl3, pc_lvl1, pc_lvl2, pc_lvl3, t_lvl1, t_lvl2,t_lvl3);
                    setUserOwnedItems(f1,f2,f3,p1,p2,p3,t1,t2,t3);

                    checkIfEmployeeIsHired();
                    checkIfDeskIsOwned();
                    checkIfFloorIsOwned();
                    checkIfPlantIsOwned();

                    checkIfComputerIsOwned();
                    checkIfGraphicCardIsOwned();
                    checkIfTabletIsOwned();

                    addImprovements();
                    addFurniture();

                    studio_level = userOwnedUpgrades.checkCurrentLvl();

                    numbOfFurniture.setText(String.valueOf(numberOfFurniture));
                    numbOfEmployees.setText(String.valueOf(numberOfEmployees));
                    numbOfImprovements.setText(String.valueOf(numberOfImprovements));
                    studioLevel.setText(String.valueOf(studio_level));

                    numbOfDesks.setText(String.valueOf(desksCounter));
                    numbOfPlants.setText(String.valueOf(plantsCounter));
                    numbOfFloors.setText(String.valueOf(floorsCounter));

                    numbOfComputers.setText(String.valueOf(computersCounter));
                    numbOfTablets.setText(String.valueOf(tabletsCounter));
                    numbOfGraphicCards.setText(String.valueOf(graphicCardsCounter));
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
        if(userEmployeesInfo.getEmployee1Hired() && userEmployeesInfo.getEmployee2Hired() && userEmployeesInfo.getEmployee3Hired()){
            numberOfEmployees=3;
        }
        else if(userEmployeesInfo.getEmployee1Hired() && userEmployeesInfo.getEmployee2Hired()){
            numberOfEmployees=2;
        }
        else if(userEmployeesInfo.getEmployee2Hired() && userEmployeesInfo.getEmployee3Hired()){
            numberOfEmployees=2;
        }
        else if(userEmployeesInfo.getEmployee1Hired() && userEmployeesInfo.getEmployee3Hired()){
            numberOfEmployees=2;
        }
        else if(userEmployeesInfo.getEmployee1Hired())
        {
            numberOfEmployees=1;
        }
        else if(userEmployeesInfo.getEmployee2Hired()){
            numberOfEmployees=1;
        }
        else if(userEmployeesInfo.getEmployee3Hired()){
            numberOfEmployees=1;
        }
        else {
            numberOfEmployees=0;
        }
    }

    private void addFurniture(){
        numberOfFurniture = plantsCounter + floorsCounter + desksCounter;
    }

    private void addImprovements(){
        numberOfImprovements = computersCounter + tabletsCounter + graphicCardsCounter;
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

    private void setUserOwnedUpgrades(int c1, int c2, int c3, int pc1, int pc2, int pc3, int tab1, int tab2, int tab3){
        userOwnedUpgrades.setCard_lvl1(c1);
        userOwnedUpgrades.setCard_lvl2(c2);
        userOwnedUpgrades.setCard_lvl3(c3);

        userOwnedUpgrades.setPc_lvl1(pc1);
        userOwnedUpgrades.setPc_lvl2(pc2);
        userOwnedUpgrades.setPc_lvl3(pc3);

        userOwnedUpgrades.setT_lvl1(tab1);
        userOwnedUpgrades.setT_lvl2(tab2);
        userOwnedUpgrades.setT_lvl3(tab3);
    }

    private void checkIfPlantIsOwned(){
        if(userOwnedItems.getP1()==1 && userOwnedItems.getP2()==1 && userOwnedItems.getP3()==1){
            plantsCounter =3;
        }
        else if(userOwnedItems.getP2()==1 && userOwnedItems.getP3()==1){
            plantsCounter =2;
        }
        else if(userOwnedItems.getP3()==1 && userOwnedItems.getP1()==1)
        {
            plantsCounter=2;
        }
        else if(userOwnedItems.getP1()==1 && userOwnedItems.getP2()==1)
        {
            plantsCounter=2;
        }
        else if(userOwnedItems.getP1()==1){
            plantsCounter=1;
        }
        else if(userOwnedItems.getP2()==1){
            plantsCounter=1;
        }
        else if(userOwnedItems.getP3()==1){
            plantsCounter=1;
        }
        else
        {
            plantsCounter=0;
        }

    }

    private void checkIfFloorIsOwned(){
        if(userOwnedItems.getF1()==1 && userOwnedItems.getF2()==1 && userOwnedItems.getF3()==1){
            floorsCounter =3;
        }
        else if(userOwnedItems.getF2()==1 && userOwnedItems.getF3()==1){
            floorsCounter =2;
        }
        else if(userOwnedItems.getF3()==1 && userOwnedItems.getF1()==1)
        {
            floorsCounter=2;
        }
        else if(userOwnedItems.getF1()==1 && userOwnedItems.getF2()==1)
        {
            floorsCounter=2;
        }
        else if(userOwnedItems.getF1()==1){
            floorsCounter=1;
        }
        else if(userOwnedItems.getF2()==1){
            floorsCounter=1;
        }
        else if(userOwnedItems.getF3()==1){
            floorsCounter=1;
        }
        else
        {
            floorsCounter=0;
        }
    }

    private void checkIfDeskIsOwned(){
        if(userOwnedItems.getT1()==1 && userOwnedItems.getT2()==1 && userOwnedItems.getT3()==1){
            desksCounter =3;
        }
        else if(userOwnedItems.getT2()==1 && userOwnedItems.getT3()==1){
            desksCounter =2;
        }
        else if(userOwnedItems.getT3()==1 && userOwnedItems.getT1()==1)
        {
            desksCounter=2;
        }
        else if(userOwnedItems.getT1()==1 && userOwnedItems.getT2()==1)
        {
            desksCounter=2;
        }
        else if(userOwnedItems.getT1()==1){
            desksCounter=1;
        }
        else if(userOwnedItems.getT2()==1){
            desksCounter=1;
        }
        else if(userOwnedItems.getT3()==1){
            desksCounter=1;
        }
        else
        {
            floorsCounter=0;
        }
    }

    private void checkIfComputerIsOwned(){
        if(userOwnedUpgrades.getPc_lvl1()==1 && userOwnedUpgrades.getPc_lvl2()==1 && userOwnedUpgrades.getPc_lvl3()==1){
            computersCounter =3;
        }
        else if(userOwnedUpgrades.getPc_lvl2()==1 && userOwnedUpgrades.getPc_lvl3()==1){
            computersCounter =2;
        }
        else if(userOwnedUpgrades.getPc_lvl3()==1 && userOwnedUpgrades.getPc_lvl1()==1)
        {
            computersCounter=2;
        }
        else if(userOwnedUpgrades.getPc_lvl1()==1 && userOwnedUpgrades.getPc_lvl2()==1)
        {
            computersCounter=2;
        }
        else if(userOwnedUpgrades.getPc_lvl1()==1){
            computersCounter=1;
        }
        else if(userOwnedUpgrades.getPc_lvl2()==1){
            computersCounter=1;
        }
        else if(userOwnedUpgrades.getPc_lvl3()==1){
            computersCounter=1;
        }
        else
        {
            computersCounter=0;
        }
    }

    private void checkIfTabletIsOwned(){
        if(userOwnedUpgrades.getT_lvl1()==1 && userOwnedUpgrades.getT_lvl2()==1 && userOwnedUpgrades.getT_lvl3()==1){
            tabletsCounter =3;
        }
        else if(userOwnedUpgrades.getT_lvl2()==1 && userOwnedUpgrades.getT_lvl3()==1){
            tabletsCounter =2;
        }
        else if(userOwnedUpgrades.getT_lvl3()==1 && userOwnedUpgrades.getT_lvl1()==1)
        {
            tabletsCounter=2;
        }
        else if(userOwnedUpgrades.getT_lvl1()==1 && userOwnedUpgrades.getT_lvl2()==1)
        {
            tabletsCounter=2;
        }
        else if(userOwnedUpgrades.getT_lvl1()==1){
            tabletsCounter=1;
        }
        else if(userOwnedUpgrades.getT_lvl2()==1){
            tabletsCounter=1;
        }
        else if(userOwnedUpgrades.getT_lvl3()==1){
            tabletsCounter=1;
        }
        else
        {
            tabletsCounter=0;
        }
    }

    private void checkIfGraphicCardIsOwned(){
        if(userOwnedUpgrades.getCard_lvl1()==1 && userOwnedUpgrades.getCard_lvl2()==1 && userOwnedUpgrades.getCard_lvl3()==1){
            graphicCardsCounter =3;
        }
        else if(userOwnedUpgrades.getCard_lvl2()==1 && userOwnedUpgrades.getCard_lvl3()==1){
            graphicCardsCounter =2;
        }
        else if(userOwnedUpgrades.getCard_lvl3()==1 && userOwnedUpgrades.getCard_lvl1()==1)
        {
            graphicCardsCounter=2;
        }
        else if(userOwnedUpgrades.getCard_lvl1()==1 && userOwnedUpgrades.getCard_lvl2()==1)
        {
            graphicCardsCounter=2;
        }
        else if(userOwnedUpgrades.getCard_lvl1()==1){
            graphicCardsCounter=1;
        }
        else if(userOwnedUpgrades.getCard_lvl2()==1){
            graphicCardsCounter=1;
        }
        else if(userOwnedUpgrades.getCard_lvl3()==1){
            graphicCardsCounter=1;
        }
        else
        {
            graphicCardsCounter=0;
        }
    }
}