package com.github.noxan.blommagraphs.graphs;


/**
 * Generic interface for a task graph edge.
 * 
 * @author noxan
 */
public interface TaskGraphEdge {
    /**
     * Returns the node where the edges starts.
     * 
     * @return previous node
     */
    public TaskGraphNode getPrevNode();

    /**
     * Returns the node to which the edges goes.
     * 
     * @return next node
     */
    public TaskGraphNode getNextNode();

    /**
     * Returns the communication time (weight) of this edge.
     * 
     * @return communication time
     */
    public int getCommunicationTime();
}
