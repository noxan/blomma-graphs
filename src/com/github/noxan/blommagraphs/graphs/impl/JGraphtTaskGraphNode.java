package com.github.noxan.blommagraphs.graphs.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class JGraphtTaskGraphNode implements TaskGraphNode {
    private SimpleDirectedWeightedGraph<TaskGraphNode, TaskGraphEdge> graph;
    private JGraphtTaskGraph jGraphtTaskGraph;
    private int computationTime;
    private int id;
    private int layersToTop;
    private int layersToBottom;

    public JGraphtTaskGraphNode(SimpleDirectedWeightedGraph<TaskGraphNode, TaskGraphEdge> graph,
            JGraphtTaskGraph jGraphtTaskGraph, int id, int computationTime) {
        this.graph = graph;
        this.jGraphtTaskGraph = jGraphtTaskGraph;
        this.id = id;
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
        return getPrevNodes().size();
    }

    @Override
    public int getNextNodeCount() {
        return getNextNodes().size();
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
        return getPrevEdges().size();
    }

    @Override
    public int getNextEdgeCount() {
        return getNextEdges().size();
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

    @Override
    public int getTopLayerCount() {
        return getTopLayerCount(0, this);
    }

    /**
     * recursive function which counts the layers to the first node from the
     * given node
     * 
     * @param layer
     * @param vertex
     * @return
     */
    private int getTopLayerCount(int layer, TaskGraphNode vertex) {
        int max = layer;
        ArrayList<TaskGraphEdge> taskGraphEdge = new ArrayList<TaskGraphEdge>();

        taskGraphEdge.addAll(graph.incomingEdgesOf(vertex));
        for (int j = 0; j < taskGraphEdge.size(); j++) {
            if (graph.getEdgeSource(taskGraphEdge.get(j)).equals(jGraphtTaskGraph.getFirstNode())) {
                this.layersToTop = layer + 1;
            } else {
                getTopLayerCount(layer + 1, graph.getEdgeSource(taskGraphEdge.get(j)));
            }
            if (layer > this.layersToTop) {
                this.layersToTop = layer;
            }
        }
        max = this.layersToTop;
        return max;
    }

    @Override
    public int getBottomLayerCount() {
        return getBottomLayerCount(0, this);
    }

    /**
     * recursive function which counts the layers to the last node from the
     * given node
     * 
     * @param layer
     * @param vertex
     * @return
     */
    private int getBottomLayerCount(int layer, TaskGraphNode vertex) {
        int max = layer;
        ArrayList<TaskGraphEdge> taskGraphEdge = new ArrayList<TaskGraphEdge>();

        taskGraphEdge.addAll(graph.outgoingEdgesOf(vertex));
        for (int j = 0; j < taskGraphEdge.size(); j++) {
            if (graph.getEdgeTarget(taskGraphEdge.get(j)).equals(jGraphtTaskGraph.getLastNode())) {
                this.layersToBottom = layer + 1;
            } else {
                getBottomLayerCount(layer + 1, graph.getEdgeTarget(taskGraphEdge.get(j)));
            }
            if (layer > this.layersToBottom) {
                this.layersToBottom = layer;
            }
        }
        max = this.layersToBottom;
        return max;
    }
}
