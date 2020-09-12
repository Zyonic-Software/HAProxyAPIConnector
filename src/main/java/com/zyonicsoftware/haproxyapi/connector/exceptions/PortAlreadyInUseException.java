package com.zyonicsoftware.haproxyapi.connector.exceptions;

public class PortAlreadyInUseException extends Exception{

    public PortAlreadyInUseException() {
        System.err.println("the specefied port is already in use!");
    }

}
