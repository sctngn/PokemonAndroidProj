package com.example.myapplication.model;

import com.example.myapplication.R;

/**
 * Mewtwo Pokemon class
 */
public class Mewtwo extends Pokemon {
    
    /**
     * Constructor for Mewtwo
     */
    public Mewtwo(int id, String name, int HP, int attack, int defense) {
        super(id, name, "Mewtwo", HP, attack, defense);
        this.attackSkillName = "Psychic";
        this.defendSkillName = "Barrier";
    }
    
    @Override
    public int getImageResourceId() {
        return R.drawable.mewtwo_idle;
    }
    
    /**
     * Mewtwo doesn't need anything special here - no unique battle mechanics
     */
}
