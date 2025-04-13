package com.example.myapplication.model;

import com.example.myapplication.R;
import java.util.Random;

/**
 * Pikachu Pokemon class
 */
public class Pikachu extends Pokemon {
    private Random random = new Random();
    private double lastMultiplier = 1.0; // Track the last multiplier used
    
    public Pikachu(String name) {
        super(name, "Pikachu", 5, 4, 20);
    }
    
    @Override
    public String getAttackSkillName() {
        return "Thunderbolt";
    }
    
    @Override
    public String getDefendSkillName() {
        return "Quick Guard";
    }
    
    @Override
    public int getImageResourceId() {
        return R.drawable.pikachu;
    }
    
    /**
     * Get the multiplier that was used in the last attack
     */
    public double getLastMultiplier() {
        return lastMultiplier;
    }
    
    /**
     * Override attack method to add random multiplier
     * Has a chance to deal 0x, 1x, or 2x normal damage
     */
    @Override
    public int attack() {
        int baseAttack = super.attack(); // Get the base attack value
        
        // Generate a random number 0-2 to determine the multiplier
        int randomValue = random.nextInt(3);
        
        switch (randomValue) {
            case 0:
                lastMultiplier = 0; // No damage
                return 0; // Return 0 immediately to ensure no damage is dealt
            case 1:
                lastMultiplier = 1.0; // Normal damage
                break;
            case 2:
                lastMultiplier = 2.0; // Double damage
                break;
            default:
                lastMultiplier = 1.0; // Fallback to normal damage
        }
        
        // Apply the multiplier and return the result
        return (int)(baseAttack * lastMultiplier);
    }
}
