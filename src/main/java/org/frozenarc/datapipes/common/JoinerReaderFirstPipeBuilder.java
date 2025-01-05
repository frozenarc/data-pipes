package org.frozenarc.datapipes.common;

import org.frozenarc.datapipes.DataPipeException;
import org.frozenarc.datapipes.joiner.StreamsJoiner;
import org.frozenarc.datapipes.joiner.StreamsJoinerFI;
import org.frozenarc.datapipes.reader.StreamsReader;
import org.frozenarc.datapipes.reader.StreamsReaderFI;

/**
 * Date: 01-01-2025 14:01
 * Author: manan
 */
public interface JoinerReaderFirstPipeBuilder {

    JoinerReaderFirstPipeBuilder joiner(StreamsJoiner joiner) throws DataPipeException;

    JoinerReaderFirstPipeBuilder joiner(String name, StreamsJoinerFI joinerFI) throws DataPipeException;


    WriterFinisherBuilder reader(StreamsReader reader);

    WriterFinisherBuilder reader(String name, StreamsReaderFI readerFI);
}
