package com.example.myapplication.model;

import com.example.myapplication.R;
import java.util.Random;

/**
 * Pikachu Pokemon class with special random attack multiplier
 */
public class Pikachu extends Pokemon {
    private Random random = new Random();
    private double lastMultiplier = 1.0;
    
    /**
     * Constructor for Pikachu
     */
    public Pikachu(int id, String name, int HP, int attack, int defense) {
        super(id, name, "Pikachu", HP, attack, defense);
        this.attackSkillName = "Thunderbolt";
        this.defendSkillName = "Agility";
    }
    
    @Override
    public String getAttackSkillName() {
        return "Thunderbolt";
    }
    
    @Override
    public String getDefendSkillName() {
        return "Agility";
    }
    
    @Override
    public int getImageResourceId() {
        return R.drawable.pikachu_idle;
    }
    
    /**
     * Get the last attack multiplier used
     * @return The last multiplier value
     */
    public double getLastMultiplier() {
        return lastMultiplier;
    }
    
    /**
     * Pikachu's special attack with random multiplier:
     * 0x (miss), 1x (normal), or 2x (critical) damage
     */
    @Override
    public int attack() {
        int baseAttack = super.attack();
        int randomValue = random.nextInt(3);
        
        switch (randomValue) {
            case 0:
                lastMultiplier = 0; // No damage (miss)
                return 0;
            case 1:
                lastMultiplier = 1.0; // Normal damage
                break;
            case 2:
                lastMultiplier = 2.0; // Double damage (critical)
                break;
        }
        
        return (int)(baseAttack * lastMultiplier);
    }
}
