package com.github.noxan.blommagraphs.graphs.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.ContainsNoEdgeException;
import com.github.noxan.blommagraphs.graphs.exceptions.DuplicateEdgeException;
import com.github.noxan.blommagraphs.graphs.meta.TaskGraphMetaInformation;
import com.github.noxan.blommagraphs.graphs.meta.impl.DefaultTaskGraphMetaInformation;


public class DefaultTaskGraph implements TaskGraph {
    private TaskGraphMetaInformation metaInformation;

    private TaskGraphNode firstNode;
    private TaskGraphNode lastNode;

    private Set<TaskGraphEdge> edgeSet;

    public DefaultTaskGraph() {
        this(new DefaultTaskGraphMetaInformation());
    }

    public DefaultTaskGraph(TaskGraphMetaInformation metaInformation) {
        this.metaInformation = metaInformation;
        firstNode = new DefaultTaskGraphNode(this, 0, 1);
        lastNode = new DefaultTaskGraphNode(this, 1, 1);

        TaskGraphEdge edge = new DefaultTaskGraphEdge(firstNode, lastNode, 1);
        ((DefaultTaskGraphNode) firstNode).addNextNode(edge);
        ((DefaultTaskGraphNode) lastNode).addPrevNode(edge);

        edgeSet = new TreeSet<TaskGraphEdge>();
        edgeSet.add(edge);
    }

    protected void setFirstNode(TaskGraphNode firstNode) {
        this.firstNode = firstNode;
    }

    @Override
    public TaskGraphNode getFirstNode() {
        return firstNode;
    }

    protected void setLastNode(TaskGraphNode lastNode) {
        this.lastNode = lastNode;
    }

    @Override
    public TaskGraphNode getLastNode() {
        return lastNode;
    }

    @Override
    public int getLayerCount() {
        return getLayerCount(firstNode, 1);
    }

    private int getLayerCount(TaskGraphNode node, int layer) {
        int maxLayer = layer;
        for (TaskGraphNode nextNode : node.getNextNodes()) {
            int result = getLayerCount(nextNode, layer + 1);
            if (maxLayer < result) {
                maxLayer = result;
            }
        }
        return maxLayer;
    }

    @Override
    public int getEdgeCount() {
        return getEdgeSet().size();
    }

    @Override
    public int getNodeCount() {
        return getNodeSet().size();
    }

    private int cpTime = 0;
    private List<TaskGraphEdge> cpEdgeList = new ArrayList<TaskGraphEdge>();

    @Override
    public List<TaskGraphEdge> getCriticalPath() {
        return criticalPath(this.firstNode, 0, new ArrayList<TaskGraphEdge>());
    }

    private List<TaskGraphEdge> criticalPath(TaskGraphNode node, int time,
            List<TaskGraphEdge> currentEdgeList) {
        int maxTime = time + node.getComputationTime();

        ArrayList<TaskGraphEdge> taskGraphEdgeList = new ArrayList<TaskGraphEdge>();

        taskGraphEdgeList.addAll(node.getNextEdges());
        for (int i = 0; i < taskGraphEdgeList.size(); i++) {
            ArrayList<TaskGraphEdge> currentEdgeListCopy = new ArrayList<TaskGraphEdge>();
            for (int j = 0; j < currentEdgeList.size(); j++) {
                currentEdgeListCopy.add(currentEdgeList.get(j));
            }
            currentEdgeListCopy.add(taskGraphEdgeList.get(i));
            criticalPath(taskGraphEdgeList.get(i).getNextNode(), maxTime
                    + taskGraphEdgeList.get(i).getCommunicationTime(), currentEdgeListCopy);

            if (maxTime > cpTime) {
                cpTime = maxTime;
                cpEdgeList = currentEdgeListCopy;
            }
        }
        return cpEdgeList;
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

        TaskGraphNode node = new DefaultTaskGraphNode(this, lastId, prevNode,
                prevCommunicationTime, nextNode, nextCommunicationTime, computationTime);

        if (!keepExistingEdge) {
            deleteEdge(prevNode, nextNode);
        }

        return node;
    }

    @Override
    public TaskGraphEdge insertEdge(TaskGraphNode prevNode, TaskGraphNode nextNode,
            int communicationTime) throws DuplicateEdgeException {
        try {
            findEdge(prevNode, nextNode);
            throw new DuplicateEdgeException();
        } catch (ContainsNoEdgeException e) {
            TaskGraphEdge newEdge = new DefaultTaskGraphEdge(prevNode, nextNode, communicationTime);
            ((DefaultTaskGraphNode) prevNode).addNextNode(newEdge);
            ((DefaultTaskGraphNode) nextNode).addPrevNode(newEdge);
            edgeSet.add(newEdge);
            return newEdge;
        }
    }

    @Override
    public TaskGraphEdge findEdge(TaskGraphNode prevNode, TaskGraphNode nextNode)
            throws ContainsNoEdgeException {
        for (TaskGraphEdge edge : getEdgeSet()) {
            if (edge.getPrevNode() == prevNode && edge.getNextNode() == nextNode) {
                return edge;
            }
        }
        throw new ContainsNoEdgeException();
    }

    @Override
    public TaskGraphEdge deleteEdge(TaskGraphNode prevNode, TaskGraphNode nextNode) {
        try {
            TaskGraphEdge edge = findEdge(prevNode, nextNode);

            ((DefaultTaskGraphNode) prevNode).removeNextNode(nextNode);
            ((DefaultTaskGraphNode) nextNode).removePrevNode(prevNode);

            edgeSet.remove(edge);

            return edge;
        } catch (ContainsNoEdgeException e) {
            return null;
        }
    }

    @Override
    public void modifyEdge(TaskGraphNode prevNode, TaskGraphNode nextNode, int newCommunicationTime) {
        try {
            TaskGraphEdge edge = findEdge(prevNode, nextNode);
            ((DefaultTaskGraphEdge) edge).setCommunicationTime(newCommunicationTime);
        } catch (ContainsNoEdgeException ignored) {
        }
    }

    @Override
    public Set<TaskGraphEdge> getEdgeSet() {
        return edgeSet;
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
        try {
            findEdge(prevNode, nextNode);
            return true;
        } catch (ContainsNoEdgeException e) {
            return false;
        }
    }

    @Override
    public Map<String, Object> getMetaInformation() {
        metaInformation.setMetaInformation("nodeCount", getNodeCount());
        metaInformation.setMetaInformation("dummyNodeCount", 2);
        metaInformation.setMetaInformation("edgeCount", getEdgeCount());
        metaInformation.setMetaInformation("dummyEdgeCount", getFirstNode().getNextEdgeCount()
                + getLastNode().getPrevEdgeCount());
        metaInformation.setMetaInformation("layerCount", getLayerCount());
        metaInformation.setMetaInformation("edgesPerNodeRatio", getEdgeCount() / getNodeCount());

        return metaInformation.getMetaInformation();
    }

    @Override
    public void resetDeadLine(int deadLine) {
        for (TaskGraphNode node : getNodeSet()) {
            node.setDeadLine(deadLine);
        }
    }

    @Override
    public void mergeGraph(TaskGraph taskGraph, TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime) {
        // copy nodes with new ids
        Map<Integer, TaskGraphNode> nodeList = new HashMap<Integer, TaskGraphNode>();
        for (TaskGraphNode node : taskGraph.getNodeSet()) {
            TaskGraphNode duplicatedNode = new DefaultTaskGraphNode(this, lastNode.getId()
                    + node.getId(), node.getComputationTime());
            duplicatedNode.setDeadLine(node.getDeadLine());
            nodeList.put(node.getId(), duplicatedNode);
        }
        // update last node id
        ((DefaultTaskGraphNode) getLastNode()).setId(lastNode.getId() + nodeList.size());
        // add edges
        try {
            for (TaskGraphEdge edge : taskGraph.getEdgeSet()) {
                insertEdge(nodeList.get(edge.getPrevNode().getId()),
                        nodeList.get(edge.getNextNode().getId()), edge.getCommunicationTime());
            }
            // connect new graph to this one
            insertEdge(prevNode, nodeList.get(0), prevCommunicationTime);
            insertEdge(nodeList.get(nodeList.size() - 1), nextNode, nextCommunicationTime);
        } catch (DuplicateEdgeException ignored) {
        }
    }

    @Override
    public TaskGraph clone() {
        TaskGraph clonedTaskGraph = new DefaultTaskGraph();
        Map<Integer, TaskGraphNode> nodeList = new HashMap<Integer, TaskGraphNode>();
        for (TaskGraphNode node : getNodeSet()) {
            nodeList.put(node.getId(),
                    new DefaultTaskGraphNode(this, node.getId(), node.getComputationTime()));
        }
        ((DefaultTaskGraph) clonedTaskGraph).setFirstNode(nodeList.get(0));
        ((DefaultTaskGraph) clonedTaskGraph).setLastNode(nodeList.get(nodeList.size() - 1));
        for (TaskGraphEdge edge : getEdgeSet()) {
            try {
                clonedTaskGraph.insertEdge(nodeList.get(edge.getPrevNode().getId()),
                        nodeList.get(edge.getNextNode().getId()), edge.getCommunicationTime());
            } catch (DuplicateEdgeException ignored) {
            }
        }
        return clonedTaskGraph;
    }

    @Override
    public void setDeadline(int deadline) {
        for (TaskGraphNode node : getNodeSet()) {
            node.setDeadLine(deadline);
        }
    }
}
