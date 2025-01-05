package org.frozenarc.datapipes.reader;

import org.frozenarc.datapipes.common.StreamsWorker;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Date: 26-12-2024 18:31
 * Author: manan
 */
public interface StreamsReader extends StreamsWorker {

    default void work(InputStream[] inputStreams, OutputStream[] outputStreams) throws ReadException {
        readFrom(inputStreams);
    }

    default String type() {
        return "StreamReader";
    }

    void readFrom(InputStream[] inputStreams) throws ReadException;
}
