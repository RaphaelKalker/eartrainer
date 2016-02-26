package com.music.eartrainr;

import com.music.eartrainr.model.User;

import java.util.ArrayList;
import java.util.List;


public class Test {


  public static List<User> getFriendList() {

    List<User> friends = new ArrayList(10);

    User user = new User();
    user.setUserName("Raphael");

    friends.add(user);
    friends.add(user);
    friends.add(user);
    friends.add(user);
    friends.add(user);
    friends.add(user);
    friends.add(user);
    friends.add(user);
    friends.add(user);

    return friends;

  }
}
