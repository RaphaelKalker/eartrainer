package com.music.eartrainr.retrofit;

/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.music.eartrainr.Wtf;

import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public final class FirebaseService {
  public static final String API_URL = "https://shining-heat-2718.firebaseio.com/";
  public static final String API_URL_GIT = "https://api.github.com";

  //THE CLASS THAT IS SERIALIZED
  public static class users {
    public final int rank;

    public users(int rank) {
      this.rank = rank;
    }
  }

  public interface Firebase {
    @GET("/users.json")
    Call<String> user(
        @Path("userName") String userName
    );
  }

  public static class Contributor {
    public final String login;
    public final int contributions;

    public Contributor(String login, int contributions) {
      this.login = login;
      this.contributions = contributions;
    }
  }

  public interface GitHub {
    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributors(
        @Path("owner") String owner,
        @Path("repo") String repo);
  }

  public static void main(String... args) throws IOException {
    // Create a very simple REST adapter which points the GitHub API.
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(API_URL_GIT)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    // Create an instance of our GitHub API interface.
    Firebase github = retrofit.create(Firebase.class);

    // Create a call instance for looking up Retrofit contributors.
    Call<String> call = github.user("raphael");

    call.enqueue(new Callback<String>() {
      @Override public void onResponse(
          final Call<String> call,
          final Response<String> response) {
        Wtf.log(response.toString());
      }

      @Override public void onFailure(
          final Call<String> call,
          final Throwable t) {
        Wtf.log(t.getMessage() + t.toString());
      }
    });

    // Fetch and print a list of the contributors to the library.
//    List<Users> contributors = call.execute().body();
//    for (Users contributor : contributors) {
//      System.out.println(contributor.login + " (" + contributor.contributions + ")");
//    }
  }
}