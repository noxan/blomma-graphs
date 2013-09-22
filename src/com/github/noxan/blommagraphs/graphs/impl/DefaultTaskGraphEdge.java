package com.github.noxan.blommagraphs.graphs.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class DefaultTaskGraphEdge implements TaskGraphEdge, Comparable<DefaultTaskGraphEdge> {
    private int communicationTime;
    private TaskGraphNode prevNode;
    private TaskGraphNode nextNode;

    public DefaultTaskGraphEdge(TaskGraphNode prevNode, TaskGraphNode nextNode,
            int communicationTime) {
        this.prevNode = prevNode;
        this.nextNode = nextNode;
        this.communicationTime = communicationTime;
    }

    @Override
    public TaskGraphNode getPrevNode() {
        return prevNode;
    }

    @Override
    public TaskGraphNode getNextNode() {
        return nextNode;
    }

    @Override
    public int getCommunicationTime() {
        return communicationTime;
    }

    protected void setCommunicationTime(int newCommunicationTime) {
        this.communicationTime = newCommunicationTime;
    }

    @Override
    public int compareTo(DefaultTaskGraphEdge edge) {
        int value = nextNode.compareTo(edge.getNextNode());
        if (value != 0) {
            return value;
        }
        return prevNode.compareTo(edge.getPrevNode());
    }
}
