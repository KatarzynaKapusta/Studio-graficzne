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

