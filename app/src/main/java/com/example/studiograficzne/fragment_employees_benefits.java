package com.example.studiograficzne;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class fragment_employees_benefits extends Fragment {

    private Button benefits_and_salary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_employees_benefits, container, false);
        //thisContext = container.getContext();
        benefits_and_salary = (Button) view.findViewById(R.id.benefits_and_salary_button);
        benefits_and_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), activity_employees_benefits_and_salary.class);
                startActivity(intent);
            }
        });
        return view;
    }
}