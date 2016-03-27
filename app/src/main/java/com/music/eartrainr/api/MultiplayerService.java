package com.music.eartrainr.api;

import com.music.eartrainr.Bus;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.event.MultiPlayerEvent;
import com.music.eartrainr.model.MultiplayerGame;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by rapha on 3/26/2016.
 */
public final class MultiplayerService {
  public static final String API_URL = "http://eartrainr-nsimone.rhcloud.com";

  public static MultiplayerService INSTANCE = null;

  private final Retrofit mRetrofit;
  private final Api mApi;

  private MultiplayerService() {
    mRetrofit = new Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    mApi = mRetrofit.create(Api.class);
  }

  public static synchronized MultiplayerService getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new MultiplayerService();
    }
    return INSTANCE;
  }


  //region PUBLIC API METHODS
  public void cancelRequest(final String gameId) {
      Wtf.log("Canceling MultiPlayer Request: " + gameId);
      mApi.updateGameState(gameId, "cancelled").enqueue(new Callback<Void>() {
          @Override
          public void onResponse(
                  Call<Void> call,
                  Response<Void> response) {
              Bus.post(new MultiPlayerEvent().cancelSuccess());
          }

          @Override
          public void onFailure(
                  Call<Void> call,
                  Throwable t) {
              Wtf.log("Failed to execute cancelRequest!");
          }
      });
  }

  public void requestMatch(String username, String competitor) {

    mApi.getMatchRequest(username, competitor).enqueue(new Callback<MultiplayerGame>() {
      @Override
      public void onResponse(
          Call<MultiplayerGame> call,
          Response<MultiplayerGame> response) {
        Wtf.log("Requested match. Resulting game ID: " + response.body().gameID);
        Bus.post(new MultiPlayerEvent().success(response.body()));
      }

      @Override
      public void onFailure(
          Call<MultiplayerGame> call,
          Throwable t) {
        Wtf.log("Failed to execute requestMatch!");
        Bus.post(new MultiPlayerEvent().success(null));

      }
    });
  }

  public void updateGameState(String gameId, String state) {
    Wtf.log("Updating game state: " + state + " " + gameId);
    mApi.updateGameState(gameId, state);
  }

  public void declineRequest(final String gameId) {
      Wtf.log("Declining MultiPlayer Request: " + gameId);
      mApi.updateGameState("decline", gameId).enqueue(new Callback<Void>() {
          @Override
          public void onResponse(
                  Call<Void> call,
                  Response<Void> response) {
              Bus.post(new MultiPlayerEvent().declineSuccess());
          }

          @Override
          public void onFailure(
                  Call<Void> call,
                  Throwable t) {
              Wtf.log("Failed to execute declineRequest!");
          }
      });
  }

  public void acceptRequest(final String gameId) {
      Wtf.log("Accepting MultiPlayer Request: " + gameId);
      mApi.updateGameState("accept", gameId).enqueue(new Callback<Void>() {
          @Override
          public void onResponse(
                  Call<Void> call,
                  Response<Void> response) {
              Bus.post(new MultiPlayerEvent().acceptSuccess());
          }

          @Override
          public void onFailure(
                  Call<Void> call,
                  Throwable t) {
              Wtf.log("Failed to execute acceptRequest!");
          }
      });
  }
  //endregion
}