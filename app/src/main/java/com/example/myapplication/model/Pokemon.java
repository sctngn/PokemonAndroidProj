package com.example.myapplication.model;

import java.io.Serializable;
import java.util.Random;

/**
 * Abstract class for Pokemon
 */
public abstract class Pokemon implements Serializable {
    // Add serialVersionUID for serialization
    private static final long serialVersionUID = 1L;
    
    protected int id;
    protected String name;
    protected String species;
    protected int maxHP;
    protected int HP;
    protected int attack;
    protected int defense;
    protected int baseDefense; // Store original defense value
    protected int experience;
    protected String attackSkillName;
    protected String defendSkillName;
    protected transient Random random;
    
    // Statistics tracking
    protected int totalBattles = 0;
    protected int wins = 0;
    protected int losses = 0;
    protected int trainingDays = 0;
    
    /**
     * Constructor for creating a Pokemon
     * @param id unique identifier
     * @param name name for the Pokemon
     * @param species type of Pokemon
     * @param HP health points
     * @param attack attack value
     * @param defense defense value
     */
    public Pokemon(int id, String name, String species, int HP, int attack, int defense) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.maxHP = HP;
        this.HP = HP;
        this.attack = attack;
        this.defense = defense;
        this.baseDefense = defense; // Store original defense value
        this.experience = 0;
        this.random = new Random();
        this.attackSkillName = "Attack";
        this.defendSkillName = "Defend";
    }
    
    /**
     * Perform an attack and return the attack value
     * @return attack value
     */
    public int attack() {
        // Base attack + experience bonus
        return attack + experience;
    }
    
    /**
     * Defend against an attack and lose HP
     * @param attacker the attacking Pokemon
     */
    public void defense(Pokemon attacker) {
        int attackValue = attacker.attack();
        int damage = Math.max(0, attackValue - defense);
        HP = Math.max(0, HP - damage);
    }
    
    /**
     * Increase defense temporarily
     */
    public void defend() {
        defense += 2;
    }
    
    /**
     * Return the Pokemon to normal after battle
     */
    public void resetAfterBattle() {
        defense = baseDefense; // Reset defense to original value
    }
    
    /**
     * Gain experience after winning a battle
     */
    public void gainExperience() {
        experience++;
    }
    
    /**
     * Reset experience points
     */
    public void resetExperience() {
        experience = 0;
    }
    
    /**
     * Restore HP to maximum
     */
    public void restore() {
        HP = maxHP;
    }
    
    /**
     * Check if the Pokemon is alive
     * @return true if HP > 0
     */
    public boolean isAlive() {
        return HP > 0;
    }
    
    /**
     * Get resource ID for Pokemon image
     */
    public abstract int getImageResourceId();
    
    /**
     * Ensures Random is initialized after deserialization
     */
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Re-initialize transient fields
        this.random = new Random();
    }

    // Getters and setters
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public int getHP() {
        return HP;
    }
    
    public int getMaxHP() {
        return maxHP;
    }

    public int getAttack() {
        return attack + experience;
    }

    public int getDefense() {
        return defense;
    }
    
    public int getBaseDefense() {
        return baseDefense;
    }
    
    public int getExperience() {
        return experience;
    }
    
    /**
     * Set the experience points for this Pokemon
     * @param experience New experience value
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }
    
    public String getAttackSkillName() {
        return attackSkillName;
    }
    
    public String getDefendSkillName() {
        return defendSkillName;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDefense(int defense) {
        this.defense = defense;
    }
    
    public void setHP(int hp) {
        this.HP = Math.max(0, Math.min(hp, maxHP));
    }
    
    /**
     * Get the number of battles this Pokemon has participated in
     */
    public int getTotalBattles() {
        return totalBattles;
    }
    
    /**
     * Get the number of battles this Pokemon has won
     */
    public int getWins() {
        return wins;
    }
    
    /**
     * Get the number of battles this Pokemon has lost
     */
    public int getLosses() {
        return losses;
    }
    
    /**
     * Get the number of days this Pokemon has spent training
     */
    public int getTrainingDays() {
        return trainingDays;
    }
    
    /**
     * Record a battle win for this Pokemon
     */
    public void recordWin() {
        wins++;
        totalBattles++;
    }
    
    /**
     * Record a battle loss for this Pokemon
     */
    public void recordLoss() {
        losses++;
        totalBattles++;
    }
    
    /**
     * Record a day of training for this Pokemon
     */
    public void recordTrainingDay() {
        trainingDays++;
    }
}
