package com.music.eartrainr.model;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.music.eartrainr.Bus;
import com.music.eartrainr.Database;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.event.RankItemGetEvent;

import java.util.ArrayList;
import java.util.List;


public class FirebaseRank {

  private static final String RANK = "rank";
  private static final String GAME1_WON = "game_1_won";
  private static final String GAME2_WON = "game_2_won";
  private static final String GAME3_WON = "game_3_won";
  private static final String GAME1_LOST = "game_1_lost";
  private static final String GAME2_LOST = "game_2_lost";
  private static final String GAME3_LOST = "game_3_lost";

  public static void initDB() {
    Firebase firebaseRef = Database.getSingleton().getFirebase();

    firebaseRef.child(RANK)
        .child("ExampleUserName")
        .child(GAME1_LOST)
        .setValue(12);

    firebaseRef.child(RANK)
        .child("ExampleUserName")
        .child(GAME1_WON)
        .setValue(23);
  }

  public static void get() {
    Wtf.log();

    Firebase firebaseRef = Database.getSingleton().getFirebase();



    final List<Rank> rankList = new ArrayList<>();

    firebaseRef.child(RANK)
        .addListenerForSingleValueEvent(new ValueEventListener() {
          @Override public void onDataChange(final DataSnapshot dataSnapshot) {

            Iterable<DataSnapshot> rankIter = dataSnapshot.getChildren();

            for (DataSnapshot snap : rankIter) {
              Rank rank = (Rank) snap.getValue(Rank.class);
              rank.setUsername(snap.getKey());
              rankList.add(rank);
            }

            Bus.post(new RankItemGetEvent().success(rankList));
          }

          @Override public void onCancelled(final FirebaseError firebaseError) {
            Wtf.log();
          }
        });
  }
}
