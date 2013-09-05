package com.github.noxan.blommagraphs.graphs.impl;


import java.util.Map;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class DefaultTaskGraph implements TaskGraph {
    private TaskGraphNode firstNode;
    private TaskGraphNode lastNode;

    public DefaultTaskGraph() {
        firstNode = new DefaultTaskGraphNode(0, 1);
        lastNode = new DefaultTaskGraphNode(1, 1);
        ((DefaultTaskGraphNode) firstNode).addNextNode(lastNode, 1);
        ((DefaultTaskGraphNode) lastNode).addPrevNode(firstNode, 1);
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
                computationTime, false);
    }

    @Override
    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime,
            boolean keepExistingEdge) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TaskGraphEdge insertEdge(TaskGraphNode prevNode, TaskGraphNode nextNode,
            int communicationTime) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TaskGraphEdge deleteEdge(TaskGraphNode prevNode, TaskGraphNode nextNode) {
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

    @Override
    public boolean containsEdge(TaskGraphNode prevNode, TaskGraphNode nextNode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Map<String, Object> getMetaInformation() {
        // TODO Auto-generated method stub
        return null;
    }
}
