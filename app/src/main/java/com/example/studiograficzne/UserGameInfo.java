package com.example.studiograficzne;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserGameInfo implements Parcelable {
    private double level = 0;
    private double money = 0;
    private double experience = 0;
    private double resources = 0;

    //pusty konstruktor
    public UserGameInfo() {
    }

    protected UserGameInfo(Parcel in) {
        level = in.readDouble();
        money = in.readDouble();
        experience = in.readDouble();
        resources = in.readDouble();
    }

    public static final Creator<UserGameInfo> CREATOR = new Creator<UserGameInfo>() {
        @Override
        public UserGameInfo createFromParcel(Parcel in) {
            return new UserGameInfo(in);
        }

        @Override
        public UserGameInfo[] newArray(int size) {
            return new UserGameInfo[size];
        }
    };

    //Gettery i settery
    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getResources() {
        return resources;
    }

    public void setResources(double resources) {
        this.resources = resources;
    }

    public void addMissionRewardsStorage(double m_resources, double m_experience) {
        this.resources += m_resources;
        this.experience += m_experience;
    };
    public void addTimedMissionRewardsBank(double b_mon, double b_exp) {
        this.money += b_mon;
        this.experience += b_exp;
    };

    public void addMatchMissionRewardsBank(double b_mon, double b_exp) {
        this.money += b_mon;
        this.experience += b_exp;
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(level);
        parcel.writeDouble(money);
        parcel.writeDouble(experience);
        parcel.writeDouble(resources);
    }
}

