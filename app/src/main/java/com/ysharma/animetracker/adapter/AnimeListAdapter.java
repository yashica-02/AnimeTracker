//Created by Yashica Sharma
package com.ysharma.animetracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.ysharma.animetracker.R;
import com.ysharma.animetracker.activities.EditAnimeActivity;
import com.ysharma.animetracker.model.AnimeItem;

import java.util.List;

public class AnimeListAdapter extends RecyclerView.Adapter<AnimeListAdapter.ViewHolder> {

    private List<AnimeItem> animeList;
    private Context context;
    private String currentTabStatus;

    public AnimeListAdapter(List<AnimeItem> animeList, Context context, String currentTabStatus) {
        this.animeList = animeList;
        this.context = context;
        this.currentTabStatus = currentTabStatus.toLowerCase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_anime_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnimeItem anime = animeList.get(position);
        holder.title.setText(anime.title);
        holder.episodes.setText("Episodes: " + anime.episodes);
        holder.score.setText("Score: " + anime.score);
        holder.year.setText("Year: " + anime.year);

        if (currentTabStatus.equals("watching")) {
            holder.watchedProgress.setVisibility(View.VISIBLE);
            holder.watchedProgress.setText("Watched: " + anime.watchedEpisodes + "/" + anime.episodes);
        } else if (currentTabStatus.equals("completed")) {
            holder.watchedProgress.setVisibility(View.VISIBLE);
            holder.watchedProgress.setText("Watched: " + anime.episodes + "/" + anime.episodes);
        } else {
            holder.watchedProgress.setVisibility(View.GONE);
        }

        Glide.with(context).load(anime.imageUrl).into(holder.image);

        // Open Edit screen
        holder.editIcon.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditAnimeActivity.class);
            intent.putExtra("title", anime.title);
            intent.putExtra("image", anime.imageUrl);
            intent.putExtra("episodes", anime.episodes);
            intent.putExtra("score", anime.score);
            intent.putExtra("year", anime.year);
            intent.putExtra("watchedEpisodes", anime.watchedEpisodes);
            intent.putExtra("firebaseKey", anime.firebaseKey);
            intent.putExtra("status", currentTabStatus);
            intent.putExtra("synopsis", anime.synopsis);
            context.startActivity(intent);
        });

        // Tap on card also opens Edit (will change in later versions)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditAnimeActivity.class);
            intent.putExtra("title", anime.title);
            intent.putExtra("image", anime.imageUrl);
            intent.putExtra("episodes", anime.episodes);
            intent.putExtra("score", anime.score);
            intent.putExtra("year", anime.year);
            intent.putExtra("watchedEpisodes", anime.watchedEpisodes);
            intent.putExtra("firebaseKey", anime.firebaseKey);
            intent.putExtra("status", currentTabStatus);
            intent.putExtra("synopsis", anime.synopsis);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, episodes, score, year, watchedProgress;
        ImageView image;
        ImageButton editIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.animeTitle);
            episodes = itemView.findViewById(R.id.animeEpisodes);
            score = itemView.findViewById(R.id.animeScore);
            year = itemView.findViewById(R.id.animeYear);
            watchedProgress = itemView.findViewById(R.id.animeWatchedProgress);
            image = itemView.findViewById(R.id.animeImage);
            editIcon = itemView.findViewById(R.id.editIcon);
        }
    }
}