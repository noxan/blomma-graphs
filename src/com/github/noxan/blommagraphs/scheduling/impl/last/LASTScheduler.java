package com.github.noxan.blommagraphs.scheduling.impl.last;


import java.util.List;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.Scheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class LASTScheduler implements Scheduler {
    private float threshold;
    private SystemMetaInformation systemInformation;

    @Override
    public List<ScheduledTask> schedule(TaskGraph graph, SystemMetaInformation systemMetaInformation) {
        systemInformation = systemMetaInformation;

        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Calculates the D_NODE value used to select the next task that is
     * scheduled.
     * 
     * @param node The node for which the D_NODE value is calculated.
     * @return D_NODE value as float.
     */
    private float calcDNode(TaskGraphNode node) {
        return 0.0f;
    }

    /**
     * Calculates the D_EDGE value which says if the edge between node1 and
     * node2 is defined. An edge is defined if either node1 or node2 is already
     * scheduled on a task.
     * 
     * @param node1
     * @param node2
     * @return 1 if the edge is defined or 0 if it's not defined.
     */
    private int calcDEdge(TaskGraphNode node1, TaskGraphNode node2) {
        return 0;
    }

    /**
     * Calculates how much node1 is connected to a cpu.
     * 
     * @param node1
     * @param cpuId The id of the cpu.
     * @return STRENGTH of node1 related to cpu.
     */
    private float calcStrength(TaskGraphNode node1, int cpuId) {
        return 0.0f;
    }

}
