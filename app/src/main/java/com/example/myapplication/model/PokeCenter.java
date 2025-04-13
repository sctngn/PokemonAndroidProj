package com.example.myapplication.model;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

/**
 * Manages all Pokemon and their movement between different areas
 */
public class PokeCenter {
    private static PokeCenter instance;
    private String name;
    private final Storage home;
    private final Storage training;
    private final Storage battle;
    private Context context;
    private Uri lastSelectedSaveUri;
    private Uri lastSelectedLoadUri;

    /**
     * Private constructor for singleton pattern
     */
    private PokeCenter() {
        this.name = "Poke Center";
        home = new Storage("Home");
        training = new Storage("Training Area");
        battle = new Storage("Tournament");
    }

    public static PokeCenter getInstance() {
        if (instance == null) {
            instance = new PokeCenter();
        }
        return instance;
    }

    /**
     * Set the application context for saving/loading
     * @param context The application context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Catch a Pokemon and add it to storage
     * @param pokemon The Pokemon to catch
     */
    public void catchPokemon(Pokemon pokemon) {
        home.addPokemon(pokemon);
    }

    /**
     * Get a Pokemon by its ID
     * @param id The ID of the Pokemon to retrieve
     * @return The Pokemon if found, null otherwise
     */
    public Pokemon getPokemon(int id) {
        // Check all storage areas
        Pokemon pokemon = home.getPokemon(id);
        if (pokemon != null) return pokemon;
        
        pokemon = training.getPokemon(id);
        if (pokemon != null) return pokemon;
        
        return battle.getPokemon(id);
    }

    /**
     * Create a new Pokemon with the given name and species
     * @param name Name of the Pokemon
     * @param species Type of Pokemon to create
     * @return The created Pokemon or null if species is invalid
     */
    public Pokemon createPokemon(String name, String species) {
        // Generate a unique ID for the new Pokemon
        int id = generateNextId();
        
        // Create the appropriate Pokemon type
        Pokemon pokemon = null;
        switch (species.toLowerCase()) {
            case "pikachu":
                pokemon = new Pikachu(id, name, 10, 8, 2);
                break;
            case "charizard":
                pokemon = new Charizard(id, name, 12, 6, 4);
                break;
            case "blastoise":
                pokemon = new Blastoise(id, name, 11, 4, 6);
                break;
            case "venusaur":
                pokemon = new Venusaur(id, name, 10, 6, 5);
                break;
            case "mewtwo":
                pokemon = new Mewtwo(id, name, 18, 8, 5);
                break;
        }
        
        // Add the created Pokemon to home storage
        if (pokemon != null) {
            home.addPokemon(pokemon);
        }
        
        return pokemon;
    }
    
    /**
     * Generate the next unique ID for a new Pokemon
     * @return A unique ID
     */
    private int generateNextId() {
        int maxId = 0;
        
        // Check home storage
        for (Pokemon pokemon : home.listPokemons()) {
            maxId = Math.max(maxId, pokemon.getId());
        }
        
        // Check training storage
        for (Pokemon pokemon : training.listPokemons()) {
            maxId = Math.max(maxId, pokemon.getId());
        }
        
        // Check battle storage
        for (Pokemon pokemon : battle.listPokemons()) {
            maxId = Math.max(maxId, pokemon.getId());
        }
        
        // Return max ID + 1
        return maxId + 1;
    }

    /**
     * Open the PC to access Pokemon
     */
    public void openPC() {
        // Implementation for opening the PC interface
    }

    /**
     * Move a Pokemon from one area to another
     * @param pokemonId ID of the Pokemon to move
     * @param from Source storage area
     * @param to Destination storage area
     * @return true if move was successful, false otherwise
     */
    public boolean movePokemon(int pokemonId, Storage from, Storage to) {
        Pokemon pokemon = from.removePokemon(pokemonId);
        if (pokemon != null) {
            to.addPokemon(pokemon);
            return true;
        }
        return false;
    }

    /**
     * Train a Pokemon to gain experience
     * @param pokemonId ID of the Pokemon to train
     * @return true if training was successful, false if Pokemon not found in training area
     */
    public boolean train(int pokemonId) {
        Pokemon pokemon = training.getPokemon(pokemonId);
        if (pokemon != null) {
            pokemon.gainExperience();
            return true;
        }
        return false;
    }

    /**
     * Train a Pokemon to increase its stats
     * @param pokemon Pokemon to train
     */
    public void trainPokemon(Pokemon pokemon) {
        if (pokemon != null) {
            // Increase experience
            pokemon.gainExperience();
            
            // Record training day
            pokemon.recordTrainingDay();
        }
    }

    /**
     * Start a battle between two Pokemon in the battle arena
     * @param pokemonAId ID of first Pokemon
     * @param pokemonBId ID of second Pokemon
     * @return The battle instance, or null if either Pokemon not found
     */
    public Battle fight(int pokemonAId, int pokemonBId) {
        Pokemon pokemonA = battle.getPokemon(pokemonAId);
        Pokemon pokemonB = battle.getPokemon(pokemonBId);
        
        if (pokemonA != null && pokemonB != null) {
            return new Battle(pokemonA, pokemonB);
        }
        return null;
    }

    /**
     * Set the URI for saving Pokemon CSV files
     * @param uri URI selected by the user for saving
     */
    public void setSelectedSaveUri(Uri uri) {
        this.lastSelectedSaveUri = uri;
    }
    
    /**
     * Set the URI for loading Pokemon CSV files
     * @param uri URI selected by the user for loading
     */
    public void setSelectedLoadUri(Uri uri) {
        this.lastSelectedLoadUri = uri;
    }
    
    /**
     * Save all Pokemon to a CSV file
     * @return true if save was successful
     */
    public boolean savePokemonToCSV() {
        if (context == null || lastSelectedSaveUri == null) {
            return false;
        }
        
        // Combine Pokemon from all storages
        Storage allPokemon = new Storage("All");
        for (Pokemon pokemon : home.listPokemons()) {
            allPokemon.addPokemon(pokemon);
        }
        for (Pokemon pokemon : training.listPokemons()) {
            allPokemon.addPokemon(pokemon);
        }
        for (Pokemon pokemon : battle.listPokemons()) {
            allPokemon.addPokemon(pokemon);
        }
        
        return PokemonCSVManager.savePokemonToCSV(context, allPokemon, lastSelectedSaveUri);
    }
    
    /**
     * Load Pokemon from a CSV file
     * @return true if load was successful
     */
    public boolean loadPokemonFromCSV() {
        if (context == null || lastSelectedLoadUri == null) {
            return false;
        }
        

        return PokemonCSVManager.loadPokemonFromCSV(context, home, lastSelectedLoadUri);
    }

    // Getters for storage areas
    public Storage getHome() {
        return home;
    }
    
    public Storage getTraining() {
        return training;
    }
    
    public Storage getBattle() {
        return battle;
    }
    
    public String getName() {
        return name;
    }
}
