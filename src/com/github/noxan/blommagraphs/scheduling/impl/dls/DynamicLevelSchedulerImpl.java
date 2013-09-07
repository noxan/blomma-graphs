package com.github.noxan.blommagraphs.scheduling.impl.dls;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.ContainsNoEdgeException;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.Scheduler;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTask;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class DynamicLevelSchedulerImpl implements Scheduler {
    private TaskGraph taskGraph;
    private List<ScheduledTask> scheduledTaskList;
    private List<ReadyPoolNode> readyNodePool;

    @Override
    public List<ScheduledTask> schedule(TaskGraph graph, SystemMetaInformation systemInformation) {

        scheduledTaskList =  new ArrayList<ScheduledTask>();
        readyNodePool =  new ArrayList<ReadyPoolNode>();
        int cpuCount = systemInformation.getProcessorCount();
        ArrayList<ArrayList<ScheduledTask>> allCpuScheduleTasks = new ArrayList<ArrayList<ScheduledTask>>();

        // initialize ready-pool at first only start node
        readyNodePool.add(new ReadyPoolNode(graph.getFirstNode(),
                graph.getFirstNode().getStaticBLevel()));

        //TODO: check/implement special case of first task
        // main loop
        while (!readyNodePool.isEmpty()) {

            // compute the earliest start time for every ready node for each processor
            // write first starttime for every ready pool node for every cpu
            for (ReadyPoolNode poolNode : readyNodePool) {
                // check first starttime for every cpu
                for (ArrayList<ScheduledTask> cpuScheduleTaskList : allCpuScheduleTasks) {
                    // get last task
                    ScheduledTask lastTask = cpuScheduleTaskList.get(cpuScheduleTaskList.size()-1);
                    int firstStarttime = lastTask.getStartTime() + lastTask.getComputationTime();
                    // check with the dependencies to other cpus
                    int latestDependencyTime = 0;
                    for (ArrayList<ScheduledTask> otherCpuSchedulerTaskList : allCpuScheduleTasks) {
                        // just if its not the actual cpu
                        if (otherCpuSchedulerTaskList != cpuScheduleTaskList) {
                            int latestDependencyTimePerCpu = 0;
                            // check all dependent tasks
                            for (ScheduledTask task : otherCpuSchedulerTaskList) {
                                int currentDependencyTimePerTask = 0;
                                try {
                                    // get latest dependency time per task
                                    currentDependencyTimePerTask = task.getStartTime() + task.getComputationTime() + graph.findEdge(task.getTaskGraphNode(),poolNode.getNode()).getCommunicationTime();
                                } catch (ContainsNoEdgeException e) {
                                    e.printStackTrace();
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
                    poolNode.setEarliestStarttime(allCpuScheduleTasks.indexOf(cpuScheduleTaskList),firstStarttime);
                }
            }

            // choose the node-processor pair with the biggest dynamic level and
            ReadyPoolNode chosenScheduledTask = null;
            int maxDynamicLevel = 0;
            int nextCpu = 0;
            for (ReadyPoolNode poolNode : readyNodePool) {
                if (poolNode.getMaxDynamicLevel().getFirst() > maxDynamicLevel) {
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

            // add new ready nodes to the ready node pool and compute static b level of the new nodes
            Set<TaskGraphNode> nextNodes = chosenScheduledTask.getNode().getNextNodes();
            //TODO: check case if no more nodes left to schedule
            if (nextNodes.size() != 0) {
                for (TaskGraphNode node : nextNodes) {
                    if (isReadyNode(node)) {
                        readyNodePool.add(new ReadyPoolNode(node, node.getStaticBLevel()));
                    }
                }
            }
        }
        return scheduledTaskList;
    }

    private boolean isReadyNode(TaskGraphNode taskGraphNode) {
        Set<TaskGraphNode> prevNodes = taskGraphNode.getPrevNodes();
        Set<TaskGraphNode> computedNodes = new HashSet<TaskGraphNode>();

        for (ScheduledTask task : scheduledTaskList) {
            computedNodes.add(task.getTaskGraphNode());
        }

        for (TaskGraphNode node : prevNodes) {
            if (!computedNodes.contains(node)){
                return false;
            }
        }
        return true;
    }

}


