package org.frozenarc.datapipes.common;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Date: 26-12-2024 18:28
 * Author: manan
 */
public interface StreamsWorker {

    void work(InputStream[] inputStreams, OutputStream[] outputStreams) throws StreamProcessException;

    String type();

    String name();
}
