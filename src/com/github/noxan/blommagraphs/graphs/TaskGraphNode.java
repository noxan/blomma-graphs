package com.github.noxan.blommagraphs.graphs;


import java.util.List;


public interface TaskGraphNode {
    public List<TaskGraphNode> getPrevNodes();

    public List<TaskGraphNode> getNextNodes();

    public int getPrevNodeCount();

    public int getNextNodeCount();

    public List<TaskGraphEdge> getPrevEdges();

    public List<TaskGraphEdge> getNextEdges();

    public int getPrevEdgeCount();

    public int getNextEdgeCount();

    public int getComputationTime();
}
