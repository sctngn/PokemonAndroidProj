package com.example.myapplication.model;

import com.example.myapplication.R;

/**
 * Charizard Pokemon class
 */
public class Charizard extends Pokemon {
    
    public Charizard(String name) {
        super(name, "Charizard", 7, 2, 18);
    }
    
    @Override
    public String getAttackSkillName() {
        return "Flamethrower";
    }
    
    @Override
    public String getDefendSkillName() {
        return "Heat Wave";
    }
    
    @Override
    public int getImageResourceId() {
        return R.drawable.charizard_idle;
    }
}
