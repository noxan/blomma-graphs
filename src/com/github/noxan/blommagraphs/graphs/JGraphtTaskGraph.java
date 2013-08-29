package com.github.noxan.blommagraphs.graphs;


public class JGraphtTaskGraph implements TaskGraph {
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
