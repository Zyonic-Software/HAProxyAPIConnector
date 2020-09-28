package com.zyonicsoftware.haproxyapi.connector;

public class ProxyPort {

    private final int inputPort;
    private final int destinationPort;
    private final String name;
    private final String destinationAlias;

    public ProxyPort(int inputPort, int destinationPort, String name, String destinationAlias) {
        this.inputPort = inputPort;
        this.destinationPort = destinationPort;
        this.name = name;
        this.destinationAlias = destinationAlias;
    }

    public String getName() {
        return name;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public int getInputPort() {
        return inputPort;
    }

    public String getDestinationAlias() {
        return destinationAlias;
    }
}
