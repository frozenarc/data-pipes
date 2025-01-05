package org.frozenarc.datapipes.joiner;

import org.frozenarc.datapipes.common.StreamsWorker;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Date: 26-12-2024 17:24
 * Author: manan
 */
public interface StreamsJoiner extends StreamsWorker {

    default void work(InputStream[] inputStreams, OutputStream[] outputStreams) throws JoinException {
        join(inputStreams, outputStreams);
    }

    default String type() {
        return "StreamJoiner";
    }

    void join(InputStream[] inputStreams, OutputStream[] outputStreams) throws JoinException;
}
