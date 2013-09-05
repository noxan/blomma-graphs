package com.github.noxan.blommagraphs.graphs.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class DefaultTaskGraphEdge implements TaskGraphEdge {
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
}
