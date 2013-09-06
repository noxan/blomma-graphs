package com.github.noxan.blommagraphs.scheduling.impl.genetic.utils;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTask;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTaskList;


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
        ScheduledTaskList scheduledTasks = new DefaultScheduledTaskList();

        Set<TaskGraphNode> unscheduledNodes = taskGraph.getNodeSet();

        Iterator<TaskGraphNode> it = unscheduledNodes.iterator();
        while (it.hasNext()) {
            TaskGraphNode taskNode = it.next();
            ScheduledTask scheduledTask = new DefaultScheduledTask(0,
                    getProcessorForTask(taskNode), 0, taskNode);
            scheduledTasks.add(scheduledTask);
        }

        return scheduledTasks;
    }
}
