package org.frozenarc.datapipes;

import org.frozenarc.datapipes.common.StreamProcessException;
import org.frozenarc.datapipes.common.StreamsWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * Date: 26-12-2024 18:38
 * Author: manan
 */
class WorkerWrapper {

    private static final Logger log = LoggerFactory.getLogger(WorkerWrapper.class);

    private final List<PipedStream> prevPipedStreams;
    private final StreamsWorker worker;
    private final List<PipedStream> nextPipedStreams;

    WorkerWrapper(StreamsWorker worker) {
        this.worker = worker;
        this.prevPipedStreams = new ArrayList<>();
        this.nextPipedStreams = new ArrayList<>();
    }

    void addPrevPipedStream(PipedStream pipedStream) {
        prevPipedStreams.add(pipedStream);
    }

    void addNextPipedStream(PipedStream pipedStream) {
        nextPipedStreams.add(pipedStream);
    }

    void callWorker() throws StreamProcessException {
        InputStream[] inputStreams = getInputStreams();
        OutputStream[] outputStreams = getOutputStreams();
        worker.work(inputStreams, outputStreams);
    }

    private OutputStream[] getOutputStreams() {
        OutputStream[] outputStreams = new OutputStream[nextPipedStreams.size()];
        for (int i = 0; i < nextPipedStreams.size(); i++) {
            outputStreams[i] = nextPipedStreams.get(i).getOutputStream();
        }
        return outputStreams;
    }

    private InputStream[] getInputStreams() {
        InputStream[] inputStreams = new InputStream[prevPipedStreams.size()];
        for (int i = 0; i < prevPipedStreams.size(); i++) {
            inputStreams[i] = prevPipedStreams.get(i).getInputStream();
        }
        return inputStreams;
    }

    PipedStream getNextPipedStream() {
        return nextPipedStreams.get(nextPipedStreams.size() - 1);
    }

    CompletableFuture<Void> getFuture(ExecutorService executor, Consumer<StreamProcessException> expConsumer) {
        return CompletableFuture.runAsync(() -> {
                                              boolean error = false;
                                              try {
                                                  log.debug("{}: {}: started ", worker.type(), worker.name());
                                                  callWorker();
                                                  log.debug("{}: {}: done ", worker.type(), worker.name());
                                              } catch (StreamProcessException ex) {
                                                  error = true;
                                                  expConsumer.accept(ex);
                                                  log.error("{}: {}: error ", worker.type(), worker.name(), ex);
                                              } finally {
                                                  for (PipedStream ps : prevPipedStreams) {
                                                      ps.closeAfterRead(error);
                                                  }
                                                  for (PipedStream ps : nextPipedStreams) {
                                                      ps.closeAfterWrite(error);
                                                  }
                                              }
                                          },
                                          executor);
    }

    public String toString() {
        return worker.name();
    }

    List<PipedStream> getPrevPipedStreams() {
        return prevPipedStreams;
    }

    List<PipedStream> getNextPipedStreams() {
        return nextPipedStreams;
    }

}
