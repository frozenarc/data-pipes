package org.frozenarc.datapipes.common;

import org.frozenarc.datapipes.DataPipeException;
import org.frozenarc.datapipes.writer.StreamsWriter;
import org.frozenarc.datapipes.writer.StreamsWriterFI;

/**
 * Date: 27-12-2024 18:53
 * Author: manan
 * Declares all methods which could be used for writing
 */
public interface WriterBuilder extends WriterFirstPipeBuilder {

    /**
     * The method accepts StreamsWriter instance and set it as a writer of a pipe.
     * @param writer StreamsWriter
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderBuilder writer(StreamsWriter writer) throws DataPipeException;

    /**
     * The method accepts name of writer and StreamsWriterFI (functional interface) instance to create and set it as a writer of a pipe.
     * @param name of StreamsWriter
     * @param writerFI StreamsWriterFI
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderBuilder writer(String name, StreamsWriterFI writerFI) throws DataPipeException;

    /**
     * The method returns idle writer, this could be used when next worker going to work on non-idle pipe and about to spread data onto multiple streams
     * @param _a the alignment parameter only available to arrange one worker below previous one, so whole structure would become easy to verify.
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderBuilder writerIdle(String _a) throws DataPipeException;

    /**
     * The method returns idle writer, this could be used when next worker going to work on non-idle pipe and about to spread data onto multiple streams
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderBuilder writerIdle() throws DataPipeException;

    /**
     * The method combines current writer to above one so pipe would go through that writer, so above writer can cross transfer data.
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderBuilder writerCombine() throws DataPipeException;

    /**
     * The method combines current writer to above one so pipe would go through that writer, so above writer can cross transfer data.
     * @param _a the alignment parameter only available to arrange one worker below above one, so whole structure would become easy to verify.
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderBuilder writerCombine(String _a) throws DataPipeException;

}
