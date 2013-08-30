package com.github.noxan.blommagraphs.graphs.impl;


import java.util.Set;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class JGraphtTaskGraph implements TaskGraph {
    private DefaultDirectedWeightedGraph<JGraphtTaskGraphNode, DefaultWeightedEdge> graph;

    private JGraphtTaskGraphNode firstNode;
    private JGraphtTaskGraphNode lastNode;

    public JGraphtTaskGraph() {
        graph = new DefaultDirectedWeightedGraph<JGraphtTaskGraphNode, DefaultWeightedEdge>(
                DefaultWeightedEdge.class);

        firstNode = new JGraphtTaskGraphNode(1);
        graph.addVertex(firstNode);

        lastNode = new JGraphtTaskGraphNode(1);
        graph.addVertex(lastNode);
    }

    @Override
    public TaskGraphNode getFirstNode() {
        return firstNode;
    }

    @Override
    public TaskGraphNode getLastNode() {
        return lastNode;
    }

    @Override
    public int getLayerCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getEdgeCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNodeCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime,
            boolean keepExistingEdge) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TaskGraphEdge insertEdge(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void modifyEdge(TaskGraphNode prevNode, TaskGraphNode nextNode, int newCommunicationTime) {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<TaskGraphEdge> getEdgeSet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<TaskGraphNode> getNodeSet() {
        // TODO Auto-generated method stub
        return null;
    }
}
