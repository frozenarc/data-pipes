package org.frozenarc.datapipes.common;

import org.frozenarc.datapipes.DataPipeException;
import org.frozenarc.datapipes.writer.StreamsWriter;
import org.frozenarc.datapipes.writer.StreamsWriterFI;

/**
 * Date: 01-01-2025 14:00
 * Author: manan
 */
public interface WriterFirstPipeBuilder {

    JoinerReaderFirstPipeBuilder writer(StreamsWriter writer) throws DataPipeException;

    JoinerReaderFirstPipeBuilder writer(String name, StreamsWriterFI writerFI) throws DataPipeException;
}
