package com.github.noxan.blommagraphs.graphs.impl;


import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class JGraphtTaskGraphNode implements TaskGraphNode {
    private SimpleDirectedWeightedGraph<TaskGraphNode, TaskGraphEdge> graph;
    private int computationTime;
    private int id;

    public JGraphtTaskGraphNode(SimpleDirectedWeightedGraph<TaskGraphNode, TaskGraphEdge> graph,
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
        Set<TaskGraphEdge> prevEdges = graph.incomingEdgesOf(this);
        Set<TaskGraphNode> prevNodes = new HashSet<TaskGraphNode>();

        for (TaskGraphEdge prevEdge : prevEdges) {
            prevNodes.add(prevEdge.getPrevNode());
        }

        return prevNodes;
    }

    @Override
    public Set<TaskGraphNode> getNextNodes() {
        Set<TaskGraphEdge> nextEdges = graph.outgoingEdgesOf(this);
        Set<TaskGraphNode> nextNodes = new HashSet<TaskGraphNode>();

        for (TaskGraphEdge prevEdge : nextEdges) {
            nextNodes.add(prevEdge.getNextNode());
        }

        return nextNodes;
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
    public int getId() {
        return id;
    }

    @Override
    public int getComputationTime() {
        return computationTime;
    }

}
