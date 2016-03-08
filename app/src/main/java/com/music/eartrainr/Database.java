package com.music.eartrainr;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.annotations.NotNull;
import com.music.eartrainr.event.FriendAddedEvent;
import com.music.eartrainr.event.FriendItemGetEvent;
import com.music.eartrainr.event.SignInEvent;
import com.music.eartrainr.event.SignUpEvent;
import com.music.eartrainr.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.music.eartrainr.Database.FirebaseKeys.DEFAULT;
import static com.music.eartrainr.Database.FirebaseKeys.FRIENDS;
import static com.music.eartrainr.Database.FirebaseKeys.USERS;
import static com.music.eartrainr.event.FriendAddedEvent.EVENT.FRIEND_ADDED;
import static com.music.eartrainr.event.FriendAddedEvent.EVENT.USER_UNKNOWN;


public class Database <T> {

  public static final String TAG = Database.class.getSimpleName();
  private static final String RANK_STARTING = "1";
  private Object mProfile;




  protected interface FirebaseKeys {
    String USERS = "users";
    String USERNAME = "userName";
    String FRIENDS = "friends";
    String USER_UID = "user_uid";
    String RANK = "rank";
    String EMAIL = "email";
    boolean DEFAULT = true; //default value, we only care about the key when this is used
  }

  public interface EventToken {
    int NEW_USER = Auth.generateEventToken(TAG, "new_user");
    int AUTHORIZATION = Auth.generateEventToken(TAG, "existing_user");
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
        final String username = TextUtility.getUserNameFromEmail(email);
        userDetails.put(FirebaseKeys.EMAIL, email);
        userDetails.put(FirebaseKeys.RANK, RANK_STARTING);
//        userDetails.put(FirebaseKeys.USERNAME, username);

        mFirebaseRef.child(USERS)
            .child(TextUtility.getUserNameFromEmail(email))
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


  /**
   * Performs a login request and authorizes the user.
   * Note: this must be called even for signups
   * @param email
   * @param password
   * @param callback
   */
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
   * Establishes a friend link between both userA & userB
   * @param userA - userA get's a friend link to userB
   * @param userB - similiarly userB get's a friend link to userA
   * @param linkAgain - specify whether a friend link should be specified for the opposite user
   * @return -> fires Events
   */
  public void addFriendLink(final String userA, final String userB, final boolean linkAgain){
    Wtf.log();
    mFirebaseRef.child(USERS)
                .child(userA)
                .child(FirebaseKeys.FRIENDS)
                .updateChildren(createSingleMapObj(userB, DEFAULT), new Firebase.CompletionListener() {
                  @Override public void onComplete(
                      final FirebaseError firebaseError,
                      final Firebase firebase) {
                    if (firebaseError == null) {
                      if (linkAgain) {
                        addFriendLink(userB, userA, false);
                        User user = new User();
                        user.setUserName(userB);
                        Bus.post(new FriendAddedEvent().friendAdded(user));
                      }
                    } else {
                      Bus.post(new FriendAddedEvent().error(FRIEND_ADDED, firebaseError));
                    }
                  }
                });
  }


  /**
   * Get's all the child nodes of the friends list one by one
   * This callback fires as soon as you add a friend or delete a friend
   * @param user - the username for which a list of friends are requuested
   * @return - fires FriendItemGetEvent
   */
  public void getFriends(final String user) {

    mFirebaseRef.child(USERS + "/" + user + "/" + FRIENDS)
        .addChildEventListener(new ChildEventListener() {
          @Override public void onChildAdded(final DataSnapshot dataSnapshot, final String s) {
            //THIS IS EVIL BUT ACTUALLY THE RECOMMENDED PRACTICE
            final String friendName = dataSnapshot.getKey();

            mFirebaseRef.child(USERS + "/" + friendName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override public void onDataChange(final DataSnapshot dataSnapshot) {
                    final User user = dataSnapshot.getValue(User.class);
                    user.setUserName(dataSnapshot.getKey());
                    Bus.post(new FriendItemGetEvent().itemGet(user));
                  }

                  @Override public void onCancelled(final FirebaseError firebaseError) {
                    //not used
                  }
                });
          }

          @Override public void onChildChanged(
              final DataSnapshot dataSnapshot,
              final String s) {
            //not used
          }

          @Override public void onChildRemoved(final DataSnapshot dataSnapshot) {
            //not used
          }

          @Override public void onChildMoved(
              final DataSnapshot dataSnapshot,
              final String s) {
            //not used
          }

          @Override public void onCancelled(final FirebaseError firebaseError) {
            Wtf.log();
          }
        });
  }

  public void removeFriend(final String userA, final String userB, final boolean tryAgain) {
    Wtf.log("Removing friend link: " + userA + " & " + userB);

    mFirebaseRef.child(USERS).child(userA).child(FRIENDS).child(userB)
        .setValue(null, new Firebase.CompletionListener() {
          @Override public void onComplete(
              final FirebaseError firebaseError,
              final Firebase firebase) {
            if (firebaseError == null) {
              if (tryAgain) {
                removeFriend(userB, userA, false);
              } else {
                Bus.post(new FriendItemGetEvent().itemDeleteSuccess());
              }
            } else {
              Bus.post(new FriendItemGetEvent().error(FriendItemGetEvent.EVENT.DELETE_REQUEST, firebaseError));
            }
          }
        });
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

  public String getUserName() {
    final String email = (String) mFirebaseRef.getAuth().getProviderData().get(FirebaseKeys.EMAIL);
    return TextUtility.getUserNameFromEmail(email);
  }

  /**
   * Get's the profile according to a uid.
   * Uses Callback to retrieve information
   * @param uid - unique identifer under users child
   * @param callback
   */
  public void getProfile(final String user, final FirebaseGET<User> callback) {
    mFirebaseRef.child(USERS)
        .child(user)
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


  /**
   * finds a User model
   * @param username
   */
  public void findUser(final String username) {
    Wtf.log("Searching for user: " + username);

    mFirebaseRef.child(USERS).child(username)
        .addValueEventListener(new ValueEventListener() {
          @Override public void onDataChange(final DataSnapshot dataSnapshot) {
            cleanupListener(this); //IMPORTANT

            //user exists
            if (dataSnapshot.getValue() != null) {
              final User user = dataSnapshot.getValue(User.class);

              Wtf.log("Found User: " + username);
              user.setUserName(username);
              Bus.post(new FriendAddedEvent().found(user));
            } else {
              Bus.post(new FriendAddedEvent().notFound(username));
            }

          }

          @Override public void onCancelled(final FirebaseError firebaseError) {
            cleanupListener(this);
            Bus.post(new FriendAddedEvent().error(USER_UNKNOWN, firebaseError));
          }
        });
  }

  public void logout() {
    mFirebaseRef.unauth();
  }


  private T parseFirstElement(
      final DataSnapshot dataSnapshot,
      final Class<?> clazz) {
    if (dataSnapshot.getChildrenCount() != 0) {
      return (T) dataSnapshot.getChildren().iterator().next().getValue(clazz);
    } else {
      return null;
    }
  }

  private Map<String, Object> createSingleMapObj(
      final String key,
      final Object value) {
    final Map<String, Object> map = new HashMap<>(1);
    map.put(key, value);
    return map;
  }



  /**
   * Since the single callback instance does not work when using equalsTo and no entry is found,
   * you must manually remove the listener. Use this method on the first line of the callback to prevent
   * recursive callbacks.
   * @param listener
   */
  private void cleanupListener(ValueEventListener listener) {
    mFirebaseRef.removeEventListener(listener);
  }

  private void cleanupListener(Firebase.AuthStateListener listener) {
    mFirebaseRef.removeAuthStateListener(listener);
  }

  private void cleanupListener(ChildEventListener listener) {
    mFirebaseRef.removeEventListener(listener);
  }
}
