package com.zyonicsoftware.haproxyapi.connector.exceptions;

public class InvalidDestinationAliasException extends Exception{

    public InvalidDestinationAliasException(final String destination) {
        System.err.println("The destination-Alias " + destination + " is invalid.");
    }

}
