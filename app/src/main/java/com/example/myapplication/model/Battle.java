package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles turn-based battles between Pokemon
 */
public class Battle {
    private Pokemon playerPokemon;
    private Pokemon opponentPokemon;
    private List<BattleListener> listeners;
    private boolean isOver;
    private boolean isPlayerTurn;
    private int playerWins;
    private int playerLosses;
    private int opponentWins;
    private int opponentLosses;

    public Battle(Pokemon playerPokemon, Pokemon opponentPokemon) {
        this.playerPokemon = playerPokemon;
        this.opponentPokemon = opponentPokemon;
        this.listeners = new ArrayList<>();
        this.isOver = false;
        this.isPlayerTurn = true; // Player goes first
        this.playerWins = 0;
        this.playerLosses = 0;
        this.opponentWins = 0;
        this.opponentLosses = 0;
    }

    /**
     * Execute player's turn with the chosen action
     * @param actionType the type of action chosen by the player
     * @return true if battle is over, false if it continues
     */
    public boolean executePlayerTurn(ActionType actionType) {
        if (isOver || !isPlayerTurn) {
            return isOver;
        }

        // Execute player's chosen action
        switch (actionType) {
            case ATTACK:
                performAttack(playerPokemon, opponentPokemon);
                break;
            case DEFEND:
                // Increase defense temporarily for this turn
                playerPokemon.setDefense(playerPokemon.getDefense() + 2);
                notifyDefend(playerPokemon);
                break;
        }

        // Check if opponent Pokemon is defeated
        if (opponentPokemon.getHP() <= 0) {
            playerPokemon.gainExperience();
            playerPokemon.recordWin();
            opponentPokemon.recordLoss();
            notifyBattleOver(playerPokemon, opponentPokemon);
            isOver = true;
            return true;
        }

        // Switch turns
        isPlayerTurn = false;
        return executeOpponentTurn();
    }

    /**
     * Execute opponent turn automatically
     * @return true if battle is over, false if it continues
     */
    private boolean executeOpponentTurn() {
        if (isOver || isPlayerTurn) {
            return isOver;
        }

        // Reset defense bonus from previous turn if defend was used
        if (playerPokemon.getDefense() > playerPokemon.getBaseDefense()) {
            playerPokemon.setDefense(playerPokemon.getBaseDefense());
        }

        // Opponent always attacks
        performAttack(opponentPokemon, playerPokemon);

        // Check if player Pokemon is defeated
        if (playerPokemon.getHP() <= 0) {
            opponentPokemon.gainExperience();
            playerPokemon.recordLoss();
            opponentPokemon.recordWin();
            notifyBattleOver(opponentPokemon, playerPokemon);
            isOver = true;
            return true;
        }

        // Switch turns back to player
        isPlayerTurn = true;
        return false;
    }

    /**
     * Execute a single attack between two Pokemon
     */
    private void performAttack(Pokemon attacker, Pokemon defender) {
        // Create the battle log message
        String skillName = attacker.getAttackSkillName();
        // Get attack power with any multipliers already applied
        int attackPower = attacker.attack();
        int damage = attackPower - defender.getDefense();
        // Apply the damage directly
        defender.setHP(defender.getHP() - damage);
        // Notify listeners with the enhanced skill name
        notifyAttack(attacker, defender, damage, skillName);

    }

    /**
     * Reset the battle state for a new battle
     */
    public void reset() {
        isOver = false;
        isPlayerTurn = true;
        // Reset health to max
        playerPokemon.setHP(playerPokemon.getMaxHP());
        opponentPokemon.setHP(opponentPokemon.getMaxHP());
    }

    /**
     * Handle the end of battle - loser returns home with full health and loses exp
     */
    public void handleBattleEnd() {
        if (!isOver) return;
        
        Pokemon loser = playerPokemon.getHP() > 0 ? opponentPokemon : playerPokemon;
        
        // Reset loser's health and experience
        loser.setHP(loser.getMaxHP());
        loser.resetExperience();
        
        // Notify that loser is returning home
        notifyReturnHome(loser);
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public boolean isOver() {
        return isOver;
    }

    public Pokemon getPlayerPokemon() {
        return playerPokemon;
    }

    public Pokemon getOpponentPokemon() {
        return opponentPokemon;
    }

    public void addListener(BattleListener listener) {
        listeners.add(listener);
    }

    private void notifyAttack(Pokemon attacker, Pokemon defender, int damage, String skillName) {
        for (BattleListener listener : listeners) {
            listener.onAttack(attacker, defender, damage, skillName);
        }
    }

    private void notifyDefend(Pokemon pokemon) {
        for (BattleListener listener : listeners) {
            listener.onDefend(pokemon);
        }
    }

    private void notifyBattleOver(Pokemon winner, Pokemon loser) {
        for (BattleListener listener : listeners) {
            listener.onBattleOver(winner, loser);
        }
    }

    private void notifyReturnHome(Pokemon pokemon) {
        for (BattleListener listener : listeners) {
            listener.onReturnHome(pokemon);
        }
    }

    public enum ActionType {
        ATTACK,
        DEFEND
    }

    public interface BattleListener {
        void onAttack(Pokemon attacker, Pokemon defender, int damage, String skillName);
        void onDefend(Pokemon pokemon);
        void onBattleOver(Pokemon winner, Pokemon loser);
        void onReturnHome(Pokemon pokemon);
    }
}
