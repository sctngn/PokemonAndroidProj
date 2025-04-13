package com.example.myapplication.model;

import java.io.Serializable;

/**
 * Base class for all Pokemon in the game.
 * Each Pokemon has stats like attack, defense, and health that affect battle performance.
 */
public abstract class Pokemon implements Serializable {
    private String name;
    private String species;
    private int attack;
    private int baseDefense; // Store original defense value
    private int defense;
    private int exp = 0;
    private int maxHP;
    private int HP;
    private static int idCounter = 0;
    private int id;

    public Pokemon(String name, String species, int attack, int defense, int maxHP) {
        this.id = idCounter++;
        this.name = name;
        this.species = species;
        this.attack = attack;
        this.baseDefense = defense; // Store original defense
        this.defense = defense;
        this.maxHP = maxHP;
        this.HP = maxHP;
        this.exp = 0;
    }

    /**
     * Performs an attack on another Pokemon
     * @param target The Pokemon being attacked
     * @return The amount of damage dealt
     */
    public int attack() {
        return attack + exp;
    }

    /**
     * Get the name of the attack skill
     * @return Name of the attack skill
     */
    public abstract String getAttackSkillName();

    /**
     * Get the name of the defend skill
     * @return Name of the defend skill
     */
    public abstract String getDefendSkillName();

    /**
     * Get the image resource ID for this Pokemon
     * @return Resource ID for the Pokemon's image
     */
    public int getImageResourceId() {
        // Default implementation returns a placeholder
        // Subclasses should override this to return their specific image
        return android.R.drawable.ic_menu_gallery;
    }

    /**
     * Defends against an attack from another Pokemon
     * @param attacker The Pokemon attacking this one
     * @return The amount of damage taken
     */
    public Pokemon defense(Pokemon attacker) {
        int totalAttack = attacker.attack();
        int damage = Math.max(0, totalAttack - (this.defense));
        this.HP = Math.max(0, this.HP - damage);
        return this;
    }

    /**
     * Gains experience points, which increase attack power
     * @param points Amount of experience to gain
     */
    public void gainExperience(int points) {
        exp += points;
    }

    /**
     * Fully restores health to maximum
     */
    public void heal() {
        HP = maxHP;
    }

    /**
     * Resets experience points to zero
     */
    public void resetExperience() {
        exp = 0;
    }

    /**
     * Checks if the Pokemon is still alive
     * @return true if health > 0, false otherwise
     */
    public boolean isAlive() {
        return HP > 0;
    }

    /**
     * Get the total number of Pokemon created
     * @return The number of Pokemon created
     */
    public static int getNumberOfCreatedPokemons() {
        return idCounter;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public int getBaseAttack() {
        return attack;
    }

    public int getAttack() {
        return attack + exp;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public int getExp() {
        return exp;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int hp) {
        this.HP = Math.max(0, Math.min(hp, maxHP));
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return species + ": " + name + " (ATK: " + getAttack() + ", DEF: " + defense + ", HP: " + HP + "/" + maxHP + ")";
    }
}
