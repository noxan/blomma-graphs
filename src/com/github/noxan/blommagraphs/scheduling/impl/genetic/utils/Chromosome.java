package com.github.noxan.blommagraphs.scheduling.impl.genetic.utils;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTask;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTaskList;


public class Chromosome implements Comparable<Chromosome> {
    private List<List<TaskGraphNode>> genes;

    private int numberOfProcessors;
    private TaskGraph taskGraph;

    public Chromosome(int numberOfProcessors, TaskGraph taskGraph) {
        this.numberOfProcessors = numberOfProcessors;
        this.taskGraph = taskGraph;
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

        if (unscheduledNodes.remove(taskNode)) {
            // schedule current task node
            System.out.print(taskNode.getId() + " - ");

            int processorId = getProcessorForTask(taskNode);
            int communicationTime = 0;
            int startTime = 0;

            // iterate over all previous nodes (dependencies) and determine maximum of the sums of
            // finishTime and communicationTime
            Set<TaskGraphEdge> prevEdges = taskNode.getPrevEdges();
            Iterator<TaskGraphEdge> it = prevEdges.iterator();
            // set startTime to MAX_INT to use the value of the first iteration for sure
            if (it.hasNext()) {
                startTime = Integer.MAX_VALUE;
            }
            while (it.hasNext()) {
                int tempCommunicationTime = 0;
                int tempFinishTime = 0;
                TaskGraphEdge prevEdge = it.next();
                TaskGraphNode prevNode = prevEdge.getPrevNode();
                ScheduledTask prevScheduledTask = scheduledTaskList.getTaskById(prevNode.getId());
                // previous task is another processor, else it stays zero
                if (prevScheduledTask.getCpuId() != processorId) {
                    tempCommunicationTime = prevEdge.getCommunicationTime();
                }
                tempFinishTime = prevScheduledTask.getFinishTime() + tempCommunicationTime;
                // use minimum startTime for the next task
                if (tempFinishTime < startTime) {
                    startTime = tempFinishTime;
                    communicationTime = tempCommunicationTime;
                }
            }

            System.out.print("startTime: " + startTime + " - ");

            System.out.print("communicationTime: " + communicationTime + " - ");

            scheduledTaskList.add(new DefaultScheduledTask(startTime, processorId,
                    communicationTime, taskNode));

            System.out.println("processorId: " + processorId);
        }

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

    public ScheduledTaskList decode() {
        ScheduledTaskList scheduledTaskList = new DefaultScheduledTaskList(numberOfProcessors);

        System.out.println("[ Start decoding... ]");

        Set<TaskGraphNode> unscheduledNodes = taskGraph.getNodeSet();

        decode(taskGraph.getFirstNode(), taskGraph, scheduledTaskList, unscheduledNodes);

        // TODO: maybe add a readyNode list to select from?!

        return scheduledTaskList;
    }

    @Override
    public int compareTo(Chromosome other) {
        return decode().getFinishTime() - other.decode().getFinishTime();
    }
}
