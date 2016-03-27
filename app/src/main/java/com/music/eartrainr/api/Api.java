package com.music.eartrainr.api;

import com.music.eartrainr.model.MultiplayerGame;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;


/**
 * Created by Raphael on 16-03-26.
 */
public interface Api {

  String USER = "user";
  String COMPETITOR = "competitor";
  String ID = "id";
  String GAMEID = "gameId";
  String STATE = "state";

  @GET("/matchRequest") Call<MultiplayerGame> getMatchRequest(
      @Query(USER) String user,
      @Query(COMPETITOR) String competitor
  );

  @PUT("/cancelMatch") Call<Void> cancelMatchRequest(
      @Query(USER) String user,
      @Query(ID) String id
  );

  @GET("/updateGameState") Call<Void> updateGameState(
      @Query(GAMEID) String gameId,
      @Query(STATE) String state
  );
}
