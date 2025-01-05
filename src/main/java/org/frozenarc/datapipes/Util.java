package org.frozenarc.datapipes;

import org.frozenarc.datapipes.joiner.JoinException;
import org.frozenarc.datapipes.joiner.StreamsJoiner;
import org.frozenarc.datapipes.joiner.StreamsJoinerFI;
import org.frozenarc.datapipes.reader.ReadException;
import org.frozenarc.datapipes.reader.StreamsReader;
import org.frozenarc.datapipes.reader.StreamsReaderFI;
import org.frozenarc.datapipes.writer.StreamsWriter;
import org.frozenarc.datapipes.writer.StreamsWriterFI;
import org.frozenarc.datapipes.writer.WriteException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author: mpanchal
 * Date: 28-12-2024 07:14
 */
public class Util {

    public static StreamsWriterFI idleWriterFI = outputStream -> {

    };

    public static StreamsJoinerFI idleJoinerFI = (inputStreams, outputStreams) -> {

    };

    public static StreamsReaderFI idleReaderFI = inputStreams -> {

    };

    public static StreamsWriter writerFI(String name, StreamsWriterFI fi) {
        return new StreamsWriter() {
            @Override
            public void writeTo(OutputStream[] outputStream) throws WriteException {
                fi.writeTo(outputStream);
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    public static StreamsJoiner joinerFI(String name, StreamsJoinerFI fi) {
        return new StreamsJoiner() {
            @Override
            public void join(InputStream[] inputStreams, OutputStream[] outputStreams) throws JoinException {
                fi.join(inputStreams, outputStreams);
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    public static StreamsReader readerFI(String name, StreamsReaderFI fi) {
        return new StreamsReader() {
            @Override
            public void readFrom(InputStream[] inputStreams) throws ReadException {
                fi.readFrom(inputStreams);
            }

            @Override
            public String name() {
                return name;
            }
        };
    }
}
