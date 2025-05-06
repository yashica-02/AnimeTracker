package com.ysharma.animetracker.api;

import com.ysharma.animetracker.model.AnimeResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AnimeApiService {

    @GET("anime")
    Call<AnimeResponse> searchAnime(@Query("q") String query);

    static AnimeApiService getInstance() {
        return new Retrofit.Builder()
                .baseUrl("https://api.jikan.moe/v4/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AnimeApiService.class);
    }
}
