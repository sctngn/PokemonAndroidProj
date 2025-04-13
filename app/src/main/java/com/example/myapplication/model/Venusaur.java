package com.example.myapplication.model;

import com.example.myapplication.R;

/**
 * Venusaur Pokemon class
 */
public class Venusaur extends Pokemon {
    
    public Venusaur(String name) {
        // Using similar stats to the GreenLutemon
        super(name, "Venusaur", 6, 3, 19);
    }
    
    @Override
    public String getAttackSkillName() {
        return "Razor Leaf";
    }
    
    @Override
    public String getDefendSkillName() {
        return "Vine Whip Barrier";
    }
    
    @Override
    public int getImageResourceId() {
        return R.drawable.venusaur_idle;
    }
}
