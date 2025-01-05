package org.frozenarc.datapipes.common;

import org.frozenarc.datapipes.DataPipeException;
import org.frozenarc.datapipes.writer.StreamsWriter;
import org.frozenarc.datapipes.writer.StreamsWriterFI;

/**
 * Date: 27-12-2024 18:53
 * Author: manan
 */
public interface WriterBuilder extends WriterFirstPipeBuilder {

    JoinerReaderBuilder writer(StreamsWriter writer) throws DataPipeException;

    JoinerReaderBuilder writer(String name, StreamsWriterFI writerFI) throws DataPipeException;

    JoinerReaderBuilder writerIdle(String _a) throws DataPipeException;

    JoinerReaderBuilder writerIdle() throws DataPipeException;

    JoinerReaderBuilder writerMergeUp(String _a) throws DataPipeException;

    JoinerReaderBuilder writerMergeUp() throws DataPipeException;

}
