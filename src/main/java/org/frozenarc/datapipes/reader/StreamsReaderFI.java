package org.frozenarc.datapipes.reader;

import java.io.InputStream;

/**
 * Author: mpanchal
 * Date: 31-12-2024 21:09
 * The interface declares only one method so can be implemented as functional interface
 */
public interface StreamsReaderFI {

    /**
     * The method reads previous level workers
     * @param inputStreams from previous workers
     */
    void readFrom(InputStream[] inputStreams) throws ReadException;
}
