package com.github.noxan.blommagraphs.scheduling.basic.impl.dls;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.ContainsNoEdgeException;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTask;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class DynamicLevelScheduler implements Scheduler {
    private ScheduledTaskList scheduledTaskList;

    @Override
    public ScheduledTaskList schedule(TaskGraph graph, SystemMetaInformation systemInformation) {
        int cpuCount = systemInformation.getProcessorCount();
        scheduledTaskList = new DefaultScheduledTaskList(cpuCount);
        List<ReadyPoolNode> readyNodePool = new ArrayList<ReadyPoolNode>();
        ArrayList<ArrayList<ScheduledTask>> allCpuScheduleTasks = new ArrayList<ArrayList<ScheduledTask>>();
        // add array lists for each cpu
        for (int i = 0; i < cpuCount; i++) {
            allCpuScheduleTasks.add(new ArrayList<ScheduledTask>());
        }

        // initialize ready-pool at first only start node
        readyNodePool.add(new ReadyPoolNode(graph.getFirstNode(),
                graph.getFirstNode().getStaticBLevel(), cpuCount));

        // main loop
        while (!readyNodePool.isEmpty()) {
            // write first start time for every ready pool node for every cpu
            for (ReadyPoolNode poolNode : readyNodePool) {
                // check first start time for every cpu
                for (int i = 0; i < allCpuScheduleTasks.size(); i++) {
                    ArrayList<ScheduledTask> cpuScheduleTaskList = allCpuScheduleTasks.get(i);
                    int firstStarttime;
                    // get last task and set first start time for current cpu
                    if (!cpuScheduleTaskList.isEmpty()) {
                        ScheduledTask lastTask = cpuScheduleTaskList.get(cpuScheduleTaskList.size() - 1);
                        firstStarttime = lastTask.getStartTime() + lastTask.getComputationTime();
                    } else {
                        firstStarttime = 0;
                    }
                    // check with the dependencies to other cpus
                    int latestDependencyTime = 0;
                    for (ArrayList<ScheduledTask> otherCpuSchedulerTaskList : allCpuScheduleTasks) {
                        // just if its not the actual cpu
                        if (otherCpuSchedulerTaskList != cpuScheduleTaskList) {
                            int latestDependencyTimePerCpu = 0;
                            // check all dependent tasks
                            for (ScheduledTask task : otherCpuSchedulerTaskList) {
                                int currentDependencyTimePerTask = 0;
                                if (graph.containsEdge(task.getTaskGraphNode(), poolNode.getNode())) {
                                    try {
                                        // get latest dependency time per task
                                        currentDependencyTimePerTask = task.getStartTime() + task.getComputationTime() + graph.findEdge(task.getTaskGraphNode(), poolNode.getNode()).getCommunicationTime();
                                    } catch (ContainsNoEdgeException e) {
                                        e.printStackTrace();
                                    }
                                }
                                // get the latest dependency time per cpu
                                if (currentDependencyTimePerTask > latestDependencyTimePerCpu) {
                                    latestDependencyTimePerCpu = currentDependencyTimePerTask;
                                }
                            }
                            // get the latest dependency time in total
                            if (latestDependencyTimePerCpu > latestDependencyTime) {
                                latestDependencyTime = latestDependencyTimePerCpu;
                            }
                        }
                    }
                    // compare first start time with latest dependency time
                    if (latestDependencyTime > firstStarttime) {
                        firstStarttime = latestDependencyTime;
                    }
                    // set first start time per cpu in pool node
                    poolNode.setEarliestStarttime(i, firstStarttime);
                }
            }

            // choose the node-processor pair with the biggest dynamic level and
            ReadyPoolNode chosenScheduledTask = null;
            int maxDynamicLevel = Integer.MIN_VALUE;
            int nextCpu = 0;
            for (ReadyPoolNode poolNode : readyNodePool) {
                if (poolNode.getMaxDynamicLevel().getFirst() >= maxDynamicLevel) {
                    maxDynamicLevel = poolNode.getMaxDynamicLevel().getFirst();
                    nextCpu = poolNode.getMaxDynamicLevel().getSecond();
                    chosenScheduledTask = poolNode;
                }
            }

            // commit it to the processor
            // setup new ScheduledTask
            ScheduledTask newScheduledTask = new DefaultScheduledTask();
            newScheduledTask.setCpuId(nextCpu);
            newScheduledTask.setStartTime(chosenScheduledTask.getEarliestStarttime(nextCpu));
            newScheduledTask.setTaskGraphNode(chosenScheduledTask.getNode());
            // add new ScheduleTask to CPU scheduleTask list
            allCpuScheduleTasks.get(nextCpu).add(newScheduledTask);
            // remove readyPoolNode from pool
            readyNodePool.remove(chosenScheduledTask);
            // add scheduled task to scheduledTaskList
            scheduledTaskList.add(newScheduledTask);

            // add new ready nodes to the ready node pool and compute static b level of the new nodes
            Set<TaskGraphNode> nextNodes = chosenScheduledTask.getNode().getNextNodes();
            if (nextNodes.size() != 0) {
                for (TaskGraphNode node : nextNodes) {
                    if (isReadyNode(node)) {
                        readyNodePool.add(new ReadyPoolNode(node, node.getStaticBLevel(), cpuCount));
                    }
                }
            }
        }
        return scheduledTaskList;
    }

    protected boolean isReadyNode(TaskGraphNode taskGraphNode) {
        Set<TaskGraphNode> prevNodes = taskGraphNode.getPrevNodes();
        Set<TaskGraphNode> scheduledTasks = new HashSet<TaskGraphNode>();

        for (ScheduledTask task : scheduledTaskList) {
            scheduledTasks.add(task.getTaskGraphNode());
        }

        for (TaskGraphNode node : prevNodes) {
            if (!scheduledTasks.contains(node)) {
                return false;
            }
        }
        return true;
    }

}


