package com.github.noxan.blommagraphs.scheduling.impl.genetic;


import java.util.List;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.Scheduler;


public class GeneticScheduler implements Scheduler {
    private Scheduler initialScheduler;
    private List<ScheduledTask> initialPopulation;

    public GeneticScheduler(Scheduler initialScheduler) {
        this.initialScheduler = initialScheduler;
    }

    @Override
    public List<ScheduledTask> schedule(TaskGraph graph) {
        initialPopulation = initialScheduler.schedule(graph);

        // TODO Auto-generated method stub
        return null;
    }
}
