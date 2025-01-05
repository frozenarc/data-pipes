package org.frozenarc.datapipes;

import org.frozenarc.datapipes.common.JoinerReaderBuilder;
import org.frozenarc.datapipes.common.WriterFinisherBuilder;
import org.frozenarc.datapipes.common.WriterFirstPipeBuilder;
import org.frozenarc.datapipes.joiner.StreamsJoiner;
import org.frozenarc.datapipes.joiner.StreamsJoinerFI;
import org.frozenarc.datapipes.reader.StreamsReader;
import org.frozenarc.datapipes.reader.StreamsReaderFI;
import org.frozenarc.datapipes.writer.StreamsWriter;
import org.frozenarc.datapipes.writer.StreamsWriterFI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date: 26-12-2024 17:10
 * Author: manan
 */
public class DataPipes implements WriterFinisherBuilder, JoinerReaderBuilder {

    private static final Logger log = LoggerFactory.getLogger(DataPipes.class);

    private final List<List<WorkerWrapper>> net = new ArrayList<>();
    private final List<WorkerWrapper> workers = new ArrayList<>();
    private final List<PipedStream> pipedStreams = new ArrayList<>();

    public static WriterFirstPipeBuilder init() {
        return new DataPipes();
    }

    public JoinerReaderBuilder writer(StreamsWriter writer) throws DataPipeException {
        WorkerWrapper worker = new WorkerWrapper(writer);
        setWriter(worker);
        workers.add(worker);
        return this;
    }

    public JoinerReaderBuilder writerMergeUp() throws DataPipeException {
        return writerMergeUp("");
    }

    public JoinerReaderBuilder writerMergeUp(String _a) throws DataPipeException {
        List<WorkerWrapper> prevPipe = getLatestPipe(net);
        WorkerWrapper worker = getFirstWorker(prevPipe);
        setWriter(worker);
        return this;
    }

    public JoinerReaderBuilder writer(String name, StreamsWriterFI writerFI) throws DataPipeException {
        return writer(Util.writerFI(name, writerFI));
    }

    public JoinerReaderBuilder writerIdle(String _a) throws DataPipeException {
        return writer("Idle", Util.idleWriterFI);
    }

    public JoinerReaderBuilder writerIdle() throws DataPipeException {
        return writerIdle("");
    }

    private void setWriter(WorkerWrapper worker) throws DataPipeException {
        PipedStream pipedStream = new PipedStream();
        worker.addNextPipedStream(pipedStream);
        List<WorkerWrapper> pipe = new ArrayList<>();
        pipe.add(worker);
        net.add(pipe);
        pipedStreams.add(pipedStream);
    }

    public JoinerReaderBuilder joiner(StreamsJoiner joiner) throws DataPipeException {
        List<WorkerWrapper> pipe = getLatestPipe(net);
        WorkerWrapper prevWorker = getLatestWorker(pipe);
        WorkerWrapper worker = new WorkerWrapper(joiner);
        setJoiner(worker, prevWorker, pipe);
        workers.add(worker);
        return this;
    }

    public JoinerReaderBuilder joinerMergeUp() throws DataPipeException {
        return joinerMergeUp("");
    }

    public JoinerReaderBuilder joinerMergeUp(String _a) throws DataPipeException {
        List<WorkerWrapper> prevPipe = getPrevPipe(net);
        List<WorkerWrapper> pipe = getLatestPipe(net);
        WorkerWrapper prevWorker = getLatestWorker(pipe);
        WorkerWrapper worker = getWorkerAtLevel(prevPipe, pipe);
        setJoiner(worker, prevWorker, pipe);
        return this;
    }

    private WorkerWrapper getWorkerAtLevel(List<WorkerWrapper> prevPipe, List<WorkerWrapper> pipe) {
        return prevPipe.get(pipe.size());
    }

    public JoinerReaderBuilder joiner(String name, StreamsJoinerFI joinerFI) throws DataPipeException {
        return joiner(Util.joinerFI(name, joinerFI));
    }

    public JoinerReaderBuilder joinerIdle(String _a) throws DataPipeException {
        return joiner("Idle", Util.idleJoinerFI);
    }

    public JoinerReaderBuilder joinerIdle() throws DataPipeException {
        return joinerIdle("");
    }

    private void setJoiner(WorkerWrapper worker, WorkerWrapper prevWorker, List<WorkerWrapper> pipe) throws DataPipeException {
        worker.addPrevPipedStream(prevWorker.getNextPipedStream());
        PipedStream pipedStream = new PipedStream();
        worker.addNextPipedStream(pipedStream);
        pipe.add(worker);
        pipedStreams.add(pipedStream);
    }

    public WriterFinisherBuilder reader(StreamsReader reader) {
        List<WorkerWrapper> pipe = getLatestPipe(net);
        WorkerWrapper prevWorker = getLatestWorker(pipe);
        WorkerWrapper worker = new WorkerWrapper(reader);
        setReader(worker, prevWorker, pipe);
        workers.add(worker);
        return this;
    }

    public WriterFinisherBuilder readerMergeUp() {
        return readerMergeUp("");
    }

    public WriterFinisherBuilder readerMergeUp(String _a) {
        List<WorkerWrapper> prevPipe = getPrevPipe(net);
        List<WorkerWrapper> pipe = getLatestPipe(net);
        WorkerWrapper prevWorker = getLatestWorker(pipe);
        WorkerWrapper worker = getLatestWorker(prevPipe);
        setReader(worker, prevWorker, pipe);
        return this;
    }

    public WriterFinisherBuilder reader(String name, StreamsReaderFI readerFI) {
        return reader(Util.readerFI(name, readerFI));
    }

    public WriterFinisherBuilder readerIdle(String _a) {
        return reader("Idle", Util.idleReaderFI);
    }

    public WriterFinisherBuilder readerIdle() {
        return readerIdle("");
    }

    private void setReader(WorkerWrapper worker, WorkerWrapper prevWorker, List<WorkerWrapper> pipe) {
        worker.addPrevPipedStream(prevWorker.getNextPipedStream());
        pipe.add(worker);
    }

    private static WorkerWrapper getLatestWorker(List<WorkerWrapper> pipe) {
        return pipe.get(pipe.size() - 1);
    }

    private static WorkerWrapper getFirstWorker(List<WorkerWrapper> pipe) {
        return pipe.get(0);
    }

    private List<WorkerWrapper> getLatestPipe(List<List<WorkerWrapper>> net) {
        return net.get(net.size() - 1);
    }

    private List<WorkerWrapper> getPrevPipe(List<List<WorkerWrapper>> net) {
        return net.get(net.size() - 2);
    }

    public DataPipes done() {
        return this;
    }

    public DataPipes displayNet() {
        System.out.println();
        for (List<WorkerWrapper> workerWrappers : net) {
            for (WorkerWrapper worker : workerWrappers) {
                System.out.print("--" + worker.getPrevPipedStreams().size() + "--");
                System.out.print(worker);
                System.out.print("--" + worker.getNextPipedStreams().size() + "--");
            }
            System.out.println();
        }
        System.out.println();
        return this;
    }

    public void doStream() throws DataPipeException {
        ExecutorService executor = null;
        try {
            executor = Executors.newFixedThreadPool(workers.size());
            doStream(executor);
        } finally {
            if (executor != null) {
                executor.shutdown();
                log.debug("Executor has been shutdown");
            }
        }
    }

    public void doStream(ExecutorService executor) throws DataPipeException {
        try {
            List<CompletableFuture<Void>> futureList = new ArrayList<>();
            List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());
            for (WorkerWrapper worker : workers) {
                futureList.add(worker.getFuture(executor, exceptions::add));
            }
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{}))
                             .join();

            log.debug("streaming is done");

            if (!exceptions.isEmpty()) {
                DataPipeException exp = new DataPipeException(exceptions.get(0));
                for (int j = 1; j < exceptions.size(); j++) {
                    exp.addSuppressed(exceptions.get(j));
                }
                throw exp;
            }

            log.debug("doStream end");
        } finally {
            closeAllPipedStreams();
            log.debug("all piped streams are closed");
        }
    }

    public void closeAllPipedStreams() throws DataPipeException {
        for (PipedStream pipedStream : pipedStreams) {
            pipedStream.close();
        }
    }
}
