package com.example.studiograficzne;

public class Missions {
}

////// BANK MISSIONS //////

class BankTimedMission1 extends Missions{

    double money_1 = 200;
    double exp_1 = 150;

    public double getMoney_1() { return money_1; }
    public double getExp_1() { return exp_1; }
}
class BankTimedMission2 extends Missions{

    double money_2 = 250;
    double exp_2 = 200;

    public double getMoney_2() { return money_2; }
    public double getExp_2() { return exp_2; }
}

class BankTimedMission3 extends Missions{

    double money_3 = 300;
    double exp_3 = 250;

    public double getMoney_3() { return money_3; }
    public double getExp_3() { return exp_3; }
}

class BankMatchMission1 extends Missions{

    double bm_money;
    double bm_exp;

    public double getBm_exp() { return bm_exp; }
    public double getBm_money() { return bm_money; }

    public void setBm_exp(double bm_exp) { this.bm_exp = bm_exp; }
    public void setBm_money(double bm_money) { this.bm_money = bm_money; }

    public void HowManyMatched(int counter)
    {
        switch (counter)
        {
            case 0:
                setBm_exp(0); setBm_money(0);
                break;
            case 1:
                setBm_exp(50); setBm_money(75);
                break;
            case 2:
                setBm_exp(75); setBm_money(100);
                break;
            case 3:
                setBm_exp(100); setBm_money(125);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + counter);
        }
    }
}

class BankMatchMission2 extends Missions{

    double bm_money;
    double bm_exp;

    public double getBm_exp() { return bm_exp; }
    public double getBm_money() { return bm_money; }

    public void setBm_exp(double bm_exp) { this.bm_exp = bm_exp; }
    public void setBm_money(double bm_money) { this.bm_money = bm_money; }

    public void HowManyMatched(int counter)
    {
        switch (counter)
        {
            case 0:
                setBm_exp(0); setBm_money(0);
                break;
            case 1:
                setBm_exp(75); setBm_money(100);
                break;
            case 2:
                setBm_exp(100); setBm_money(125);
                break;
            case 3:
                setBm_exp(125); setBm_money(150);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + counter);
        }
    }
}

class BankMatchMission3 extends Missions{

    double bm_money;
    double bm_exp;

    public double getBm_exp() { return bm_exp; }
    public double getBm_money() { return bm_money; }

    public void setBm_exp(double bm_exp) { this.bm_exp = bm_exp; }
    public void setBm_money(double bm_money) { this.bm_money = bm_money; }

    public void HowManyMatched(int counter)
    {
        switch (counter)
        {
            case 0:
                setBm_exp(0); setBm_money(0);
                break;
            case 1:
                setBm_exp(100); setBm_money(125);
                break;
            case 2:
                setBm_exp(125); setBm_money(150);
                break;
            case 3:
                setBm_exp(150); setBm_money(175);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + counter);
        }
    }
}

////// STORAGE MISSIONS //////

class StorageTimedMission1 extends Missions{

    double res_1 = 200;
    double exp_1 = 150;

    public double getRes_1() { return res_1; }
    public double getExp_1() { return exp_1; }
}
class StorageTimedMission2 extends Missions{

    double res_2 = 250;
    double exp_2 = 200;

    public double getRes_2() { return res_2; }
    public double getExp_2() { return exp_2; }
}

class StorageTimedMission3 extends Missions{

    double res_3 = 300;
    double exp_3 = 250;

    public double getRes_3() { return res_3; }
    public double getExp_3() { return exp_3; }
}

class StorageMatchMission1 extends Missions{

    double sm_res;
    double sm_exp;

    public double getSm_exp() { return sm_exp; }
    public double getSm_res() { return sm_res; }

    public void setSm_exp(double sm_exp) { this.sm_exp = sm_exp; }
    public void setSm_res(double sm_res) { this.sm_res = sm_res; }

    public void HowManyMatched(int counter)
    {
        switch (counter)
        {
            case 0:
                setSm_exp(0); setSm_res(0);
                break;
            case 1:
                setSm_exp(50); setSm_res(75);
                break;
            case 2:
                setSm_exp(75); setSm_res(100);
                break;
            case 3:
                setSm_exp(100); setSm_res(125);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + counter);
        }
    }
}

class StorageMatchMission2 extends Missions{

    double sm_res;
    double sm_exp;

    public double getSm_exp() { return sm_exp; }
    public double getSm_res() { return sm_res; }

    public void setSm_exp(double sm_exp) { this.sm_exp = sm_exp; }
    public void setSm_res(double sm_res) { this.sm_res = sm_res; }

    public void HowManyMatched(int counter)
    {
        switch (counter)
        {
            case 0:
                setSm_exp(0); setSm_res(0);
                break;
            case 1:
                setSm_exp(75); setSm_res(100);
                break;
            case 2:
                setSm_exp(100); setSm_res(125);
                break;
            case 3:
                setSm_exp(125); setSm_res(150);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + counter);
        }
    }
}

class StorageMatchMission3 extends Missions{

    double sm_res;
    double sm_exp;

    public double getSm_exp() { return sm_exp; }
    public double getSm_res() { return sm_res; }

    public void setSm_exp(double sm_exp) { this.sm_exp = sm_exp; }
    public void setSm_res(double sm_res) { this.sm_res = sm_res; }

    public void HowManyMatched(int counter)
    {
        switch (counter)
        {
            case 0:
                setSm_exp(0); setSm_res(0);
                break;
            case 1:
                setSm_exp(100); setSm_res(125);
                break;
            case 2:
                setSm_exp(125); setSm_res(150);
                break;
            case 3:
                setSm_exp(150); setSm_res(175);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + counter);
        }
    }
}