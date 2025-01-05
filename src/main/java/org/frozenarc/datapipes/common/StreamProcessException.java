package org.frozenarc.datapipes.common;

/**
 * Date: 01-01-2025 13:25
 * Author: manan
 */
public class StreamProcessException extends Exception {

    public StreamProcessException(String message) {
        super(message);
    }

    public StreamProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public StreamProcessException(Throwable cause) {
        super(cause);
    }
}
