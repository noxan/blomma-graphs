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
                    gap = startTimeOnCpu; // correct????? could be maybe -1
                }

                PhantomTask newPhantomTask = new PhantomTask(cpuId, currentTask, gap, startTimeOnCpu,
                        communicationTimeOnCpu);
                
                if (phantomTaskList.isEmpty()) {
                    phantomTaskList.add(newPhantomTask);
                }
                else {
                    for(PhantomTask phantomTask : phantomTaskList) {
                        // optimization if the one with the bigger communication time would be put
                        // on the cpu with the prev task or something in this case
                        if (gap < phantomTask.getGap()) {
                            phantomTaskList.add(phantomTaskList.indexOf(phantomTask),
                                    newPhantomTask);
                            break;
                        } else if (gap == phantomTask.getGap()
                                && currentTask.getDeadLine() <
                                phantomTask.getTaskGraphNode().getDeadLine()) {
                            phantomTaskList.add(phantomTaskList.indexOf(phantomTask),
                                    newPhantomTask);
                            break;
                        } else if (gap == phantomTask.getGap()
                                && currentTask.getDeadLine() ==
                                phantomTask.getTaskGraphNode().getDeadLine()
                                && startTimeOnCpu < phantomTask.getEarliestStarttime()) {
                                // adding pure deadline priority just makes sense if all graphes
                                // are the same, if not calculate a relation to the number of
                                // nodes of a graph
                            phantomTaskList.add(phantomTaskList.indexOf(phantomTask),
                                    newPhantomTask);
                            break;
                        } else {
                            //System.out.print("ELSE " + startTimeOnCpu + "|" + phantomTask.getEarliestStarttime() + " DL:" + currentTask.getDeadLine() + "|" + phantomTask.getTaskGraphNode().getDeadLine() + "||");
                            phantomTaskList.add(newPhantomTask);
                            break;
                        }
                    }
                }
            }
        }

        // check ifs last task of last graph and check deadlines
        if(phantomTaskList.size() == scheduledTaskList.getCpuCount()
                && phantomTaskList.get(0).getTaskGraphNode().getNextNodes().isEmpty()) {

            PhantomTask lastPhantomTask = phantomTaskList.get(0);
            ScheduledTask lastTask = new DefaultScheduledTask(
                    lastPhantomTask.getEarliestStarttime(), lastPhantomTask.getCpuId(),
                    lastPhantomTask.getCommunicationTime(), lastPhantomTask.getTaskGraphNode());
            scheduledTaskList.add(lastTask);

            if (checkDeadline(scheduledTaskList)) {
                return scheduledTaskList;
            } else {
                scheduledTaskList.remove(lastTask);
                return scheduledTaskList;
            }
        }

        PhantomTask nextPhantomTask;
        ScheduledTaskList currentTaskList;
        ScheduledTaskList nextScheduledTaskList;
        int i = 0;
        int nextScheduledTaskListSize;
        do {
            if(i >= phantomTaskList.size()) {
                if (scheduledTaskList.size() <= 4)
                    System.out.println("Back from " + scheduledTaskList.size());
                return scheduledTaskList;
            }
            nextPhantomTask = phantomTaskList.get(i);
            Set<TaskGraphNode> newReadySet = new HashSet<TaskGraphNode>();
            for (TaskGraphNode currentNode : readySet) {
                newReadySet.add(currentNode);
            }
            nextScheduledTaskList = new DefaultScheduledTaskList(scheduledTaskList.getCpuCount());
            for(ScheduledTask scheduledTask : scheduledTaskList) {
                nextScheduledTaskList.add(scheduledTask);
            }

            nextScheduledTaskList.add(new DefaultScheduledTask(nextPhantomTask
                    .getEarliestStarttime(), nextPhantomTask.getCpuId(), nextPhantomTask
                    .getCommunicationTime(), nextPhantomTask.getTaskGraphNode()));
            updateReadySet(newReadySet, nextScheduledTaskList, nextPhantomTask.getTaskGraphNode());
            nextScheduledTaskListSize = nextScheduledTaskList.size();
            currentTaskList = getScheduledTaskList(newReadySet, nextScheduledTaskList);
            i++;
        } while(currentTaskList.size() == nextScheduledTaskListSize);
        return currentTaskList;
    }

    /*
        Muss noch gecheckt werden:
        zu kurze deadlines
        kritische deadline: alle taskgraphen haben min deadline (root nicht ganz oben)
        zu lange deadlines: throughput is schrott da nur auf einem cpus
        geht noch nicht kommentar in der falschen sprache und an der falschen stelle
     */

    /**
     * checks the deadline if all tasks have been scheduled
     * @param scheduledTaskList
     * @return
     */
    private boolean checkDeadline(ScheduledTaskList scheduledTaskList) {
        ArrayList<ScheduledTask> startTasks = new ArrayList<ScheduledTask>();
        ArrayList<ScheduledTask> endTasks = new ArrayList<ScheduledTask>();

        for (ScheduledTask scheduledTask : scheduledTaskList) {
            if (scheduledTask.getTaskGraphNode().getPrevNodes().isEmpty()) {
                startTasks.add(scheduledTask);
            } else if (scheduledTask.getTaskGraphNode().getNextNodes().isEmpty()) {
                endTasks.add(scheduledTask);
            }
        }

        for(ScheduledTask startTask : startTasks) {
            for(ScheduledTask endTask : endTasks) {
                if(startTask.getTaskGraphNode().getTaskGraph()
                        == endTask.getTaskGraphNode().getTaskGraph()) {
                    if((endTask.getFinishTime()-startTask.getStartTime())
                            > endTask.getTaskGraphNode().getDeadLine()) {
                        return false;
                    }
                }
            }
        }
        return true;
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