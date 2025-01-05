package org.frozenarc.datapipes.writer;

import org.frozenarc.datapipes.common.StreamsWorker;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Date: 26-12-2024 17:11
 * Author: manan
 * Writer representation
 */
public interface StreamsWriter extends StreamsWorker {

    /**
     * The method calls writeTo method
     * @param inputStreams from previous workers
     * @param outputStreams to next workers
     */
    default void work(InputStream[] inputStreams, OutputStream[] outputStreams) throws WriteException {
        writeTo(outputStreams);
    }

    /**
     * Returns type
     * @return type
     */
    default String type() {
        return "StreamWriter";
    }

    /**
     * The method writes to next level workers
     * @param outputStreams to next workers
     */
    void writeTo(OutputStream[] outputStreams) throws WriteException;

}
