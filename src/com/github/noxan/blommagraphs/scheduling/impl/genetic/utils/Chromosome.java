package com.github.noxan.blommagraphs.scheduling.impl.genetic.utils;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.ContainsNoEdgeException;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
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

        // select next task node
        Set<TaskGraphNode> readyList = createReadyTaskList(taskGraph, scheduledTaskList);

        Iterator<TaskGraphNode> it = readyList.iterator();
        while (it.hasNext()) {
            TaskGraphNode nextTaskNode = it.next();
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

        Iterator<TaskGraphNode> it = unscheduledNodes.iterator();
        while (it.hasNext()) {
            TaskGraphNode taskNode = it.next();

            // processorId for the task
            int processorId = getProcessorForTask(taskNode);

            // search for a previous scheduled task on this processor
            ScheduledTask lastScheduledTask = scheduledTaskList
                    .getLastScheduledTaskOnProcessor(processorId);

            try {
                TaskGraphEdge prevEdge;
                if (lastScheduledTask == null) {
                    prevEdge = taskNode.getPrevEdges().iterator().next();
                } else {
                    prevEdge = taskGraph.findEdge(lastScheduledTask.getTaskGraphNode(), taskNode);
                }

                int communicationTime = prevEdge.getCommunicationTime();
                // set communicationTime to zero if previous task is on same processor
                if (lastScheduledTask != null
                        && scheduledTaskList.isTaskOnProcessor(processorId,
                                lastScheduledTask.getTaskId())) {
                    communicationTime = 0;
                }

                int startTime = communicationTime;
                if (lastScheduledTask != null) {
                    startTime += lastScheduledTask.getFinishTime();
                }

                ScheduledTask scheduledTask = new DefaultScheduledTask(startTime, processorId,
                        communicationTime, taskNode);
                scheduledTaskList.add(scheduledTask);
            } catch (ContainsNoEdgeException e) {
                e.printStackTrace();
            }
        }

        return scheduledTaskList;
    }
}
