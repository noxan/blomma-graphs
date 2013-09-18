package com.github.noxan.blommagraphs.scheduling.basic.impl.genetic.chromosome;


import java.util.Collections;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;


public class ScheduledChromosome extends AbstractChromosome {
    public ScheduledChromosome(int cpuCount, TaskGraph taskGraph,
            ScheduledTaskList initialTaskSchedule) {
        super(cpuCount, taskGraph);

        Collections.sort(initialTaskSchedule);
        for (ScheduledTask task : initialTaskSchedule) {
            addTaskToCpu(task.getCpuId(), task.getTaskGraphNode());
        }
    }

    @Override
    public String toString() {
        return "Scheduled" + super.toString();
    }
}
