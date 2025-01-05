package org.frozenarc.datapipes.writer;

import org.frozenarc.datapipes.common.StreamsWorker;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Date: 26-12-2024 17:11
 * Author: manan
 */
public interface StreamsWriter extends StreamsWorker {

    default void work(InputStream[] inputStreams, OutputStream[] outputStreams) throws WriteException {
        writeTo(outputStreams);
    }

    default String type() {
        return "StreamWriter";
    }

    void writeTo(OutputStream[] outputStream) throws WriteException;

}
