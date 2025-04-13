package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.myapplication.R;
import com.example.myapplication.model.Pokemon;

import java.util.ArrayList;
import java.util.List;

public class PokemonDetailStatsFragment extends Fragment {
    
    private static final String ARG_POKEMON_NAME = "pokemonName";
    private static final String ARG_POKEMON_SPECIES = "pokemonSpecies";
    private static final String ARG_POKEMON_TOTAL_BATTLES = "pokemonTotalBattles";
    private static final String ARG_POKEMON_WINS = "pokemonWins";
    private static final String ARG_POKEMON_LOSSES = "pokemonLosses";
    private static final String ARG_POKEMON_TRAINING_DAYS = "pokemonTrainingDays";
    
    private String pokemonName;
    private String pokemonSpecies;
    private int totalBattles;
    private int wins;
    private int losses;
    private int trainingDays;
    
    public static PokemonDetailStatsFragment newInstance(Pokemon pokemon) {
        PokemonDetailStatsFragment fragment = new PokemonDetailStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POKEMON_NAME, pokemon.getName());
        args.putString(ARG_POKEMON_SPECIES, pokemon.getSpecies());
        args.putInt(ARG_POKEMON_TOTAL_BATTLES, pokemon.getTotalBattles());
        args.putInt(ARG_POKEMON_WINS, pokemon.getWins());
        args.putInt(ARG_POKEMON_LOSSES, pokemon.getLosses());
        args.putInt(ARG_POKEMON_TRAINING_DAYS, pokemon.getTrainingDays());
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pokemonName = getArguments().getString(ARG_POKEMON_NAME);
            pokemonSpecies = getArguments().getString(ARG_POKEMON_SPECIES);
            totalBattles = getArguments().getInt(ARG_POKEMON_TOTAL_BATTLES);
            wins = getArguments().getInt(ARG_POKEMON_WINS);
            losses = getArguments().getInt(ARG_POKEMON_LOSSES);
            trainingDays = getArguments().getInt(ARG_POKEMON_TRAINING_DAYS);
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pokemon_detail_stats, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Set up back button
        view.findViewById(R.id.backButton).setOnClickListener(v -> {
            // Navigate back using Navigation component
            Navigation.findNavController(v).navigateUp();
        });
        
        // Set up Pokemon name and battle info
        TextView nameTextView = view.findViewById(R.id.pokemonNameTextView);
        TextView battlesInfoTextView = view.findViewById(R.id.battlesInfoTextView);
        
        nameTextView.setText(String.format("%s (%s)", pokemonName, pokemonSpecies));
        battlesInfoTextView.setText(String.format("Total Battles: %d\nWins: %d\nLosses: %d\nTraining Days: %d", 
                totalBattles, wins, losses, trainingDays));
        
        // Set up pie chart
        setupWinRateChart(view);
    }
    
    private void setupWinRateChart(View view) {
        AnyChartView chartView = view.findViewById(R.id.winRateChartView);
        
        Pie pie = AnyChart.pie();
        
        List<DataEntry> data = new ArrayList<>();
        
        // Only create chart if there have been battles
        if (totalBattles > 0) {
            // Add data for wins and losses
            data.add(new ValueDataEntry("Wins", wins));
            data.add(new ValueDataEntry("Losses", losses));
            
            pie.title("Win/Loss Ratio for " + pokemonName);
            
            // Configure pie chart
            pie.data(data);
            
            pie.labels().position("outside");
            
            pie.legend()
                    .position("center-bottom")
                    .itemsLayout(LegendLayout.HORIZONTAL)
                    .align(Align.CENTER);
            
            chartView.setChart(pie);
        } else {
            // No battles yet
            data.add(new ValueDataEntry("No Battles", 1));
            pie.data(data);
            pie.title("No battle data yet for " + pokemonName);
            chartView.setChart(pie);
        }
    }
}
