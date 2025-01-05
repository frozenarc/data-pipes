package org.frozenarc.datapipes.reader;

import java.io.InputStream;

/**
 * Author: mpanchal
 * Date: 31-12-2024 21:09
 */
public interface StreamsReaderFI {

    void readFrom(InputStream[] inputStreams) throws ReadException;
}
