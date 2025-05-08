package com.ysharma.animetracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ysharma.animetracker.R;
import com.ysharma.animetracker.activities.AnimeDetailsActivity;
import com.ysharma.animetracker.model.Anime;

import java.util.List;

public class AnimeSearchAdapter extends RecyclerView.Adapter<AnimeSearchAdapter.ViewHolder> {

    private List<Anime> animeList;
    private Context context;

    public AnimeSearchAdapter(List<Anime> animeList, Context context) {
        this.animeList = animeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Anime anime = animeList.get(position);
        holder.title.setText(anime.getTitle());
        holder.info.setText("Episodes: " + anime.getEpisodes() + "  |  Score: " + anime.getScore());

        String yearStr = (anime.getYear() != null) ? anime.getYear().toString() : "N/A";
        holder.year.setText("Year: " + yearStr);

        Glide.with(context).load(anime.getImageUrl()).into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AnimeDetailsActivity.class);
            intent.putExtra("title", anime.getTitle());
            intent.putExtra("image", anime.getImageUrl());
            intent.putExtra("episodes", anime.getEpisodes());
            intent.putExtra("score", anime.getScore());
            intent.putExtra("year", yearStr);
            intent.putExtra("type", "TV"); // fallback
            intent.putExtra("synopsis", anime.getSynopsis());
            intent.putExtra("watchedEpisodes", 0);
            intent.putExtra("status", "Watchlist");
            intent.putExtra("trailerUrl", anime.getTrailerUrl());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, info, year;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.animeTitle);
            info = itemView.findViewById(R.id.animeInfo);
            year = itemView.findViewById(R.id.animeYear);
            image = itemView.findViewById(R.id.animeImage);
        }
    }
}