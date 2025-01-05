package org.frozenarc.datapipes.writer;

import org.frozenarc.datapipes.common.StreamProcessException;

/**
 * Date: 01-01-2025 13:26
 * Author: manan
 */
public class WriteException extends StreamProcessException {

    public WriteException(String message) {
        super(message);
    }

    public WriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public WriteException(Throwable cause) {
        super(cause);
    }
}
