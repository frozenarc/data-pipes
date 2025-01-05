package org.frozenarc.datapipes.common;

import org.frozenarc.datapipes.DataPipeException;
import org.frozenarc.datapipes.writer.StreamsWriter;
import org.frozenarc.datapipes.writer.StreamsWriterFI;

/**
 * Date: 01-01-2025 14:00
 * Author: manan
 * Declares writing methods only to be used while creation of first pipe
 */
public interface WriterFirstPipeBuilder {

    /**
     * The method accepts StreamsWriter instance and set it as a writer of a pipe.
     * @param writer StreamsWriter
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderFirstPipeBuilder writer(StreamsWriter writer) throws DataPipeException;

    /**
     * The method accepts name of writer and StreamsWriterFI (functional interface) instance to create and set it as a writer of a pipe.
     * @param name of StreamsWriter
     * @param writerFI StreamsWriterFI
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderFirstPipeBuilder writer(String name, StreamsWriterFI writerFI) throws DataPipeException;
}
