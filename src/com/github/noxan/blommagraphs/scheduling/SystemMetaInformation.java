package com.github.noxan.blommagraphs.scheduling;


/**
 * Interface for all methods to define the system meta information which are
 * later used for the scheduling algorithm.
 * 
 * @author noxan
 */
public interface SystemMetaInformation {
    /**
     * Sets the count of processors of the system.
     * 
     * @param processorCount
     *            value for processor count to be set
     */
    public void setProcessorCount(int processorCount);

    /**
     * Get the count of processors of the system.
     * 
     * @return count of processors
     */
    public int getProcessorCount();
}
