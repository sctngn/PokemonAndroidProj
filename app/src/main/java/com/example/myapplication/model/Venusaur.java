package com.example.myapplication.model;

import com.example.myapplication.R;

/**
 * Venusaur Pokemon class
 */
public class Venusaur extends Pokemon {
    
    /**
     * Constructor for Venusaur
     */
    public Venusaur(int id, String name, int HP, int attack, int defense) {
        super(id, name, "Venusaur", HP, attack, defense);
        this.attackSkillName = "Razor Leaf";
        this.defendSkillName = "Growth";
    }
    
    @Override
    public int getImageResourceId() {
        return R.drawable.venusaur_idle;
    }
}
