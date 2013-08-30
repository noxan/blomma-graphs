package com.github.noxan.blommagraphs.graphs;


import java.util.Set;

import com.github.noxan.blommagraphs.graphs.exceptions.DuplicateEdgeException;


public interface TaskGraph {
    public TaskGraphNode getFirstNode();

    public TaskGraphNode getLastNode();

    public int getLayerCount();

    public int getEdgeCount();

    public int getNodeCount();

    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime);

    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime,
            boolean keepExistingEdge);

    public TaskGraphEdge insertEdge(TaskGraphNode prevNode, TaskGraphNode nextNode,
            int communicationTime) throws DuplicateEdgeException;

    public void modifyEdge(TaskGraphNode prevNode, TaskGraphNode nextNode, int newCommunicationTime);

    public Set<TaskGraphEdge> getEdgeSet();

    public Set<TaskGraphNode> getNodeSet();
}
