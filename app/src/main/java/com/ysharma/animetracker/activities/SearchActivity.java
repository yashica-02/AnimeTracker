package com.ysharma.animetracker.activities;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ysharma.animetracker.adapter.AnimeSearchAdapter;
import com.ysharma.animetracker.R;
import com.ysharma.animetracker.api.AnimeApiService;
import com.ysharma.animetracker.model.Anime;
import com.ysharma.animetracker.model.AnimeResponse;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private EditText searchInput;
    private RecyclerView recyclerView;
    private AnimeSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        MaterialToolbar toolbar = findViewById(R.id.searchToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Anime");

        searchInput = findViewById(R.id.searchInput);
        recyclerView = findViewById(R.id.searchResultsRecycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchAnime(searchInput.getText().toString());
                return true;
            }
            return false;
        });
    }

    private void searchAnime(String query) {
        AnimeApiService.getInstance().searchAnime(query).enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(Call<AnimeResponse> call, Response<AnimeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Anime> animeList = response.body().getData();
                    adapter = new AnimeSearchAdapter(animeList, SearchActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(SearchActivity.this, "No results found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AnimeResponse> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}