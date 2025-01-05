package org.frozenarc.datapipes.joiner;

import org.frozenarc.datapipes.common.StreamsWorker;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Date: 26-12-2024 17:24
 * Author: manan
 * Joiner representation
 */
public interface StreamsJoiner extends StreamsWorker {

    /**
     * The method calls join method
     * @param inputStreams from previous workers
     * @param outputStreams to next workers
     * @throws JoinException e
     */
    default void work(InputStream[] inputStreams, OutputStream[] outputStreams) throws JoinException {
        join(inputStreams, outputStreams);
    }

    /**
     * Returns type
     * @return type
     */
    default String type() {
        return "StreamJoiner";
    }

    /**
     * The method joins previous level workers to next level ones
     * @param inputStreams from previous workers
     * @param outputStreams to next workers
     * @throws JoinException e
     */
    void join(InputStream[] inputStreams, OutputStream[] outputStreams) throws JoinException;
}
