package com.application.gatekeeper.exception;

public class GateKeeperApplicationException extends RuntimeException{

    public GateKeeperApplicationException(String message) {
        super(message);
    }

    public GateKeeperApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GateKeeperApplicationException(Throwable cause) {
        super(cause);
    }
}
