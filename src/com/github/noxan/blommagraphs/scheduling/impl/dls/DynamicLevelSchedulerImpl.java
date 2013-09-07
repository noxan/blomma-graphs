package com.github.noxan.blommagraphs.scheduling.impl.dls;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.Scheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class DynamicLevelSchedulerImpl implements Scheduler {
    private TaskGraph taskGraph;
    private List<ScheduledTask> scheduledTaskList;
    private List<TaskGraphNode> readyNodePool;


    @Override
    public List<ScheduledTask> schedule(TaskGraph graph, SystemMetaInformation systemInformation) {

        scheduledTaskList =  new ArrayList<ScheduledTask>();
        readyNodePool =  new ArrayList<TaskGraphNode>();


        // compute static level of all nodes


        // initialize ready-pool at first only start node
        readyNodePool.add(graph.getFirstNode());

        // compute the earliest start time for every ready node for each
        // processor


        // calculate the dl
        // choose the node-processor pair with the biggest dynamic level and
        // comit it to the processor
        // add new ready nodes to the ready node pool
        return null;
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


