package com.example.myapplication.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentBattleSceneBinding;
import com.example.myapplication.model.Battle;
import com.example.myapplication.model.Pokemon;
import com.example.myapplication.model.PokeCenter;

/**
 * Fragment for the actual battle scene
 */
public class BattleScene extends Fragment implements Battle.BattleListener {
    private FragmentBattleSceneBinding binding;
    private PokeCenter pokeCenter;
    private Battle battle;
    private Pokemon playerPokemon;
    private Pokemon opponentPokemon;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean attackEffectInProgress = false;
    private String pendingAttackEffect = null;
    private boolean isPendingPlayerAttack = false;
    
    // Use this factory method to create a new instance with Pokemon IDs
    public static BattleScene newInstance(int playerPokemonId, int opponentPokemonId) {
        BattleScene fragment = new BattleScene();
        Bundle args = new Bundle();
        args.putInt("playerPokemonId", playerPokemonId);
        args.putInt("opponentPokemonId", opponentPokemonId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBattleSceneBinding.inflate(inflater, container, false);
        pokeCenter = PokeCenter.getInstance();
        
        // Get Pokemon IDs from arguments
        if (getArguments() != null) {
            int playerPokemonId = getArguments().getInt("playerPokemonId", -1);
            int opponentPokemonId = getArguments().getInt("opponentPokemonId", -1);
            
            // Get the Pokemon objects
            playerPokemon = pokeCenter.getBattle().getPokemon(playerPokemonId);
            opponentPokemon = pokeCenter.getBattle().getPokemon(opponentPokemonId);
            
            if (playerPokemon != null && opponentPokemon != null) {
                setupBattle();
            } else {
                Toast.makeText(requireContext(), "Error: Pokemon not found", Toast.LENGTH_SHORT).show();
                returnToSelection();
            }
        } else {
            Toast.makeText(requireContext(), "Error: No Pokemon selected", Toast.LENGTH_SHORT).show();
            returnToSelection();
        }
        
        return binding.getRoot();
    }
    
    private void setupBattle() {
        // Setup Pokemon info
        updatePokemonDisplay();
        
        // Create battle
        battle = new Battle(playerPokemon, opponentPokemon);
        battle.addListener(this);
        
        // Setup action buttons
        binding.attackButton.setOnClickListener(v -> {
            if (battle != null && !battle.isOver() && battle.isPlayerTurn()) {
                battle.executePlayerTurn(Battle.ActionType.ATTACK);
                updatePokemonDisplay();
            }
        });
        
        binding.defendButton.setOnClickListener(v -> {
            if (battle != null && !battle.isOver() && battle.isPlayerTurn()) {
                battle.executePlayerTurn(Battle.ActionType.DEFEND);
                updatePokemonDisplay();
            }
        });
        
        // Hide special button
        binding.specialButton.setVisibility(View.GONE);
        
        binding.returnHomeButton.setOnClickListener(v -> {
            if (battle != null && !battle.isOver()) {
                binding.battleLogText.append("Battle cancelled.\n");
            }
            returnToHome();
        });
        
        // Start battle log
        binding.battleLogText.setText("");
        binding.battleLogText.append("Battle started: " + playerPokemon.getName() + 
                                   " vs " + opponentPokemon.getName() + "\n\n");
        binding.battleLogText.append("Your turn! Choose your action.\n");
    }
    
    private void updatePokemonDisplay() {
        if (playerPokemon == null || opponentPokemon == null) return;
        
        // Update player Pokemon
        binding.playerNameText.setText(playerPokemon.getName());
        binding.playerSpeciesText.setText(playerPokemon.getSpecies());
        binding.playerStatsText.setText(String.format("HP: %d/%d | ATK: %d | DEF: %d", 
                playerPokemon.getHP(), playerPokemon.getMaxHP(), 
                playerPokemon.getAttack(), playerPokemon.getDefense()));
        
        // Update player health bar
        int playerHealthPercent = (int)((float)playerPokemon.getHP() / playerPokemon.getMaxHP() * 100);
        binding.playerHealthBar.setProgress(playerHealthPercent);
        
        // Set player Pokemon image
        binding.playerPokemonImage.setImageResource(playerPokemon.getImageResourceId());
        
        // Update opponent Pokemon
        binding.opponentNameText.setText(opponentPokemon.getName());
        binding.opponentSpeciesText.setText(opponentPokemon.getSpecies());
        binding.opponentStatsText.setText(String.format("HP: %d/%d | ATK: %d | DEF: %d", 
                opponentPokemon.getHP(), opponentPokemon.getMaxHP(), 
                opponentPokemon.getAttack(), opponentPokemon.getDefense()));
        
        // Update opponent health bar
        int opponentHealthPercent = (int)((float)opponentPokemon.getHP() / opponentPokemon.getMaxHP() * 100);
        binding.opponentHealthBar.setProgress(opponentHealthPercent);
        
        // Set opponent Pokemon image
        binding.opponentPokemonImage.setImageResource(opponentPokemon.getImageResourceId());
        
        // Disable buttons if battle is over
        if (battle != null && battle.isOver()) {
            binding.attackButton.setEnabled(false);
            binding.defendButton.setEnabled(false);
        }
    }
    
    private void returnToSelection() {
        Navigation.findNavController(requireView()).popBackStack();
    }
    
    private void returnToHome() {
        // Handle end of battle effects if battle is over
        if (battle != null && battle.isOver()) {
            battle.handleBattleEnd();
        }
        
        // Move all Pokemon back to home
        for (Pokemon pokemon : pokeCenter.getBattle().listPokemons()) {
            if (pokemon.isAlive()) {
                pokeCenter.movePokemon(pokemon.getId(), pokeCenter.getBattle(), pokeCenter.getHome());
            }
        }
        
        // Navigate to home
        Navigation.findNavController(requireView())
                .navigate(R.id.action_battleSceneFragment_to_homeFragment);
    }

    /**
     * Show attack effect animation based on the skill name
     */
    private void showAttackEffect(String skillName, boolean isPlayerAttack) {
        // If there's already an effect playing, queue this one
        if (attackEffectInProgress) {
            pendingAttackEffect = skillName;
            isPendingPlayerAttack = isPlayerAttack;
            return;
        }
        
        // Mark that an effect is in progress
        attackEffectInProgress = true;
        
        // Make effect image visible
        binding.attackEffectImage.setVisibility(View.VISIBLE);
        
        // Get the appropriate effect resource based on skill name
        int effectResourceId = getEffectResourceForSkill(skillName);
        
        // Always show effects at full opacity
        binding.attackEffectImage.setAlpha(1.0f);
        
        // Load and play the effect animation
        Glide.with(this)
            .asGif()
            .load(effectResourceId)
            .into(binding.attackEffectImage);
        
        // Hide the effect after animation and play the next effect if queued
        handler.postDelayed(() -> {
            if (binding != null && binding.attackEffectImage != null) {
                binding.attackEffectImage.setVisibility(View.GONE);
                
                // Mark that this effect is done
                attackEffectInProgress = false;
                
                // Check if we have a pending effect to show
                if (pendingAttackEffect != null) {
                    String nextEffect = pendingAttackEffect;
                    boolean nextIsPlayerAttack = isPendingPlayerAttack;
                    
                    // Clear the pending effect
                    pendingAttackEffect = null;
                    
                    // Show the next effect after a short delay
                    handler.postDelayed(() -> {
                        showAttackEffect(nextEffect, nextIsPlayerAttack);
                    }, 300); // 300ms delay between effects
                }
            }
        }, 3000); // 3 seconds to show effects
    }
    
    /**
     * Get the appropriate effect resource based on skill name
     */
    private int getEffectResourceForSkill(String skillName) {
        String normalizedSkillName = skillName.toLowerCase();
        
        // Strip any descriptive text in parentheses
        if (normalizedSkillName.contains("(")) {
            normalizedSkillName = normalizedSkillName.substring(0, normalizedSkillName.indexOf("(")).trim();
        }
        
        // Match skill name to effect resource
        switch (normalizedSkillName) {
            case "thunderbolt":
                return R.drawable.thunderbolt_effect;
            case "flamethrower":
                return R.drawable.flamethrower_effect;
            case "hydro pump":
                return R.drawable.hydropump_effect;
            case "razor leaf":
                return R.drawable.razorleaf_effect;
            case "psychic":
                return R.drawable.psystrike_effect;
            default:
                // Use thunderbolt as default if no match
                return R.drawable.thunderbolt_effect;
        }
    }

    @Override
    public void onAttack(Pokemon attacker, Pokemon defender, int damage, String skillName) {
        binding.battleLogText.append(attacker.getName() + " used " + skillName + 
                                   " on " + defender.getName() + "!\n");
        binding.battleLogText.append(defender.getName() + " HP: " + 
                                   defender.getHP() + "/" + defender.getMaxHP() + "\n\n");
        
        // Show the attack effect animation
        showAttackEffect(skillName, attacker == playerPokemon);
        
        // Update display
        updatePokemonDisplay();
    }
    
    @Override
    public void onDefend(Pokemon pokemon) {
        binding.battleLogText.append(pokemon.getName() + " used " + pokemon.getDefendSkillName() + 
                                   " (+2 DEF)\n");
        
        // Update display
        updatePokemonDisplay();
    }

    @Override
    public void onBattleOver(Pokemon winner, Pokemon loser) {
        binding.battleLogText.append("Battle over! " + winner.getName() + " wins!\n");
        binding.battleLogText.append(winner.getName() + " gained 1 experience point.\n\n");
        
        // Update display
        updatePokemonDisplay();
    }
    
    @Override
    public void onReturnHome(Pokemon pokemon) {
        binding.battleLogText.append(pokemon.getName() + " returns home to recover.\n");
        binding.battleLogText.append("All experience points have been reset.\n\n");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null); // Clean up any pending animations
        binding = null;
    }
}
