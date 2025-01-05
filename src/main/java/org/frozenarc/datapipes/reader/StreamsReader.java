package org.frozenarc.datapipes.reader;

import org.frozenarc.datapipes.common.StreamsWorker;
import org.frozenarc.datapipes.joiner.JoinException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Date: 26-12-2024 18:31
 * Author: manan
 * Reader representation
 */
public interface StreamsReader extends StreamsWorker {

    /**
     * The method calls readFrom method
     * @param inputStreams from previous workers
     * @param outputStreams to next workers
     */
    default void work(InputStream[] inputStreams, OutputStream[] outputStreams) throws ReadException {
        readFrom(inputStreams);
    }

    /**
     * Returns type
     * @return type
     */
    default String type() {
        return "StreamReader";
    }

    /**
     * The method reads from previous level workers
     * @param inputStreams from previous workers
     */
    void readFrom(InputStream[] inputStreams) throws ReadException;
}
