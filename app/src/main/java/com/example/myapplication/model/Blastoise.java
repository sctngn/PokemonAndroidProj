package com.example.myapplication.model;

import com.example.myapplication.R;

/**
 * Blastoise Pokemon class
 */
public class Blastoise extends Pokemon {
    
    public Blastoise(String name) {
        // Using similar stats to the OrangeLutemon
        super(name, "Blastoise", 8, 1, 17);
    }
    
    @Override
    public String getAttackSkillName() {
        return "Hydro Pump";
    }
    
    @Override
    public String getDefendSkillName() {
        return "Withdraw";
    }
    
    @Override
    public int getImageResourceId() {
        return R.drawable.blastoise_idle;
    }
}
