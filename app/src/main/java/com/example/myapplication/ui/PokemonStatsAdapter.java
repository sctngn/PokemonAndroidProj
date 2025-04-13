package com.example.myapplication.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Pokemon;

import java.util.List;
import java.util.Locale;

public class PokemonStatsAdapter extends RecyclerView.Adapter<PokemonStatsAdapter.PokemonStatsViewHolder> {

    private List<Pokemon> pokemonList;
    private StatisticsFragment parentFragment;

    public PokemonStatsAdapter(List<Pokemon> pokemonList, StatisticsFragment parentFragment) {
        this.pokemonList = pokemonList;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public PokemonStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon_stats, parent, false);
        return new PokemonStatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonStatsViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.bind(pokemon);
        
        // Add click listener to show detailed stats
        holder.itemView.setOnClickListener(v -> showDetailedStats(pokemon));
    }
    
    private void showDetailedStats(Pokemon pokemon) {
        // Create and display the detail fragment
        PokemonDetailStatsFragment detailFragment = PokemonDetailStatsFragment.newInstance(pokemon);
        
        // Use Navigation component for navigation
        Navigation.findNavController(parentFragment.requireView())
                .navigate(R.id.action_statisticsFragment_to_pokemonDetailStatsFragment,
                        detailFragment.getArguments());
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    static class PokemonStatsViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView totalBattlesTextView;
        TextView winsTextView;
        TextView lossesTextView;
        TextView winRateTextView;
        TextView trainingDaysTextView;

        public PokemonStatsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.pokemonNameTextView);
            totalBattlesTextView = itemView.findViewById(R.id.totalBattlesTextView);
            winsTextView = itemView.findViewById(R.id.winsTextView);
            lossesTextView = itemView.findViewById(R.id.lossesTextView);
            winRateTextView = itemView.findViewById(R.id.winRateTextView);
            trainingDaysTextView = itemView.findViewById(R.id.trainingDaysTextView);
        }

        public void bind(Pokemon pokemon) {
            nameTextView.setText(String.format(Locale.getDefault(), "%s (%s)", pokemon.getName(), pokemon.getSpecies()));
            totalBattlesTextView.setText(String.format(Locale.getDefault(), "Total Battles: %d", pokemon.getTotalBattles()));
            winsTextView.setText(String.format(Locale.getDefault(), "Wins: %d", pokemon.getWins()));
            lossesTextView.setText(String.format(Locale.getDefault(), "Losses: %d", pokemon.getLosses()));

            if (pokemon.getTotalBattles() > 0) {
                double winRate = (double) pokemon.getWins() / pokemon.getTotalBattles() * 100;
                winRateTextView.setText(String.format(Locale.getDefault(), "Win Rate: %.1f%%", winRate));
            } else {
                winRateTextView.setText("Win Rate: N/A");
            }

            trainingDaysTextView.setText(String.format(Locale.getDefault(), "Training Days: %d", pokemon.getTrainingDays()));
        }
    }
}
