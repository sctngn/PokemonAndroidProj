package com.example.myapplication.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.model.Pokemon;
import com.example.myapplication.model.PokeCenter;
import com.example.myapplication.util.FilePickerHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeScreen extends Fragment {
    private FragmentHomeBinding binding;
    private PokeCenter pokeCenter;
    private PokemonAdapter adapter;
    private RadioButton selectedColor = null;
    private FilePickerHelper filePickerHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        pokeCenter = PokeCenter.getInstance();
        
        // Initialize file picker helper
        filePickerHelper = new FilePickerHelper(
            this, 
            uri -> onSaveUriSelected(uri), 
            uri -> onLoadUriSelected(uri)
        );
        
        setupViews();
        return binding.getRoot();
    }

    private void setupViews() {
        // Setup RecyclerView
        adapter = new PokemonAdapter(pokeCenter.getHome().listPokemons());
        binding.lutemonList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.lutemonList.setAdapter(adapter);

        // Setup create button
        binding.createButton.setOnClickListener(v -> createPokemon());

        // Setup area buttons
        binding.homeButton.setOnClickListener(v -> showAreaPokemons("home"));
        binding.trainingButton.setOnClickListener(v -> {
            // First move selected Pokemon to training
            List<Pokemon> pokemons = pokeCenter.getHome().listPokemons();
            for (Pokemon pokemon : pokemons) {
                pokeCenter.movePokemon(pokemon.getId(), pokeCenter.getHome(), pokeCenter.getTraining());
            }
            // Then navigate to training fragment
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_trainingFragment);
        });
        binding.battleButton.setOnClickListener(v -> {
            // First move selected Pokemon to battle
            List<Pokemon> pokemons = pokeCenter.getHome().listPokemons();
            for (Pokemon pokemon : pokemons) {
                pokeCenter.movePokemon(pokemon.getId(), pokeCenter.getHome(), pokeCenter.getBattle());
            }
            // Then navigate to battle fragment
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_battleFragment);
        });
        
        // Setup save/load buttons
        binding.saveButton.setOnClickListener(v -> launchSaveFilePicker());
        binding.loadButton.setOnClickListener(v -> launchLoadFilePicker());
        
        // Setup direct color selection
        setupDirectColorSelection();
    }
    
    /**
     * Launch file picker for saving Pokemon
     */
    private void launchSaveFilePicker() {
        // Generate a default file name with current date/time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timeStamp = sdf.format(new Date());
        String fileName = "pokemon_" + timeStamp + ".csv";
        
        filePickerHelper.launchSaveFilePicker(fileName);
    }
    
    /**
     * Launch file picker for loading Pokemon
     */
    private void launchLoadFilePicker() {
        filePickerHelper.launchLoadFilePicker();
    }
    
    /**
     * Called when a save URI is selected
     */
    private void onSaveUriSelected(Uri uri) {
        pokeCenter.setSelectedSaveUri(uri);
        if (pokeCenter.savePokemonToCSV()) {
            Toast.makeText(requireContext(), "Pokemon saved successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Error saving Pokemon", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Called when a load URI is selected
     */
    private void onLoadUriSelected(Uri uri) {
        pokeCenter.setSelectedLoadUri(uri);
        if (pokeCenter.loadPokemonFromCSV()) {
            // Update the adapter with the loaded Pokemon
            adapter.updatePokemonList(pokeCenter.getHome().listPokemons());
            Toast.makeText(requireContext(), "Pokemon loaded successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Error loading Pokemon", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void setupDirectColorSelection() {
        // Clear any existing selection
        binding.colorRadioGroup.clearCheck();
        selectedColor = null;
        
        // Setup click listeners for each radio button
        binding.whiteButton.setOnClickListener(v -> selectColor(binding.whiteButton));
        binding.greenButton.setOnClickListener(v -> selectColor(binding.greenButton));
        binding.pinkButton.setOnClickListener(v -> selectColor(binding.pinkButton));
        binding.orangeButton.setOnClickListener(v -> selectColor(binding.orangeButton));
        binding.blackButton.setOnClickListener(v -> selectColor(binding.blackButton));
    }
    
    private void selectColor(RadioButton button) {
        // Clear previous selection
        if (selectedColor != null) {
            selectedColor.setChecked(false);
        }
        
        // Set new selection
        button.setChecked(true);
        selectedColor = button;
    }

    private void createPokemon() {
        String name = binding.nameInput.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if a color is selected using our direct tracking
        if (selectedColor == null) {
            Toast.makeText(requireContext(), "Please select a species", Toast.LENGTH_SHORT).show();
            return;
        }

        String species;
        int id = selectedColor.getId();
        if (id == R.id.whiteButton) species = "pikachu";
        else if (id == R.id.greenButton) species = "venusaur";
        else if (id == R.id.pinkButton) species = "charizard";
        else if (id == R.id.orangeButton) species = "blastoise";
        else if (id == R.id.blackButton) species = "mewtwo";
        else {
            Toast.makeText(requireContext(), "Please select a species", Toast.LENGTH_SHORT).show();
            return;
        }

        Pokemon pokemon = pokeCenter.createPokemon(name, species);
        adapter.updatePokemonList(pokeCenter.getHome().listPokemons());
        binding.nameInput.setText("");
        
        // Clear color selection after creating
        if (selectedColor != null) {
            selectedColor.setChecked(false);
            selectedColor = null;
        }
        
        Toast.makeText(requireContext(), "Created " + species + " Pokemon: " + name, Toast.LENGTH_SHORT).show();
    }

    private void showAreaPokemons(String area) {
        switch (area) {
            case "home":
                adapter.updatePokemonList(pokeCenter.getHome().listPokemons());
                break;
            case "training":
                adapter.updatePokemonList(pokeCenter.getTraining().listPokemons());
                break;
            case "battle":
                adapter.updatePokemonList(pokeCenter.getBattle().listPokemons());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
