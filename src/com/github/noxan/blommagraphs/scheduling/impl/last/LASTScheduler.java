package com.github.noxan.blommagraphs.scheduling.impl.last;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.ContainsNoEdgeException;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.Scheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class LASTScheduler implements Scheduler {
    private float threshold = 0.65f;
    private SystemMetaInformation systemInformation;
    private TaskGraph taskGraph;

    private List<List<LASTNode>> groups;
    private List<List<LASTNode>> frontiers;
    private Set<TaskGraphNode> nodeSet;

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    @Override
    public List<ScheduledTask> schedule(TaskGraph graph, SystemMetaInformation systemMetaInformation) {
        initialize(graph, systemMetaInformation);
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Takes a TaskGraph and SystemMetaInformation and initializes the
     * scheduler.
     * 
     * @param graph
     * @param systemMetaInformation
     */
    private void initialize(TaskGraph graph, SystemMetaInformation systemMetaInformation) {
        systemInformation = systemMetaInformation;
        taskGraph = graph;
        int processorCount = systemMetaInformation.getProcessorCount();

        groups = new ArrayList<List<LASTNode>>();
        frontiers = new ArrayList<List<LASTNode>>();

        for (int i = 0; i < processorCount; ++i) {
            groups.add(new ArrayList<LASTNode>());
            frontiers.add(new ArrayList<LASTNode>());
        }

        nodeSet = graph.getNodeSet();
    }

    /**
     * Calculates the D_NODE value used to select the next task that is
     * scheduled.
     * 
     * @param node The node for which the D_NODE value is calculated.
     * @return D_NODE value as float.
     */
    private float calcDNode(LASTNode node) {
        return 0.0f;
    }

    /**
     * Calculates the D_EDGE value which says if the edge between node1 and
     * node2 is defined. An edge is defined if either node1 or node2 is already
     * scheduled on a processor.
     * 
     * @param node1
     * @param node2
     * @return 1 if the edge is defined or 0 if it's not defined.
     */
    private int calcDEdge(LASTNode node1, LASTNode node2) {
        // saves if node1 or node2 is scheduled.
        int node1scheduled = 0;
        int node2scheduled = 0;

        // Iterate through groups and check if node1 or node2 is included.
        for (List<LASTNode> group : groups) {
            if (group.contains(node1)) {
                node1scheduled = 1;
            }
            if (group.contains(node2)) {
                node2scheduled = 1;
            }
        }

        // If either node1 or node2 is scheduled, return 1, else return 0.
        return node1scheduled ^ node2scheduled;
    }

    /**
     * Calculates how much node1 is connected to a cpu.
     * strength = sum(communicationTime (node1, lastNodePrevNodes.get(i)) / computiationTime(node1))
     *            +
     *            sum(communicationTime (node1, lastNodeNextNodes.get(i)) / computiationTime(node1))
     *            -
     *            dif(computiationTime(node1) / communicationTime (node1, lastNodeNextNodes.get(i))
     * 
     * @param node1
     * @param cpuId The id of the cpu.
     * @return STRENGTH of node1 related to cpu.
     */
    private float calcStrength(LASTNode node1, int cpuId) {
        int computationTimeNode1 = node1.getTaskGraphNode().getComputationTime();
        double strength = 0.0;

        // Lists of the previous and following nodes of the node1.
        ArrayList<TaskGraphNode> lastNodePrevNodes = new ArrayList<TaskGraphNode>
                (node1.getTaskGraphNode().getPrevNodes());
        ArrayList<TaskGraphNode> lastNodeNextNodes = new ArrayList<TaskGraphNode>
                (node1.getTaskGraphNode().getNextNodes());

        // Iterate through the lastNodePrevNodes and calculate the first sum.
        for (int i = 0; i < lastNodePrevNodes.size(); i++) {
            try {
                strength += ((taskGraph.findEdge(lastNodePrevNodes.get(i),
                        node1.getTaskGraphNode()).getCommunicationTime()) / computationTimeNode1);
            } catch (ContainsNoEdgeException e) {
                e.printStackTrace();
            }
        }

        // Iterate through the lastNodeNextNodes and calculate the second sum.
        for (int i = 0; i < lastNodeNextNodes.size(); i++) {
            try{
                strength += ((taskGraph.findEdge(node1.getTaskGraphNode(),
                        lastNodeNextNodes.get(i)).getCommunicationTime()) / computationTimeNode1);
            } catch (ContainsNoEdgeException e) {
                e.printStackTrace();
            }
        }

        // Iterate through the lastNodeNextNodes and calculate the difference.
        for (int i = 0; i < lastNodeNextNodes.size(); i++) {
            try {
                strength -= ( computationTimeNode1 / (taskGraph.findEdge(node1.getTaskGraphNode(),
                        lastNodeNextNodes.get(i)).getCommunicationTime()));
            } catch (ContainsNoEdgeException e) {
                e.printStackTrace();
            }
        }
        return (float) strength;
    }

}
