package com.example.myapplication.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.model.Pokemon;
import java.util.List;

/**
 * Adapter for displaying Pokemon in a RecyclerView
 */
public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {
    private List<Pokemon> pokemons;
    private OnPokemonClickListener clickListener;

    /**
     * Interface for handling Pokemon selection
     */
    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
    }

    /**
     * Constructor with Pokemon list and click listener
     */
    public PokemonAdapter(List<Pokemon> pokemons, OnPokemonClickListener clickListener) {
        this.pokemons = pokemons;
        this.clickListener = clickListener;
    }

    /**
     * Constructor with just Pokemon list (no click listener)
     */
    public PokemonAdapter(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
        this.clickListener = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lutemon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = pokemons.get(position);
        holder.nameText.setText(pokemon.getName());
        holder.speciesText.setText(pokemon.getSpecies());
        holder.statsText.setText("ATK: " + pokemon.getAttack() + 
                               " DEF: " + pokemon.getDefense() + 
                               " HP: " + pokemon.getHP() + "/" + pokemon.getMaxHP() +
                               " EXP: " + pokemon.getExp());
        
        // Set click listener if available
        if (clickListener != null) {
            holder.itemView.setOnClickListener(v -> clickListener.onPokemonClick(pokemon));
        }
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    /**
     * Update the Pokemon list and refresh the view
     */
    public void updatePokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for Pokemon items
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView speciesText;
        TextView statsText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            speciesText = itemView.findViewById(R.id.speciesText);
            statsText = itemView.findViewById(R.id.statsText);
        }
    }
}
