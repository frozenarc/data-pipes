package org.frozenarc.datapipes;

import org.frozenarc.datapipes.reader.ReadException;
import org.frozenarc.datapipes.reader.StreamsReader;
import org.frozenarc.datapipes.writer.StreamsWriter;
import org.frozenarc.datapipes.writer.WriteException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;

/**
 * Author: mpanchal
 * Date: 02-01-2025 16:39
 */
public class DeadLockTest {

    private static StreamsWriter writer = new StreamsWriter() {
        @Override
        public void writeTo(OutputStream[] outputStream) throws WriteException {
            try {
                StringBuilder builder = new StringBuilder();
                builder.append("A".repeat(1025));
                System.out.println("Writing started");
                outputStream[0].write(builder.toString().getBytes());
                System.out.println("Writing 1st done");
                outputStream[1].write(builder.toString().toLowerCase().getBytes());
                System.out.println("Writing done");
            } catch (IOException e) {
                throw new WriteException(e);
            }
        }

        @Override
        public String name() {
            return "writer";
        }
    };

    private static StreamsReader reader = new StreamsReader() {
        @Override
        public void readFrom(InputStream[] inputStreams) throws ReadException {
            try {
                System.out.println("Reading started");
                inputStreams[1].transferTo(System.out);
                System.out.println("Reading 2nd done");
                inputStreams[0].transferTo(System.out);
                System.out.println("Reading done");
            } catch (IOException e) {
                throw new ReadException(e);
            }
        }

        @Override
        public String name() {
            return "reader";
        }
    };

    /*@Test
    public void test() {

        DataPipes net = (DataPipes) DataPipes.init();
        CompletableFuture<Void> futures = CompletableFuture.allOf(CompletableFuture.runAsync(() -> {
            try {
                net.writer(writer).reader(reader)
                   .writerMergeUp().readerMergeUp()
                   .done()
                   .displayNet()
                   .doStream();
            } catch (DataPipeException e) {
                throw new RuntimeException(e);
            }
        }), CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Sleep started");
                Thread.sleep(5000);
                System.out.println("Sleep done, closing all pipes");
                net.closeAllPipedStreams();
                System.out.println("All pipes closed");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));

        futures.join();
    }*/
}
