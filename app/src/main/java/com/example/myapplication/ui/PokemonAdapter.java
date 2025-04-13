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
import java.util.function.Consumer;

/**
 * Adapter for displaying Pokemon in a RecyclerView
 */
public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {
    private List<Pokemon> pokemons;
    private Consumer<Pokemon> onPokemonClickListener;

    public PokemonAdapter(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
        this.onPokemonClickListener = null;
    }

    public PokemonAdapter(List<Pokemon> pokemons, Consumer<Pokemon> onPokemonClickListener) {
        this.pokemons = pokemons;
        this.onPokemonClickListener = onPokemonClickListener;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lutemon, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemons.get(position);
        holder.nameText.setText(pokemon.getName());
        holder.speciesText.setText(pokemon.getSpecies());
        holder.statsText.setText(String.format("ATK: %d, DEF: %d, HP: %d/%d, EXP: %d",
                pokemon.getAttack(), pokemon.getDefense(), pokemon.getHP(), pokemon.getMaxHP(), pokemon.getExp()));

        if (onPokemonClickListener != null) {
            holder.itemView.setOnClickListener(v -> onPokemonClickListener.accept(pokemon));
        }
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    public void updatePokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
        notifyDataSetChanged();
    }

    static class PokemonViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView speciesText;
        TextView statsText;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            speciesText = itemView.findViewById(R.id.speciesText);
            statsText = itemView.findViewById(R.id.statsText);
        }
    }
}
