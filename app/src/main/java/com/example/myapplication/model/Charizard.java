package com.example.myapplication.model;

import com.example.myapplication.R;

/**
 * Charizard Pokemon class
 */
public class Charizard extends Pokemon {
    
    /**
     * Constructor for Charizard
     */
    public Charizard(int id, String name, int HP, int attack, int defense) {
        super(id, name, "Charizard", HP, attack, defense);
        this.attackSkillName = "Flamethrower";
        this.defendSkillName = "Endure";
    }
    
    @Override
    public int getImageResourceId() {
        return R.drawable.charizard_idle;
    }
}
