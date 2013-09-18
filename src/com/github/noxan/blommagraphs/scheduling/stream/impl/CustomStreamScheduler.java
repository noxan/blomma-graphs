package com.github.noxan.blommagraphs.scheduling.stream.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTask;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.stream.StreamScheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class CustomStreamScheduler implements StreamScheduler {
    @Override
    public ScheduledTaskList schedule(TaskGraph[] taskGraphs, SystemMetaInformation systemInfo) {
        /*
        Set<TaskGraphNode> readySet = initializeReadySet(taskGraphs);
        int[] startTime = new int[taskGraphs.length];
        ScheduledTaskList scheduledTaskList = new DefaultScheduledTaskList(
                systemInfo.getProcessorCount());

        int numberOfTasks = 0;
        for (TaskGraph taskGraph : taskGraphs) {
            numberOfTasks += taskGraph.getNodeCount();
        }

        for (int i = 0; i < numberOfTasks; i++) {
            TaskGraphNode nextTask = searchNextTask(readySet, scheduledTaskList, taskGraphs, startTime);

            ScheduledTask scheduledTask = scheduleNextTask(nextTask, scheduledTaskList);
            scheduledTaskList.add(scheduledTask);

            updateReadySet(readySet, scheduledTaskList, nextTask);
        }
        */
        ScheduledTaskList scheduledTaskList = getScheduledTaskList(initializeReadySet(taskGraphs), new DefaultScheduledTaskList(systemInfo.getProcessorCount()));

        return scheduledTaskList;
    }

    private ScheduledTaskList getScheduledTaskList(Set<TaskGraphNode> readySet, ScheduledTaskList scheduledTaskList ){
        ArrayList<PhantomTask> phantomTaskList = new ArrayList<PhantomTask>();

        for (TaskGraphNode currentTask : readySet) {

            Set<ScheduledTask> dependencySet = new HashSet<ScheduledTask>();
            for (TaskGraphNode dependencyNode : currentTask.getPrevNodes()) {
                dependencySet.add(scheduledTaskList.getScheduledTask(dependencyNode));
            }

            for (int cpuId = 0; cpuId < scheduledTaskList.getProcessorCount(); cpuId++) {
                ScheduledTask lastScheduledTask = scheduledTaskList
                        .getLastScheduledTaskOnProcessor(cpuId);

                int startTimeOnProcessor;
                int communicationTimeOnProcessor = 0;

                if (lastScheduledTask == null) {
                    startTimeOnProcessor = 0;
                } else {
                    startTimeOnProcessor = lastScheduledTask.getFinishTime();
                }

                int maxDependencyTime = Integer.MIN_VALUE;
                for (ScheduledTask dependencyTask : dependencySet) {
                    if (dependencyTask.getCpuId() != cpuId) {
                        int currentDependencyTime = dependencyTask.getFinishTime();
                        TaskGraphEdge edge = dependencyTask.getTaskGraphNode().findNextEdge(currentTask);
                        currentDependencyTime += edge.getCommunicationTime();
                        if (currentDependencyTime > maxDependencyTime) {
                            maxDependencyTime = currentDependencyTime;
                            communicationTimeOnProcessor = edge.getCommunicationTime();

                        }
                    }
                }

                if (startTimeOnProcessor < maxDependencyTime) {
                    startTimeOnProcessor = maxDependencyTime;
                }

                int gap;
                if (lastScheduledTask != null) {
                    gap = startTimeOnProcessor - (lastScheduledTask.getStartTime() + lastScheduledTask.getComputationTime());
                } else {
                    gap = 0; //correct?????
                }

                if (phantomTaskList.isEmpty()) {
                    phantomTaskList.add(new PhantomTask(cpuId, currentTask, gap, startTimeOnProcessor, communicationTimeOnProcessor));
                }
                else {
                    for (PhantomTask phantomTask : phantomTaskList) {
                        if (phantomTask.getGap() > gap) {
                            phantomTaskList.add(phantomTaskList.indexOf(phantomTask), new PhantomTask(cpuId, currentTask, gap, startTimeOnProcessor, communicationTimeOnProcessor));
                            break;
                        }
                    }
                }
            }
        }

        if(phantomTaskList.size() == scheduledTaskList.getProcessorCount() && phantomTaskList.get(0).getTaskGraphNode().getNextNodes().isEmpty()) {
            ArrayList<ScheduledTask> startTasks = new ArrayList<ScheduledTask>();
            ArrayList<ScheduledTask> endTasks = new ArrayList<ScheduledTask>();

            for(ScheduledTask scheduledTask : scheduledTaskList) {
                if(scheduledTask.getTaskGraphNode().getPrevNodes().isEmpty()) {
                    startTasks.add(scheduledTask);
                } else if (scheduledTask.getTaskGraphNode().getNextNodes().isEmpty()) {
                    endTasks.add(scheduledTask);
                }
            }

            for(ScheduledTask startTask : startTasks) {
                for(ScheduledTask endTask : endTasks) {
                    if(startTask.getTaskGraphNode().getTaskGraph() == endTask.getTaskGraphNode().getTaskGraph()) {
                        if((endTask.getFinishTime()-startTask.getStartTime()) > endTask.getTaskGraphNode().getDeadLine()) {
                            return scheduledTaskList;
                        }
                    }
                }

            }
            PhantomTask lastPhantomTask = phantomTaskList.get(0);
            scheduledTaskList.add(new DefaultScheduledTask(lastPhantomTask.getEarliestStarttime(), lastPhantomTask.getCpuId(), lastPhantomTask.getCommunicationTime(), lastPhantomTask.getTaskGraphNode()));
            return scheduledTaskList;
        }

        PhantomTask nextPhantomTask;
        ScheduledTaskList currentTaskList;
        int i = 0;
        do {
            nextPhantomTask = phantomTaskList.get(i);
            Set<TaskGraphNode> newReadySet = new HashSet<TaskGraphNode>();
            for(TaskGraphNode currentNode : readySet) {
                newReadySet.add(currentNode);
            }
            updateReadySet(newReadySet, scheduledTaskList, nextPhantomTask.getTaskGraphNode());

            ScheduledTaskList nextScheduledTaskList = new DefaultScheduledTaskList(scheduledTaskList.getProcessorCount());
            for(ScheduledTask scheduledTask : scheduledTaskList) {
                nextScheduledTaskList.add(scheduledTask);
            }

            nextScheduledTaskList.add(new DefaultScheduledTask(nextPhantomTask.getEarliestStarttime(), nextPhantomTask.getCpuId(), nextPhantomTask.getCommunicationTime(), nextPhantomTask.getTaskGraphNode()));
            currentTaskList = getScheduledTaskList(newReadySet, nextScheduledTaskList);
            i++;
        } while(currentTaskList == scheduledTaskList);
        return scheduledTaskList;

    }

    private Set<TaskGraphNode> initializeReadySet(TaskGraph[] taskGraphs) {
        Set<TaskGraphNode> readyList = new HashSet<TaskGraphNode>();

        for (TaskGraph taskGraph : taskGraphs) {
            readyList.add(taskGraph.getFirstNode());
        }

        return readyList;
    }

    private void updateReadySet(Set<TaskGraphNode> readySet, ScheduledTaskList scheduledTaskList,
            TaskGraphNode lastScheduledNode) {
        // remove last scheduled task from ready list
        readySet.remove(lastScheduledNode);
        // find new ready tasks based on the last scheduled one
        for (TaskGraphNode nextNode : lastScheduledNode.getNextNodes()) {
            boolean isReady = true;
            for (TaskGraphNode dependency : nextNode.getPrevNodes()) {
                if (!scheduledTaskList.containsTask(dependency)) {
                    isReady = false;
                    break;
                }
            }
            if (isReady) {
                readySet.add(nextNode);
            }
        }
    }
}