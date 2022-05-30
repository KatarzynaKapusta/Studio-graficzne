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
<<<<<<< HEAD
import android.widget.TextView;
=======
>>>>>>> bfcbbad656dd6bfb82ec36cecbeec6b582d1e70c
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

public class floor_fragment extends Fragment {
    private static final String FURNITURE = "Furniture", USERS = "Users";
    private final String TAG = this.getClass().getName().toUpperCase();
    private Button buyFloor1Button, buyFloor2Button, buyFloor3Button;
<<<<<<< HEAD
    private Button previewFloor1Button, previewFloor2Button, previewFloor3Button;
    private TextView priceFloor1TxtView, priceFloor2TxtView, priceFloor3TxtView;
=======
>>>>>>> bfcbbad656dd6bfb82ec36cecbeec6b582d1e70c

    // Database variables
    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;
    private UserOwnedItems userOwnedItems;
    private List<Floor> floors = new ArrayList<>();
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
        View view = inflater.inflate(R.layout.fragment_floor_fragment, container, false);
        DatabaseReference userRef = rootRef.child(USERS);
        DatabaseReference furnitureRef = rootRef.child(FURNITURE).child("Floors");
        Log.v("USERID", userRef.getKey());
        readFromDatabase(currentUser, userRef, furnitureRef);

<<<<<<< HEAD
        priceFloor1TxtView = view.findViewById(R.id.floor1PriceTextView);
        priceFloor2TxtView = view.findViewById(R.id.floor2PriceTextView);
        priceFloor3TxtView = view.findViewById(R.id.floor3PriceTextView);

=======
>>>>>>> bfcbbad656dd6bfb82ec36cecbeec6b582d1e70c
        buyFloor1Button = view.findViewById(R.id.floor1BuyButton);
        buyFloor1Button.setOnClickListener(v -> {
            performBuyItem(floors.get(0));
        });
        buyFloor2Button = view.findViewById(R.id.floor2BuyButton);
        buyFloor2Button.setOnClickListener(v -> {
            performBuyItem(floors.get(1));
        });
        buyFloor3Button = view.findViewById(R.id.floor3BuyButton);
        buyFloor3Button.setOnClickListener(v -> {
            performBuyItem(floors.get(2));
        });

<<<<<<< HEAD
        // Preview buttons
        previewFloor1Button = view.findViewById(R.id.floor1PreviewButton);
        previewFloor1Button.setOnClickListener(v -> {
            performViewItem(floors.get(0));
        });
        previewFloor2Button = view.findViewById(R.id.floor2PreviewButton);
        previewFloor2Button.setOnClickListener(v -> {
            performViewItem(floors.get(1));

        });
        previewFloor3Button = view.findViewById(R.id.floor3PreviewButton);
        previewFloor3Button.setOnClickListener(v -> {
            performViewItem(floors.get(2));
        });
        
=======
>>>>>>> bfcbbad656dd6bfb82ec36cecbeec6b582d1e70c
        // Reading information from the database if user is logged


        return view;
    } //OnCreate() End

<<<<<<< HEAD
    private void performViewItem(Floor p) {
        if(p.getId().equals("f1"))
            userOwnedItems.setF1(ItemStatus.PREVIEW.value);

        if(p.getId().equals("f2"))
            userOwnedItems.setF2(ItemStatus.PREVIEW.value);

        if(p.getId().equals("f3"))
            userOwnedItems.setF3(ItemStatus.PREVIEW.value);
        updateItemStatus();

        Intent intent = new Intent(getActivity(), activity_studio_preview.class);
        startActivity(intent);

    }
    
=======
>>>>>>> bfcbbad656dd6bfb82ec36cecbeec6b582d1e70c
    private void performBuyItem(Floor f) {
        if(ableToBuy(f)) {
            if(f.getId().equals("f1"))
                userOwnedItems.setF1(ItemStatus.OWNED.value);

            if(f.getId().equals("f2"))
                userOwnedItems.setF2(ItemStatus.OWNED.value);

            if(f.getId().equals("f3"))
                userOwnedItems.setF3(ItemStatus.OWNED.value);

            updateItemStatus();
            ownedMoney-=f.getPrice();
            updateUserMoney();
            Toast.makeText(getActivity(), "Kupiono przedmiot",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), "Nie kupiono",
                    Toast.LENGTH_SHORT).show();
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

                            userOwnedItems = new UserOwnedItems(m.get("f1").intValue(), m.get("f2").intValue(),m.get("f3").intValue(),m.get("p1").intValue(),m.get("p2").intValue(),m.get("p3").intValue(),m.get("t1").intValue(),m.get("t2").intValue(),m.get("t3").intValue());
                            break;
                        }
                    }

                    // Read from "Furniture" branch in db
                    furnitureRef.addValueEventListener(new ValueEventListener() {
                        int itemLevel,itemPrice;
                        String itemId;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
<<<<<<< HEAD
                            floors.clear();
=======
>>>>>>> bfcbbad656dd6bfb82ec36cecbeec6b582d1e70c
                            for (DataSnapshot keyId : dataSnapshot.getChildren()) {
                                    itemId = keyId.child("Id").getValue(String.class);
                                    itemLevel = keyId.child("Level").getValue(Integer.class);
                                    itemPrice = keyId.child("Price").getValue(Integer.class);

                                    Floor f = new Floor(itemId, itemLevel, itemPrice);
                                    floors.add(f);
<<<<<<< HEAD
                                    setPriceTag(floors.size(),itemPrice);
                                    enableButtons(userOwnedItems);
=======
>>>>>>> bfcbbad656dd6bfb82ec36cecbeec6b582d1e70c

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

<<<<<<< HEAD
    private void enableButtons(UserOwnedItems uoi) {
        if(uoi.getF1() == ItemStatus.NOTOWNED.value)
        {
            buyFloor1Button.setEnabled(true);
            previewFloor1Button.setEnabled(true);
        }
        else {
            buyFloor1Button.setEnabled(false);
            previewFloor1Button.setEnabled(false);
        }

        if(uoi.getF2() == ItemStatus.NOTOWNED.value) {
            buyFloor2Button.setEnabled(true);
            previewFloor2Button.setEnabled(true);
        }
        else {
            buyFloor2Button.setEnabled(false);
            previewFloor2Button.setEnabled(false);
        }

        if(uoi.getF3() == ItemStatus.NOTOWNED.value) {
            buyFloor3Button.setEnabled(true);
            previewFloor3Button.setEnabled(true);
        }
        else {
            buyFloor3Button.setEnabled(false);
            previewFloor3Button.setEnabled(false);
        }
    }

    private void setPriceTag(int size, int itemPrice) {
        if(size == 1)
            priceFloor1TxtView.setText(String.valueOf(itemPrice));

        else if(size == 2)
            priceFloor2TxtView.setText(String.valueOf(itemPrice));

        else if(size == 3)
            priceFloor3TxtView.setText(String.valueOf(itemPrice));

    }

=======
>>>>>>> bfcbbad656dd6bfb82ec36cecbeec6b582d1e70c
    private void updateUserMoney() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
            rootRef = database.getReference("Users");
            databaseOperations.updateDatabase(uid,rootRef,"money",ownedMoney);
        }
    }

    private boolean ableToBuy(Floor f) {
        if(f.getPrice()<=ownedMoney)
        {
<<<<<<< HEAD
            if(f.getId().equals("f1") && !(userOwnedItems.getF1()==ItemStatus.OWNED.value || userOwnedItems.getF1()==ItemStatus.HIDDENOWNED.value))
                return true;

            if(f.getId().equals("f2") && !(userOwnedItems.getF2()==ItemStatus.OWNED.value || userOwnedItems.getF2()==ItemStatus.HIDDENOWNED.value))
                return true;

            if(f.getId().equals("f3") && !(userOwnedItems.getF3()==ItemStatus.OWNED.value || userOwnedItems.getF3()==ItemStatus.HIDDENOWNED.value))
=======
            if(f.getId().equals("f1") && userOwnedItems.getF1()!=ItemStatus.OWNED.value)
                return true;

            if(f.getId().equals("f2") && userOwnedItems.getF2()!=ItemStatus.OWNED.value)
                return true;

            if(f.getId().equals("f3") && userOwnedItems.getF3()!=ItemStatus.OWNED.value)
>>>>>>> bfcbbad656dd6bfb82ec36cecbeec6b582d1e70c
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