package com.sgbd.exceptions;

/**
 * Created by Lazarm on 5/16/2017.
 */
public class InvalidUserPasswordException extends Exception {

    public InvalidUserPasswordException(String message){
        super(message);
    }
}
