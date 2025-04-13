package com.example.myapplication.model;

/**
 * Base class for all Pokemon in the game.
 * Each Pokemon has stats like attack, defense, and health that affect battle performance.
 */
public class Pokemon {
    private String name;
    private String species;
    private int attack;
    private int defense;
    private int exp = 0;
    private int HP;
    private int maxHP;
    private static int idCounter = 0;
    private int id;

    public Pokemon(String name, String species, int attack, int defense, int maxHP) {
        this.name = name;
        this.species = species;
        this.attack = attack;
        this.defense = defense;
        this.maxHP = maxHP;
        this.HP = maxHP;
        this.id = idCounter++;
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
     * Defends against an attack from another Pokemon
     * @param attacker The Pokemon attacking this one
     * @return The amount of damage taken
     */
    public Pokemon defense(Pokemon attacker) {
        int totalAttack = attacker.attack();
        int damage = Math.max(0, totalAttack - this.defense);
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

    public int getAttack() {
        return attack + exp;
    }

    public int getBaseAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getExp() {
        return exp;
    }

    public int getHP() {
        return HP;
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
