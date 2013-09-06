package com.github.noxan.blommagraphs.scheduling.impl.genetic.utils;


import java.util.ArrayList;
import java.util.List;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;


public class Chromosome {
    private List<List<TaskGraphNode>> genes;

    public Chromosome(int numberOfCPUs) {
        genes = new ArrayList<List<TaskGraphNode>>();
        for (int i = 0; i < numberOfCPUs; i++) {
            genes.add(new ArrayList<TaskGraphNode>());
        }
    }

    public void addTaskToProcessor(int cpu, TaskGraphNode task) {
        genes.get(cpu).add(task);
    }

    public int getProcessorForTask(TaskGraphNode task) {
        for (int processorId = 0; processorId < genes.size(); processorId++) {
            if (genes.get(processorId).contains(task)) {
                return processorId;
            }
        }
        return -1; // TODO: should not happen but not nice
    }

    public List<ScheduledTask> decode(TaskGraph taskGraph) {
        List<ScheduledTask> scheduledTasks = new ArrayList<ScheduledTask>();

        return scheduledTasks;
    }
}
