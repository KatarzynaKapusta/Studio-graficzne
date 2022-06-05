package com.example.studiograficzne;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class plants_fragment extends Fragment {
    private static final String FURNITURE = "Furniture", USERS = "Users";
    private final String TAG = this.getClass().getName().toUpperCase();
    private Button buyPlant1Button, buyPlant2Button, buyPlant3Button;
    private Button previewPlant1Button, previewPlant2Button, previewPlant3Button;
    private TextView pricePlant1TxtView, pricePlant2TxtView, pricePlant3TxtView;

    // Database variables
    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;
    private UserOwnedItems userOwnedItems;
    private UserOwnedUpgrades userOwnedUpgrades;
    private List<Plant> plants = new ArrayList<>();
    double ownedMoney;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Checking if user is logged or not and getting his email
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email = currentUser.getEmail();
        }
        View view = inflater.inflate(R.layout.fragment_plants_fragment, container, false);
        DatabaseReference userRef = rootRef.child(USERS);
        DatabaseReference furnitureRef = rootRef.child(FURNITURE).child("Plants");
        Log.v("USERID", userRef.getKey());
        readFromDatabase(currentUser, userRef, furnitureRef);

        pricePlant1TxtView = view.findViewById(R.id.plant1PriceTextView);
        pricePlant2TxtView = view.findViewById(R.id.plant2PriceTextView);
        pricePlant3TxtView = view.findViewById(R.id.plant3PriceTextView);

        // Buy buttons
        buyPlant1Button = view.findViewById(R.id.plant1BuyButton);
        buyPlant1Button.setOnClickListener(v -> {
            performBuyItem(plants.get(0));
        });
        buyPlant2Button = view.findViewById(R.id.plant2BuyButton);
        buyPlant2Button.setOnClickListener(v -> {
            performBuyItem(plants.get(1));
        });
        buyPlant3Button = view.findViewById(R.id.plant3BuyButton);
        buyPlant3Button.setOnClickListener(v -> {
            performBuyItem(plants.get(2));
        });

        // Preview buttons
        previewPlant1Button = view.findViewById(R.id.plant1PreviewButton);
        previewPlant1Button.setOnClickListener(v -> {
            performViewItem(plants.get(0));
        });
        previewPlant2Button = view.findViewById(R.id.plant2PreviewButton);
        previewPlant2Button.setOnClickListener(v -> {
            performViewItem(plants.get(1));

        });
        previewPlant3Button = view.findViewById(R.id.plant3PreviewButton);
        previewPlant3Button.setOnClickListener(v -> {
            performViewItem(plants.get(2));
        });

        // Reading information from the database if user is logged


        return view;
    } //OnCreate() End

    private void performViewItem(Plant p) {
        if(p.getId().equals("p1"))
            userOwnedItems.setP1(ItemStatus.PREVIEW.value);

        if(p.getId().equals("p2"))
            userOwnedItems.setP2(ItemStatus.PREVIEW.value);

        if(p.getId().equals("p3"))
            userOwnedItems.setP3(ItemStatus.PREVIEW.value);
        updateItemStatus();

        Intent intent = new Intent(getActivity(), activity_studio_preview.class);
        startActivity(intent);

    }

    private void performBuyItem(Plant p) {
        if(ableToBuy(p)) {
            if(p.getId().equals("p1"))
                userOwnedItems.setP1(ItemStatus.OWNED.value);

            if(p.getId().equals("p2"))
                userOwnedItems.setP2(ItemStatus.OWNED.value);

            if(p.getId().equals("p3"))
                userOwnedItems.setP3(ItemStatus.OWNED.value);

            updateItemStatus();
            ownedMoney-=p.getPrice();
            updateUserMoney();
            Toast.makeText(getActivity(), "Kupiono przedmiot",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), "Nie kupiono",
                    Toast.LENGTH_SHORT).show();
        }

        if(isGameOver()) {
            Intent intent = new Intent(getActivity(), activity_endOfGame.class);
            startActivity(intent);
        }
    }

    private void readFromDatabase(FirebaseUser currentUser, DatabaseReference userRef, DatabaseReference furnitureRef) {
        if (currentUser != null) {
            // Read from "Users" branch in db
            userRef.addValueEventListener(new ValueEventListener() {
                Double money, level, resources, experience;
                String moneyString, resourcesString, experienceString;
                Integer numStatus;

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

                    // Read from "Furniture" branch in db
                    furnitureRef.addValueEventListener(new ValueEventListener() {
                        int itemLevel,itemPrice;
                        String itemId;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            plants.clear();
                            for (DataSnapshot keyId : dataSnapshot.getChildren()) {
                                itemId = keyId.child("Id").getValue(String.class);
                                itemLevel = keyId.child("Level").getValue(Integer.class);
                                itemPrice = keyId.child("Price").getValue(Integer.class);

                                Plant p = new Plant(itemId, itemLevel, itemPrice);
                                plants.add(p);
                                setPriceTag(plants.size(),itemPrice);
                                enableButtons(userOwnedItems);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    }); // End of reading from "Furniture" branch

                    ownedMoney = money;
                } // End of reading from "Users" branch

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
    }

    private boolean isGameOver()
    {
        if(userOwnedUpgrades.checkCurrentLvl() == 3 && userOwnedItems.isEverythingOwned())
            return true;
        return false;
    }

    private void enableButtons(UserOwnedItems uoi) {
        if(uoi.getP1() == ItemStatus.NOTOWNED.value)
        {
            buyPlant1Button.setEnabled(true);
            previewPlant1Button.setEnabled(true);
        }
        else {
            buyPlant1Button.setEnabled(false);
            previewPlant1Button.setEnabled(false);
        }

        if(uoi.getP2() == ItemStatus.NOTOWNED.value) {
            buyPlant2Button.setEnabled(true);
            previewPlant2Button.setEnabled(true);
        }
        else {
            buyPlant2Button.setEnabled(false);
            previewPlant2Button.setEnabled(false);
        }

        if(uoi.getP3() == ItemStatus.NOTOWNED.value) {
            buyPlant3Button.setEnabled(true);
            previewPlant3Button.setEnabled(true);
        }
        else {
            buyPlant3Button.setEnabled(false);
            previewPlant3Button.setEnabled(false);
        }
    }

    private void updateUserMoney() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
            rootRef = database.getReference("Users");
            databaseOperations.updateDatabase(uid,rootRef,"money",ownedMoney);
        }
    }

    private void setPriceTag(int size, int itemPrice) {
        if(size == 1)
            pricePlant1TxtView.setText(String.valueOf(itemPrice));

        else if(size == 2)
            pricePlant2TxtView.setText(String.valueOf(itemPrice));

        else if(size == 3)
            pricePlant3TxtView.setText(String.valueOf(itemPrice));

    }

    private boolean ableToBuy(Plant p) {
        if(p.getPrice()<=ownedMoney)
        {
            if(p.getId().equals("p1") && !(userOwnedItems.getP1()==ItemStatus.OWNED.value || userOwnedItems.getP1()==ItemStatus.HIDDENOWNED.value))
                return true;

            if(p.getId().equals("p2") && !(userOwnedItems.getP2()==ItemStatus.OWNED.value || userOwnedItems.getP2()==ItemStatus.HIDDENOWNED.value))
                return true;

            if(p.getId().equals("p3") && !(userOwnedItems.getP3()==ItemStatus.OWNED.value || userOwnedItems.getP3()==ItemStatus.HIDDENOWNED.value))
                return true;
        }
        return false;
    }

    private void updateItemStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("UserOwnedItems",userOwnedItems);

            rootRef = database.getReference("Users");
            rootRef.child(uid).updateChildren(childUpdates);
        }
    }
}