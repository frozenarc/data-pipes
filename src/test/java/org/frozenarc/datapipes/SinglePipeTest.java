package org.frozenarc.datapipes;

import org.frozenarc.datapipes.joiner.JoinException;
import org.frozenarc.datapipes.joiner.StreamsJoinerFI;
import org.frozenarc.datapipes.reader.ReadException;
import org.frozenarc.datapipes.reader.StreamsReaderFI;
import org.frozenarc.datapipes.writer.StreamsWriterFI;
import org.frozenarc.datapipes.writer.WriteException;
import org.junit.Test;

import java.io.IOException;

/**
 * Author: mpanchal
 * Date: 28-12-2024 07:12
 */
public class SinglePipeTest {

    private static StreamsWriterFI writerFI = outputStream -> {
        try {
            outputStream[0].write("hello".getBytes());
        } catch (IOException e) {
            throw new WriteException(e);
        }
    };

    private static StreamsJoinerFI joinerFI = (inputStreams, outputStreams) -> {
        try {
            inputStreams[0].transferTo(outputStreams[0]);
        } catch (IOException e) {
            throw new JoinException(e);
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
                 .done()
                 .displayNet()
                 .doStream();
    }
}
