package org.frozenarc.datapipes;

import org.frozenarc.datapipes.joiner.JoinException;
import org.frozenarc.datapipes.joiner.StreamsJoiner;
import org.frozenarc.datapipes.reader.ReadException;
import org.frozenarc.datapipes.reader.StreamsReader;
import org.frozenarc.datapipes.writer.StreamsWriter;
import org.frozenarc.datapipes.writer.WriteException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author: mpanchal
 * Date: 28-12-2024 08:34
 */
public class _2_2M_1_Test {

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

    private static StreamsJoiner joiner = new StreamsJoiner() {
        @Override
        public void join(InputStream[] inputStreams, OutputStream[] outputStreams) throws JoinException {
            try {
                int d1, d2;
                do {
                    d1 = inputStreams[0].read();
                    d2 = inputStreams[1].read();
                    if (d1 != -1) {
                        outputStreams[0].write(d1);
                    }
                    if (d2 != -1) {
                        outputStreams[0].write(d2);
                    }
                } while (d1 != -1 || d2 != -1);
            } catch (IOException e) {
                throw new JoinException(e);
            }
        }

        @Override
        public String name() {
            return "hello";
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

    @Test
    public void test() throws Exception {
        DataPipes.init()
                 .writer(writer).joiner(joiner).reader(reader)
                 .writer(writer).joinerMergeUp().readerIdle()
                 .done()
                 .displayNet()
                 .doStream();
    }
}
