package com.music.eartrainr.utils;

import java.util.UUID;


public class Auth {

  private static final String EVENT = "event";
  private static final String KEY = "key";

  public static int generateID(final String tag) {
    return (EVENT + ".tag." + tag).hashCode();
  }

  public static int generateEventToken(final String tag, final String event) {
    return (EVENT + "." + event + ".tag." + tag).hashCode();
  }

  public static String createKey(final String tag, final String name){
    return KEY + "." + tag + "." + name;
  }

  public static String createUUID() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }
}
