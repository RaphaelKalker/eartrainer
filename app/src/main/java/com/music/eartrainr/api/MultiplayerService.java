package com.music.eartrainr.api;

import android.util.Log;

import com.music.eartrainr.Wtf;
import com.music.eartrainr.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by rapha on 3/26/2016.
 */
public final class MultiplayerService {
    public static final String API_URL = "http://eartrainr-nsimone.rhcloud.com";

    public static class MultiplayerGame {
        public final String username;
        public final String competitor;
        public final int gameID;


        public MultiplayerGame(String username, String competitor, int gameID) {
            this.username = username;
            this.competitor = competitor;
            this.gameID = gameID;
        }
    }

    public interface MatchRequest {
        @GET("/matchRequest")
        Call<MultiplayerGame> getMatchRequest(
                @Query("user") String user,
                @Query("competitor") String competitor
        );
    }

    public static void requestMatch(String username, String competitor) {
        // Create a very simple REST adapter which points the GitHub API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our MatchRequest API interface.
        MatchRequest matchRequest = retrofit.create(MatchRequest.class);

        // Create a call instance for looking up sessionID.
        Call<MultiplayerGame> call = matchRequest.getMatchRequest(username, competitor);

        // Fetch and print a list of the contributors to the library.
        call.enqueue(new Callback<MultiplayerGame>() {
            @Override
            public void onResponse(Call<MultiplayerGame> call, Response<MultiplayerGame> response) {
                Wtf.log("Requested match. Resulting game ID: " + response.body().gameID);
            }

            @Override
            public void onFailure(Call<MultiplayerGame> call, Throwable t) {
                Wtf.log("Failed to execute requestMatch!");
            }
        });
    }
}