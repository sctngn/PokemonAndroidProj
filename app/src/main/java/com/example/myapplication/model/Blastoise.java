package com.example.myapplication.model;

import com.example.myapplication.R;

/**
 * Blastoise Pokemon class
 */
public class Blastoise extends Pokemon {
    
    /**
     * Constructor for Blastoise
     */
    public Blastoise(int id, String name, int HP, int attack, int defense) {
        super(id, name, "Blastoise", HP, attack, defense);
        this.attackSkillName = "Hydro Pump";
        this.defendSkillName = "Withdraw";
    }
    
    @Override
    public int getImageResourceId() {
        return R.drawable.blastoise_idle;
    }
}
