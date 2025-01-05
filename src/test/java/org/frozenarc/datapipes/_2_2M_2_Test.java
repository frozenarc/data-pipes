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
 * Date: 28-12-2024 07:37
 */
public class _2_2M_2_Test {

    private static StreamsWriter writer1 = new StreamsWriter() {
        @Override
        public void writeTo(OutputStream[] outputStream) throws WriteException {
            try {
                outputStream[0].write("AFCH".getBytes());
            } catch (IOException e) {
                throw new WriteException(e);
            }
        }

        @Override
        public String name() {
            return "writer1";
        }
    };

    private static StreamsWriter writer2 = new StreamsWriter() {
        @Override
        public void writeTo(OutputStream[] outputStream) throws WriteException {
            try {
                outputStream[0].write("EBGD".getBytes());
            } catch (IOException e) {
                throw new WriteException(e);
            }
        }

        @Override
        public String name() {
            return "writer2";
        }
    };

    private static StreamsJoiner joiner = new StreamsJoiner() {
        @Override
        public void join(InputStream[] inputStreams, OutputStream[] outputStreams) throws JoinException {
            try {
                int d1, d2;
                boolean swap = true;
                do {
                    d1 = inputStreams[0].read();
                    d2 = inputStreams[1].read();
                    if (swap) {
                        if (d1 != -1) {
                            outputStreams[0].write(d1);
                        }
                        if (d2 != -1) {
                            outputStreams[1].write(d2);
                        }
                    } else {
                        if (d1 != -1) {
                            outputStreams[1].write(d1);
                        }
                        if (d2 != -1) {
                            outputStreams[0].write(d2);
                        }
                    }
                    swap = !swap;
                } while (d1 != -1 || d2 != -1);
            } catch (IOException e) {
                throw new JoinException(e);
            }
        }

        @Override
        public String name() {
            return "joiner";
        }
    };

    private static StreamsReader reader1 = new StreamsReader() {
        @Override
        public void readFrom(InputStream[] inputStreams) throws ReadException {
            try {
                inputStreams[0].transferTo(System.out);
            } catch (IOException e) {
                throw new ReadException(e);
            }

        }

        @Override
        public String name() {
            return "reader1";
        }
    };

    private static StreamsReader reader2 = new StreamsReader() {
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
            return "reader2";
        }
    };

    @Test
    public void test() throws Exception {
        DataPipes.init()
                 .writer(writer1).joiner(joiner).reader(reader1)
                 .writer(writer2).joinerMergeUp().reader(reader2)
                 .done()
                 .displayNet()
                 .doStream();
    }
}
