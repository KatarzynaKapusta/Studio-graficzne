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

public class tables_fragment extends Fragment {
    private static final String FURNITURE = "Furniture", USERS = "Users";
    private final String TAG = this.getClass().getName().toUpperCase();
    private Button buyTable1Button, buyTable2Button, buyTable3Button;
    private Button previewTable1Button, previewTable2Button, previewTable3Button;
    private TextView priceTable1TxtView, priceTable2TxtView, priceTable3TxtView;

    // Database variables
    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;
    private UserOwnedItems userOwnedItems;
    private List<Table> tables = new ArrayList<>();
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

        View view = inflater.inflate(R.layout.fragment_tables_fragment, container, false);
        DatabaseReference userRef = rootRef.child(USERS);
        DatabaseReference furnitureRef = rootRef.child(FURNITURE).child("Tables");
        Log.v("USERID", userRef.getKey());
        readFromDatabase(currentUser, userRef, furnitureRef);

        priceTable1TxtView = view.findViewById(R.id.table1PriceTextView);
        priceTable2TxtView = view.findViewById(R.id.table2PriceTextView);
        priceTable3TxtView = view.findViewById(R.id.table3PriceTextView);

        buyTable1Button = view.findViewById(R.id.table1BuyButton);
        buyTable1Button.setOnClickListener(v ->
            performBuyItem(tables.get(0))
        );
        buyTable2Button = view.findViewById(R.id.table2BuyButton);
        buyTable2Button.setOnClickListener(v -> {
            performBuyItem(tables.get(1));
        });
        buyTable3Button = view.findViewById(R.id.table3BuyButton);
        buyTable3Button.setOnClickListener(v -> {
            performBuyItem(tables.get(2));
        });

        // Preview buttons
        previewTable1Button = view.findViewById(R.id.table1PreviewButton);
        previewTable1Button.setOnClickListener(v -> {
            performViewItem(tables.get(0));
        });
        previewTable2Button = view.findViewById(R.id.table2PreviewButton);
        previewTable2Button.setOnClickListener(v -> {
            performViewItem(tables.get(1));

        });
        previewTable3Button = view.findViewById(R.id.table3PreviewButton);
        previewTable3Button.setOnClickListener(v -> {
            performViewItem(tables.get(2));
        });

        // Reading information from the database if user is logged


        return view;
    } //OnCreate() End

    private void performViewItem(Table t) {
        if(t.getId().equals("t1"))
            userOwnedItems.setT1(ItemStatus.PREVIEW.value);

        if(t.getId().equals("t2"))
            userOwnedItems.setT2(ItemStatus.PREVIEW.value);

        if(t.getId().equals("t3"))
            userOwnedItems.setT3(ItemStatus.PREVIEW.value);
        updateItemStatus();

        Intent intent = new Intent(getActivity(), activity_studio_preview.class);
        startActivity(intent);

    }
    
    private void performBuyItem(Table t) {
        if(ableToBuy(t)) {
            if(t.getId().equals("t1"))
                userOwnedItems.setT1(ItemStatus.OWNED.value);

            if(t.getId().equals("t2"))
                userOwnedItems.setT2(ItemStatus.OWNED.value);

            if(t.getId().equals("t3"))
                userOwnedItems.setT3(ItemStatus.OWNED.value);

            updateItemStatus();
            ownedMoney-=t.getPrice();
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
                            tables.clear();
                            for (DataSnapshot keyId : dataSnapshot.getChildren()) {
                                itemId = keyId.child("Id").getValue(String.class);
                                itemLevel = keyId.child("Level").getValue(Integer.class);
                                itemPrice = keyId.child("Price").getValue(Integer.class);

                                Table t = new Table(itemId, itemLevel, itemPrice);
                                tables.add(t);
                                setPriceTag(tables.size(),itemPrice);
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

    private void enableButtons(UserOwnedItems uoi) {
        if(uoi.getT1() == ItemStatus.NOTOWNED.value)
        {
            buyTable1Button.setEnabled(true);
            previewTable1Button.setEnabled(true);
        }
        else {
            buyTable1Button.setEnabled(false);
            previewTable1Button.setEnabled(false);
        }

        if(uoi.getT2() == ItemStatus.NOTOWNED.value) {
            buyTable2Button.setEnabled(true);
            previewTable2Button.setEnabled(true);
        }
        else {
            buyTable2Button.setEnabled(false);
            previewTable2Button.setEnabled(false);
        }

        if(uoi.getT3() == ItemStatus.NOTOWNED.value) {
            buyTable3Button.setEnabled(true);
            previewTable3Button.setEnabled(true);
        }
        else {
            buyTable3Button.setEnabled(false);
            previewTable3Button.setEnabled(false);
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
            priceTable1TxtView.setText(String.valueOf(itemPrice));

        else if(size == 2)
            priceTable2TxtView.setText(String.valueOf(itemPrice));

        else if(size == 3)
            priceTable3TxtView.setText(String.valueOf(itemPrice));

    }

    private boolean ableToBuy(Table t) {
        if(t.getPrice()<=ownedMoney)
        {
            if(t.getId().equals("t1") && !(userOwnedItems.getT1()==ItemStatus.OWNED.value || userOwnedItems.getT1()==ItemStatus.HIDDENOWNED.value))
                return true;

            if(t.getId().equals("t2") && !(userOwnedItems.getT2()==ItemStatus.OWNED.value || userOwnedItems.getT2()==ItemStatus.HIDDENOWNED.value))
                return true;

            if(t.getId().equals("t3") && !(userOwnedItems.getT3()==ItemStatus.OWNED.value || userOwnedItems.getT3()==ItemStatus.HIDDENOWNED.value))
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