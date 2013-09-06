package com.github.noxan.blommagraphs.scheduling.impl.genetic.utils;


import java.util.ArrayList;
import java.util.List;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;


public class Chromosome {
    private List<List<Integer>> genes;

    public Chromosome(int numberOfCPUs) {
        genes = new ArrayList<List<Integer>>();
        for (int i = 0; i < numberOfCPUs; i++) {
            genes.add(new ArrayList<Integer>());
        }
    }

    public void addTaskToProcessor(int cpu, int taskId) {
        genes.get(cpu).add(taskId);
    }

    public int getProcessorForTask(int taskId) {
        for (int processorId = 0; processorId < genes.size(); processorId++) {
            if (genes.get(processorId).contains(taskId)) {
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
