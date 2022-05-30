package com.example.studiograficzne;

import android.view.View;

public class UserOwnedItems {

    private int p1;
    private int p2;
    private int p3;
    private int t1;
    private int t2;
    private int t3;
    private int f1;
    private int f2;
    private int f3;

    public UserOwnedItems(int f1, int f2, int f3, int p1, int p2, int p3,int t1, int t2, int t3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
    }

    public UserOwnedItems(){
        p1 = ItemStatus.NOTOWNED.value;
        p2 = ItemStatus.NOTOWNED.value;
        p3 = ItemStatus.NOTOWNED.value;
        t1 = ItemStatus.NOTOWNED.value;
        t2 = ItemStatus.NOTOWNED.value;
        t3 = ItemStatus.NOTOWNED.value;
        f1 = ItemStatus.NOTOWNED.value;
        f2 = ItemStatus.NOTOWNED.value;
        f3 = ItemStatus.NOTOWNED.value;
    }

    public int getP1() {
        return p1;
    }

    public void setP1(int p1) {
        this.p1 = p1;
    }

    public int getP2() {
        return p2;
    }

    public void setP2(int p2) {
        this.p2 = p2;
    }

    public int getP3() {
        return p3;
    }

    public void setP3(int p3) {
        this.p3 = p3;
    }

    public int getT1() {
        return t1;
    }

    public void setT1(int t1) {
        this.t1 = t1;
    }

    public int getT2() {
        return t2;
    }

    public void setT2(int t2) {
        this.t2 = t2;
    }

    public int getT3() {
        return t3;
    }

    public void setT3(int t3) {
        this.t3 = t3;
    }

    public int getF1() {
        return f1;
    }

    public void setF1(int f1) {
        this.f1 = f1;
    }

    public int getF2() {
        return f2;
    }

    public void setF2(int f2) {
        this.f2 = f2;
    }

    public int getF3() {
        return f3;
    }

    public void setF3(int f3) {
        this.f3 = f3;
    }

}
