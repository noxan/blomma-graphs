package com.github.noxan.blommagraphs.graphs;


import java.util.Set;


public interface TaskGraphNode {
    public Set<TaskGraphNode> getPrevNodes();

    public Set<TaskGraphNode> getNextNodes();

    public int getPrevNodeCount();

    public int getNextNodeCount();

    public Set<TaskGraphEdge> getPrevEdges();

    public Set<TaskGraphEdge> getNextEdges();

    public int getPrevEdgeCount();

    public int getNextEdgeCount();

    public int getComputationTime();
}
