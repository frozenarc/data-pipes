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
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Author: mpanchal
 * Date: 28-12-2024 08:17
 */
public class _1_2M_2_Test {

    private static StreamsWriter writer = new StreamsWriter() {
        @Override
        public void writeTo(OutputStream[] outputStream) throws WriteException {
            try {
                outputStream[0].write("hello".getBytes());
            } catch (IOException e) {
                throw new WriteException(e);
            }
        }

        @Override
        public String name() {
            return "hello";
        }
    };

    private static StreamsWriterFI writerFI = outputStream -> {
        try {
            outputStream[0].write("hello".getBytes());
        } catch (IOException e) {
            throw new WriteException(e);
        }
    };

    private static StreamsJoiner joiner = new StreamsJoiner() {
        @Override
        public void join(InputStream[] inputStreams, OutputStream[] outputStreams) throws JoinException {
            try {
                int d;
                while ((d = inputStreams[0].read()) != -1) {
                    outputStreams[0].write(d);
                    outputStreams[1].write(d);
                }
            } catch (IOException e) {
                throw new JoinException(e);
            }
        }

        @Override
        public String name() {
            return "hello";
        }
    };

    private static StreamsJoinerFI joinerFI = (inputStreams, outputStreams) -> {
        try {
            int d;
            while ((d = inputStreams[0].read()) != -1) {
                outputStreams[0].write(d);
                outputStreams[1].write(d);
            }
        } catch (IOException e) {
            throw new JoinException(e);
        }
    };

    private static StreamsReader reader = new StreamsReader() {
        @Override
        public void readFrom(InputStream[] inputStreams) throws ReadException {
            try {
                inputStreams[0].transferTo(System.out);
                System.out.println();
            } catch (IOException e) {
                throw new ReadException(e);
            }

        }

        @Override
        public String name() {
            return "hello";
        }
    };

    private static StreamsReaderFI readerFI = inputStreams -> {
        try {
            inputStreams[0].transferTo(System.out);
            System.out.println();
        } catch (IOException e) {
            throw new ReadException(e);
        }

    };

    @Test
    public void test() throws Exception {
        DataPipes.init()
                 .writer("hello", writerFI).joiner("hello", joinerFI).reader("hello", readerFI)
                 .writerIdle("--------------").joinerMergeUp("----------").reader("hello", readerFI)
                 .done()
                 .displayNet()
                 .doStream();

    }
}
