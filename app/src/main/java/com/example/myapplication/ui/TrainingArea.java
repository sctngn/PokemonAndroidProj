package com.example.myapplication.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentTrainingBinding;
import com.example.myapplication.model.Pokemon;
import com.example.myapplication.model.PokeCenter;
import java.util.List;

public class TrainingArea extends Fragment {
    private FragmentTrainingBinding binding;
    private PokeCenter pokeCenter;
    private PokemonAdapter adapter;
    private Pokemon selectedPokemon = null;
    private CountDownTimer trainingTimer = null;
    private boolean isTraining = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTrainingBinding.inflate(inflater, container, false);
        pokeCenter = PokeCenter.getInstance();
        setupViews();
        return binding.getRoot();
    }

    private void setupViews() {
        // Setup RecyclerView with selection listener
        adapter = new PokemonAdapter(pokeCenter.getTraining().listPokemons(), this::onPokemonSelected);
        binding.lutemonList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.lutemonList.setAdapter(adapter);

        // Add a status text view to the layout
        binding.titleText.setText("Training Area");

        // Setup buttons
        binding.returnHomeButton.setOnClickListener(v -> {
            // Cancel any ongoing training
            if (trainingTimer != null) {
                trainingTimer.cancel();
                isTraining = false;
            }
            
            // Move all Pokemon back to home
            for (Pokemon pokemon : pokeCenter.getTraining().listPokemons()) {
                pokeCenter.movePokemon(pokemon.getId(), pokeCenter.getTraining(), pokeCenter.getHome());
            }
            // Navigate back to home
            Navigation.findNavController(v).navigate(R.id.action_trainingFragment_to_homeFragment);
        });

        binding.trainButton.setOnClickListener(v -> {
            if (!isTraining) {
                startTraining();
            } else {
                Toast.makeText(requireContext(), "Training already in progress!", Toast.LENGTH_SHORT).show();
            }
        });
        
        binding.toBattleButton.setOnClickListener(v -> {
            // Cancel any ongoing training
            if (trainingTimer != null) {
                trainingTimer.cancel();
                isTraining = false;
            }
            
            // Move all Pokemon to battle
            for (Pokemon pokemon : pokeCenter.getTraining().listPokemons()) {
                pokeCenter.movePokemon(pokemon.getId(), pokeCenter.getTraining(), pokeCenter.getBattle());
            }
            // Navigate to battle
            Navigation.findNavController(v).navigate(R.id.action_trainingFragment_to_battleFragment);
        });
    }
    
    private void onPokemonSelected(Pokemon pokemon) {
        // Only allow selection if not currently training
        if (!isTraining) {
            selectedPokemon = pokemon;
            Toast.makeText(requireContext(), pokemon.getName() + " selected for training", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Cannot select during training", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTraining() {
        if (selectedPokemon == null) {
            Toast.makeText(requireContext(), "Please select a Pokemon to train", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Disable buttons during training
        binding.trainButton.setEnabled(false);
        binding.returnHomeButton.setEnabled(false);
        binding.toBattleButton.setEnabled(false);
        isTraining = true;
        
        // Create a 30-second countdown timer
        trainingTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update UI with remaining time
                binding.titleText.setText("Training: " + selectedPokemon.getName() + 
                                         " - " + (millisUntilFinished / 1000) + " seconds left");
            }

            @Override
            public void onFinish() {
                // Training complete
                pokeCenter.train(selectedPokemon.getId());
                
                // Update UI
                binding.titleText.setText("Training Area");
                adapter.updatePokemons(pokeCenter.getTraining().listPokemons());
                
                // Re-enable buttons
                binding.trainButton.setEnabled(true);
                binding.returnHomeButton.setEnabled(true);
                binding.toBattleButton.setEnabled(true);
                isTraining = false;
                
                // Show completion message
                Toast.makeText(requireContext(), 
                               selectedPokemon.getName() + " completed training! EXP gained.",
                               Toast.LENGTH_SHORT).show();
            }
        };
        
        // Start the timer
        trainingTimer.start();
        Toast.makeText(requireContext(), "Training started! Please wait 30 seconds.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cancel timer if fragment is destroyed
        if (trainingTimer != null) {
            trainingTimer.cancel();
        }
        binding = null;
    }
}
