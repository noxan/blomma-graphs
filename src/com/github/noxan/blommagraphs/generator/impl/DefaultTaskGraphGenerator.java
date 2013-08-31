package com.github.noxan.blommagraphs.generator.impl;


import java.util.ArrayList;
import java.util.Random;

import com.github.noxan.blommagraphs.generator.TaskGraphGenerator;
import com.github.noxan.blommagraphs.generator.exceptions.BoundaryConflictException;
import com.github.noxan.blommagraphs.generator.exceptions.GeneratorException;
import com.github.noxan.blommagraphs.generator.exceptions.OutOfRangeException;
import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;


/**
 *
 */
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

    /**
     *
     */
    public DefaultTaskGraphGenerator() {
        graph = new DefaultTaskGraph();

        numberOfNodes = 10;
        minIncommingEdges = 1;
        maxIncommingEdges = 2;
        spreadEdges = 1;
        minComputationTime = 1;
        maxComputationTime = 10;
        spreadComputationTime = 1;
        minCommunicationTime = 1;
        maxCommunicationTime = 10;
        spreadCommunicationTime = 1;
    }

    /**
     * 
     * @param numberOfNodes
     * @throws OutOfRangeException
     */
    @Override
    public void setNumberOfNodes(int numberOfNodes) throws OutOfRangeException {
        if (numberOfNodes >= 0) {
            this.numberOfNodes = numberOfNodes;
        } else {
            throw new OutOfRangeException();
        }
    }

    /**
     * 
     * @param minIncommingEdges
     * @throws GeneratorException
     */
    @Override
    public void setMinIncommingEdges(int minIncommingEdges) throws GeneratorException {
        if (minIncommingEdges == 1) {
            if (minIncommingEdges <= this.maxIncommingEdges) {
                this.minIncommingEdges = minIncommingEdges;
            } else {
                throw new BoundaryConflictException();
            }
        } else {
            throw new OutOfRangeException();
        }
    }

    /**
     * 
     * @param maxIncommingEdges
     * @throws GeneratorException
     */
    @Override
    public void setMaxIncommingEdges(int maxIncommingEdges) throws GeneratorException {
        if (maxIncommingEdges > 0) {
            if (maxIncommingEdges >= this.minIncommingEdges) {
                this.maxIncommingEdges = maxIncommingEdges;
            } else {
                throw new BoundaryConflictException();
            }
        } else {
            throw new OutOfRangeException();
        }
    }

    /**
     * 
     * @param spreadEdges
     * @throws OutOfRangeException
     */
    @Override
    public void setSpreadEdges(int spreadEdges) throws OutOfRangeException {
        if (spreadEdges >= 0) {
            this.spreadEdges = spreadEdges;
        } else {
            throw new OutOfRangeException();
        }
    }

    /**
     * 
     * @param minComputationTime
     * @throws GeneratorException
     */
    @Override
    public void setMinComputationTime(int minComputationTime) throws GeneratorException {
        if (minComputationTime > 0) {
            if (minComputationTime <= this.maxCommunicationTime) {
                this.minCommunicationTime = minComputationTime;
            } else {
                throw new BoundaryConflictException();
            }
        } else {
            throw new OutOfRangeException();
        }
    }

    /**
     * 
     * @param maxComputationTime
     * @throws GeneratorException
     */
    @Override
    public void setMaxComputationTime(int maxComputationTime) throws GeneratorException {
        if (maxComputationTime > 0) {
            if (maxComputationTime < minComputationTime) {
                throw new BoundaryConflictException();
            } else {
                this.maxComputationTime = maxComputationTime;
            }
        } else {
            throw new OutOfRangeException();
        }
    }

    /**
     * 
     * @param spreadComputationTime
     */
    @Override
    public void setSpreadComputationTime(int spreadComputationTime) {
        // coming soon...
        this.spreadComputationTime = spreadComputationTime;
    }

    /**
     * 
     * @param minCommunicationTime
     * @throws GeneratorException
     */
    @Override
    public void setMinCommunicationTime(int minCommunicationTime) throws GeneratorException {
        if (minCommunicationTime >= 0) {
            if (minCommunicationTime > maxCommunicationTime) {
                throw new BoundaryConflictException();
            } else {
                this.minCommunicationTime = minCommunicationTime;
            }
        } else {
            throw new OutOfRangeException();
        }
    }

    /**
     * 
     * @param maxCommunicationTime
     * @throws GeneratorException
     */
    @Override
    public void setMaxCommunicationTime(int maxCommunicationTime) throws GeneratorException {
        if (maxCommunicationTime >= 0) {
            if (maxCommunicationTime < minCommunicationTime) {
                throw new BoundaryConflictException();
            } else {
                this.maxCommunicationTime = maxCommunicationTime;
            }
        } else {
            throw new OutOfRangeException();
        }
    }

    /**
     * 
     * @param spreadCommunicationTime
     */
    @Override
    public void setSpreadCommunicationTime(int spreadCommunicationTime) {
        // coming soon...
        this.spreadCommunicationTime = spreadCommunicationTime;
    }

    /**
     *
     */
    @Override
    public TaskGraph generator() {
        TaskGraphNode firstNode = graph.getFirstNode();
        TaskGraphNode lastNode = graph.getLastNode();
        ArrayList<TaskGraphNode> nodes = new ArrayList<TaskGraphNode>();
        // nodes.add(...);

        int numberOfNodesFirstLevel = maxIncommingEdges
                + (int) Math.round(Math.random() * (numberOfNodes - maxIncommingEdges));
        for (int i = 0; i < numberOfNodesFirstLevel; i++) {
            int computationTime = (int) Math.round(Math.random()
                    * (maxComputationTime - minComputationTime) + minComputationTime);
            int prevCommunicationTime = (int) Math.round(Math.random()
                    * (maxCommunicationTime - minCommunicationTime) + minCommunicationTime);
            int nextCommunicationTime = (int) Math.round(Math.random()
                    * (maxCommunicationTime - minCommunicationTime) + minCommunicationTime);

            nodes.add(graph.insertNode(firstNode, prevCommunicationTime, lastNode,
                    nextCommunicationTime, computationTime));
        }

        Random random = new Random();
        int restNodes = numberOfNodes - numberOfNodesFirstLevel;
        for (int i = 0; i < restNodes; i++) {
            int computationTime = (int) Math.round(Math.random()
                    * (maxComputationTime - minComputationTime) + minComputationTime);
            int prevCommunicationTime = (int) Math.round(Math.random()
                    * (maxCommunicationTime - minCommunicationTime) + minCommunicationTime);
            int nextCommunicationTime = (int) Math.round(Math.random()
                    * (maxCommunicationTime - minCommunicationTime) + minCommunicationTime);

            nodes.add(graph.insertNode(nodes.get(random.nextInt(nodes.size())),
                    prevCommunicationTime, lastNode, nextCommunicationTime, computationTime));

        }
        int nodesSize = nodes.size();
        for (int i = numberOfNodesFirstLevel; i < nodesSize; i++) {
            TaskGraphNode currentNode = nodes.get(i);
            if (currentNode.getPrevEdgeCount() < minIncommingEdges) {
                int newEdges = (int) Math.round(Math.random()
                        * (maxIncommingEdges - minIncommingEdges))
                        - currentNode.getPrevEdgeCount();
                for (int j = 0; j < newEdges; j++) {
                    TaskGraphNode prevNode;
                    do {
                        prevNode = nodes.get(random.nextInt(i));
                    } while (graph.containsEdge(currentNode, prevNode));
                    int communicationTime = (int) Math.round(Math.random()
                            * (maxCommunicationTime - minCommunicationTime) + minCommunicationTime);
                    graph.insertEdge(prevNode, currentNode, communicationTime);
                }
            }
        }
        return graph;
    }

}
