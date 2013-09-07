package com.github.noxan.blommagraphs.scheduling.impl.genetic.utils;


import java.util.Collections;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;


public class ScheduledChromosome extends Chromosome {
    public ScheduledChromosome(int numberOfProcessors, TaskGraph taskGraph,
            ScheduledTaskList initialTaskSchedule) {
        super(numberOfProcessors, taskGraph);

        Collections.sort(initialTaskSchedule);
        for (ScheduledTask task : initialTaskSchedule) {
            addTaskToProcessor(task.getCpuId(), task.getTaskGraphNode());
        }
    }
}
