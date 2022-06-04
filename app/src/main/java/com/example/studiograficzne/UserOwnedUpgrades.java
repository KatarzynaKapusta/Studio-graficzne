package com.example.studiograficzne;

public class UserOwnedUpgrades {

    private int pc_lvl1;
    private int pc_lvl2;
    private int pc_lvl3;
    private int card_lvl1;
    private int card_lvl2;
    private int card_lvl3;
    private int t_lvl1;
    private int t_lvl2;
    private int t_lvl3;

    public UserOwnedUpgrades(int pc_lvl1, int pc_lvl2, int pc_lvl3, int card_lvl1, int card_lvl2, int card_lvl3,int t_lvl1, int t_lvl2, int t_lvl3) {
        this.pc_lvl1 = pc_lvl1;
        this.pc_lvl2 = pc_lvl2;
        this.pc_lvl3 = pc_lvl3;
        this.card_lvl1 = card_lvl1;
        this.card_lvl2 = card_lvl2;
        this.card_lvl3 = card_lvl3;
        this.t_lvl1 = t_lvl1;
        this.t_lvl2 = t_lvl2;
        this.t_lvl3 = t_lvl3;
    }

    public UserOwnedUpgrades(){
        pc_lvl1 = ItemStatus.NOTOWNED.value;
        pc_lvl2 = ItemStatus.NOTOWNED.value;
        pc_lvl3 = ItemStatus.NOTOWNED.value;
        card_lvl1 = ItemStatus.NOTOWNED.value;
        card_lvl2 = ItemStatus.NOTOWNED.value;
        card_lvl3 = ItemStatus.NOTOWNED.value;
        t_lvl1 = ItemStatus.NOTOWNED.value;
        t_lvl2 = ItemStatus.NOTOWNED.value;
        t_lvl3 = ItemStatus.NOTOWNED.value;
    }

    public int getPc_lvl1() {
        return pc_lvl1;
    }

    public void setPc_lvl1(int pc_lvl1) {
        this.pc_lvl1 = pc_lvl1;
    }

    public int getPc_lvl2() {
        return pc_lvl2;
    }

    public void setPc_lvl2(int pc_lvl2) {
        this.pc_lvl2 = pc_lvl2;
    }

    public int getPc_lvl3() {
        return pc_lvl3;
    }

    public void setPc_lvl3(int pc_lvl3) {
        this.pc_lvl3 = pc_lvl3;
    }

    public int getCard_lvl1() {
        return card_lvl1;
    }

    public void setCard_lvl1(int card_lvl1) {
        this.card_lvl1 = card_lvl1;
    }

    public int getCard_lvl2() {
        return card_lvl2;
    }

    public void setCard_lvl2(int card_lvl2) {
        this.card_lvl2 = card_lvl2;
    }

    public int getCard_lvl3() {
        return card_lvl3;
    }

    public void setCard_lvl3(int card_lvl3) {
        this.card_lvl3 = card_lvl3;
    }

    public int getT_lvl1() {
        return t_lvl1;
    }

    public void setT_lvl1(int t_lvl1) {
        this.t_lvl1 = t_lvl1;
    }

    public int getT_lvl2() {
        return t_lvl2;
    }

    public void setT_lvl2(int t_lvl2) {
        this.t_lvl2 = t_lvl2;
    }

    public int getT_lvl3() {
        return t_lvl3;
    }

    public void setT_lvl3(int t_lvl3) {
        this.t_lvl3 = t_lvl3;
    }
}
