package org.frozenarc.datapipes.common;

import org.frozenarc.datapipes.DataPipeException;
import org.frozenarc.datapipes.joiner.StreamsJoiner;
import org.frozenarc.datapipes.joiner.StreamsJoinerFI;

/**
 * Date: 27-12-2024 18:56
 * Author: manan
 * Declares all methods which could be used for joining and reading
 */
public interface JoinerReaderBuilder extends JoinerReaderFirstPipeBuilder {

    /**
     * The method accepts StreamsJoiner instance and set it as a joiner of a pipe.
     * @param joiner StreamsJoiner
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderBuilder joiner(StreamsJoiner joiner) throws DataPipeException;

    /**
     * The method accepts name of joiner and StreamsJoinerFI (functional interface) instance to create and set it as a joiner of a pipe.
     * @param name of StreamsJoiner
     * @param joinerFI StreamsJoinerFI
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderBuilder joiner(String name, StreamsJoinerFI joinerFI) throws DataPipeException;

    /**
     * The method returns idle joiner, this could be used when next and/or previous worker going to work on non-idle pipe and about to spread data onto multiple streams
     * @param _a the alignment parameter only available to arrange one worker below above one, so whole structure would become easy to verify.
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderBuilder joinerIdle(String _a) throws DataPipeException;

    /**
     * The method returns idle joiner, this could be used when next and/or previous worker going to work on non-idle pipe and about to spread data onto multiple streams
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderBuilder joinerIdle() throws DataPipeException;

    /**
     * The method combines current joiner to above one so pipe would go through that joiner, so above joiner can cross transfer data.
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderBuilder joinerCombine() throws DataPipeException;

    /**
     * The method combines current joiner to above one so pipe would go through that joiner, so above joiner can cross transfer data.
     * @param _a the alignment parameter only available to arrange one worker below above one, so whole structure would become easy to verify.
     * @return instance of JoinerReaderBuilder, so only joining and reading related method would be exposed
     * @throws DataPipeException e
     */
    JoinerReaderBuilder joinerCombine(String _a) throws DataPipeException;

    /**
     * The method returns idle reader, this could be used when previous worker going to work on non-idle pipe and about to converge data onto few streams
     * @param _a the alignment parameter only available to arrange one worker below above one, so whole structure would become easy to verify.
     * @return instance of WriterFinisherBuilder, so only writing and finishing related method would be exposed
     */
    WriterFinisherBuilder readerIdle(String _a);

    /**
     * The method returns idle reader, this could be used when previous worker going to work on non-idle pipe and about to converge data onto few streams
     * @return instance of WriterFinisherBuilder, so only writing and finishing related method would be exposed
     */
    WriterFinisherBuilder readerIdle();

    /**
     * The method combines current reader to above one so pipe would go through that reader, so above reader can cross transfer data.
     * @return instance of WriterFinisherBuilder, so only writing and finishing related method would be exposed
     */
    WriterFinisherBuilder readerCombine();

    /**
     * The method combines current reader to above one so pipe would go through that reader, so above reader can cross transfer data.
     * @param _a the alignment parameter only available to arrange one worker below above one, so whole structure would become easy to verify.
     * @return instance of WriterFinisherBuilder, so only writing and finishing related method would be exposed
     */
    WriterFinisherBuilder readerCombine(String _a);
}
