package com.github.noxan.blommagraphs.scheduling.basic;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public interface Scheduler {

    /**
     * Schedules a TaskGraph.
     * 
     * @param graph TaskGraph that is scheduled
     * @return List which holds all scheduled tasks ordered by starting time.
     */
    public ScheduledTaskList schedule(TaskGraph taskGraph, SystemMetaInformation metaInformation);

    public String getName();
}
