package com.github.noxan.blommagraphs.graphs;


public interface TaskGraphEdge {
    public TaskGraphNode getPrevNode();

    public TaskGraphNode getNextNode();

    public int getCommunicationTime();
}
