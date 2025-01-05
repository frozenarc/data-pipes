package org.frozenarc.datapipes.joiner;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author: mpanchal
 * Date: 31-12-2024 21:08
 */
public interface StreamsJoinerFI {

    void join(InputStream[] inputStreams, OutputStream[] outputStreams) throws JoinException;
}
