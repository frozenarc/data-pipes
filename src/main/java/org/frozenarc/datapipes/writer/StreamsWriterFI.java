package org.frozenarc.datapipes.writer;

import java.io.OutputStream;

/**
 * Author: mpanchal
 * Date: 31-12-2024 21:06
 * The interface declares only one method so can be implemented as functional interface
 */
public interface StreamsWriterFI {

    /**
     * The method reads previous level workers
     * @param outputStreams to next workers
     */
    void writeTo(OutputStream[] outputStreams) throws WriteException;
}
