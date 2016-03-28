package com.music.eartrainr.firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.music.eartrainr.Database;
import com.music.eartrainr.Wtf;

import static com.music.eartrainr.firebase.Rank.KEYS.*;

/**
 * Created by Nicole on 2016-03-28.
 */
public class Rank {

    public interface KEYS {
        String RANK = "rank";
        String GAME_1_SCORE = "game_1_score";
    }

    public static void updateGame1Score(final int correct) {
        Firebase fb = Database.getSingleton().getFirebase();

        Firebase scoreRef = fb.child(RANK)
            .child(Database.getSingleton().getUserName())
            .child(GAME_1_SCORE);
        scoreRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentScore) {
                if (currentScore.getValue() == null) {
                    currentScore.setValue(0);
                } else {
                    currentScore.setValue((Long) currentScore.getValue() + correct);
                }
                return Transaction.success(currentScore);
            }

            @Override
            public void onComplete (FirebaseError err, boolean committed, DataSnapshot currentData) {
                if (err != null) {
                    Wtf.log("Error updating game 1 score: " + err.getMessage());
                } else {
                    Wtf.log("Game 1 score updated to " + currentData.getValue());
                }
            }
        });
    }
}
