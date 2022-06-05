package com.example.studiograficzne;

import android.content.Context;
import android.content.SharedPreferences;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class fragment_employees_hire extends Fragment {

    private static final String USERS = "Users", EMPLOYEES ="Employees";
    private final String TAG = this.getClass().getName().toUpperCase();

    // Database variables
    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;

    UserEmployeesInfo userEmployeesInfo;
    UserGameInfo userGameInfo;

    private Button hire_first_employee;
    private Button hire_second_employee;
    private Button hire_third_employee;

    private boolean e1hired = false;
    private boolean e2hired = false;
    private boolean e3hired = false;

    Context thisContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employees_hire, container, false);
        // Inflate the layout for this fragment

        thisContext = container.getContext();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email = currentUser.getEmail();
        }

        hire_first_employee = view.findViewById(R.id.employee1_hire);
        hire_second_employee = view.findViewById(R.id.employee2_hire);
        hire_third_employee = view.findViewById(R.id.employee3_hire);

        DatabaseReference userRef = rootRef.child(USERS);
        //DatabaseReference employeeRef = rootRef.child(EMPLOYEES);
        Log.v("USERID", userRef.getKey());

        userEmployeesInfo = new UserEmployeesInfo();
        userGameInfo = new UserGameInfo();
        readFromDatabase(currentUser, userRef);
        //updateEmployeesHiredButtons();


        hire_first_employee.setOnClickListener(view1 -> {
            userEmployeesInfo.setEmployee1Hired(true);
            hire_first_employee.setVisibility(View.INVISIBLE);
            updateDataToFirebase();
        });

        hire_second_employee.setOnClickListener(view1 -> {
            userEmployeesInfo.setEmployee2Hired(true);
            hire_second_employee.setVisibility(View.INVISIBLE);
            updateDataToFirebase();
        });

        hire_third_employee.setOnClickListener(view1 -> {
            userEmployeesInfo.setEmployee3Hired(true);
            hire_third_employee.setVisibility(View.INVISIBLE);
            updateDataToFirebase();
        });

        return view;
    }

    private void readFromDatabase(FirebaseUser currentUser, DatabaseReference userRef) {
        if (currentUser != null) {
            // Read from "Users" branch in db
            userRef.addValueEventListener(new ValueEventListener() {
                Boolean isEmployee1Hired, isEmployee2Hired, isEmployee3Hired;
                double userLevel;

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot keyId : dataSnapshot.getChildren()) {
                        if (keyId.child("UserInfo").child("email").getValue().equals(email)) {
                            userLevel = keyId.child("UserGameInfo").child("level").getValue(Double.class);
                            isEmployee1Hired = keyId.child("UserEmployeesInfo").child("employee1Hired").getValue(Boolean.class);
                            isEmployee2Hired = keyId.child("UserEmployeesInfo").child("employee2Hired").getValue(Boolean.class);
                            isEmployee3Hired = keyId.child("UserEmployeesInfo").child("employee3Hired").getValue(Boolean.class);
                            break;
                        }
                    }
                    userGameInfo.setLevel(userLevel);
                    userEmployeesInfo.setEmployee1Hired(isEmployee1Hired);
                    userEmployeesInfo.setEmployee2Hired(isEmployee2Hired);
                    userEmployeesInfo.setEmployee3Hired(isEmployee3Hired);

                    enableHireButtons();
                    updateEmployeesHiredButtons();

                } // End of reading from "Users" branch

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }

    }

    public void updateEmployeesHiredButtons(){
        if(userEmployeesInfo.getEmployee1Hired())
        {
            hire_first_employee.setVisibility(View.INVISIBLE);
        }
        else{
            hire_first_employee.setVisibility(View.VISIBLE);
        }
        if(userEmployeesInfo.getEmployee2Hired())
        {
            hire_second_employee.setVisibility(View.INVISIBLE);
        }
        else
        {
            hire_second_employee.setVisibility(View.VISIBLE);
        }
        if(userEmployeesInfo.getEmployee3Hired())
        {
            hire_third_employee.setVisibility(View.INVISIBLE);
        }
        else
        {
            hire_third_employee.setVisibility(View.VISIBLE);
        }
    }

    private void enableHireButtons(){
        if(userGameInfo.getLevel()>=3){
            hire_first_employee.setEnabled(true);
        }
        else
        {
            hire_first_employee.setEnabled(false);
        }
        if(userGameInfo.getLevel()>=5){
            hire_second_employee.setEnabled(true);
        }
        else
        {
            hire_second_employee.setEnabled(false);
        }
        if(userGameInfo.getLevel()>=10){
            hire_third_employee.setEnabled(true);
        }
        else
        {
            hire_third_employee.setEnabled(false);
        }
    }

//Updating data to firebase
    private void updateDataToFirebase() {

    FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null) {
//            databaseOperations.updateDatabase(user.getUid(),rootRef,"employee1Hired",userEmployeesInfo.getEmployee1Hired());
//            databaseOperations.updateDatabase(user.getUid(),rootRef,"employee2Hired",userEmployeesInfo.getEmployee2Hired());
//            databaseOperations.updateDatabase(user.getUid(),rootRef,"employee3Hired",userEmployeesInfo.getEmployee3Hired());
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("employee1Hired", userEmployeesInfo.getEmployee1Hired());
            childUpdates.put("employee2Hired", userEmployeesInfo.getEmployee2Hired());
            childUpdates.put("employee3Hired", userEmployeesInfo.getEmployee3Hired());

            rootRef.child(USERS).child(user.getUid()).child("UserEmployeesInfo").updateChildren(childUpdates);
        }
    }
}