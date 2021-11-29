package com.nezzar064.ocafe.exception;

public class DataNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DataNotFoundException(String msg) {
    super(msg);
  }
}