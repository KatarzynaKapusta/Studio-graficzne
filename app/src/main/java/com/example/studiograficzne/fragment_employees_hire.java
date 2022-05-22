package com.example.studiograficzne;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fragment_employees_hire extends Fragment {

    Employee1 employee1;
    Employee2 employee2;
    Employee3 employee3;
    UserEmployeesInfo userEmployeesInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employees_hire, container, false);
        // Inflate the layout for this fragment
        Button hire_first_employee = view.findViewById(R.id.employee1_hire_button);
        Button hire_second_employee = view.findViewById(R.id.employee2_hire_button);
        Button hire_third_employee = view.findViewById(R.id.employee3_hire_button);

        employee1 = new Employee1();
        employee2 = new Employee2();
        employee3 = new Employee3();

        userEmployeesInfo = new UserEmployeesInfo();

        hire_first_employee.setOnClickListener(view1 -> {
            userEmployeesInfo.setEmployee1Hired(true);
            hire_first_employee.setVisibility(View.INVISIBLE);
        });

        hire_first_employee.setOnClickListener(view1 -> {
            userEmployeesInfo.setEmployee2Hired(true);
            hire_second_employee.setVisibility(View.INVISIBLE);
        });

        hire_first_employee.setOnClickListener(view1 -> {
            userEmployeesInfo.setEmployee3Hired(true);
            hire_third_employee.setVisibility(View.INVISIBLE);
        });

        return view;
    }
}