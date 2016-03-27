package com.music.eartrainr.firebase;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.music.eartrainr.Database;
import com.music.eartrainr.Wtf;

import static com.music.eartrainr.firebase.Users.KEYS.*;


public class Users {

  public interface KEYS {
    String USERS = "users";
    String REG_ID = "reg_id";
  }

  public static void registerGCMToken(final String token) {
    Firebase fb = Database.getSingleton().getFirebase();

    fb.child(USERS)
        .child(Database.getSingleton().getUserName())
        .child(REG_ID)
        .setValue(token, new Firebase.CompletionListener() {
          @Override public void onComplete(
              final FirebaseError firebaseError,
              final Firebase firebase) {
            Wtf.log("Successfully added Token to Profile");
          }
        });
  }
}
