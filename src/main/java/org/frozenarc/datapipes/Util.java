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

    /**
     * Represents StreamsWriterFI (functional interface)
     */
    public static StreamsWriterFI idleWriterFI = outputStream -> {

    };

    /**
     * Represents StreamsJoinerFI (functional interface)
     */
    public static StreamsJoinerFI idleJoinerFI = (inputStreams, outputStreams) -> {

    };

    /**
     * Represents StreamsReaderFI (functional interface)
     */
    public static StreamsReaderFI idleReaderFI = inputStreams -> {

    };

    /**
     * The method accepts name and fi and returns worker
     * @param name of StreamsWriter
     * @param fi instance of StreamsWriterFI
     * @return instance of StreamsWriter
     */
    public static StreamsWriter writer(String name, StreamsWriterFI fi) {
        return new StreamsWriter() {
            @Override
            public void writeTo(OutputStream[] outputStreams) throws WriteException {
                fi.writeTo(outputStreams);
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    /**
     * The method accepts name and fi and returns worker
     * @param name of StreamsJoiner
     * @param fi instance of StreamsJoinerFI
     * @return instance of StreamsJoiner
     */
    public static StreamsJoiner joiner(String name, StreamsJoinerFI fi) {
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

    /**
     * The method accepts name and fi and returns worker
     * @param name of StreamsReader
     * @param fi instance of StreamsReaderFI
     * @return instance of StreamsReader
     */
    public static StreamsReader reader(String name, StreamsReaderFI fi) {
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
