package com.example.studiograficzne;

public class UserStudioInfo {

    private String studioName = " ";
    private Integer studioLevel = 0;
    private Integer employees = 0;
    private double earningsSM = 0;

    public UserStudioInfo() {}

    public UserStudioInfo(String studioName, Integer studioLevel, Integer employees) {
        this.studioName = studioName;
        this.studioLevel = studioLevel;
        this.employees = employees;
        this.earningsSM = earningsSM;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }

    public void setStudioLevel(Integer studioLevel) {
        this.studioLevel = studioLevel;
    }

    public void setEmployees(Integer employees) {
        this.employees = employees;
    }

    public void setEarningsSM(){this.earningsSM = earningsSM;}

    public String getStudioName() {
        return studioName;
    }

    public Integer getStudioLevel() {
        return studioLevel;
    }

    public Integer getEmployees() {
        return employees;
    }

    public double getEarningsSM() {return earningsSM;}

    public void addEarningsFromEmployees(){
        //switch do dodania zarobków

    }

}
