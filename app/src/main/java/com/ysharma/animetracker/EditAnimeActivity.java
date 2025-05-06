package com.ysharma.animetracker;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.*;

public class EditAnimeActivity extends AppCompatActivity {

    private ImageView animeImage;
    private TextView titleText, episodesText, scoreText, yearText;
    private Spinner statusSpinner;
    private Button plusButton, minusButton, saveButton;
    private TextView episodesWatchedText, episodesWatchedLabel;

    private String originalStatus, newStatus, firebaseKey;
    private int totalEpisodes, watchedEpisodes;
    private String title, image, year, synopsis;
    private double score;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_anime);

        // Views
        animeImage = findViewById(R.id.animeImage);
        titleText = findViewById(R.id.animeTitle);
        episodesText = findViewById(R.id.animeEpisodes);
        scoreText = findViewById(R.id.animeScore);
        yearText = findViewById(R.id.animeYear);
        statusSpinner = findViewById(R.id.statusSpinner);
        plusButton = findViewById(R.id.plusButton);
        minusButton = findViewById(R.id.minusButton);
        episodesWatchedText = findViewById(R.id.episodesWatchedText);
        episodesWatchedLabel = findViewById(R.id.episodesWatchedLabel);
        saveButton = findViewById(R.id.saveButton);

        // Intent data
        title = getIntent().getStringExtra("title");
        image = getIntent().getStringExtra("image");
        totalEpisodes = getIntent().getIntExtra("episodes", 0);
        score = getIntent().getDoubleExtra("score", 0);
        year = getIntent().getStringExtra("year");
        watchedEpisodes = getIntent().getIntExtra("watchedEpisodes", 0);
        originalStatus = getIntent().getStringExtra("status");
        firebaseKey = getIntent().getStringExtra("firebaseKey");
        synopsis = getIntent().getStringExtra("synopsis"); // ✅ NEW

        if (synopsis == null) synopsis = "No synopsis available.";
        if (originalStatus == null) originalStatus = "watchlist";
        newStatus = originalStatus;

        // Set UI values
        Glide.with(this).load(image).into(animeImage);
        titleText.setText(title);
        episodesText.setText("Episodes: " + totalEpisodes);
        scoreText.setText("Score: " + score);
        yearText.setText("Year: " + year);

        database = FirebaseDatabase.getInstance().getReference("animeTracker");

        // Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.status_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        int pos = adapter.getPosition(capitalize(originalStatus));
        statusSpinner.setSelection(pos);
        updateEpisodeUI();

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                newStatus = parent.getItemAtPosition(i).toString();
                updateEpisodeUI();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Episode buttons
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

        // Save logic
        saveButton.setOnClickListener(v -> {
            if (newStatus.equalsIgnoreCase("completed")) {
                watchedEpisodes = totalEpisodes;
            }

            AnimeItem updatedAnime = new AnimeItem(title, image, totalEpisodes, score, year, watchedEpisodes, synopsis);

            String originalKey = originalStatus.toLowerCase();
            String newKey = newStatus.toLowerCase();

            if (firebaseKey != null && !originalKey.equals(newKey)) {
                database.child(originalKey).child(firebaseKey)
                        .removeValue()
                        .addOnSuccessListener(aVoid -> saveToNewList(updatedAnime, newKey))
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to remove old entry", Toast.LENGTH_SHORT).show());
            } else if (firebaseKey != null) {
                database.child(originalKey).child(firebaseKey)
                        .setValue(updatedAnime)
                        .addOnSuccessListener(aVoid -> Toast.makeText(this, "Anime updated!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
            } else {
                saveToNewList(updatedAnime, newKey);
            }
        });
    }

    private void saveToNewList(AnimeItem anime, String newKey) {
        database.child(newKey).push().setValue(anime)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Anime moved to " + capitalize(newKey), Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private void updateEpisodeUI() {
        boolean showProgress = newStatus.equalsIgnoreCase("watching");
        boolean isCompleted = newStatus.equalsIgnoreCase("completed");

        episodesWatchedLabel.setVisibility(showProgress || isCompleted ? View.VISIBLE : View.GONE);
        episodesWatchedText.setVisibility(showProgress || isCompleted ? View.VISIBLE : View.GONE);

        plusButton.setVisibility(showProgress ? View.VISIBLE : View.GONE);
        minusButton.setVisibility(showProgress ? View.VISIBLE : View.GONE);

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