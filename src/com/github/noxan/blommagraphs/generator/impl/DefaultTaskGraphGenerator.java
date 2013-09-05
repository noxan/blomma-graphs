package com.github.noxan.blommagraphs.generator.impl;


import com.github.noxan.blommagraphs.generator.TaskGraphGenerator;
import com.github.noxan.blommagraphs.generator.exceptions.BoundaryConflictException;
import com.github.noxan.blommagraphs.generator.exceptions.GeneratorException;
import com.github.noxan.blommagraphs.generator.exceptions.OutOfRangeException;
import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.DuplicateEdgeException;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;

import java.util.ArrayList;
import java.util.Random;


/**
 * TaskgraphGenerator implementation with a random seed and parameters to restrict the randomly
 * generated Graph. Following parameters could be set through setter.
 *
 * long seed - seed to generate randomly
 * int numberOfNodes
 * int minIncomingEdges
 * int maxIncomingEdges
 * int spreadEdges
 * int minComputationTime
 * int maxComputationTime
 * int spreadComputationTime
 * int minCommunicationTime
 * int maxCommunicationTime
 * int spreadCommunicationTime
 */
public class DefaultTaskGraphGenerator implements TaskGraphGenerator {
    private long seed;
    private int numberOfNodes;
    private int minIncomingEdges;
    private int maxIncomingEdges;
    private int spreadEdges;
    private int minComputationTime;
    private int maxComputationTime;
    private int spreadComputationTime;
    private int minCommunicationTime;
    private int maxCommunicationTime;
    private int spreadCommunicationTime;

    /**
     * Constructor with default values and random seed
     */
    public DefaultTaskGraphGenerator() {

        Random random = new Random();
        seed = random.nextLong();

        numberOfNodes = 10;
        minIncomingEdges = 1;
        maxIncomingEdges = 2;
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
     * @param seed
     */
    public void setSeed(long seed) {
        this.seed = seed;
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
     * @param minIncomingEdges
     * @throws GeneratorException
     */
    @Override
    public void setMinIncomingEdges(int minIncomingEdges) throws BoundaryConflictException,
            OutOfRangeException {
        if (minIncomingEdges >= 1) {
            if (minIncomingEdges <= this.maxIncomingEdges) {
                this.minIncomingEdges = minIncomingEdges;
            } else {
                throw new BoundaryConflictException();
            }
        } else {
            throw new OutOfRangeException();
        }
    }

    /**
     * 
     * @param maxIncomingEdges
     * @throws GeneratorException
     */
    @Override
    public void setMaxIncomingEdges(int maxIncomingEdges) throws BoundaryConflictException,
            OutOfRangeException {
        if (maxIncomingEdges > 0) {
            if (maxIncomingEdges >= this.minIncomingEdges) {
                this.maxIncomingEdges = maxIncomingEdges;
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
    public void setMinComputationTime(int minComputationTime) throws BoundaryConflictException,
            OutOfRangeException {
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
    public void setMaxComputationTime(int maxComputationTime) throws BoundaryConflictException,
            OutOfRangeException {
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
    public void setMinCommunicationTime(int minCommunicationTime) throws BoundaryConflictException,
            OutOfRangeException {
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
    public void setMaxCommunicationTime(int maxCommunicationTime) throws BoundaryConflictException,
            OutOfRangeException {
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
     * Graphgenerator generates Graph depending to the setted values and seed
     */
    @Override
    public TaskGraph generator() {
        TaskGraph graph = new JGraphtTaskGraph();
        Random random = new Random(seed);
        TaskGraphNode firstNode = graph.getFirstNode();
        TaskGraphNode lastNode = graph.getLastNode();
        ArrayList<TaskGraphNode> nodes = new ArrayList<TaskGraphNode>();

        //insert nodes in the first level
        int numberOfNodesFirstLevel = maxIncomingEdges
                + (int) Math.round(random.nextFloat() * (numberOfNodes - maxIncomingEdges));
        for (int i = 0; i < numberOfNodesFirstLevel; i++) {
            int computationTime = (int) Math.round(random.nextFloat()
                    * (maxComputationTime - minComputationTime) + minComputationTime);
            int prevCommunicationTime = (int) Math.round(random.nextFloat()
                    * (maxCommunicationTime - minCommunicationTime) + minCommunicationTime);
            int nextCommunicationTime = (int) Math.round(random.nextFloat()
                    * (maxCommunicationTime - minCommunicationTime) + minCommunicationTime);
            nodes.add(graph.insertNode(firstNode, prevCommunicationTime, lastNode,
                    nextCommunicationTime, computationTime));
        }

        //insert rest nodes randomly
        int restNodes = numberOfNodes - numberOfNodesFirstLevel;
        for (int i = 0; i < restNodes; i++) {
            int computationTime = (int) Math.round(random.nextFloat()
                    * (maxComputationTime - minComputationTime) + minComputationTime);
            int prevCommunicationTime = (int) Math.round(random.nextFloat()
                    * (maxCommunicationTime - minCommunicationTime) + minCommunicationTime);
            int nextCommunicationTime = (int) Math.round(random.nextFloat()
                    * (maxCommunicationTime - minCommunicationTime) + minCommunicationTime);

            nodes.add(graph.insertNode(nodes.get(random.nextInt(nodes.size())),
                    prevCommunicationTime, lastNode, nextCommunicationTime, computationTime));

        }

        //added edges to keep incomming edge count
        int nodesSize = nodes.size();
        for (int i = numberOfNodesFirstLevel; i < nodesSize; i++) {
            TaskGraphNode currentNode = nodes.get(i);
            if (currentNode.getPrevEdgeCount() <= minIncomingEdges) {
                int newEdges = (int) Math.round(random.nextFloat()
                        * (maxIncomingEdges - minIncomingEdges))
                        + (minIncomingEdges - currentNode.getPrevEdgeCount());
                for (int j = 0; j < newEdges; j++) {
                    TaskGraphNode prevNode;
                    do {
                        prevNode = nodes.get(random.nextInt(i));
                    } while (graph.containsEdge(prevNode, currentNode));
                    int communicationTime = (int) Math.round(random.nextFloat()
                            * (maxCommunicationTime - minCommunicationTime) + minCommunicationTime);
                    try {
                        graph.insertEdge(prevNode, currentNode, communicationTime);
                    } catch (DuplicateEdgeException e) {
                        // Should not be possible because we checked it before.
                        e.printStackTrace();
                    }
                }
            }
        }
        return graph;
    }
}
