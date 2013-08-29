package com.github.noxan.blommagraphs.graphs;


import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;


public class JGraphtTaskGraph implements TaskGraph {
    private DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph;

    public JGraphtTaskGraph() {
        graph = new DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge>(
                DefaultWeightedEdge.class);

        for (int i = 0; i < 10; i++) {
            graph.addVertex(i);
        }
        for (int i = 0; i < 9; i++) {
            DefaultWeightedEdge edge = graph.addEdge(i, i + 1);
            graph.setEdgeWeight(edge, i * 10);
        }

        System.out.println(graph.toString());
    }

    @Override
    public TaskGraphNode getFirstNode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TaskGraphNode getLastNode() {
        // TODO Auto-generated method stub
        return null;
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
}
