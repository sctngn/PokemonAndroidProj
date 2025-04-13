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
import java.lang.reflect.Field;
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
            writer.write("id,name,species,hp,maxhp,attack,defense,experience,attackSkill,defendSkill,totalBattles,wins,losses,trainingDays");
            writer.newLine();
            
            // Write each Pokemon to the CSV file
            for (Pokemon pokemon : pokemonList) {
                writer.write(String.format("%d,%s,%s,%d,%d,%d,%d,%d,%s,%s,%d,%d,%d,%d",
                    pokemon.getId(),
                    pokemon.getName(),
                    pokemon.getSpecies(),
                    pokemon.getHP(),
                    pokemon.getMaxHP(),
                    pokemon.getAttack(),
                    pokemon.getDefense(),
                    pokemon.getExperience(),
                    pokemon.getAttackSkillName(),
                    pokemon.getDefendSkillName(),
                    pokemon.getTotalBattles(),
                    pokemon.getWins(),
                    pokemon.getLosses(),
                    pokemon.getTrainingDays()
                ));
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
            
            // Skip the header line
            String line = reader.readLine();
            
            // Read each line and create a Pokemon
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10) { // Need at least the basic fields
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String species = parts[2];
                    int hp = Integer.parseInt(parts[3]);
                    int maxHp = Integer.parseInt(parts[4]);
                    int attack = Integer.parseInt(parts[5]);
                    int defense = Integer.parseInt(parts[6]);
                    int experience = Integer.parseInt(parts[7]);
                    String attackSkill = parts[8];
                    String defendSkill = parts[9];
                    
                    // Create the appropriate Pokemon type
                    Pokemon pokemon = null;
                    switch (species.toLowerCase()) {
                        case "pikachu":
                            pokemon = new Pikachu(id, name, maxHp, attack, defense);
                            break;
                        case "charizard":
                            pokemon = new Charizard(id, name, maxHp, attack, defense);
                            break;
                        case "blastoise":
                            pokemon = new Blastoise(id, name, maxHp, attack, defense);
                            break;
                        case "venusaur":
                            pokemon = new Venusaur(id, name, maxHp, attack, defense);
                            break;
                        case "mewtwo":
                            pokemon = new Mewtwo(id, name, maxHp, attack, defense);
                            break;
                    }
                    
                    if (pokemon != null) {
                        // Set the current HP and experience
                        pokemon.setHP(hp);
                        pokemon.setExperience(experience);
                        
                        // Set statistics if available in CSV
                        if (parts.length >= 14) {
                            int totalBattles = Integer.parseInt(parts[10]);
                            int wins = Integer.parseInt(parts[11]);
                            int losses = Integer.parseInt(parts[12]);
                            int trainingDays = Integer.parseInt(parts[13]);
                            
                            // Set statistics using reflection to avoid adding public setters
                            try {
                                Field totalBattlesField = Pokemon.class.getDeclaredField("totalBattles");
                                totalBattlesField.setAccessible(true);
                                totalBattlesField.set(pokemon, totalBattles);
                                
                                Field winsField = Pokemon.class.getDeclaredField("wins");
                                winsField.setAccessible(true);
                                winsField.set(pokemon, wins);
                                
                                Field lossesField = Pokemon.class.getDeclaredField("losses");
                                lossesField.setAccessible(true);
                                lossesField.set(pokemon, losses);
                                
                                Field trainingDaysField = Pokemon.class.getDeclaredField("trainingDays");
                                trainingDaysField.setAccessible(true);
                                trainingDaysField.set(pokemon, trainingDays);
                            } catch (Exception e) {
                                Log.e(TAG, "Error setting statistics: " + e.getMessage());
                            }
                        }
                        
                        // Add to storage
                        targetStorage.addPokemon(pokemon);
                    }
                }
            }
            
            reader.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.e(TAG, "Error reading file: " + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error loading Pokemon: " + e.getMessage());
            return false;
        }
    }
}
