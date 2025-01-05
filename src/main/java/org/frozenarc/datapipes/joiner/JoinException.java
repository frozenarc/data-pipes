package org.frozenarc.datapipes.joiner;

import org.frozenarc.datapipes.common.StreamProcessException;

/**
 * Author: mpanchal
 * Date: 2022-12-03 16:02
 * The exception can be thrown from joiner stage.
 */
public class JoinException extends StreamProcessException {

    public JoinException(String message) {
        super(message);
    }

    public JoinException(String message, Throwable cause) {
        super(message, cause);
    }

    public JoinException(Throwable cause) {
        super(cause);
    }
}
