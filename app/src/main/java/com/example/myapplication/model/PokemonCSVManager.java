package com.example.myapplication.model;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Manager class for saving and loading Pokemon data to/from CSV files
 */
public class PokemonCSVManager {
    private static final String TAG = "PokemonCSVManager";
    
    /**
     * Save Pokemon from storage to a CSV file
     * @param context Application context
     * @param storage Storage containing Pokemon to save
     * @param uri URI of the file to save to (selected by user)
     * @return true if save was successful
     */
    public static boolean savePokemonToCSV(Context context, Storage storage, Uri uri) {
        List<Pokemon> pokemonList = storage.listPokemons();
        return savePokemonListToCSV(context, pokemonList, uri);
    }
    
    /**
     * Save a list of Pokemon to a CSV file
     * @param context Application context
     * @param pokemonList List of Pokemon to save
     * @param uri URI of the file to save to (selected by user)
     * @return true if save was successful
     */
    public static boolean savePokemonListToCSV(Context context, List<Pokemon> pokemonList, Uri uri) {
        try {
            OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            
            // Write CSV header
            writer.write("id,name,species,hp,maxhp,attack,defense,experience,attackSkill,defendSkill");
            writer.newLine();
            
            // Write each Pokemon to the CSV file
            for (Pokemon pokemon : pokemonList) {
                String pokemonClass = pokemon.getClass().getSimpleName();
                String line = String.format("%d,%s,%s,%d,%d,%d,%d,%d,%s,%s", 
                        pokemon.getId(),
                        pokemon.getName(),
                        pokemonClass,
                        pokemon.getHP(),
                        pokemon.getMaxHP(),
                        pokemon.getAttack(),
                        pokemon.getDefense(),
                        pokemon.getExperience(),
                        pokemon.getAttackSkillName(),
                        pokemon.getDefendSkillName());
                writer.write(line);
                writer.newLine();
            }
            
            writer.close();
            outputStream.close();
            
            Log.d(TAG, "Saved " + pokemonList.size() + " Pokemon to CSV file");
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error saving Pokemon to CSV file", e);
            return false;
        }
    }
    
    /**
     * Load Pokemon from a CSV file and add them to the target storage
     * @param context Application context
     * @param targetStorage Storage to add loaded Pokemon to
     * @param uri URI of the file to load from (selected by user)
     * @return true if load was successful
     */
    public static boolean loadPokemonFromCSV(Context context, Storage targetStorage, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            
            String line;
            boolean isFirstLine = true;
            int pokemonCount = 0;
            
            // Read each line of the CSV file
            while ((line = reader.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Parse Pokemon data from CSV
                String[] data = line.split(",");
                if (data.length < 10) {
                    Log.e(TAG, "Invalid CSV data: " + line);
                    continue;
                }
                
                try {
                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    String species = data[2];
                    int hp = Integer.parseInt(data[3]);
                    int maxHp = Integer.parseInt(data[4]);
                    int attack = Integer.parseInt(data[5]);
                    int defense = Integer.parseInt(data[6]);
                    int experience = Integer.parseInt(data[7]);
                    
                    // Create the appropriate Pokemon type
                    Pokemon pokemon = null;
                    switch (species) {
                        case "Pikachu":
                            pokemon = new Pikachu(id, name, maxHp, attack, defense);
                            break;
                        case "Charizard":
                            pokemon = new Charizard(id, name, maxHp, attack, defense);
                            break;
                        case "Blastoise":
                            pokemon = new Blastoise(id, name, maxHp, attack, defense);
                            break;
                        case "Venusaur":
                            pokemon = new Venusaur(id, name, maxHp, attack, defense);
                            break;
                        case "Mewtwo":
                            pokemon = new Mewtwo(id, name, maxHp, attack, defense);
                            break;
                        default:
                            Log.e(TAG, "Unknown Pokemon species: " + species);
                            continue;
                    }
                    
                    // Set current HP and experience
                    pokemon.setHP(hp);
                    for (int i = 0; i < experience; i++) {
                        pokemon.gainExperience();
                    }
                    
                    // Add the Pokemon to the target storage
                    targetStorage.addPokemon(pokemon);
                    pokemonCount++;
                    
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Error parsing Pokemon data: " + line, e);
                }
            }
            
            reader.close();
            
            Log.d(TAG, "Loaded " + pokemonCount + " Pokemon from CSV file");
            return pokemonCount > 0;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "CSV file not found", e);
            return false;
        } catch (IOException e) {
            Log.e(TAG, "Error reading CSV file", e);
            return false;
        }
    }
}
