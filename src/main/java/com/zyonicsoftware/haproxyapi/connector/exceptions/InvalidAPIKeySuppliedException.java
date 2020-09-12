package com.zyonicsoftware.haproxyapi.connector.exceptions;

public class InvalidAPIKeySuppliedException extends Exception{
    public InvalidAPIKeySuppliedException() {
        System.err.println("Your API-Key is invalid");
    }
}
