package org.frozenarc.datapipes.joiner;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author: mpanchal
 * Date: 31-12-2024 21:08
 * The interface declares only one method so can be implemented as functional interface
 */
public interface StreamsJoinerFI {

    /**
     * The method joins previous level workers to next level ones
     * @param inputStreams from previous workers
     * @param outputStreams to next workers
     * @throws JoinException e
     */
    void join(InputStream[] inputStreams, OutputStream[] outputStreams) throws JoinException;
}
