package com.github.noxan.blommagraphs.graphs.impl;


import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class DefaultTaskGraphNode implements TaskGraphNode {
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<TaskGraphEdge> getNextEdges() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getPrevEdgeCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNextEdgeCount() {
        // TODO Auto-generated method stub
        return 0;
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