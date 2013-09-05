package com.github.noxan.blommagraphs.graphs.impl;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class DefaultTaskGraphNode implements TaskGraphNode {
    private int id;
    private int computationTime;
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
        addPrevNode(prevNode, prevCommunicationTime);
        addNextNode(nextNode, nextCommunicationTime);
    }

    protected void addPrevNode(TaskGraphNode prevNode, int prevCommunicationTime) {
        TaskGraphEdge prevEdge = new DefaultTaskGraphEdge(prevNode, this, prevCommunicationTime);
        prevEdges.add(prevEdge);
    }

    protected void addNextNode(TaskGraphNode nextNode, int nextCommunicationTime) {
        TaskGraphEdge nextEdge = new DefaultTaskGraphEdge(this, nextNode, nextCommunicationTime);
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
    public int getComputationTime() {
        return computationTime;
    }

    @Override
    public int compareTo(TaskGraphNode other) {
        return this.getId() - other.getId();
    }
}
