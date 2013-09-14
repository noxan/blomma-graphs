package com.github.noxan.blommagraphs.graphs;


import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.exceptions.ContainsNoEdgeException;
import com.github.noxan.blommagraphs.graphs.exceptions.DuplicateEdgeException;


/**
 * Generic interface for the task graph data structure.
 * 
 * @author noxan
 */
public interface TaskGraph extends Cloneable {
    /**
     * Returns the first node of the task graph.
     * 
     * @return first node
     */
    public TaskGraphNode getFirstNode();

    /**
     * Returns the last node of the task graph.
     * 
     * @return last node
     */
    public TaskGraphNode getLastNode();

    /**
     * Returns the layer count of the complete task graph.
     * 
     * @return number of layer in the graph
     */
    public int getLayerCount();

    /**
     * Returns the edge count of the complete task graph.
     * 
     * @return number of edges in the graph
     */
    public int getEdgeCount();

    /**
     * Returns the node (task) count of the complete task graph.
     * 
     * @return number of nodes in the graph
     */
    public int getNodeCount();

    /**
     * Returns the critical path (the path with takes the longest time to execute from first node to
     * last node) of the graph as list of edges
     * 
     * @return list of edges
     */
    public List<TaskGraphEdge> getCriticalPath();

    /**
     * Adds a new node into the task graph and connecting it to a previous and next node. Makes sure
     * the task graph itself stays valid and returns the created node.
     * 
     * @param prevNode previous node
     * @param prevCommunicationTime communication time to the previous node
     * @param nextNode next node
     * @param nextCommunicationTime communication time to the next node
     * @param computationTime computation time of the new node
     * @return the created node
     */
    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime);

    /**
     * Adds a new node into the task graph and connecting it to a previous and next node. Makes sure
     * the task graph itself stays valid and returns the created node.
     * 
     * This methods allows to keep an existing edge or connection between the previous and next
     * node.
     * 
     * @see insertNode
     * 
     * @param prevNode previous node
     * @param prevCommunicationTime communication time to the previous node
     * @param nextNode next node
     * @param nextCommunicationTime communication time to the next node
     * @param computationTime computation time of the new node
     * @param keepExistingEdge if true the graph keeps and existing edge between next and previous
     *            node
     * @return the created node
     */
    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime,
            boolean keepExistingEdge);

    /**
     * Adds a new edge between the previous and next node with a given communication time. Does not
     * allow duplicate edges and loops.
     * 
     * @param prevNode previous node
     * @param nextNode next node
     * @param communicationTime communication time for the new edge
     * @return the created edge
     * @throws DuplicateEdgeException thrown if an edge between those two nodes already exists
     */
    public TaskGraphEdge insertEdge(TaskGraphNode prevNode, TaskGraphNode nextNode,
            int communicationTime) throws DuplicateEdgeException;

    /**
     * Removes an edge between the two given nodes
     * 
     * @param prevNode previous node
     * @param nextNode next node
     * @return the deleted edge
     */
    public TaskGraphEdge deleteEdge(TaskGraphNode prevNode, TaskGraphNode nextNode);

    /**
     * Modifies the communication time between two nodes.
     * 
     * @param prevNode previous node
     * @param nextNode next node
     * @param newCommunicationTime the new communication time
     */
    public void modifyEdge(TaskGraphNode prevNode, TaskGraphNode nextNode, int newCommunicationTime);

    /**
     * Returns a set of all edges contained in the task graph.
     * 
     * @return set of all edges
     */
    public Set<TaskGraphEdge> getEdgeSet();

    /**
     * Returns a set of all nodes (tasks) contained in the task graph.
     * 
     * @return a set of all nodes (tasks)
     */
    public Set<TaskGraphNode> getNodeSet();

    /**
     * Checks if there is a edge between the two given nodes.
     * 
     * @param prevNode previous node
     * @param nextNode next node
     * @return true if there is a edge between the two given nodes, else false
     */
    public boolean containsEdge(TaskGraphNode prevNode, TaskGraphNode nextNode);

    /**
     * returns an edge between two given nodes throws exception in case there is no edge between
     * those nodes
     * 
     * @param prevNode previous node
     * @param nextNode next node
     * @return TaskGraphEdge if there is one, otherwise exception is thrown
     */
    public TaskGraphEdge findEdge(TaskGraphNode prevNode, TaskGraphNode nextNode)
            throws ContainsNoEdgeException;

    /**
     * Returns all the meta information related to this graph. Also contains the generator meta
     * information if available. The returned map should be considered as immutable.
     * 
     * @return meta information related to this graph
     */
    public Map<String, Object> getMetaInformation();

    /**
     * Resets the deadline of a graph to deadLine. That means that for all nodes in this graph
     * TaskGraphNode.setDeadline(deadLine) is called.
     * 
     * @param deadLine The new deadline of the graph.
     */
    public void resetDeadLine(int deadLine);

    /**
     * This method merges TaskGraph srcGraph into this instance of TaskGraph. All nodes and edges of
     * srcGraph are inserted. Afterwards, additional edges are inserted to connect inserted nodes
     * with the rest of the graph: one from this.prevNode -> srcGraph.firstNode and one from
     * srcGraph.lastNode -> this.nextNode.
     * 
     * @param srcGraph TaskGraph instance that is merged into this graph.
     * @param prevNode srcGraph is merged into this graph after prevNode.
     * @param prevCommunicationTime The communication time between this.prevNode ->
     *            srcGraph.firstNode.
     * @param nextNode The srcGraph is merged into this graph before nextNode.
     * @param nextCommunicationTime The communication time between srcGraph.lastNode ->
     *            this.nextNode.
     */
    public void mergeGraph(TaskGraph srcGraph, TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime);

    public TaskGraph clone();
}
