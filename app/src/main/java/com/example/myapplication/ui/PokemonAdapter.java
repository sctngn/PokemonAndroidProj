package com.example.myapplication.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.model.Pokemon;
import java.util.List;

/**
 * Adapter for displaying Pokemon in a RecyclerView
 */
public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {
    private List<Pokemon> pokemonList;
    private OnPokemonClickListener listener;

    public PokemonAdapter(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }

    public void setOnPokemonClickListener(OnPokemonClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lutemon, parent, false);
        return new PokemonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.nameText.setText(pokemon.getName());
        holder.speciesText.setText(pokemon.getSpecies());
        holder.statsText.setText(String.format("ATK: %d, DEF: %d, HP: %d/%d, EXP: %d",
                pokemon.getAttack(), pokemon.getDefense(),
                pokemon.getHP(), pokemon.getMaxHP(), pokemon.getExp()));
        
        // Set the Pokemon image
        holder.pokemonImage.setImageResource(pokemon.getImageResourceId());
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPokemonClick(pokemon);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public void updatePokemonList(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
        notifyDataSetChanged();
    }

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView speciesText;
        public TextView statsText;
        public ImageView pokemonImage;

        public PokemonViewHolder(View view) {
            super(view);
            nameText = view.findViewById(R.id.nameText);
            speciesText = view.findViewById(R.id.speciesText);
            statsText = view.findViewById(R.id.statsText);
            pokemonImage = view.findViewById(R.id.pokemonImage);
        }
    }

    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
    }
}
