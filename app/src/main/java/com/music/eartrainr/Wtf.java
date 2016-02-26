package com.music.eartrainr;

import android.util.Log;


public class Wtf {

  private static final int STACK_TRACE_LEVELS_UP = 5;

  public static void log(final String message) {
    Log.v("RAPHTAG", getClassNameMethodNameAndLineNumber() + "  --> " + message);
  }

  public static void logError(final String message) {
    Log.e("RAPHTAG", getClassNameMethodNameAndLineNumber() + " --> " + message);
  }

  public static void log() {
    Log.v("RAPHTAG", getClassNameMethodNameAndLineNumber());
  }

  private static int getLineNumber() {
    return Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS_UP].getLineNumber();
  }

  private static String getClassName() {
    String fileName = Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS_UP].getFileName();
    return fileName.substring(0, fileName.length() - 5);
  }

  private static String getMethodName() {
    return Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS_UP].getMethodName();
  }

  private static String getClassNameMethodNameAndLineNumber() {
    return "[" + getClassName() + "." + getMethodName() + "() -" + getLineNumber() + "]: ";
  }

  public static void notUsedEx() throws
      Exception {
    Exception e = new IllegalStateException("YOU FORGOT SOMETHING... THIS IS NOT USED!");
    throw e;
  }

//  public static void toast(final Activity activity, String msg) {
//    CareDroidToast.makeText(activity, msg, CareDroidToast.Style.INFO).show();
//  }
//
//  public static void toast(final Activity activity) {
//    CareDroidToast.makeText(activity, getClassNameMethodNameAndLineNumber(), CareDroidToast.Style.INFO).show();
//  }
}