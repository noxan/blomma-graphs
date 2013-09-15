package com.github.noxan.blommagraphs.graphs.impl;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class DefaultTaskGraphNode implements TaskGraphNode {
    private int id;
    private int computationTime;
    private int deadLine;
    private Set<TaskGraphEdge> prevEdges;
    private Set<TaskGraphEdge> nextEdges;

    public DefaultTaskGraphNode(int id, int computationTime) {
        this.id = id;
        this.computationTime = computationTime;
        prevEdges = new HashSet<TaskGraphEdge>();
        nextEdges = new HashSet<TaskGraphEdge>();
    }

    public DefaultTaskGraphNode(int id, TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime) {
        this(id, computationTime);

        TaskGraphEdge prevEdge = new DefaultTaskGraphEdge(prevNode, this, prevCommunicationTime);
        addPrevNode(prevEdge);
        ((DefaultTaskGraphNode) prevNode).addNextNode(prevEdge);

        TaskGraphEdge nextEdge = new DefaultTaskGraphEdge(this, nextNode, nextCommunicationTime);
        addNextNode(nextEdge);
        ((DefaultTaskGraphNode) nextNode).addPrevNode(nextEdge);
    }

    protected void addPrevNode(TaskGraphEdge prevEdge) {
        prevEdges.add(prevEdge);
    }

    protected void addNextNode(TaskGraphEdge nextEdge) {
        nextEdges.add(nextEdge);
    }

    protected void removePrevNode(TaskGraphNode prevNode) {
        Iterator<TaskGraphEdge> it = prevEdges.iterator();
        while (it.hasNext()) {
            if (it.next().getPrevNode() == prevNode) {
                it.remove();
            }
        }
    }

    protected void removeNextNode(TaskGraphNode nextNode) {
        Iterator<TaskGraphEdge> it = nextEdges.iterator();
        while (it.hasNext()) {
            if (it.next().getNextNode() == nextNode) {
                it.remove();
            }
        }
    }

    @Override
    public Set<TaskGraphNode> getPrevNodes() {
        Set<TaskGraphNode> prevNodes = new HashSet<TaskGraphNode>();
        for (TaskGraphEdge prevEdge : prevEdges) {
            prevNodes.add(prevEdge.getPrevNode());
        }
        return prevNodes;
    }

    @Override
    public Set<TaskGraphNode> getNextNodes() {
        Set<TaskGraphNode> nextNodes = new HashSet<TaskGraphNode>();
        for (TaskGraphEdge nextEdge : nextEdges) {
            nextNodes.add(nextEdge.getNextNode());
        }
        return nextNodes;
    }

    @Override
    public int getPrevNodeCount() {
        return getPrevEdges().size();
    }

    @Override
    public int getNextNodeCount() {
        return getNextEdges().size();
    }

    @Override
    public Set<TaskGraphEdge> getPrevEdges() {
        return prevEdges;
    }

    @Override
    public Set<TaskGraphEdge> getNextEdges() {
        return nextEdges;
    }

    @Override
    public TaskGraphEdge findPrevEdge(TaskGraphNode prevNode) {
        for (TaskGraphEdge edge : prevEdges) {
            if (edge.getPrevNode().equals(prevNode)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public TaskGraphEdge findNextEdge(TaskGraphNode nextNode) {
        for (TaskGraphEdge edge : nextEdges) {
            if (edge.getNextNode().equals(nextNode)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public int getPrevEdgeCount() {
        return prevEdges.size();
    }

    @Override
    public int getNextEdgeCount() {
        return nextEdges.size();
    }

    protected void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setComputationTime(int computationTime) {
        if (computationTime <= 0)
            throw new IllegalArgumentException("Computation time must be > 0!");
        this.computationTime = computationTime;
    }

    @Override
    public int getComputationTime() {
        return computationTime;
    }

    @Override
    public int compareTo(TaskGraphNode other) {
        return this.getId() - other.getId();
    }

    @Override
    public int getTopLayerCount() {
        return getTopLayerCount(this, 0);
    }

    private int getTopLayerCount(TaskGraphNode node, int layer) {
        int maxLayer = layer;
        for (TaskGraphNode prevNode : node.getPrevNodes()) {
            int result = getTopLayerCount(prevNode, layer + 1);
            if (maxLayer < result) {
                maxLayer = result;
            }
        }
        return maxLayer;
    }

    @Override
    public int getBottomLayerCount() {
        return getBottomLayerCount(this, 0);
    }

    private int getBottomLayerCount(TaskGraphNode node, int layer) {
        int maxLayer = layer;
        for (TaskGraphNode nextNode : node.getNextNodes()) {
            int result = getBottomLayerCount(nextNode, layer + 1);
            if (maxLayer < result) {
                maxLayer = result;
            }
        }
        return maxLayer;
    }

    @Override
    public int getStaticBLevel() {
        return getStaticBLevel(0, this);
    }

    private int getStaticBLevel(int currenttime, TaskGraphNode node) {
        currenttime += node.getComputationTime();
        int max = currenttime;
        if (!this.getNextEdges().isEmpty()) {
            for (TaskGraphNode currentNode : node.getNextNodes()) {
                int pathTime = getStaticBLevel(currenttime, currentNode);
                if (pathTime > max) {
                    max = pathTime;
                }
            }
        }
        return max;
    }

    @Override
    public void setDeadLine(int deadLine) {
        this.deadLine = deadLine;

    }

    @Override
    public int getDeadLine() {
        return deadLine;
    }

    @Override
    public String toString() {
        return super.toString() + "[" + getId() + "]";
    }
}
