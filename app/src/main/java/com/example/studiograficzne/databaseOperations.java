package com.example.studiograficzne;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class databaseOperations {

    public static void updateDatabase(String userUid, DatabaseReference databaseReference,String thing, Object value){
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(thing, value);

        databaseReference.child(userUid).child("UserGameInfo").updateChildren(childUpdates);
    }


}
