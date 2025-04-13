package com.example.myapplication.model;

import com.example.myapplication.R;

/**
 * Mewtwo Pokemon class
 */
public class Mewtwo extends Pokemon {
    
    public Mewtwo(String name) {
        // Using similar stats to the BlackLutemon
        super(name, "Mewtwo", 9, 0, 16);
    }
    
    @Override
    public String getAttackSkillName() {
        return "Psychic";
    }
    
    @Override
    public String getDefendSkillName() {
        return "Barrier";
    }
    
    @Override
    public int getImageResourceId() {
        return R.drawable.mewtwo_idle;
    }
    
    /**
     * Mewtwo's special move
     */
    public void PsychicAttack() {
        // Special move implementation
    }
}
