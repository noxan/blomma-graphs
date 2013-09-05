package com.github.noxan.blommagraphs.scheduling;


import java.util.List;

import com.github.noxan.blommagraphs.graphs.TaskGraph;


public interface Scheduler {

    /**
     * Schedules a TaskGraph.
     * 
     * @param graph TaskGraph that is scheduled
     * @return List which holds all scheduled tasks ordered by starting time.
     */
    public List<ScheduledTask> schedule(TaskGraph graph);
}