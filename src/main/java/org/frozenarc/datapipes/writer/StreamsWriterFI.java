package org.frozenarc.datapipes.writer;

import java.io.OutputStream;

/**
 * Author: mpanchal
 * Date: 31-12-2024 21:06
 */
public interface StreamsWriterFI {
    void writeTo(OutputStream[] outputStreams) throws WriteException;
}
