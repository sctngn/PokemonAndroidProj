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
import com.example.myapplication.model.Pokemon;
import com.example.myapplication.model.PokeCenter;
import java.util.List;

/**
 * Tournament fragment for selecting Pokemon for battle
 */
public class Tournament extends Fragment {
    private FragmentBattleBinding binding;
    private PokeCenter pokeCenter;
    private PokemonAdapter adapter;
    private Pokemon selectedPlayerPokemon = null;
    private Pokemon selectedOpponentPokemon = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBattleBinding.inflate(inflater, container, false);
        pokeCenter = PokeCenter.getInstance();
        setupViews();
        return binding.getRoot();
    }

    private void setupViews() {
        // Setup RecyclerView
        adapter = new PokemonAdapter(pokeCenter.getBattle().listPokemons());
        adapter.setOnPokemonClickListener(this::onPokemonSelected);
        binding.lutemonList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.lutemonList.setAdapter(adapter);

        // Update title
        binding.titleText.setText("Select Pokemon for Battle");

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

        binding.startBattleButton.setOnClickListener(v -> startBattle());
        
        // Clear battle log
        binding.battleLogText.setText("Select your Pokemon for battle.\n" +
                                     "First selection: Your Pokemon\n" +
                                     "Second selection: Opponent Pokemon (AI controlled)\n");
    }

    private void onPokemonSelected(Pokemon pokemon) {
        if (selectedPlayerPokemon == null) {
            // First selection is player's Pokemon
            selectedPlayerPokemon = pokemon;
            binding.battleLogText.append("You selected: " + pokemon.getName() + " (" + pokemon.getSpecies() + ")\n");
            binding.battleLogText.append("Now select an opponent Pokemon.\n");
        } else if (selectedOpponentPokemon == null && pokemon.getId() != selectedPlayerPokemon.getId()) {
            // Second selection is opponent Pokemon
            selectedOpponentPokemon = pokemon;
            binding.battleLogText.append("Opponent selected: " + pokemon.getName() + " (" + pokemon.getSpecies() + ")\n");
            binding.battleLogText.append("Ready for battle! Press 'Start Battle' button.\n");
            binding.startBattleButton.setEnabled(true);
        } else if (pokemon.getId() == selectedPlayerPokemon.getId()) {
            // Clicked on already selected player Pokemon
            Toast.makeText(requireContext(), "This Pokemon is already selected as your Pokemon", Toast.LENGTH_SHORT).show();
        } else {
            // Reset selections
            selectedPlayerPokemon = pokemon;
            selectedOpponentPokemon = null;
            binding.battleLogText.setText("You selected: " + pokemon.getName() + " (" + pokemon.getSpecies() + ")\n");
            binding.battleLogText.append("Now select an opponent Pokemon.\n");
            binding.startBattleButton.setEnabled(false);
        }
    }

    private void startBattle() {
        if (selectedPlayerPokemon == null || selectedOpponentPokemon == null) {
            Toast.makeText(requireContext(), "Please select both Pokemon for battle", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Create bundle with Pokemon IDs
        Bundle args = new Bundle();
        args.putInt("playerPokemonId", selectedPlayerPokemon.getId());
        args.putInt("opponentPokemonId", selectedOpponentPokemon.getId());
        
        // Navigate to battle scene with arguments
        Navigation.findNavController(requireView())
                .navigate(R.id.action_battleFragment_to_battleSceneFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
