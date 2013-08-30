package com.github.noxan.blommagraphs.graphs.impl;


import java.util.Set;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class JGraphtTaskGraph implements TaskGraph {
    private DefaultDirectedWeightedGraph<TaskGraphNode, DefaultWeightedEdge> graph;

    private TaskGraphNode firstNode;
    private TaskGraphNode lastNode;

    public JGraphtTaskGraph() {
        graph = new DefaultDirectedWeightedGraph<TaskGraphNode, DefaultWeightedEdge>(
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
        return insertNode(prevNode, prevCommunicationTime, nextNode, nextCommunicationTime,
                computationTime, true);
    }

    @Override
    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime,
            boolean keepExistingEdge) {
        TaskGraphNode node = new JGraphtTaskGraphNode(computationTime);
        graph.addVertex(node);

        DefaultWeightedEdge prevEdge = graph.addEdge(prevNode, node);
        graph.setEdgeWeight(prevEdge, prevCommunicationTime);

        DefaultWeightedEdge nextEdge = graph.addEdge(node, nextNode);
        graph.setEdgeWeight(nextEdge, nextCommunicationTime);

        if (!keepExistingEdge) {
            graph.removeAllEdges(prevNode, nextNode);
        }

        return node;
    }

    @Override
    public TaskGraphEdge insertEdge(TaskGraphNode prevNode, TaskGraphNode nextNode,
            int communicationTime) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void modifyEdge(TaskGraphNode prevNode, TaskGraphNode nextNode, int newCommunicationTime) {
        DefaultWeightedEdge edge = graph.getEdge(prevNode, nextNode);

        graph.setEdgeWeight(edge, newCommunicationTime);
    }

    @Override
    public Set<TaskGraphEdge> getEdgeSet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<TaskGraphNode> getNodeSet() {
        return graph.vertexSet();
    }
}
