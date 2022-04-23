package com.strawbwine.tasks.backend;

public class ExceptionUtilities {
  public static void handleException(Exception ex) {
    System.out.println(ex.getMessage());
    ex.printStackTrace();
  }
}
