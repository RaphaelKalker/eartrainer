package com.music.eartrainr;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.annotations.NotNull;
import com.music.eartrainr.event.FireBaseEvent;
import com.music.eartrainr.event.FriendAddedEvent;
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
    String USER_UID = "user_uid";
  }

  public interface EventToken {
    int NEW_USER = Auth.generateEventToken(TAG, "new_user");
    int AUTHORIZATION = Auth.generateEventToken(TAG, "existing_user");
    int FRIEND_ADDED = Auth.generateEventToken(TAG, "new_friend");
    int FRIEND_FOUND = Auth.generateEventToken(TAG, "friend_found");
  }

  public interface FirebaseGET<T> {
    void onSuccess(T snapshot);
    void onError(FirebaseError error);
  }

  public interface FirebasePut<T> {
    void doShit();
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
    mFirebaseRef.keepSynced(true);
    mFirebaseRef.getApp().goOnline();
  }

  /**
   * 1. Creates a user - fires SignUpEvent for UI update
   * 2. Creates a profile - fires event?
   * 3. Signs in user -  fires SignInEvent
   * @param email
   * @param password
   */
  public void createUser(@NotNull final String email, @NotNull final String password) {
    mFirebaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {

      @Override public void onSuccess(final Map<String, Object> stringObjectMap) {
        final String uid = (String)stringObjectMap.get("uid");
        Wtf.log("Successfully create user: " + uid);
//        authorize(email, password);
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
                  authorize(email, password, null);
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
    Wtf.log("Authorizing User: " + email);
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


  /**
   * adds a friend to the profile with the uid specified
   * @param uid - identity of the profile to which a friend should be added
   * @param user - user object from profile
   */
  public void addFriend(final User user) {
    final Map<String, String> friendDetails = new HashMap<>();
    friendDetails.put(FirebaseKeys.USERNAME, user.getUserName());
    friendDetails.put(FirebaseKeys.USER_UID, user.getUid());

    mFirebaseRef.child(USERS)
                .child(getUserId())
                .child(FirebaseKeys.FRIENDS)
                .push()
                .setValue(friendDetails, new Firebase.CompletionListener() {
                  @Override public void onComplete(
                      final FirebaseError firebaseError,
                      final Firebase firebase) {
                    if (firebaseError == null) {
                      Bus.post(new FriendAddedEvent().success(EventToken.FRIEND_ADDED, user));
                    } else {
                      Bus.post(new FriendAddedEvent().error(EventToken.FRIEND_ADDED, firebaseError));
                    }
                  }
                });
  }

  public void doShit(final String uid) {
//    Firebase postRef =  mFirebaseRef.child(FirebaseKeys.USERS);
//    Map<String, String> user = new HashMap();
//    user.put("userName", "raphael");
//    user.put("rank", "100");
//    user.put("email", "raphael.kalker@gmail.com");
//    postRef.push().setValue(user);
  }


  public String getUserId() {
    //TODO: save uid to shared prefs, getAuth is a blocking call!
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
    return mFirebaseRef.child(USERS).addValueEventListener(new ValueEventListener() {
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


  /**
   * Get's the profile according to a uid.
   * Uses Callback to retrieve information
   * @param uid - unique identifer under users child
   * @param callback
   */
  public void getProfile(final String uid, final FirebaseGET<User> callback) {
    mFirebaseRef.child(USERS)
        .child(uid)
        .addListenerForSingleValueEvent(new ValueEventListener() {
          @Override public void onDataChange(final DataSnapshot dataSnapshot) {
            //TODO: BUG: there are times where getValue returns null

            User user = dataSnapshot.getValue(User.class);

            if (user != null) {
              callback.onSuccess(user);
            } else {
              callback.onError(new FirebaseError(FirebaseError.UNKNOWN_ERROR, "User details was null"));
            }
          }

          @Override public void onCancelled(final FirebaseError firebaseError) {
            Wtf.log(firebaseError.getMessage());
            callback.onError(firebaseError);
          }
        });
  }


  public void logout() {
    mFirebaseRef.unauth();
  }


  /**
   * finds a User model
   * @param username
   */
  public void findUser(final String username) {
    mFirebaseRef.child(USERS).orderByChild(FirebaseKeys.USERNAME)
                .equalTo(username)
                .addListenerForSingleValueEvent(
                    new ValueEventListener() {
                      @Override public void onDataChange(final DataSnapshot dataSnapshot) {
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                          final User user = snapshot.getValue(User.class);
                          user.setUid(snapshot.getKey());
                          addFriend(user);
                          Wtf.log("Found User: " + user.getUserName());
                        }
                      }

                      @Override public void onCancelled(final FirebaseError firebaseError) {
                        Wtf.log(firebaseError.getMessage());
                        Bus.post(new FriendAddedEvent().error(EventToken.FRIEND_ADDED, firebaseError));
                      }
                    }
                );
  }
}
