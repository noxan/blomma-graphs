package com.github.noxan.blommagraphs.graphs.impl;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class DefaultTaskGraph implements TaskGraph {
    private TaskGraphNode firstNode;
    private TaskGraphNode lastNode;

    public DefaultTaskGraph() {
        firstNode = new DefaultTaskGraphNode(0, 1);
        lastNode = new DefaultTaskGraphNode(1, 1);
        ((DefaultTaskGraphNode) firstNode).addNextNode(lastNode, 1);
        ((DefaultTaskGraphNode) lastNode).addPrevNode(firstNode, 1);
    }

    @Override
    public TaskGraphNode getFirstNode() {
        return firstNode;
    }

    @Override
    public TaskGraphNode getLastNode() {
        return lastNode;
    }

    @Override
    public int getLayerCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getEdgeCount() {
        return getEdgeSet().size();
    }

    @Override
    public int getNodeCount() {
        return getNodeSet().size();
    }

    @Override
    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime) {
        return insertNode(prevNode, prevCommunicationTime, nextNode, nextCommunicationTime,
                computationTime, false);
    }

    @Override
    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime,
            boolean keepExistingEdge) {

        int lastId = lastNode.getId();

        ((DefaultTaskGraphNode) lastNode).setId(lastId + 1);

        TaskGraphNode node = new DefaultTaskGraphNode(lastId, prevNode, prevCommunicationTime,
                nextNode, nextCommunicationTime, computationTime);

        ((DefaultTaskGraphNode) prevNode).addNextNode(node, prevCommunicationTime);
        ((DefaultTaskGraphNode) nextNode).addPrevNode(node, nextCommunicationTime);

        if (!keepExistingEdge) {
            ((DefaultTaskGraphNode) prevNode).removeNextNode(nextNode);
            ((DefaultTaskGraphNode) nextNode).removePrevNode(prevNode);
        }

        return node;
    }

    @Override
    public TaskGraphEdge insertEdge(TaskGraphNode prevNode, TaskGraphNode nextNode,
            int communicationTime) {
        ((DefaultTaskGraphNode) prevNode).addNextNode(nextNode, communicationTime);
        ((DefaultTaskGraphNode) nextNode).addPrevNode(prevNode, communicationTime);

        return new DefaultTaskGraphEdge(prevNode, nextNode, communicationTime);
    }

    private TaskGraphEdge findEdge(TaskGraphNode prevNode, TaskGraphNode nextNode) {
        for (TaskGraphEdge edge : getEdgeSet()) {
            if (edge.getPrevNode() == prevNode && edge.getNextNode() == nextNode) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public TaskGraphEdge deleteEdge(TaskGraphNode prevNode, TaskGraphNode nextNode) {
        TaskGraphEdge edge = findEdge(prevNode, nextNode);

        ((DefaultTaskGraphNode) prevNode).removeNextNode(nextNode);
        ((DefaultTaskGraphNode) nextNode).removePrevNode(prevNode);

        return edge;
    }

    @Override
    public void modifyEdge(TaskGraphNode prevNode, TaskGraphNode nextNode, int newCommunicationTime) {
        TaskGraphEdge edge = findEdge(prevNode, nextNode);
        if (edge != null) {
            ((DefaultTaskGraphEdge) edge).setCommunicationTime(newCommunicationTime);
        }
    }

    @Override
    public Set<TaskGraphEdge> getEdgeSet() {
        return getEdgeSet(firstNode);
    }

    private Set<TaskGraphEdge> getEdgeSet(TaskGraphNode node) {
        Set<TaskGraphEdge> edgeSet = new HashSet<TaskGraphEdge>();

        edgeSet.addAll(node.getNextEdges());
        for (TaskGraphEdge nextEdge : node.getNextEdges()) {
            TaskGraphNode nextNode = nextEdge.getNextNode();
            if (nextNode != lastNode) {
                edgeSet.addAll(getEdgeSet(nextNode));
            }
        }

        return edgeSet;
    }

    @Override
    public Set<TaskGraphNode> getNodeSet() {
        Set<TaskGraphNode> nodeSet = getNodeSet(firstNode);
        nodeSet.add(firstNode);
        return nodeSet;
    }

    private Set<TaskGraphNode> getNodeSet(TaskGraphNode node) {
        Set<TaskGraphNode> nodeSet = new HashSet<TaskGraphNode>();

        nodeSet.addAll(node.getNextNodes());
        for (TaskGraphNode nextNode : node.getNextNodes()) {
            if (nextNode != lastNode) {
                nodeSet.addAll(getNodeSet(nextNode));
            }
        }

        return nodeSet;
    }

    @Override
    public boolean containsEdge(TaskGraphNode prevNode, TaskGraphNode nextNode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Map<String, Object> getMetaInformation() {
        // TODO Auto-generated method stub
        return null;
    }
}
