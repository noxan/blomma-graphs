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

/**
 * custom stream scheduler
 * schedules taskgraphs trying to minimize the gaps between the tasks and depending to their
 * deadline to get the best throughput
 * to short deadline -> empty scheduledtasklist
 */
public class CustomStreamScheduler implements StreamScheduler {
    @Override
    public ScheduledTaskList schedule(TaskGraph[] taskGraphs, SystemMetaInformation systemInfo) {
        ScheduledTaskList scheduledTaskList = getScheduledTaskList(initializeReadySet(taskGraphs),
                new DefaultScheduledTaskList(systemInfo.getCpuCount()));

        return scheduledTaskList;
    }

    /**
     * recursive methode to schedule taskgraph which first nodes are in the ready set
     * @param readySet ready set with the first node at the beginning
     * @param scheduledTaskList empty scheduledtasklist at the beginning
     * @return scheduled task list
     */
    private ScheduledTaskList getScheduledTaskList(Set<TaskGraphNode> readySet,
            ScheduledTaskList scheduledTaskList) {

        // get the phantomTaskList for the current readySet
        ArrayList<PhantomTask> phantomTaskList = getPhantomTaskList(readySet, scheduledTaskList);

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

    /**
     * checks the deadline if all tasks have been scheduled
     * @param scheduledTaskList scheduledtasklist with deadlines to check
     * @return boolean true if all deadlines are met
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

    /**
     * creates a phantomTaskList which includes the gaps of every Task on every cpu, taskGraphNode,
     * earliestStarttime, communicationTime
     * @param readySet current ready set
     * @param scheduledTaskList current scheduledtasklist
     * @return ArrayList<PhantomTask>
     */
    private ArrayList<PhantomTask> getPhantomTaskList(Set<TaskGraphNode> readySet,
                                                      ScheduledTaskList scheduledTaskList) {
        ArrayList<PhantomTask> phantomTaskList = new ArrayList<PhantomTask>();
        for (TaskGraphNode currentTask : readySet) {

            // get set of task which the currenttask depend on
            Set<ScheduledTask> dependencySet = getDependencySet(scheduledTaskList, currentTask);

            for (int cpuId = 0; cpuId < scheduledTaskList.getCpuCount(); cpuId++) {

                // get the phantomtask with the gap
                PhantomTask newPhantomTask = createPhantomTask(scheduledTaskList,
                        cpuId, dependencySet, currentTask);

                if (phantomTaskList.isEmpty()) {
                    phantomTaskList.add(newPhantomTask);
                }
                else {
                    for(PhantomTask phantomTask : phantomTaskList) {
                        // optimization if the one with the bigger communication time would be put
                        // on the cpu with the prev depending task to save the communication time
                        if (newPhantomTask.getGap() < phantomTask.getGap()) {
                            phantomTaskList.add(phantomTaskList.indexOf(phantomTask),
                                    newPhantomTask);
                            break;
                        } else if (newPhantomTask.getGap() == phantomTask.getGap()
                                && currentTask.getDeadLine() <
                                phantomTask.getTaskGraphNode().getDeadLine()) {
                            phantomTaskList.add(phantomTaskList.indexOf(phantomTask),
                                    newPhantomTask);
                            break;
                        } else if (newPhantomTask.getGap() == phantomTask.getGap()
                                && currentTask.getDeadLine() ==
                                phantomTask.getTaskGraphNode().getDeadLine()
                                && newPhantomTask.getEarliestStarttime() <
                                phantomTask.getEarliestStarttime()) {
                            // adding pure deadline priority just makes sense if all graphs
                            // are the equals, if not calculate a relation to the number of
                            // nodes of a graph
                            phantomTaskList.add(phantomTaskList.indexOf(phantomTask),
                                    newPhantomTask);
                            break;
                        } else {
                            phantomTaskList.add(newPhantomTask);
                            break;
                        }
                    }
                }
            }
        }
        return phantomTaskList;
    }

    /**
     * create the phantomtask with the gap
     * @param scheduledTaskList  current scheduledtasklist
     * @param cpuId cpuId for the new phantomtask
     * @param dependencySet set of dependent scheduled tasks
     * @param currentTask task to be in the new phantomtask
     * @return new phantomtask
     */
    private PhantomTask createPhantomTask(ScheduledTaskList scheduledTaskList, int cpuId, Set<ScheduledTask> dependencySet, TaskGraphNode currentTask) {
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
            gap = startTimeOnCpu;
        }

        return new PhantomTask(cpuId, currentTask, gap, startTimeOnCpu,
                communicationTimeOnCpu);
    }

    /**
     * creats a set of scheduledTasks which the task depends on
     * @param scheduledTaskList current scheduledtasklist
     * @param task task to get dependencies of
     * @return dependencyset
     */
    private Set<ScheduledTask> getDependencySet(ScheduledTaskList scheduledTaskList, TaskGraphNode task) {
        Set<ScheduledTask> dependencySet = new HashSet<ScheduledTask>();
        for (TaskGraphNode dependencyNode : task.getPrevNodes()) {
            dependencySet.add(scheduledTaskList.getScheduledTask(dependencyNode));
        }
        return dependencySet;
    }

    /**
     * adds the first node of each taskgraph to the readypool
     * @param taskGraphs taskgraphs to be added
     * @return readyset
     */
    private Set<TaskGraphNode> initializeReadySet(TaskGraph[] taskGraphs) {
        Set<TaskGraphNode> readyList = new HashSet<TaskGraphNode>();

        for (TaskGraph taskGraph : taskGraphs) {
            readyList.add(taskGraph.getFirstNode());
        }

        return readyList;
    }

    /**
     * removes the scheduled taskgraph from the readypool and adds the new nodes which dependencies
     * has been fulfilled
     * @param readySet current readyset
     * @param scheduledTaskList current scheduledtasklist
     * @param lastScheduledNode node to remove
     */
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
    
    public String getName() {
        return "CustomStreamScheduler";
    }
}