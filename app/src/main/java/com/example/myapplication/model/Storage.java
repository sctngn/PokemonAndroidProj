package com.example.myapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Storage class for managing Pokemon in different areas (home, training, battle)
 */
public class Storage implements Serializable {
    private static final long serialVersionUID = 1L;
    private final HashMap<Integer, Pokemon> pokemons;
    private final String name;

    public Storage(String name) {
        this.name = name;
        this.pokemons = new HashMap<>();
    }

    /**
     * Add a Pokemon to this storage
     * @param pokemon The Pokemon to add
     */
    public void addPokemon(Pokemon pokemon) {
        pokemons.put(pokemon.getId(), pokemon);
    }

    /**
     * Get a Pokemon by its ID
     * @param id The ID of the Pokemon to retrieve
     * @return The Pokemon if found, null otherwise
     */
    public Pokemon getPokemon(int id) {
        return pokemons.get(id);
    }

    /**
     * Remove a Pokemon from this storage
     * @param id The ID of the Pokemon to remove
     * @return The removed Pokemon if found, null otherwise
     */
    public Pokemon removePokemon(int id) {
        return pokemons.remove(id);
    }

    /**
     * Get all Pokemon in this storage
     * @return List of all Pokemon
     */
    public List<Pokemon> listPokemons() {
        return new ArrayList<>(pokemons.values());
    }

    /**
     * Get the name of this storage area
     * @return Storage area name
     */
    public String getName() {
        return name;
    }


    /**
     * Clear all Pokemon from this storage
     */
    public void clearPokemons() {
        pokemons.clear();
    }
}
