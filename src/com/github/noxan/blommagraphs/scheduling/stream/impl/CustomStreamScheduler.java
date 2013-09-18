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
        ScheduledTaskList scheduledTaskList = getScheduledTaskList(initializeReadySet(taskGraphs),
                new DefaultScheduledTaskList(systemInfo.getCpuCount()));

        return scheduledTaskList;
    }

    private ScheduledTaskList getScheduledTaskList(Set<TaskGraphNode> readySet,
            ScheduledTaskList scheduledTaskList) {
        ArrayList<PhantomTask> phantomTaskList = new ArrayList<PhantomTask>();

        for (TaskGraphNode currentTask : readySet) {

            Set<ScheduledTask> dependencySet = new HashSet<ScheduledTask>();
            for (TaskGraphNode dependencyNode : currentTask.getPrevNodes()) {
                dependencySet.add(scheduledTaskList.getScheduledTask(dependencyNode));
            }

            for (int cpuId = 0; cpuId < scheduledTaskList.getCpuCount(); cpuId++) {
                ScheduledTask lastScheduledTask = scheduledTaskList
                        .getLastScheduledTaskOnCpu(cpuId);

                int startTimeOnCpu;
                int communicationTimeOnCpu = 0;

                if (lastScheduledTask == null) {
                    startTimeOnCpu = 0;
                } else {
                    startTimeOnCpu = lastScheduledTask.getFinishTime();
                }

                int maxDependencyTime = Integer.MIN_VALUE;
                for (ScheduledTask dependencyTask : dependencySet) {
                    if (dependencyTask.getCpuId() != cpuId) {
                        int currentDependencyTime = dependencyTask.getFinishTime();
                        TaskGraphEdge edge = dependencyTask.getTaskGraphNode().findNextEdge(
                                currentTask);
                        currentDependencyTime += edge.getCommunicationTime();
                        if (currentDependencyTime > maxDependencyTime) {
                            maxDependencyTime = currentDependencyTime;
                            communicationTimeOnCpu = edge.getCommunicationTime();

                        }
                    }
                }

                if (startTimeOnCpu < maxDependencyTime) {
                    startTimeOnCpu = maxDependencyTime;
                }

                int gap;
                if (lastScheduledTask != null) {
                    gap = startTimeOnCpu
                            - (lastScheduledTask.getStartTime() + lastScheduledTask
                                    .getComputationTime());
                } else {
                    gap = 0; // correct?????
                }

                if (phantomTaskList.isEmpty()) {
                    phantomTaskList.add(new PhantomTask(cpuId, currentTask, gap, startTimeOnCpu,
                            communicationTimeOnCpu));
                } else {
                    for (PhantomTask phantomTask : phantomTaskList) {
                        if (phantomTask.getGap() > gap) {
                            phantomTaskList.add(phantomTaskList.indexOf(phantomTask),
                                    new PhantomTask(cpuId, currentTask, gap, startTimeOnCpu,
                                            communicationTimeOnCpu));
                            break;
                        } else {
                            phantomTaskList.add(new PhantomTask(cpuId, currentTask, gap,
                                    startTimeOnCpu, communicationTimeOnCpu));
                            break;
                        }
                    }
                }
            }
        }
        if (phantomTaskList.size() == scheduledTaskList.getCpuCount()
                && phantomTaskList.get(0).getTaskGraphNode().getNextNodes().isEmpty()) {
            ArrayList<ScheduledTask> startTasks = new ArrayList<ScheduledTask>();
            ArrayList<ScheduledTask> endTasks = new ArrayList<ScheduledTask>();

            for (ScheduledTask scheduledTask : scheduledTaskList) {
                if (scheduledTask.getTaskGraphNode().getPrevNodes().isEmpty()) {
                    startTasks.add(scheduledTask);
                } else if (scheduledTask.getTaskGraphNode().getNextNodes().isEmpty()) {
                    endTasks.add(scheduledTask);
                }
            }

            for (ScheduledTask startTask : startTasks) {
                for (ScheduledTask endTask : endTasks) {
                    if (startTask.getTaskGraphNode().getTaskGraph() == endTask.getTaskGraphNode()
                            .getTaskGraph()) {
                        if ((endTask.getFinishTime() - startTask.getStartTime()) > endTask
                                .getTaskGraphNode().getDeadLine()) {
                            return scheduledTaskList;
                        }
                    }
                }

            }
            PhantomTask lastPhantomTask = phantomTaskList.get(0);
            scheduledTaskList.add(new DefaultScheduledTask(lastPhantomTask.getEarliestStarttime(),
                    lastPhantomTask.getCpuId(), lastPhantomTask.getCommunicationTime(),
                    lastPhantomTask.getTaskGraphNode()));
            return scheduledTaskList;
        }

        PhantomTask nextPhantomTask;
        ScheduledTaskList currentTaskList;
        int i = 0;
        do {
            nextPhantomTask = phantomTaskList.get(i);
            Set<TaskGraphNode> newReadySet = new HashSet<TaskGraphNode>();
            for (TaskGraphNode currentNode : readySet) {
                newReadySet.add(currentNode);
            }

            ScheduledTaskList nextScheduledTaskList = new DefaultScheduledTaskList(
                    scheduledTaskList.getCpuCount());
            for (ScheduledTask scheduledTask : scheduledTaskList) {
                nextScheduledTaskList.add(scheduledTask);
            }

            nextScheduledTaskList.add(new DefaultScheduledTask(nextPhantomTask
                    .getEarliestStarttime(), nextPhantomTask.getCpuId(), nextPhantomTask
                    .getCommunicationTime(), nextPhantomTask.getTaskGraphNode()));
            updateReadySet(newReadySet, nextScheduledTaskList, nextPhantomTask.getTaskGraphNode());
            currentTaskList = getScheduledTaskList(newReadySet, nextScheduledTaskList);
            i++;
        } while (currentTaskList == scheduledTaskList);
        return currentTaskList;

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