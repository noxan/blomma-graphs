package com.github.noxan.blommagraphs.generator.impl;

import com.github.noxan.blommagraphs.generator.TaskGraphGenerator;
import com.github.noxan.blommagraphs.generator.exceptions.BoundaryConflictException;
import com.github.noxan.blommagraphs.generator.exceptions.GeneratorException;
import com.github.noxan.blommagraphs.generator.exceptions.OutOfRangeException;
import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;

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
        if (numberOfNodes >= 0) {
            this.numberOfNodes = numberOfNodes;
        } else {
            throw new OutOfRangeException();
        }
    }

    public void setMinIncommingEdges(int minIncommingEdges) throws GeneratorException {
        if (minIncommingEdges > 0) {
            if (minIncommingEdges <= this.maxIncommingEdges) {
                this.minIncommingEdges = minIncommingEdges;
            }
            else {
                throw new BoundaryConflictException();
            }
        }
        else {
            throw new OutOfRangeException();
        }
    }

    public void setMaxIncommingEdges(int maxIncommingEdges) throws GeneratorException{
        if (maxIncommingEdges > 0) {
            if (maxIncommingEdges >= this.minIncommingEdges) {
                this.maxIncommingEdges = maxIncommingEdges;
            }
            else {
                throw new BoundaryConflictException();
            }
        }
        else {
            throw new OutOfRangeException();
        }
    }

    public void setSpreadEdges(int spreadEdges) throws OutOfRangeException {
        if (spreadEdges >= 0) {
            this.spreadEdges = spreadEdges;
        }
        else {
            throw new OutOfRangeException();
        }
    }

    public void setMinComputationTime(int minComputationTime) throws GeneratorException {
        if (minComputationTime > 0) {
            if (minComputationTime <= this.maxCommunicationTime) {
                this.minCommunicationTime = minComputationTime;
            }
            else {
                throw new BoundaryConflictException();
            }
        }
        else {
            throw new OutOfRangeException();
        }
    }

    public void setMaxComputationTime(int maxComputationTime) throws GeneratorException{
        if (maxComputationTime > 0) {
            this.maxComputationTime = maxComputationTime;
        } else {
            throw new OutOfRangeException();
        }
        if (maxComputationTime < minComputationTime) {
            throw new BoundaryConflictException();
        }
    }

    public void setSpreadComputationTime(int spreadComputationTime) {
        //coming soon...
        this.spreadComputationTime = spreadComputationTime;
    }

    public void setMinCommunicationTime(int minCommunicationTime) throws GeneratorException {
        if (minCommunicationTime >= 0) {
            this.minCommunicationTime = minCommunicationTime;
        } else {
            throw new OutOfRangeException();
        }
        if (minCommunicationTime > maxCommunicationTime) {
            throw new BoundaryConflictException();
        }
    }

    public void setMaxCommunicationTime(int maxCommunicationTime) throws GeneratorException {
        if (maxCommunicationTime >= 0) {
            this.maxCommunicationTime = maxCommunicationTime;
        } else {
            throw new OutOfRangeException();
        }
        if (maxCommunicationTime < minCommunicationTime) {
            throw new BoundaryConflictException();
        }

    }

    public void setSpreadCommunicationTime(int spreadCommunicationTime) {
        //coming soon...
        this.spreadCommunicationTime = spreadCommunicationTime;
    }
}
