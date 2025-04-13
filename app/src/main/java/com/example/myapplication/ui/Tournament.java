package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentBattleBinding;
import com.example.myapplication.model.Battle;
import com.example.myapplication.model.Pokemon;
import com.example.myapplication.model.PokeCenter;
import java.util.List;

public class Tournament extends Fragment implements Battle.BattleListener {
    private FragmentBattleBinding binding;
    private PokeCenter pokeCenter;
    private PokemonAdapter adapter;
    private Pokemon selectedPokemon1 = null;
    private Pokemon selectedPokemon2 = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBattleBinding.inflate(inflater, container, false);
        pokeCenter = PokeCenter.getInstance();
        setupViews();
        return binding.getRoot();
    }

    private void setupViews() {
        // Setup RecyclerView
        adapter = new PokemonAdapter(pokeCenter.getBattle().listPokemons(), this::onPokemonSelected);
        binding.lutemonList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.lutemonList.setAdapter(adapter);

        // Update title
        binding.titleText.setText("Tournament");

        // Setup buttons
        binding.returnHomeButton.setOnClickListener(v -> {
            // Move all Pokemon back to home
            List<Pokemon> pokemons = pokeCenter.getBattle().listPokemons();
            for (Pokemon pokemon : pokemons) {
                if (pokemon.isAlive()) {
                    pokeCenter.movePokemon(pokemon.getId(), pokeCenter.getBattle(), pokeCenter.getHome());
                }
            }
            // Navigate back to home
            Navigation.findNavController(v).navigate(R.id.action_battleFragment_to_homeFragment);
        });

        binding.startBattleButton.setOnClickListener(v -> fight());
        
        // Clear battle log
        binding.battleLogText.setText("");
    }

    private void onPokemonSelected(Pokemon pokemon) {
        if (selectedPokemon1 == null) {
            selectedPokemon1 = pokemon;
            binding.battleLogText.append("First Pokemon selected: " + pokemon.getName() + "\n");
        } else if (selectedPokemon2 == null && pokemon != selectedPokemon1) {
            selectedPokemon2 = pokemon;
            binding.battleLogText.append("Second Pokemon selected: " + pokemon.getName() + "\n");
            binding.battleLogText.append("Ready to battle! Press the Start Battle button.\n");
        } else {
            // Reset selections
            selectedPokemon1 = pokemon;
            selectedPokemon2 = null;
            binding.battleLogText.setText("First Pokemon selected: " + pokemon.getName() + "\n");
        }
    }

    private void fight() {
        if (selectedPokemon1 == null || selectedPokemon2 == null) {
            Toast.makeText(requireContext(), "Please select two Pokemon for battle", Toast.LENGTH_SHORT).show();
            return;
        }

        // Clear previous battle log
        binding.battleLogText.setText("");
        binding.battleLogText.append("Battle started: " + selectedPokemon1.getName() + " vs " + selectedPokemon2.getName() + "\n\n");

        // Start battle
        Battle battle = new Battle(selectedPokemon1, selectedPokemon2);
        battle.addListener(this);
        Pokemon winner = battle.runFullBattle();

        // Update UI
        adapter.updatePokemons(pokeCenter.getBattle().listPokemons());
        
        // Reset selections
        selectedPokemon1 = null;
        selectedPokemon2 = null;
    }

    @Override
    public void onAttack(Pokemon attacker, Pokemon defender, int damage, boolean isSpecialAttack, double multiplier) {
        StringBuilder attackMessage = new StringBuilder();
        attackMessage.append(attacker.getName()).append(" attacks ").append(defender.getName());
        
        // Add special attack information if applicable
        if (isSpecialAttack) {
            if (attacker.getSpecies().equals("Pikachu")) {
                if (multiplier < 1.0) {
                    attackMessage.append(" with a weak Thunderbolt (0.5x)");
                } else if (multiplier > 1.0) {
                    attackMessage.append(" with a critical Thunderbolt (2x)");
                }
            }
        }
        
        attackMessage.append(" for ").append(damage).append(" damage!\n");
        binding.battleLogText.append(attackMessage.toString());
        binding.battleLogText.append(defender.getName() + " HP: " + defender.getHP() + "/" + defender.getMaxHP() + "\n\n");
    }

    @Override
    public void onBattleOver(Pokemon winner, Pokemon loser) {
        binding.battleLogText.append("Battle over! " + winner.getName() + " wins!\n");
        binding.battleLogText.append(loser.getName() + " is defeated.\n\n");
        
        // Remove defeated Pokemon
        pokeCenter.getBattle().removePokemon(loser.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
