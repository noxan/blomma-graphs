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
        Set<TaskGraphNode> readySet = initializeReadySet(taskGraphs);
        ScheduledTaskList scheduledTaskList = new DefaultScheduledTaskList(
                systemInfo.getProcessorCount());

        int numberOfTasks = 0;
        for (TaskGraph taskGraph : taskGraphs) {
            numberOfTasks += taskGraph.getNodeCount();
        }

        for (int i = 0; i < numberOfTasks; i++) {
            TaskGraphNode nextTask = searchNextTask(readySet, scheduledTaskList);

            ScheduledTask scheduledTask = scheduleNextTask(nextTask, scheduledTaskList);
            scheduledTaskList.add(scheduledTask);

            updateReadySet(readySet, scheduledTaskList, nextTask);
        }

        return scheduledTaskList;
    }

    private ScheduledTask scheduleNextTask(TaskGraphNode nextTask,
            ScheduledTaskList scheduledTaskList) {
        int processorCount = scheduledTaskList.getProcessorCount();

        Set<ScheduledTask> dependencySet = new HashSet<ScheduledTask>();
        for (TaskGraphNode dependencyNode : nextTask.getPrevNodes()) {
            dependencySet.add(scheduledTaskList.getScheduledTask(dependencyNode));
        }

        int processorId = 0;
        int minStartTimeOnProcessor = Integer.MAX_VALUE;
        int communicationTime = 0;
        for (int currentProcessorId = 0; currentProcessorId < processorCount; currentProcessorId++) {
            ScheduledTask lastScheduledTask = scheduledTaskList
                    .getLastScheduledTaskOnProcessor(currentProcessorId);

            int startTimeOnProcessor;
            int communicationTimeOnProcessor = 0;

            if (lastScheduledTask == null) {
                startTimeOnProcessor = 0;
            } else {
                startTimeOnProcessor = lastScheduledTask.getFinishTime();
            }

            int maxDependencyTime = Integer.MIN_VALUE;
            for (ScheduledTask dependencyTask : dependencySet) {
                if (dependencyTask.getCpuId() != currentProcessorId) {
                    int currentDependencyTime = dependencyTask.getFinishTime();
                    TaskGraphEdge edge = dependencyTask.getTaskGraphNode().findNextEdge(nextTask);
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
            if (startTimeOnProcessor <= minStartTimeOnProcessor) {
                minStartTimeOnProcessor = startTimeOnProcessor;
                communicationTime = communicationTimeOnProcessor;
                processorId = currentProcessorId;
            }
        }

        return new DefaultScheduledTask(minStartTimeOnProcessor, processorId, communicationTime,
                nextTask);
    }

    private TaskGraphNode searchNextTask(Set<TaskGraphNode> readySet, ScheduledTaskList scheduledTaskList) {
        int nextDeadline = Integer.MAX_VALUE;
        TaskGraphNode nextTask = null;
        ArrayList<PhantomTask> phantomTasksList = new ArrayList<PhantomTask>();

        for (TaskGraphNode currentTask : readySet) {

            Set<ScheduledTask> dependencySet = new HashSet<ScheduledTask>();
            for (TaskGraphNode dependencyNode : currentTask.getPrevNodes()) {
                dependencySet.add(scheduledTaskList.getScheduledTask(dependencyNode));
            }

            for (int cpuId = 0; cpuId < scheduledTaskList.getProcessorCount(); cpuId++) {
                ScheduledTask lastScheduledTask = scheduledTaskList
                        .getLastScheduledTaskOnProcessor(cpuId);

                int startTimeOnProcessor;

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

                if (phantomTasksList.isEmpty()) {
                    phantomTasksList.add(new PhantomTask(cpuId, currentTask, gap));
                }
                else {
                    for (PhantomTask phantomTask : phantomTasksList) {
                        if (phantomTask.getGap() > gap) {
                            phantomTasksList.add(phantomTasksList.indexOf(phantomTask), new PhantomTask(cpuId, currentTask, gap));
                            break;
                        }
                    }
                }
            }
        }

        for (TaskGraphNode currentTask : readySet) {
            if (nextDeadline >= currentTask.getDeadLine()) {
                nextDeadline = currentTask.getDeadLine();
                nextTask = currentTask;
            }
        }

        return nextTask;
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