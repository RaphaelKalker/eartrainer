package com.music.eartrainr;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.annotations.NotNull;
import com.music.eartrainr.event.SignInEvent;
import com.music.eartrainr.event.SignUpEvent;
import com.music.eartrainr.model.User;
import com.music.eartrainr.retrofit.FirebaseService;

import java.util.HashMap;
import java.util.Map;

import static com.music.eartrainr.Database.FirebaseKeys.USERS;


public class Database {

  public static final String TAG = Database.class.getSimpleName();
  private static final String RANK_STARTING = "1";
  private Object mProfile;




  protected interface FirebaseKeys {
    String USERS = "users";
    String USERNAME = "userName";
    String FRIENDS = "friends";
  }

  public interface EventToken {
    int NEW_USER = Auth.generateEventToken(TAG, "new_user");
    int AUTHORIZATION = Auth.generateEventToken(TAG, "existing_user");
    int FRIEND_ADDED = Auth.generateEventToken(TAG, "new_friend");
  }

  public interface FirebaseGET<T> {
    void onSuccess(T snapshot);
    void onError(FirebaseError error);
  }


  private static Database INSTANCE;
  private static Firebase mFirebaseRef;

  private static final String FIREBASE_APP = "https://shining-heat-2718.firebaseio.com/";

  private Database() {
    //singleton class
    initFirebase();
  }

  public static synchronized Database getSingleton() {
    if (INSTANCE == null) {
      INSTANCE = new Database();
    }

    return INSTANCE;
  }

  private static synchronized void initFirebase() {
     mFirebaseRef = new Firebase(FIREBASE_APP);
  }

  public void createUser(@NotNull final String email, @NotNull final String password) {
    mFirebaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {

      @Override public void onSuccess(final Map<String, Object> stringObjectMap) {
        final String uid = (String)stringObjectMap.get("uid");
        Wtf.log("Successfully create user: " + uid);
        createProfile(uid);
      }

      private void createProfile(final String uid) {
        final Map<String, String> userDetails = new HashMap();
        userDetails.put("email", email);
        userDetails.put("rank", RANK_STARTING);
        userDetails.put("userName", email.split("\\@")[0]);

        mFirebaseRef.child(USERS)
            .child(uid)
            .setValue(userDetails, new Firebase.CompletionListener() {
              @Override public void onComplete(
                  final FirebaseError firebaseError,
                  final Firebase firebase) {
                if (firebaseError == null) {
                  Wtf.log("Successfully created Profile: " + uid);
                  Bus.post(new SignUpEvent<String>().success(EventToken.NEW_USER, uid));
                } else {
                  Bus.post(new SignUpEvent<>().error(EventToken.NEW_USER, firebaseError));
                }
              }
            });
      }

      @Override public void onError(final FirebaseError firebaseError) {
        Bus.post(new SignUpEvent().error(EventToken.NEW_USER, firebaseError));
      }
    });
  }



  public void authorize(final String email, final String password, Firebase.AuthResultHandler callback) {
    if (callback == null) {
      callback = new Firebase.AuthResultHandler() {
        @Override public void onAuthenticated(final AuthData authData) {
          Bus.post(new SignInEvent<AuthData>().success(EventToken.AUTHORIZATION, authData));
        }

        @Override public void onAuthenticationError(final FirebaseError firebaseError) {
          Bus.post(new SignInEvent<FirebaseError>().error(EventToken.AUTHORIZATION, firebaseError));
        }
      };
    }

    mFirebaseRef.authWithPassword(email, password, callback);

  }

  public void doShit() {
//    Firebase postRef =  mFirebaseRef.child(FirebaseKeys.USERS);
//    Map<String, String> user = new HashMap();
//    user.put("userName", "raphael");
//    user.put("rank", "100");
//    user.put("email", "raphael.kalker@gmail.com");
//    postRef.push().setValue(user);
  }


  public String getUserId() {
    if (mFirebaseRef.getAuth() != null) {
      return mFirebaseRef.getAuth().getUid();
    } else {
      Wtf.log("Firebase Auth was null");
      return "";
    }
  }

  public String getEmail() {
    return mFirebaseRef.getAuth().getProviderData().get("email").toString();
  }
  public Object getProfile(final FirebaseGET callback) {
    return mFirebaseRef.child(FirebaseKeys.USERS).addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(final DataSnapshot dataSnapshot) {
        Wtf.log(dataSnapshot.getValue().toString());

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          User user = snapshot.getValue(User.class);
          Wtf.log();
        }

        callback.onSuccess(dataSnapshot);

      }

      @Override public void onCancelled(final FirebaseError firebaseError) {
        Wtf.log("error");
        callback.onError(firebaseError);
      }
    });
  }


  public Object getProfile(final String uid, final FirebaseGET<User> callback) {
    mFirebaseRef
        .child(FirebaseKeys.USERS)
        .child(uid)
        .addListenerForSingleValueEvent(new ValueEventListener() {
          @Override public void onDataChange(final DataSnapshot dataSnapshot) {
            Wtf.log();
            User user = dataSnapshot.getValue(User.class);
            callback.onSuccess(user);
            Wtf.log();
          }

          @Override public void onCancelled(final FirebaseError firebaseError) {

          }
        });
    return null;
  }


  public void logout() {
    mFirebaseRef.unauth();
  }
}
