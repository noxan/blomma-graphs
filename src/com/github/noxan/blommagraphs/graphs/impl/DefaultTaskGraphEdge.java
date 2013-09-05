package com.github.noxan.blommagraphs.graphs.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class DefaultTaskGraphEdge implements TaskGraphEdge {
    private TaskGraphNode prevNode;
    private TaskGraphNode nextNode;

    public DefaultTaskGraphEdge(TaskGraphNode prevNode, TaskGraphNode nextNode) {
        this.prevNode = prevNode;
        this.nextNode = nextNode;
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
        // TODO Auto-generated method stub
        return 0;
    }
}
