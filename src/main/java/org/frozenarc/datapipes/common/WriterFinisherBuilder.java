package org.frozenarc.datapipes.common;

import org.frozenarc.datapipes.DataPipes;

/**
 * Date: 27-12-2024 19:06
 * Author: manan
 * Declares method which could be used while finishing pipe structure
 */
public interface WriterFinisherBuilder extends WriterBuilder {

    /**
     * The method return instance of DataPipes so other methods declared in class can be called after the call.
     * @return instance of DataPipes
     */
    DataPipes done();
}
