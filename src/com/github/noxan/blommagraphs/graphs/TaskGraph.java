package com.github.noxan.blommagraphs.graphs;


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

    public TaskGraphEdge insertEdge(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime);

    public void modifyEdge(TaskGraphNode prevNode, TaskGraphNode nextNode, int newCommunicationTime);
}
