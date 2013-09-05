package com.github.noxan.blommagraphs.graphs.impl;


import java.util.HashSet;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class DefaultTaskGraphNode implements TaskGraphNode {
    private Set<TaskGraphEdge> prevEdges;
    private Set<TaskGraphEdge> nextEdges;

    public DefaultTaskGraphNode() {
        prevEdges = new HashSet<TaskGraphEdge>();
        nextEdges = new HashSet<TaskGraphEdge>();
    }

    @Override
    public Set<TaskGraphNode> getPrevNodes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<TaskGraphNode> getNextNodes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getPrevNodeCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNextNodeCount() {
        // TODO Auto-generated method stub
        return 0;
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

    @Override
    public int getId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getComputationTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int compareTo(TaskGraphNode o) {
        // TODO Auto-generated method stub
        return 0;
    }
}
