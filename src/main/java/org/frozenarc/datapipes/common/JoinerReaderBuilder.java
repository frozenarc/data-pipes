package org.frozenarc.datapipes.common;

import org.frozenarc.datapipes.DataPipeException;
import org.frozenarc.datapipes.joiner.StreamsJoiner;
import org.frozenarc.datapipes.joiner.StreamsJoinerFI;

/**
 * Date: 27-12-2024 18:56
 * Author: manan
 */
public interface JoinerReaderBuilder extends JoinerReaderFirstPipeBuilder {


    JoinerReaderBuilder joiner(StreamsJoiner joiner) throws DataPipeException;

    JoinerReaderBuilder joiner(String name, StreamsJoinerFI joinerFI) throws DataPipeException;


    JoinerReaderBuilder joinerIdle(String _a) throws DataPipeException;

    JoinerReaderBuilder joinerIdle() throws DataPipeException;

    JoinerReaderBuilder joinerMergeUp(String _a) throws DataPipeException;

    JoinerReaderBuilder joinerMergeUp() throws DataPipeException;


    WriterFinisherBuilder readerIdle(String _a);

    WriterFinisherBuilder readerIdle();

    WriterFinisherBuilder readerMergeUp(String _a);

    WriterFinisherBuilder readerMergeUp();
}
