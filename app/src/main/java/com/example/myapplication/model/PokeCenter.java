package com.example.myapplication.model;

/**
 * Manages all Pokemon and their movement between different areas
 */
public class PokeCenter {
    private static PokeCenter instance;
    private String name;
    private final Storage home;
    private final Storage training;
    private final Storage battle;

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
     * Create a new Pokemon of the specified species
     * @param name Name of the Pokemon
     * @param species Species of the Pokemon (Pikachu, Venusaur, Charizard, Blastoise, Mewtwo)
     * @return The created Pokemon
     */
    public Pokemon createPokemon(String name, String species) {
        Pokemon pokemon;
        switch (species.toLowerCase()) {
            case "pikachu":
                pokemon = new Pikachu(name);
                break;
            case "venusaur":
                pokemon = new Venusaur(name);
                break;
            case "charizard":
                pokemon = new Charizard(name);
                break;
            case "blastoise":
                pokemon = new Blastoise(name);
                break;
            case "mewtwo":
                pokemon = new Mewtwo(name);
                break;
            default:
                throw new IllegalArgumentException("Invalid Pokemon species: " + species);
        }
        home.addPokemon(pokemon);
        return pokemon;
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
     * Train a Pokemon in the training area
     * @param pokemonId ID of the Pokemon to train
     * @return true if training was successful, false if Pokemon not found in training area
     */
    public boolean train(int pokemonId) {
        Pokemon pokemon = training.getPokemon(pokemonId);
        if (pokemon != null) {
            pokemon.gainExperience(1);
            return true;
        }
        return false;
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
