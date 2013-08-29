package com.github.noxan.blommagraphs.generator.impl;

import com.github.noxan.blommagraphs.generator.TaskGraphGenerator;
import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;
import com.github.noxan.blommagraphs.generator.exceptions.OutOfRangeException;

public class DefaultTaskGraphGenerator implements TaskGraphGenerator {
    TaskGraph graph;
    private int numberOfNodes;
    private int minIncommingEdges;
    private int maxIncommingEdges;
    private int spreadEdges;
    private int minComputationTime;
    private int maxComputationTime;
    private int spreadComputationTime;
    private int minCommunicationTime;
    private int maxCommunicationTime;
    private int spreadCommunicationTime;

    public DefaultTaskGraphGenerator() {
        graph = new DefaultTaskGraph();

        numberOfNodes = 10;
        minIncommingEdges= 1;
        maxIncommingEdges = 2;
        spreadEdges = 1;
        minComputationTime = 1;
        maxComputationTime = 10;
        spreadComputationTime = 1;
        minCommunicationTime = 1;
        maxCommunicationTime = 10;
        spreadCommunicationTime = 1;
    }

    public void setNumberOfNodes(int numberOfNodes) throws OutOfRangeException {
        if(numberOfNodes < 0){
            this.numberOfNodes = numberOfNodes;
        } else {
            throw new OutOfRangeException();
        }
    }

    public void setMinIncommingEdges(int minIncommingEdges) {
        if(minIncommingEdges < 0){
            this.minIncommingEdges = minIncommingEdges;
        }
    }

    public void setMaxIncommingEdges(int maxIncommingEdges) {
        this.maxIncommingEdges = maxIncommingEdges;
    }

    public void setSpreadEdges(int spreadEdges) {
        this.spreadEdges = spreadEdges;
    }

    public void setMinComputationTime(int minComputationTime) {
        this.minComputationTime = minComputationTime;
    }

    public void setMaxComputationTime(int maxComputationTime) {
        this.maxComputationTime = maxComputationTime;
    }

    public void setSpreadComputationTime(int spreadComputationTime) {
        this.spreadComputationTime = spreadComputationTime;
    }

    public void setMinCommunicationTime(int minCommunicationTime) {
        this.minCommunicationTime = minCommunicationTime;
    }

    public void setMaxCommunicationTime(int maxCommunicationTime) {
        this.maxCommunicationTime = maxCommunicationTime;
    }

    public void setSpreadCommunicationTime(int spreadCommunicationTime) {
        this.spreadCommunicationTime = spreadCommunicationTime;
    }
}
