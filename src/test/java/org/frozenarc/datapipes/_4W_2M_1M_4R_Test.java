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
 * Date: 02-01-2025 15:04
 */
public class _4W_2M_1M_4R_Test {

    private static StreamsWriterFI getWriter(String abcd) {
        return outputStreams -> {
            try {
                outputStreams[0].write(abcd.getBytes());
            } catch (IOException e) {
                throw new WriteException(e);
            }
        };
    }

    StreamsJoinerFI joinerFI1 = (inputStreams, outputStreams) -> {
        try {
            System.out.println(inputStreams.length);
            int d;
            while ((d = inputStreams[0].read()) != -1) {
                outputStreams[0].write(d);
            }
            while ((d = inputStreams[1].read()) != -1) {
                outputStreams[0].write(d);
            }
        } catch (IOException ex) {
            throw new JoinException(ex);
        }
    };

    StreamsJoinerFI joinerFI2 = (inputStreams, outputStreams) -> {
        try {
            System.out.println(inputStreams.length);
            int d;
            while ((d = inputStreams[0].read()) != -1) {
                outputStreams[0].write(d);
            }
            while ((d = inputStreams[2].read()) != -1) {
                outputStreams[0].write(d);
            }
        } catch (IOException ex) {
            throw new JoinException(ex);
        }
    };

    StreamsReaderFI readerFI = inputStreams -> {
        try {
            inputStreams[0].transferTo(System.out);
            System.out.println("****************");
        } catch (IOException e) {
            throw new ReadException(e);
        }
    };

    @Test
    public void test() throws Exception {
        DataPipes.init()
                 .writer("ABCD", getWriter("ABCD")).joiner("ABCDEFGH", joinerFI1).joiner("ALL", joinerFI2).reader("ABCD", readerFI)
                 .writer("EFGH", getWriter("EFGH")).joinerCombine("------------- ").joinerCombine("--------- ").reader("EFGH", readerFI)
                 .writer("IJKL", getWriter("IJKL")).joiner("IJKLMNOP", joinerFI1).joinerCombine("--------- ").reader("IJKL", readerFI)
                 .writer("MNOP", getWriter("MNOP")).joinerCombine("------------- ").joinerCombine("--------- ").reader("MNOP", readerFI)
                 .done()
                 .displayNet()
                 .doStream();
    }
}
