package com.ysharma.animetracker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ysharma.animetracker.R;
import com.ysharma.animetracker.adapter.AnimeListAdapter;
import com.ysharma.animetracker.model.AnimeItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class CompletedFragment extends Fragment {

    private RecyclerView recyclerView;
    private AnimeListAdapter adapter;
    private List<AnimeItem> animeList = new ArrayList<>();

    public CompletedFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AnimeListAdapter(animeList, getContext(), "completed");
        recyclerView.setAdapter(adapter);

        loadCompletedList();

        return view;
    }

    private void loadCompletedList() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference("animeTracker")
                .child(uid)
                .child("completed")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        animeList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            AnimeItem anime = data.getValue(AnimeItem.class);
                            if (anime != null) {
                                anime.firebaseKey = data.getKey();
                                animeList.add(anime);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error here if needed
                    }
                });
    }
}