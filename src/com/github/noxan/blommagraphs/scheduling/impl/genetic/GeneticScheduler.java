package com.github.noxan.blommagraphs.scheduling.impl.genetic;


import java.util.ArrayList;
import java.util.Collections;
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

    public GeneticScheduler(List<ScheduledTask> initialPopulation) {
        this.initialPopulation = initialPopulation;
    }

    private void initialize() {
        if (initialPopulation == null) {
            initialPopulation = initialScheduler.schedule(metaInformation, taskGraph);
        }

        List<List<ScheduledTask>> scheduledTasksGroupedByCPU = new ArrayList<List<ScheduledTask>>();
        for (int i = 0; i < metaInformation.getProcessorCount(); i++) {
            scheduledTasksGroupedByCPU.add(new ArrayList<ScheduledTask>());
        }
        // group scheduled tasks by CPU
        for (ScheduledTask task : initialPopulation) {
            scheduledTasksGroupedByCPU.get(task.getCpuId()).add(task);
        }
        // sort lists by startTime
        for (List<ScheduledTask> scheduledTasks : scheduledTasksGroupedByCPU) {
            Collections.sort(scheduledTasks);
        }
    }

    @Override
    public List<ScheduledTask> schedule(SystemMetaInformation metaInformation, TaskGraph taskGraph) {
        this.metaInformation = metaInformation;
        this.taskGraph = taskGraph;

        initialize();

        // TODO Auto-generated method stub
        return null;
    }
}
