package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentStatisticsBinding;
import com.example.myapplication.model.PokeCenter;
import com.example.myapplication.model.Pokemon;
import com.example.myapplication.model.Storage;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private static final String TAG = "StatisticsFragment";
    private FragmentStatisticsBinding binding;
    private PokeCenter pokeCenter;
    private PokemonStatsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get PokeCenter instance
        pokeCenter = PokeCenter.getInstance();
        if (pokeCenter == null) {
            Log.e(TAG, "PokeCenter instance is null!");
            showEmptyState("Error loading PokeCenter data");
            return;
        }

        // Setup return button
        binding.returnHomeButton.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_statisticsFragment_to_homeFragment));

        // Setup RecyclerView
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.statsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Add dividers between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Collect data for all Pokemon from all areas
        List<Pokemon> allPokemons = new ArrayList<>();
        
        try {
            // Get references to all storage areas
            Storage home = pokeCenter.getHome();
            Storage training = pokeCenter.getTraining();
            Storage battle = pokeCenter.getBattle();
            
            // Check if storage areas are initialized properly
            if (home == null || training == null || battle == null) {
                Log.e(TAG, "One or more storage areas are null: Home=" + (home != null) + 
                      ", Training=" + (training != null) + 
                      ", Battle=" + (battle != null));
                showEmptyState("Error loading Pokemon storage areas");
                return;
            }
            
            // Add Pokemon from each storage area with null checks
            List<Pokemon> homePokemons = home.listPokemons();
            List<Pokemon> trainingPokemons = training.listPokemons();
            List<Pokemon> battlePokemons = battle.listPokemons();
            
            if (homePokemons != null) {
                allPokemons.addAll(homePokemons);
                Log.d(TAG, "Added " + homePokemons.size() + " Pokemon from Home");
            }
            
            if (trainingPokemons != null) {
                allPokemons.addAll(trainingPokemons);
                Log.d(TAG, "Added " + trainingPokemons.size() + " Pokemon from Training");
            }
            
            if (battlePokemons != null) {
                allPokemons.addAll(battlePokemons);
                Log.d(TAG, "Added " + battlePokemons.size() + " Pokemon from Battle");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error collecting Pokemon data: " + e.getMessage());
            e.printStackTrace();
            showEmptyState("Error collecting Pokemon data");
            return;
        }

        // Log the number of Pokemon found
        Log.d(TAG, "Total Pokemon found for stats: " + allPokemons.size());

        if (allPokemons.isEmpty()) {
            showEmptyState("No Pokemon found. Create some Pokemon first!");
            return;
        }

        // Create and set adapter
        adapter = new PokemonStatsAdapter(allPokemons, this);
        recyclerView.setAdapter(adapter);
    }
    
    private void showEmptyState(String message) {
        // Create a TextView to show a message when there's no data
        TextView emptyView = new TextView(requireContext());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        emptyView.setText(message);
        emptyView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        emptyView.setPadding(16, 64, 16, 16);
        emptyView.setTextSize(16);
        
        // Replace the RecyclerView with this TextView
        ViewGroup parent = (ViewGroup) binding.statsRecyclerView.getParent();
        int index = parent.indexOfChild(binding.statsRecyclerView);
        parent.removeViewAt(index);
        parent.addView(emptyView, index);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
