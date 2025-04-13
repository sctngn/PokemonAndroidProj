package com.example.myapplication.model;

import java.util.Random;

/**
 * Pikachu Pokemon class
 */
public class Pikachu extends Pokemon {
    private Random random = new Random();
    
    public Pikachu(String name) {
        // Using similar stats to the WhiteLutemon
        super(name, "Pikachu", 5, 4, 20);
    }
    
    /**
     * Pikachu's special move
     */
    public void Thunderbolt() {
        // Special move implementation
    }
    
    /**
     * Override attack method to add random multiplier
     * Has a chance to deal 0.5x, 1x, or 2x normal damage
     */
    @Override
    public int attack() {
        int baseAttack = super.attack(); // Get the base attack value
        
        // Generate a random number 0-2 to determine the multiplier
        int randomValue = random.nextInt(3);
        double multiplier;
        
        switch (randomValue) {
            case 0:
                multiplier = 0.5; // 50% damage
                break;
            case 1:
                multiplier = 1.0; // Normal damage
                break;
            case 2:
                multiplier = 2.0; // Double damage
                break;
            default:
                multiplier = 1.0; // Fallback to normal damage
        }
        
        // Apply the multiplier and return the result
        return (int)(baseAttack * multiplier);
    }
}
