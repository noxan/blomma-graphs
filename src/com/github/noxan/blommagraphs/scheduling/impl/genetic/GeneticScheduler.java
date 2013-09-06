package com.github.noxan.blommagraphs.scheduling.impl.genetic;


import java.util.List;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.Scheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class GeneticScheduler implements Scheduler {
    private SystemMetaInformation metaInformation;
    private TaskGraph taskGraph;

    private Scheduler initialScheduler;
    private List<ScheduledTask> initialPopulation;

    public GeneticScheduler(Scheduler initialScheduler) {
        this.initialScheduler = initialScheduler;
    }

    @Override
    public List<ScheduledTask> schedule(SystemMetaInformation metaInformation, TaskGraph taskGraph) {
        this.metaInformation = metaInformation;
        this.taskGraph = taskGraph;

        // TODO Auto-generated method stub
        return null;
    }
}
