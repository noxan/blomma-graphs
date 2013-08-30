package com.github.noxan.blommagraphs.graphs.impl;


import java.util.Set;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class JGraphtTaskGraphNode implements TaskGraphNode {
    private DefaultDirectedWeightedGraph<TaskGraphNode, TaskGraphEdge> graph;
    private int computationTime;

    public JGraphtTaskGraphNode(DefaultDirectedWeightedGraph<TaskGraphNode, TaskGraphEdge> graph,
            int computationTime) {
        this.graph = graph;
        if (computationTime <= 0) {
            this.computationTime = 1;
        } else {
            this.computationTime = computationTime;
        }
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
        return graph.incomingEdgesOf(this);
    }

    @Override
    public Set<TaskGraphEdge> getNextEdges() {
        return graph.outgoingEdgesOf(this);
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
    public int getComputationTime() {
        return computationTime;
    }

}
