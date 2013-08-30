package com.github.noxan.blommagraphs.graphs.impl;


import org.jgrapht.graph.DefaultWeightedEdge;

import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class JGraphtTaskGraphEdge extends DefaultWeightedEdge implements TaskGraphEdge {
    private static final long serialVersionUID = -4219328248276314926L;

    @Override
    public TaskGraphNode getPrevNode() {
        return (TaskGraphNode) getSource();
    }

    @Override
    public TaskGraphNode getNextNode() {
        return (TaskGraphNode) getTarget();
    }

    @Override
    public int getCommunicationTime() {
        return (int) getWeight();
    }

}
