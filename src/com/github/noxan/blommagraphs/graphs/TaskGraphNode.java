package com.github.noxan.blommagraphs.graphs;


import java.util.Set;


/**
 * Generic interface for a task graph node.
 * 
 * @author noxan
 */
public interface TaskGraphNode extends Comparable<TaskGraphNode> {
    /**
     * Returns a set of all previous nodes (dependencies);
     * 
     * @return set of all previous nodes
     */
    public Set<TaskGraphNode> getPrevNodes();

    /**
     * Returns a set of all next nodes (which depend on the current one).
     * 
     * @return set of all next nodes
     */
    public Set<TaskGraphNode> getNextNodes();

    /**
     * Returns the number of previous nodes (dependencies).
     * 
     * @return number of previous nodes.
     */
    public int getPrevNodeCount();

    /**
     * Returns the number of next nodes (which depend on the current one).
     * 
     * @return number of next nodes
     */
    public int getNextNodeCount();

    /**
     * Returns a set of all previous (incoming) edges.
     * 
     * @return set of previous edges
     */
    public Set<TaskGraphEdge> getPrevEdges();

    /**
     * Returns a set of all next (outgoing) edges.
     * 
     * @return set of next edges
     */
    public Set<TaskGraphEdge> getNextEdges();

    /**
     * Returns the number of previous (incoming) edges.
     * 
     * @return number of previous edges
     */
    public int getPrevEdgeCount();

    /**
     * Returns the number of next (outgoing) edges.
     * 
     * @return number of next edges
     */
    public int getNextEdgeCount();

    /**
     * Returns the internal id of the node. Started from zero and increased each
     * time a node gets inserted, while the last node has the highest id.
     * 
     * @return id of the node
     */
    public int getId();

    /**
     * Sets the computation time of the node.
     * 
     * @param computationTime The new computation time of the node.
     */
    public void setComputationTime(int computationTime);

    /**
     * Returns the computation time of the node.
     * 
     * @return computation time of the node
     */
    public int getComputationTime();

    /**
     * returns the number of nodes from the top
     * 
     * @return
     */
    public int getTopLayerCount();

    /**
     * returns the number of nodes from the bottom
     * 
     * @return
     */
    public int getBottomLayerCount();
}
