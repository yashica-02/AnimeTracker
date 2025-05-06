package com.ysharma.animetracker;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnimeDetailsActivity extends AppCompatActivity {

    private TextView titleText, episodesText, scoreText, yearText, typeText, synopsisText;
    private ImageView animeImage;
    private Spinner statusSpinner;
    private Button saveButton;

    private TextView episodesWatchedText;
    private Button plusButton, minusButton;

    private String selectedStatus;
    private int totalEpisodes = 0;
    private int watchedEpisodes = 0;

    private String title, imageUrl, year, synopsis;
    private int episodes;
    private double score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_details);

        animeImage = findViewById(R.id.animeImage);
        titleText = findViewById(R.id.animeTitle);
        episodesText = findViewById(R.id.animeEpisodes);
        scoreText = findViewById(R.id.animeScore);
        yearText = findViewById(R.id.animeYear);
        typeText = findViewById(R.id.animeType);
        synopsisText = findViewById(R.id.animeSynopsis);
        statusSpinner = findViewById(R.id.statusSpinner);
        saveButton = findViewById(R.id.saveButton);
        episodesWatchedText = findViewById(R.id.episodesWatchedText);
        plusButton = findViewById(R.id.plusButton);
        minusButton = findViewById(R.id.minusButton);

        // Get intent data
        title = getIntent().getStringExtra("title");
        imageUrl = getIntent().getStringExtra("image");
        episodes = getIntent().getIntExtra("episodes", 0);
        score = getIntent().getDoubleExtra("score", 0);
        year = getIntent().getStringExtra("year");
        synopsis = getIntent().getStringExtra("synopsis"); // ✅ new
        if (synopsis == null) synopsis = "No synopsis available.";

        String type = getIntent().getStringExtra("type");
        watchedEpisodes = getIntent().getIntExtra("watchedEpisodes", 0);
        selectedStatus = getIntent().getStringExtra("status");

        if (selectedStatus == null) selectedStatus = "Watchlist";
        totalEpisodes = episodes;

        // Set UI
        titleText.setText(title);
        episodesText.setText("Episodes: " + episodes);
        scoreText.setText("Score: " + score);
        yearText.setText("Year: " + year);
        typeText.setText("Type: " + type);
        synopsisText.setText(synopsis); // ✅ set synopsis
        Glide.with(this).load(imageUrl).into(animeImage);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.status_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        int spinnerPos = adapter.getPosition(capitalize(selectedStatus));
        statusSpinner.setSelection(spinnerPos);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedStatus = parent.getItemAtPosition(pos).toString();
                updateEpisodeTrackerUI();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        updateEpisodeTrackerUI();

        plusButton.setOnClickListener(v -> {
            if (watchedEpisodes < totalEpisodes) {
                watchedEpisodes++;
                updateEpisodeDisplay();
            }
        });

        minusButton.setOnClickListener(v -> {
            if (watchedEpisodes > 0) {
                watchedEpisodes--;
                updateEpisodeDisplay();
            }
        });

        saveButton.setOnClickListener(v -> {
            if (selectedStatus.equalsIgnoreCase("completed")) {
                watchedEpisodes = totalEpisodes;
            }

            AnimeItem anime = new AnimeItem(title, imageUrl, episodes, score, year, watchedEpisodes, synopsis); // ✅ new constructor
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("animeTracker");

            db.child(selectedStatus.toLowerCase()).push().setValue(anime)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(this, title + " added to " + selectedStatus, Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
        });
    }

    private void updateEpisodeTrackerUI() {
        boolean isWatching = selectedStatus.equalsIgnoreCase("watching");
        boolean isCompleted = selectedStatus.equalsIgnoreCase("completed");

        episodesWatchedText.setVisibility(isWatching || isCompleted ? View.VISIBLE : View.GONE);
        plusButton.setVisibility(isWatching ? View.VISIBLE : View.GONE);
        minusButton.setVisibility(isWatching ? View.VISIBLE : View.GONE);

        if (isCompleted) {
            watchedEpisodes = totalEpisodes;
        }

        updateEpisodeDisplay();
    }

    private void updateEpisodeDisplay() {
        episodesWatchedText.setText(watchedEpisodes + " / " + totalEpisodes);
    }

    private String capitalize(String str) {
        if (str == null || str.length() == 0) return "";
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}