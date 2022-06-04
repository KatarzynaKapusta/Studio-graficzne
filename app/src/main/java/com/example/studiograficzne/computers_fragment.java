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

public class computers_fragment extends Fragment {
    private static final String UPGRADES = "Upgrades", USERS = "Users";
    private final String TAG = this.getClass().getName().toUpperCase();
    private Button buyComputer1Button, buyComputer2Button, buyComputer3Button;
    private TextView priceComputer1TxtView, priceComputer2TxtView, priceComputer3TxtView;
    private TextView lvlComputer1TxtView, lvlComputer2TxtView, lvlComputer3TxtView;

    // Database variables
    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;
    private UserOwnedUpgrades userOwnedUpgrades;
    private List<Computer> computers = new ArrayList<>();
    double ownedMoney, ownedResources;
    Double level;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Checking if user is logged or not and getting his email
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email = currentUser.getEmail();
        }

        View view = inflater.inflate(R.layout.fragment_computers_fragment, container, false);
        DatabaseReference userRef = rootRef.child(USERS);
        DatabaseReference upgradesRef = rootRef.child(UPGRADES).child("Computers");
        Log.v("USERID", userRef.getKey());
        readFromDatabase(currentUser, userRef, upgradesRef);

        priceComputer1TxtView = view.findViewById(R.id.computer1PriceTextView);
        priceComputer2TxtView = view.findViewById(R.id.computer2PriceTextView);
        priceComputer3TxtView = view.findViewById(R.id.computer3PriceTextView);

        lvlComputer1TxtView = view.findViewById(R.id.computer1LevelTextView);
        lvlComputer2TxtView = view.findViewById(R.id.computer2LevelTextView);
        lvlComputer3TxtView = view.findViewById(R.id.computer3LevelTextView);

        buyComputer1Button = view.findViewById(R.id.computer1BuyButton);
        buyComputer1Button.setOnClickListener(v ->
                performBuyItem(computers.get(0))
        );
        buyComputer2Button = view.findViewById(R.id.computer2BuyButton);
        buyComputer2Button.setOnClickListener(v -> {
            performBuyItem(computers.get(1));
        });
        buyComputer3Button = view.findViewById(R.id.computer3BuyButton);
        buyComputer3Button.setOnClickListener(v -> {
            performBuyItem(computers.get(2));
        });

        // Inflate the layout for this fragment
        return view;

    } // OnCreate() end

    private void performViewItem(Computer c) {
        if(c.getId().equals("pc_lvl1"))
            userOwnedUpgrades.setPc_lvl1(ItemStatus.PREVIEW.value);

        if(c.getId().equals("pc_lvl2"))
            userOwnedUpgrades.setPc_lvl2(ItemStatus.PREVIEW.value);

        if(c.getId().equals("pc_lvl3"))
            userOwnedUpgrades.setPc_lvl3(ItemStatus.PREVIEW.value);
        updateItemStatus();

        Intent intent = new Intent(getActivity(), activity_studio_preview.class);
        startActivity(intent);

    }

    private void performBuyItem(Computer c) {
        if(ableToBuy(c)) {
            if(c.getId().equals("pc_lvl1"))
                userOwnedUpgrades.setPc_lvl1(ItemStatus.OWNED.value);

            if(c.getId().equals("pc_lvl2"))
                userOwnedUpgrades.setPc_lvl2(ItemStatus.OWNED.value);

            if(c.getId().equals("pc_lvl3"))
                userOwnedUpgrades.setPc_lvl3(ItemStatus.OWNED.value);

            updateItemStatus();
            ownedMoney-=c.getPrice();
            ownedResources-=c.getResources();
            updateUserMoneyAndResources();
            Toast.makeText(getActivity(), "Kupiono ulepszenie",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), "Nie kupiono",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void readFromDatabase(FirebaseUser currentUser, DatabaseReference userRef, DatabaseReference upgradesRef) {
        if (currentUser != null) {
            // Read from "Users" branch in db
            userRef.addValueEventListener(new ValueEventListener() {
                Double money, resources, experience;
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

                            Map<String,Long> m = (Map)keyId.child("UserOwnedUpgrades").getValue();

                            userOwnedUpgrades = new UserOwnedUpgrades(m.get("card_lvl1").intValue(), m.get("card_lvl2").intValue(),m.get("card_lvl3").intValue(),m.get("pc_lvl1").intValue(),m.get("pc_lvl2").intValue(),m.get("pc_lvl3").intValue(),m.get("t_lvl1").intValue(),m.get("t_lvl2").intValue(),m.get("t_lvl3").intValue());
                            break;
                        }
                    }

                    // Read from "Upgrades" branch in db
                    upgradesRef.addValueEventListener(new ValueEventListener() {
                        int itemLevel,itemPrice, itemResources;
                        String itemId;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            computers.clear();
                            for (DataSnapshot keyId : dataSnapshot.getChildren()) {
                                itemId = keyId.child("Id").getValue(String.class);
                                itemLevel = keyId.child("Level").getValue(Integer.class);
                                itemPrice = keyId.child("Price").getValue(Integer.class);
                                itemResources = keyId.child("Resources").getValue(Integer.class);

                                Computer c = new Computer(itemId, itemLevel, itemPrice, itemResources);
                                computers.add(c);
                                setPriceTag(computers.size(),itemPrice);
                                setResourcesTag(computers.size(),itemResources);
                                enableButtons(userOwnedUpgrades);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    }); // End of reading from "Furniture" branch

                    ownedMoney = money;
                    ownedResources = resources;
                } // End of reading from "Users" branch

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
    }

    private void enableButtons(UserOwnedUpgrades uoi) {
        if(uoi.getPc_lvl1() == ItemStatus.NOTOWNED.value && level >=3 )
        {
            buyComputer1Button.setEnabled(true);
        }
        else {
            buyComputer1Button.setEnabled(false);
        }

        if(uoi.getPc_lvl2() == ItemStatus.NOTOWNED.value && level >=5) {
            buyComputer2Button.setEnabled(true);
        }
        else {
            buyComputer2Button.setEnabled(false);
        }

        if(uoi.getPc_lvl3() == ItemStatus.NOTOWNED.value && level ==10) {
            buyComputer3Button.setEnabled(true);
        }
        else {
            buyComputer3Button.setEnabled(false);
        }
    }

    private void updateUserMoneyAndResources() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/");
            rootRef = database.getReference("Users");
            databaseOperations.updateDatabase(uid,rootRef,"money",ownedMoney);
            databaseOperations.updateDatabase(uid,rootRef,"resources",ownedResources);
        }
    }

    private void setPriceTag(int size, int itemPrice) {
        if(size == 1)
            priceComputer1TxtView.setText(String.valueOf(itemPrice));

        else if(size == 2)
            priceComputer2TxtView.setText(String.valueOf(itemPrice));

        else if(size == 3)
            priceComputer3TxtView.setText(String.valueOf(itemPrice));

    }

    private void setResourcesTag(int size, int itemResources) {
        if(size == 1)
            lvlComputer1TxtView.setText(String.valueOf(itemResources));

        else if(size == 2)
            lvlComputer2TxtView.setText(String.valueOf(itemResources));

        else if(size == 3)
            lvlComputer3TxtView.setText(String.valueOf(itemResources));

    }

    private boolean ableToBuy(Computer t) {
        if(t.getPrice()<=ownedMoney && t.getResources()<=ownedResources)
        {
            if(t.getId().equals("pc_lvl1") && !(userOwnedUpgrades.getPc_lvl1()==ItemStatus.OWNED.value || userOwnedUpgrades.getPc_lvl1()==ItemStatus.HIDDENOWNED.value))
                return true;

            if(t.getId().equals("pc_lvl2") && !(userOwnedUpgrades.getPc_lvl2()==ItemStatus.OWNED.value || userOwnedUpgrades.getPc_lvl2()==ItemStatus.HIDDENOWNED.value))
                return true;

            if(t.getId().equals("pc_lvl3") && !(userOwnedUpgrades.getPc_lvl3()==ItemStatus.OWNED.value || userOwnedUpgrades.getPc_lvl3()==ItemStatus.HIDDENOWNED.value))
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
            childUpdates.put("UserOwnedUpgrades",userOwnedUpgrades);

            rootRef = database.getReference("Users");
            rootRef.child(uid).updateChildren(childUpdates);
        }
    }
}