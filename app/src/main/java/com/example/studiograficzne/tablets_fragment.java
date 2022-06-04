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

public class tablets_fragment extends Fragment {
    private static final String UPGRADES = "Upgrades", USERS = "Users";
    private final String TAG = this.getClass().getName().toUpperCase();
    private Button buyTablet1Button, buyTablet2Button, buyTablet3Button;
    private Button previewTablet1Button, previewTablet2Button, previewTablet3Button;
    private TextView priceTablet1TxtView, priceTablet2TxtView, priceTablet3TxtView;
    private TextView lvlTablet1TxtView, lvlTablet2TxtView, lvlTablet3TxtView;

    // Database variables
    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;
    private UserOwnedUpgrades userOwnedUpgrades;
    private List<Tablet> tablets = new ArrayList<>();
    double ownedMoney, ownedResources;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Checking if user is logged or not and getting his email
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email = currentUser.getEmail();
        }

        View view = inflater.inflate(R.layout.fragment_tablets_fragment, container, false);
        DatabaseReference userRef = rootRef.child(USERS);
        DatabaseReference upgradesRef = rootRef.child(UPGRADES).child("Tablets");
        Log.v("USERID", userRef.getKey());
        readFromDatabase(currentUser, userRef, upgradesRef);

        priceTablet1TxtView = view.findViewById(R.id.tablet1PriceTextView);
        priceTablet2TxtView = view.findViewById(R.id.tablet2PriceTextView);
        priceTablet3TxtView = view.findViewById(R.id.tablet3PriceTextView);

        lvlTablet1TxtView = view.findViewById(R.id.tablet1LevelTextView);
        lvlTablet2TxtView = view.findViewById(R.id.tablet2LevelTextView);
        lvlTablet3TxtView = view.findViewById(R.id.tablet3LevelTextView);

        buyTablet1Button = view.findViewById(R.id.tablet1BuyButton);
        buyTablet1Button.setOnClickListener(v ->
                performBuyItem(tablets.get(0))
        );
        buyTablet2Button = view.findViewById(R.id.tablet2BuyButton);
        buyTablet2Button.setOnClickListener(v -> {
            performBuyItem(tablets.get(1));
        });
        buyTablet3Button = view.findViewById(R.id.tablet3BuyButton);
        buyTablet3Button.setOnClickListener(v -> {
            performBuyItem(tablets.get(2));
        });

        // Preview buttons
        previewTablet1Button = view.findViewById(R.id.tablet1PreviewButton);
        previewTablet1Button.setOnClickListener(v -> {
            performViewItem(tablets.get(0));
        });
        previewTablet2Button = view.findViewById(R.id.tablet2PreviewButton);
        previewTablet2Button.setOnClickListener(v -> {
            performViewItem(tablets.get(1));

        });
        previewTablet3Button = view.findViewById(R.id.tablet3PreviewButton);
        previewTablet3Button.setOnClickListener(v -> {
            performViewItem(tablets.get(2));
        });


        // Inflate the layout for this fragment
        return view;

    } // OnCreate() end

    private void performViewItem(Tablet t) {
        if(t.getId().equals("t_lvl1"))
            userOwnedUpgrades.setT_lvl1(ItemStatus.PREVIEW.value);

        if(t.getId().equals("t_lvl2"))
            userOwnedUpgrades.setT_lvl2(ItemStatus.PREVIEW.value);

        if(t.getId().equals("t_lvl3"))
            userOwnedUpgrades.setT_lvl3(ItemStatus.PREVIEW.value);
        updateItemStatus();

        Intent intent = new Intent(getActivity(), activity_studio_preview.class);
        startActivity(intent);

    }

    private void performBuyItem(Tablet t) {
        if(ableToBuy(t)) {
            if(t.getId().equals("t_lvl1"))
                userOwnedUpgrades.setT_lvl1(ItemStatus.OWNED.value);

            if(t.getId().equals("t_lvl2"))
                userOwnedUpgrades.setT_lvl2(ItemStatus.OWNED.value);

            if(t.getId().equals("t_lvl3"))
                userOwnedUpgrades.setT_lvl3(ItemStatus.OWNED.value);

            updateItemStatus();
            ownedMoney-=t.getPrice();
            ownedResources-=t.getResources();
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
                            tablets.clear();
                            for (DataSnapshot keyId : dataSnapshot.getChildren()) {
                                itemId = keyId.child("Id").getValue(String.class);
                                itemLevel = keyId.child("Level").getValue(Integer.class);
                                itemPrice = keyId.child("Price").getValue(Integer.class);
                                itemResources = keyId.child("Resources").getValue(Integer.class);

                                Tablet t = new Tablet(itemId, itemLevel, itemPrice, itemResources);
                                tablets.add(t);
                                setPriceTag(tablets.size(),itemPrice);
                                setResourcesTag(tablets.size(),itemResources);
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
        if(uoi.getT_lvl1() == ItemStatus.NOTOWNED.value)
        {
            buyTablet1Button.setEnabled(true);
            previewTablet1Button.setEnabled(true);
        }
        else {
            buyTablet1Button.setEnabled(false);
            previewTablet1Button.setEnabled(false);
        }

        if(uoi.getT_lvl2() == ItemStatus.NOTOWNED.value) {
            buyTablet2Button.setEnabled(true);
            previewTablet2Button.setEnabled(true);
        }
        else {
            buyTablet2Button.setEnabled(false);
            previewTablet2Button.setEnabled(false);
        }

        if(uoi.getT_lvl3() == ItemStatus.NOTOWNED.value) {
            buyTablet3Button.setEnabled(true);
            previewTablet3Button.setEnabled(true);
        }
        else {
            buyTablet3Button.setEnabled(false);
            previewTablet3Button.setEnabled(false);
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
            priceTablet1TxtView.setText(String.valueOf(itemPrice));

        else if(size == 2)
            priceTablet2TxtView.setText(String.valueOf(itemPrice));

        else if(size == 3)
            priceTablet3TxtView.setText(String.valueOf(itemPrice));

    }

    private void setResourcesTag(int size, int itemResources) {
        if(size == 1)
            lvlTablet1TxtView.setText(String.valueOf(itemResources));

        else if(size == 2)
            lvlTablet2TxtView.setText(String.valueOf(itemResources));

        else if(size == 3)
            lvlTablet3TxtView.setText(String.valueOf(itemResources));

    }

    private boolean ableToBuy(Tablet t) {
        if(t.getPrice()<=ownedMoney && t.getResources()<=ownedResources)
        {
            if(t.getId().equals("t_lvl1") && !(userOwnedUpgrades.getT_lvl1()==ItemStatus.OWNED.value || userOwnedUpgrades.getT_lvl1()==ItemStatus.HIDDENOWNED.value))
                return true;

            if(t.getId().equals("t_lvl2") && !(userOwnedUpgrades.getT_lvl2()==ItemStatus.OWNED.value || userOwnedUpgrades.getT_lvl2()==ItemStatus.HIDDENOWNED.value))
                return true;

            if(t.getId().equals("t_lvl3") && !(userOwnedUpgrades.getT_lvl3()==ItemStatus.OWNED.value || userOwnedUpgrades.getT_lvl3()==ItemStatus.HIDDENOWNED.value))
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