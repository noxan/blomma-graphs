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

    public ScheduledTaskList decode(TaskGraph taskGraph) {
        ScheduledTaskList scheduledTasks = new DefaultScheduledTaskList(numberOfProcessors);

        Set<TaskGraphNode> unscheduledNodes = taskGraph.getNodeSet();

        Iterator<TaskGraphNode> it = unscheduledNodes.iterator();
        while (it.hasNext()) {
            TaskGraphNode taskNode = it.next();

            int processorId = getProcessorForTask(taskNode);

            ScheduledTask lastScheduledTask = scheduledTasks
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
                if (scheduledTasks.isTaskOnProcessor(processorId, lastScheduledTask.getTaskId())) {
                    communicationTime = 0;
                }

                int startTime = lastScheduledTask.getFinishTime() + communicationTime;

                ScheduledTask scheduledTask = new DefaultScheduledTask(startTime, processorId,
                        communicationTime, taskNode);
                scheduledTasks.add(scheduledTask);
            } catch (ContainsNoEdgeException e) {
                e.printStackTrace();
            }
        }

        return scheduledTasks;
    }
}
