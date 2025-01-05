package org.frozenarc.datapipes.common;

import org.frozenarc.datapipes.DataPipeException;
import org.frozenarc.datapipes.joiner.StreamsJoiner;
import org.frozenarc.datapipes.joiner.StreamsJoinerFI;
import org.frozenarc.datapipes.reader.StreamsReader;
import org.frozenarc.datapipes.reader.StreamsReaderFI;

/**
 * Date: 01-01-2025 14:01
 * Author: manan
 * Declares joining and reading methods only to be used while creation of first pipe
 */
public interface JoinerReaderFirstPipeBuilder {

    /**
     * The method accepts StreamsJoiner instance and set it as a joiner of a pipe.
     * @param joiner StreamsJoiner
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderFirstPipeBuilder joiner(StreamsJoiner joiner) throws DataPipeException;

    /**
     * The method accepts name of joiner and StreamsJoinerFI (functional interface) instance to create and set it as a joiner of a pipe.
     * @param name of StreamsJoiner
     * @param joinerFI StreamsJoinerFI
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderFirstPipeBuilder joiner(String name, StreamsJoinerFI joinerFI) throws DataPipeException;


    /**
     * The method accepts StreamsReader instance and set it as a reader of a pipe.
     * @param reader StreamsReader
     * @return instance of WriterFinisherBuilder, so only writing and finishing related method would be exposed
     */
    WriterFinisherBuilder reader(StreamsReader reader);

    /**
     * The method accepts name of reader and StreamsReaderFI (functional interface) instance to create and set it as a reader of a pipe.
     * @param name of StreamsReader
     * @param readerFI StreamsReaderFI
     * @return instance of WriterFinisherBuilder, so only writing and finishing related method would be exposed
     */
    WriterFinisherBuilder reader(String name, StreamsReaderFI readerFI);
}
