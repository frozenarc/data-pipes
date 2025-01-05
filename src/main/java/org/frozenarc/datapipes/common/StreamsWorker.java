package org.frozenarc.datapipes.common;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Date: 26-12-2024 18:28
 * Author: manan
 * Parent worker interface with generate work method
 */
public interface StreamsWorker {

    /**
     * Should be implemented by workers
     * @param inputStreams from previous workers
     * @param outputStreams to next workers
     * @throws StreamProcessException e
     */
    void work(InputStream[] inputStreams, OutputStream[] outputStreams) throws StreamProcessException;

    /**
     * The method returns worker type
     * @return type
     */
    String type();

    /**
     * The method returns worker name
     * @return name
     */
    String name();
}
