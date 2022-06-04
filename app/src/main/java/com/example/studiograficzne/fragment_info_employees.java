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


public class fragment_info_employees extends Fragment {


    private static final String USERS = "Users", EMPLOYEES = "Employees";
    private final String TAG = this.getClass().getName().toUpperCase();

    // Database variables
    private FirebaseAuth mAuth;
    DatabaseReference rootRef = FirebaseDatabase.getInstance("https://studio-graficzne-baza-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private String email;

    // List for levels
    private final List<Double> lvlList = new ArrayList<>();
    // Database variables

    UserGameInfo User;
    UserEmployeesInfo userEmployeesInfo;

    //Employee earnings
    private Employee emp1;
    private Employee emp2;
    private Employee emp3;

    //TextViews
    private TextView emp1_name, emp2_name, emp3_name;
    private TextView emp1_salary, emp2_salary, emp3_salary;
    private TextView emp1_benefits, emp2_benefits, emp3_benefits;
    private TextView emp1_hired, emp2_hired, emp3_hired;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_info_employees, container, false);

        emp1_name = v.findViewById(R.id.employee1_name);
        emp2_name = v.findViewById(R.id.employee2_name);
        emp3_name = v.findViewById(R.id.employee3_name);

        emp1_hired = v.findViewById(R.id.employee1_hired);
        emp2_hired = v.findViewById(R.id.employee2_hired);
        emp3_hired = v.findViewById(R.id.employee3_hired);

        emp1_benefits = v.findViewById(R.id.benefits_of_emp1);
        emp2_benefits = v.findViewById(R.id.benefits_of_emp2);
        emp3_benefits = v.findViewById(R.id.benefits_of_emp3);

        emp1_salary = v.findViewById(R.id.salary_of_emp1);
        emp2_salary = v.findViewById(R.id.salary_of_emp2);
        emp3_salary = v.findViewById(R.id.salary_of_emp3);

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
        DatabaseReference empRef = rootRef.child(EMPLOYEES);
        Log.v("USERID", userRef.getKey());

        readFromDatabase(currentUser, userRef, empRef);



        return v;
    }

    private void readFromDatabase(FirebaseUser currentUser, DatabaseReference userRef, DatabaseReference empRef) {
        if (currentUser != null) {
            // Read from "Users" branch in db
            userRef.addValueEventListener(new ValueEventListener() {
                Double money, level, resources, experience;
                String moneyString, resourcesString, experienceString;
                Boolean isEmployee1Hired, isEmployee2Hired, isEmployee3Hired;

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

                            break;
                        }
                    }

                    // Read from "Employees" branch in db
                    empRef.addValueEventListener(new ValueEventListener() {
                        List<String> addNameList = new ArrayList<>();
                        List<Double> addMonList = new ArrayList<>();
                        List<Double> addSalaryList = new ArrayList<>();
                        String ben1, ben2, ben3, sal1,sal2,sal3;


                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot keyId : dataSnapshot.getChildren()) {
                                addNameList.add(keyId.child("name").getValue(String.class));
                                addMonList.add(keyId.child("additional_money").getValue(Double.class));
                                addSalaryList.add(keyId.child("salary").getValue(Double.class));
                            }
                            emp1.setName(addNameList.get(0));
                            emp1.setAdditional_money1(addMonList.get(0));
                            emp1.setSalary(addSalaryList.get(0));

                            emp2.setName(addNameList.get(1));
                            emp2.setAdditional_money1(addMonList.get(1));
                            emp2.setSalary(addSalaryList.get(1));

                            emp3.setName(addNameList.get(2));
                            emp3.setAdditional_money1(addMonList.get(2));
                            emp3.setSalary(addSalaryList.get(2));

                            ben1 = String.valueOf(emp1.getAdditional_money1());
                            ben2 = String.valueOf(emp2.getAdditional_money1());
                            ben3 = String.valueOf(emp3.getAdditional_money1());

                            sal1 = String.valueOf(emp1.getSalary());
                            sal2 = String.valueOf(emp2.getSalary());
                            sal3 = String.valueOf(emp3.getSalary());

                            emp1_benefits.setText(ben1);
                            emp2_benefits.setText(ben2);
                            emp3_benefits.setText(ben3);

                            emp1_salary.setText(sal1);
                            emp2_salary.setText(sal2);
                            emp3_salary.setText(sal3);

                            emp1_name.setText(emp1.getName());
                            emp2_name.setText(emp2.getName());
                            emp3_name.setText(emp3.getName());
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    }); // End of reading from "Employees" branch


                    userEmployeesInfo.setEmployee1Hired(isEmployee1Hired);
                    userEmployeesInfo.setEmployee2Hired(isEmployee2Hired);
                    userEmployeesInfo.setEmployee3Hired(isEmployee3Hired);

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
            emp1_hired.setText("TAK");
        }
        if(userEmployeesInfo.getEmployee2Hired()){
            emp2_hired.setText("TAK");
        }
        if(userEmployeesInfo.getEmployee3Hired()){
            emp3_hired.setText("TAK");
        }
    }
}