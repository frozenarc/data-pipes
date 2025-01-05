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
 * Date: 01-01-2025 15:46
 * Author: manan
 */
public class _2_2M_2M_2_Test {

    private static StreamsWriterFI getWriter(String abcd) {
        return outputStreams -> {
            try {
                outputStreams[0].write(abcd.getBytes());
            } catch (IOException e) {
                throw new WriteException(e);
            }
        };
    }

    private static StreamsJoinerFI joinerFI = (inputStreams, outputStreams) -> {
        try {
            inputStreams[0].transferTo(outputStreams[1]);
            inputStreams[1].transferTo(outputStreams[0]);
        } catch (IOException e) {
            throw new JoinException(e);
        }
    };

    private static StreamsReaderFI readerFI = inputStreams -> {
        try {
            inputStreams[0].transferTo(System.out);
        } catch (IOException e) {
            throw new ReadException(e);
        }
    };

    @Test
    public void test() throws Exception {
        DataPipes.init()
                 .writer("single1", getWriter("ABCD")).joiner("multi1", joinerFI).joiner("multi2", joinerFI).reader("single", readerFI)
                 .writer("single2", getWriter("WXYZ")).joinerMergeUp("------------").joinerMergeUp("-----------").reader("single2", readerFI)
                 .done()
                 .displayNet()
                 .doStream();
    }
}
