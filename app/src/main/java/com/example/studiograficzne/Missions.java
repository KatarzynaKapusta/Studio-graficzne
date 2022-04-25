package com.example.studiograficzne;

public class Missions {
    private final double m_resources = 300;
    private final double m_experience = 200;

    public double getM_resources() { return m_resources; }

    public double getM_experience() { return m_experience; }
}

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

