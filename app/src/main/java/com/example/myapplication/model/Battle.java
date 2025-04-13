package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles turn-based battles between Pokemon
 */
public class Battle {
    private Pokemon pokemonA;
    private Pokemon pokemonB;
    private List<BattleListener> listeners;
    private boolean isOver;

    public Battle(Pokemon pokemonA, Pokemon pokemonB) {
        this.pokemonA = pokemonA;
        this.pokemonB = pokemonB;
        this.listeners = new ArrayList<>();
        this.isOver = false;
    }

    /**
     * Execute one turn of battle
     * @return true if battle is over, false if it continues
     */
    public boolean executeTurn() {
        if (isOver) {
            return true;
        }

        // First Pokemon attacks
        int baseAttack = pokemonA.getBaseAttack() + pokemonA.getExp();
        int actualAttack = pokemonA.attack();
        
        // Check if this is a special attack (different from base attack)
        boolean isSpecialAttack = actualAttack != baseAttack;
        
        // Apply the attack
        pokemonB.defense(pokemonA);
        int damage = Math.max(0, actualAttack - pokemonB.getDefense());
        
        // Notify listeners with special attack info
        notifyAttack(pokemonA, pokemonB, damage, isSpecialAttack, (double)actualAttack / baseAttack);

        // Check if defender survived
        if (!pokemonB.isAlive()) {
            pokemonA.gainExperience(1);
            notifyBattleOver(pokemonA, pokemonB);
            isOver = true;
            return true;
        }

        // Swap roles for next turn
        Pokemon temp = pokemonA;
        pokemonA = pokemonB;
        pokemonB = temp;

        return false;
    }

    /**
     * Run the entire battle until completion
     * @return The winning Pokemon
     */
    public Pokemon runFullBattle() {
        while (!executeTurn()) {
            // Battle continues
        }
        return pokemonA.isAlive() ? pokemonA : pokemonB;
    }

    public void addListener(BattleListener listener) {
        listeners.add(listener);
    }

    private void notifyAttack(Pokemon attacker, Pokemon defender, int damage, boolean isSpecialAttack, double multiplier) {
        for (BattleListener listener : listeners) {
            listener.onAttack(attacker, defender, damage, isSpecialAttack, multiplier);
        }
    }

    private void notifyBattleOver(Pokemon winner, Pokemon loser) {
        for (BattleListener listener : listeners) {
            listener.onBattleOver(winner, loser);
        }
    }

    public interface BattleListener {
        void onAttack(Pokemon attacker, Pokemon defender, int damage, boolean isSpecialAttack, double multiplier);
        void onBattleOver(Pokemon winner, Pokemon loser);
    }
}
