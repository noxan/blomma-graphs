package com.github.noxan.blommagraphs.scheduling.impl.genetic.utils;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTask;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTaskList;


public class Chromosome {
    private List<List<TaskGraphNode>> genes;

    private int numberOfProcessors;

    public Chromosome(int numberOfProcessors) {
        this.numberOfProcessors = numberOfProcessors;
        genes = new ArrayList<List<TaskGraphNode>>();
        for (int i = 0; i < numberOfProcessors; i++) {
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

    private Set<TaskGraphNode> createReadyTaskList(TaskGraph taskGraph,
            ScheduledTaskList scheduledTaskList) {
        Set<TaskGraphNode> readyList = new HashSet<TaskGraphNode>();

        for (TaskGraphNode taskNode : taskGraph.getNodeSet()) {
            // just unscheduled tasks
            if (!scheduledTaskList.containsTask(taskNode.getId())) {
                boolean taskReady = true;
                // check all dependencies
                for (TaskGraphNode prevTaskNode : taskNode.getPrevNodes()) {
                    // break if previous task has not been scheduled
                    if (!scheduledTaskList.containsTask(prevTaskNode.getId())) {
                        taskReady = false;
                        break;
                    }
                }
                if (taskReady) {
                    readyList.add(taskNode);
                }
            }
        }

        return readyList;
    }

    private void decode(TaskGraphNode taskNode, TaskGraph taskGraph,
            ScheduledTaskList scheduledTaskList, Set<TaskGraphNode> unscheduledNodes) {
        // schedule current task node
        unscheduledNodes.remove(taskNode);

        System.out.println(taskNode.getId());

        int startTime = 0;
        int processorId = 0;
        int communicationTime = 0;

        scheduledTaskList.add(new DefaultScheduledTask(startTime, processorId, communicationTime,
                taskNode));

        // select next task node
        Set<TaskGraphNode> readyList = createReadyTaskList(taskGraph, scheduledTaskList);

        System.out.println("readyNodes: ");
        System.out.println(readyList);

        Iterator<TaskGraphNode> it = readyList.iterator();
        while (it.hasNext()) {
            TaskGraphNode nextTaskNode = it.next();
            System.out.println("nextNode: " + nextTaskNode.getId());
            if (unscheduledNodes.contains(nextTaskNode)) {
                decode(nextTaskNode, taskGraph, scheduledTaskList, unscheduledNodes);
            }
        }
    }

    public ScheduledTaskList decode(TaskGraph taskGraph) {
        ScheduledTaskList scheduledTaskList = new DefaultScheduledTaskList(numberOfProcessors);

        Set<TaskGraphNode> unscheduledNodes = taskGraph.getNodeSet();

        decode(taskGraph.getFirstNode(), taskGraph, scheduledTaskList, unscheduledNodes);

        // TODO: maybe add a readyNode list to select from?!

        return scheduledTaskList;
    }
}
