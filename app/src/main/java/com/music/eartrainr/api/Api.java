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

  @GET("/matchRequest") Call<MultiplayerGame> getMatchRequest(
      @Query(USER) String user,
      @Query(COMPETITOR) String competitor
  );

  @PUT("/cancelMatch") Call<Void> cancelMatchRequest(
      @Query(USER) String user,
      @Query(ID) int id
  );
}
