package com.enigmastation.neuralnet;

public class KeyNotFoundError extends Error{
    public KeyNotFoundError() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public KeyNotFoundError(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public KeyNotFoundError(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public KeyNotFoundError(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
