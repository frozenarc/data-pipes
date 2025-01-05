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
 * The main class being used to create multiple data pipes (streams) architecture
 */
public class DataPipes implements WriterFinisherBuilder, JoinerReaderBuilder {

    private static final Logger log = LoggerFactory.getLogger(DataPipes.class);

    private final List<List<WorkerWrapper>> net = new ArrayList<>();
    private final List<WorkerWrapper> workers = new ArrayList<>();
    private final List<PipedStream> pipedStreams = new ArrayList<>();

    /**
     * default private constructor, so no one can create instance of the class without caling `init`
     */
    private DataPipes() {

    }

    /**
     * The method would be called first
     * @return instance of WriterFirstPipeBuilder, so only required method would be exposed to create first pipe
     */
    public static WriterFirstPipeBuilder init() {
        return new DataPipes();
    }

    /**
     * The method accepts StreamsWriter instance and set it as a writer of a pipe.
     * @param writer StreamsWriter
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    public JoinerReaderBuilder writer(StreamsWriter writer) throws DataPipeException {
        WorkerWrapper worker = new WorkerWrapper(writer);
        setWriter(worker);
        workers.add(worker);
        return this;
    }

    /**
     * The method accepts name of writer and StreamsWriterFI (functional interface) instance to create and set it as a writer of a pipe.
     * @param name of StreamsWriter
     * @param writerFI StreamsWriterFI
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    public JoinerReaderBuilder writer(String name, StreamsWriterFI writerFI) throws DataPipeException {
        return writer(Util.writer(name, writerFI));
    }

    /**
     * The method returns idle writer, this could be used when next worker going to work on non-idle pipe and about to spread data onto multiple streams
     * @param _a the alignment parameter only available to arrange one worker below previous one, so whole structure would become easy to verify.
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    public JoinerReaderBuilder writerIdle(String _a) throws DataPipeException {
        return writer("Idle", Util.idleWriterFI);
    }

    /**
     * The method returns idle writer, this could be used when next worker going to work on non-idle pipe and about to spread data onto multiple streams
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    public JoinerReaderBuilder writerIdle() throws DataPipeException {
        return writerIdle("");
    }

    /**
     * The method combines current writer to above one so pipe would go through that writer, so above writer can cross transfer data.
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    public JoinerReaderBuilder writerCombine() throws DataPipeException {
        return writerCombine("");
    }

    /**
     * The method combines current writer to above one so pipe would go through that writer, so above writer can cross transfer data.
     * @param _a the alignment parameter only available to arrange one worker below above one, so whole structure would become easy to verify.
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    public JoinerReaderBuilder writerCombine(String _a) throws DataPipeException {
        List<WorkerWrapper> prevPipe = getLatestPipe(net);
        WorkerWrapper worker = getFirstWorker(prevPipe);
        setWriter(worker);
        return this;
    }

    private void setWriter(WorkerWrapper worker) throws DataPipeException {
        PipedStream pipedStream = new PipedStream();
        worker.addNextPipedStream(pipedStream);
        List<WorkerWrapper> pipe = new ArrayList<>();
        pipe.add(worker);
        net.add(pipe);
        pipedStreams.add(pipedStream);
    }

    /**
     * The method accepts StreamsJoiner instance and set it as a joiner of a pipe.
     * @param joiner StreamsJoiner
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    public JoinerReaderBuilder joiner(StreamsJoiner joiner) throws DataPipeException {
        List<WorkerWrapper> pipe = getLatestPipe(net);
        WorkerWrapper prevWorker = getLatestWorker(pipe);
        WorkerWrapper worker = new WorkerWrapper(joiner);
        setJoiner(worker, prevWorker, pipe);
        workers.add(worker);
        return this;
    }

    /**
     * The method accepts name of joiner and StreamsJoinerFI (functional interface) instance to create and set it as a joiner of a pipe.
     * @param name of StreamsJoiner
     * @param joinerFI StreamsJoinerFI
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    public JoinerReaderBuilder joiner(String name, StreamsJoinerFI joinerFI) throws DataPipeException {
        return joiner(Util.joiner(name, joinerFI));
    }

    /**
     * The method returns idle joiner, this could be used when next and/or previous worker going to work on non-idle pipe and about to spread data onto multiple streams
     * @param _a the alignment parameter only available to arrange one worker below above one, so whole structure would become easy to verify.
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    public JoinerReaderBuilder joinerIdle(String _a) throws DataPipeException {
        return joiner("Idle", Util.idleJoinerFI);
    }

    /**
     * The method returns idle joiner, this could be used when next and/or previous worker going to work on non-idle pipe and about to spread data onto multiple streams
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    public JoinerReaderBuilder joinerIdle() throws DataPipeException {
        return joinerIdle("");
    }

    /**
     * The method combines current joiner to above one so pipe would go through that joiner, so above joiner can cross transfer data.
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    public JoinerReaderBuilder joinerCombine() throws DataPipeException {
        return joinerCombine("");
    }

    /**
     * The method combines current joiner to above one so pipe would go through that joiner, so above joiner can cross transfer data.
     * @param _a the alignment parameter only available to arrange one worker below above one, so whole structure would become easy to verify.
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    public JoinerReaderBuilder joinerCombine(String _a) throws DataPipeException {
        List<WorkerWrapper> prevPipe = getPrevPipe(net);
        List<WorkerWrapper> pipe = getLatestPipe(net);
        WorkerWrapper prevWorker = getLatestWorker(pipe);
        WorkerWrapper worker = getWorkerAtLevel(prevPipe, pipe);
        setJoiner(worker, prevWorker, pipe);
        return this;
    }

    private void setJoiner(WorkerWrapper worker, WorkerWrapper prevWorker, List<WorkerWrapper> pipe) throws DataPipeException {
        worker.addPrevPipedStream(prevWorker.getNextPipedStream());
        PipedStream pipedStream = new PipedStream();
        worker.addNextPipedStream(pipedStream);
        pipe.add(worker);
        pipedStreams.add(pipedStream);
    }

    /**
     * The method accepts StreamsReader instance and set it as a reader of a pipe.
     * @param reader StreamsReader
     * @return instance of WriterFinisherBuilder, so only writing and finishing related method would be exposed
     */
    public WriterFinisherBuilder reader(StreamsReader reader) {
        List<WorkerWrapper> pipe = getLatestPipe(net);
        WorkerWrapper prevWorker = getLatestWorker(pipe);
        WorkerWrapper worker = new WorkerWrapper(reader);
        setReader(worker, prevWorker, pipe);
        workers.add(worker);
        return this;
    }

    /**
     * The method accepts name of reader and StreamsReaderFI (functional interface) instance to create and set it as a reader of a pipe.
     * @param name of StreamsReader
     * @param readerFI StreamsReaderFI
     * @return instance of WriterFinisherBuilder, so only writing and finishing related method would be exposed
     */
    public WriterFinisherBuilder reader(String name, StreamsReaderFI readerFI) {
        return reader(Util.reader(name, readerFI));
    }

    /**
     * The method returns idle reader, this could be used when previous worker going to work on non-idle pipe and about to converge data onto few streams
     * @param _a the alignment parameter only available to arrange one worker below above one, so whole structure would become easy to verify.
     * @return instance of WriterFinisherBuilder, so only writing and finishing related method would be exposed
     */
    public WriterFinisherBuilder readerIdle(String _a) {
        return reader("Idle", Util.idleReaderFI);
    }

    /**
     * The method returns idle reader, this could be used when previous worker going to work on non-idle pipe and about to converge data onto few streams
     * @return instance of WriterFinisherBuilder, so only writing and finishing related method would be exposed
     */
    public WriterFinisherBuilder readerIdle() {
        return readerIdle("");
    }

    /**
     * The method combines current reader to above one so pipe would go through that reader, so above reader can cross transfer data.
     * @return instance of WriterFinisherBuilder, so only writing and finishing related method would be exposed
     */
    public WriterFinisherBuilder readerCombine() {
        return readerCombine("");
    }

    /**
     * The method combines current reader to above one so pipe would go through that reader, so above reader can cross transfer data.
     * @param _a the alignment parameter only available to arrange one worker below above one, so whole structure would become easy to verify.
     * @return instance of WriterFinisherBuilder, so only writing and finishing related method would be exposed
     */
    public WriterFinisherBuilder readerCombine(String _a) {
        List<WorkerWrapper> prevPipe = getPrevPipe(net);
        List<WorkerWrapper> pipe = getLatestPipe(net);
        WorkerWrapper prevWorker = getLatestWorker(pipe);
        WorkerWrapper worker = getLatestWorker(prevPipe);
        setReader(worker, prevWorker, pipe);
        return this;
    }

    private void setReader(WorkerWrapper worker, WorkerWrapper prevWorker, List<WorkerWrapper> pipe) {
        worker.addPrevPipedStream(prevWorker.getNextPipedStream());
        pipe.add(worker);
    }

    private WorkerWrapper getWorkerAtLevel(List<WorkerWrapper> prevPipe, List<WorkerWrapper> pipe) {
        return prevPipe.get(pipe.size());
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

    /**
     * The method return instance of DataPipes so other methods declared in class can be called after the call.
     * @return instance of DataPipes
     */
    public DataPipes done() {
        return this;
    }

    /**
     * Shows the whole architecture, so one can verify end ot end before starting streaming
     * @return instance of DataPipes to be called further methods from the class
     */
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

    /**
     * Method to be called to start streaming
     * @throws DataPipeException e
     */
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

    /**
     * Method to be called to start streaming
     * @param executor specific executor can be pass if needed
     * @throws DataPipeException e
     */
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

    /**
     * To terminate all streams in case of deadlock
     * @throws DataPipeException e
     */
    public void closeAllPipedStreams() throws DataPipeException {
        for (PipedStream pipedStream : pipedStreams) {
            pipedStream.close();
        }
    }
}
