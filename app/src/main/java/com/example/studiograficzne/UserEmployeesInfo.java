package com.example.studiograficzne;

public class UserEmployeesInfo {
    private double employees_earnings = 0;
    private boolean employee1Hired = false;
    private boolean employee2Hired = false;
    private boolean employee3Hired = false;

    public double getEmployees_earnings() {return employees_earnings;}

    public void setEmployees_earnings(double employees_earnings) {this.employees_earnings = employees_earnings;}

    public void setEmployee1Hired(boolean employee1Hired) {this.employee1Hired = employee1Hired;}

    public void setEmployee2Hired(boolean employee2Hired) {this.employee2Hired = employee2Hired;}

    public void setEmployee3Hired(boolean employee3Hired) {this.employee3Hired = employee3Hired;}

    public void addEarningsOfEmployees(double earnings){
        this.employees_earnings += earnings;
    }


}
