package com.github.noxan.blommagraphs.graphs;


import java.util.Set;

import com.github.noxan.blommagraphs.generator.meta.TaskGraphGeneratorMetaInformation;
import com.github.noxan.blommagraphs.graphs.exceptions.DuplicateEdgeException;


/**
 * Generic interface for the task graph data structure.
 * 
 * @author noxan
 */
public interface TaskGraph {
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
     * Adds a new node into the task graph and connecting it to a previous and
     * next node. Makes sure the task graph itself stays valid and returns the
     * created node.
     * 
     * @param prevNode
     *            previous node
     * @param prevCommunicationTime
     *            communication time to the previous node
     * @param nextNode
     *            next node
     * @param nextCommunicationTime
     *            communication time to the next node
     * @param computationTime
     *            computation time of the new node
     * @return the created node
     */
    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime);

    /**
     * Adds a new node into the task graph and connecting it to a previous and
     * next node. Makes sure the task graph itself stays valid and returns the
     * created node.
     * 
     * This methods allows to keep an existing edge or connection between the
     * previous and next node.
     * 
     * @see insertNode
     * 
     * @param prevNode
     *            previous node
     * @param prevCommunicationTime
     *            communication time to the previous node
     * @param nextNode
     *            next node
     * @param nextCommunicationTime
     *            communication time to the next node
     * @param computationTime
     *            computation time of the new node
     * @param keepExistingEdge
     *            if true the graph keeps and existing edge between next and
     *            previous node
     * @return the created node
     */
    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime,
            boolean keepExistingEdge);

    /**
     * Adds a new edge between the previous and next node with a given
     * communication time. Does not allow duplicate edges and loops.
     * 
     * @param prevNode
     *            previous node
     * @param nextNode
     *            next node
     * @param communicationTime
     *            communication time for the new edge
     * @return the created edge
     * @throws DuplicateEdgeException
     *             thrown if an edge between those two nodes already exists
     */
    public TaskGraphEdge insertEdge(TaskGraphNode prevNode, TaskGraphNode nextNode,
            int communicationTime) throws DuplicateEdgeException;

    /**
     * Removes an edge between the two given nodes
     * 
     * @param prevNode
     *            previous node
     * @param nextNode
     *            next node
     * @return the deleted edge
     */
    public TaskGraphEdge deleteEdge(TaskGraphNode prevNode, TaskGraphNode nextNode);

    /**
     * Modifies the communication time between two nodes.
     * 
     * @param prevNode
     *            previous node
     * @param nextNode
     *            next node
     * @param newCommunicationTime
     *            the new communication time
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
     * @param prevNode
     *            previous node
     * @param nextNode
     *            next node
     * @return true if there is a edge between the two given nodes, else false
     */
    public boolean containsEdge(TaskGraphNode prevNode, TaskGraphNode nextNode);

    /**
     * Returns the meta information of the generator used to create this graph.
     * If no generator has been used it returns null.
     * 
     * @return generator meta information or null
     */
    public TaskGraphGeneratorMetaInformation getGeneratorMetaInformation();
}
