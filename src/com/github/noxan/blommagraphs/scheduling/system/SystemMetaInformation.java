package com.github.noxan.blommagraphs.scheduling.system;


/**
 * Interface for all methods to define the system meta information which are later used for the
 * scheduling algorithm.
 * 
 * @author noxan
 */
public interface SystemMetaInformation {
    /**
     * Sets the count of CPUs of the system.
     * 
     * @param cpuCount value for CPU count to be set
     */
    public void setCpuCount(int cpuCount);

    /**
     * Get the count of CPUs of the system.
     * 
     * @return count of CPUs
     */
    public int getCpuCount();
}
