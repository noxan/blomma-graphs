package com.github.noxan.blommagraphs.generator.impl;


import java.util.ArrayList;
import java.util.Random;

import com.github.noxan.blommagraphs.generator.TaskGraphGenerator;
import com.github.noxan.blommagraphs.generator.exceptions.BoundaryConflictException;
import com.github.noxan.blommagraphs.generator.exceptions.GeneratorException;
import com.github.noxan.blommagraphs.generator.exceptions.OutOfRangeException;
import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.DuplicateEdgeException;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;
import com.github.noxan.blommagraphs.graphs.meta.TaskGraphMetaInformation;
import com.github.noxan.blommagraphs.graphs.meta.impl.DefaultTaskGraphMetaInformation;


/**
 * TaskgraphGenerator implementation with a random seed and parameters to restrict the randomly
 * generated Graph. Following parameters could be set through setter.
 * 
 * <ul>
 * <li>long seed - seed to generate randomly</li>
 * <li>int numberOfNodes</li>
 * <li>int minIncomingEdges</li>
 * <li>int maxIncomingEdges</li>
 * <li>int spreadEdges</li>
 * <li>int minComputationTime</li>
 * <li>int maxComputationTime</li>
 * <li>int spreadComputationTime</li>
 * <li>int minCommunicationTime</li>
 * <li>int maxCommunicationTime</li>
 * <li>int spreadCommunicationTime</li>
 * </ul>
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
    @Override
    public void setSeed(long seed) {
        this.seed = seed;
    }

    /**
     * generates a new seed to get a new graph with the same values
     */
    @Override
    public void generateSeed() {
        Random random = new Random();
        this.setSeed(random.nextLong());
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
        TaskGraph graph = new JGraphtTaskGraph(getGeneratorMetaInformation());
        Random random = new Random(seed);
        TaskGraphNode firstNode = graph.getFirstNode();
        TaskGraphNode lastNode = graph.getLastNode();
        ArrayList<TaskGraphNode> nodes = new ArrayList<TaskGraphNode>();

        // insert nodes in the first level
        int numberOfNodesFirstLevel = maxIncomingEdges
                + Math.round(random.nextFloat() * random.nextFloat() * (numberOfNodes - maxIncomingEdges));
        // minus 1 because of the root note
        if (maxIncomingEdges != 1) {
            numberOfNodesFirstLevel -= 1;
        }
        for (int i = 0; i < numberOfNodesFirstLevel; i++) {
            int computationTime = Math.round(random.nextFloat()
                    * (maxComputationTime - minComputationTime) + minComputationTime);
            int prevCommunicationTime = Math.round(random.nextFloat()
                    * (maxCommunicationTime - minCommunicationTime) + minCommunicationTime);
            int nextCommunicationTime = Math.round(random.nextFloat()
                    * (maxCommunicationTime - minCommunicationTime) + minCommunicationTime);
            nodes.add(graph.insertNode(firstNode, prevCommunicationTime, lastNode,
                    nextCommunicationTime, computationTime));
        }

        // insert rest nodes randomly
        int restNodes = numberOfNodes - numberOfNodesFirstLevel;
        for (int i = 0; i < restNodes; i++) {
            int computationTime = Math.round(random.nextFloat()
                    * (maxComputationTime - minComputationTime) + minComputationTime);
            int prevCommunicationTime = Math.round(random.nextFloat()
                    * (maxCommunicationTime - minCommunicationTime) + minCommunicationTime);
            int nextCommunicationTime = Math.round(random.nextFloat()
                    * (maxCommunicationTime - minCommunicationTime) + minCommunicationTime);

            nodes.add(graph.insertNode(nodes.get(random.nextInt(nodes.size())),
                    prevCommunicationTime, lastNode, nextCommunicationTime, computationTime));

        }

        // added edges to keep incomming edge count
        nodes.add(0, firstNode);
        int nodesSize = nodes.size();
        for (int i = numberOfNodesFirstLevel+1; i < nodesSize; i++) {
            TaskGraphNode currentNode = nodes.get(i);
            if (currentNode.getPrevEdgeCount() <= minIncomingEdges) {
                int newEdges = Math.round(random.nextFloat()
                        * (maxIncomingEdges - minIncomingEdges))
                        + (minIncomingEdges - currentNode.getPrevEdgeCount());
                for (int j = 0; j < newEdges; j++) {
                    TaskGraphNode prevNode;
                    do {
                        prevNode = nodes.get(random.nextInt(i));
                    } while (graph.containsEdge(prevNode, currentNode));
                    int communicationTime = Math.round(random.nextFloat()
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

    private TaskGraphMetaInformation getGeneratorMetaInformation() {
        TaskGraphMetaInformation metaInformation = new DefaultTaskGraphMetaInformation();

        metaInformation.setMetaInformation("generatorName", "DefaultTaskGraphGenerator");
        metaInformation.setMetaInformation("generatorVersion", "0.1.0");
        metaInformation.setMetaInformation("seed", seed);
        metaInformation.setMetaInformation("numberOfNodes", numberOfNodes);
        metaInformation.setMetaInformation("minIncomingEdges", minIncomingEdges);
        metaInformation.setMetaInformation("maxIncomingEdges", maxIncomingEdges);
        metaInformation.setMetaInformation("spreadEdges", spreadEdges);
        metaInformation.setMetaInformation("minComputationTime", minComputationTime);
        metaInformation.setMetaInformation("maxComputationTime", maxComputationTime);
        metaInformation.setMetaInformation("spreadComputationTime", spreadComputationTime);
        metaInformation.setMetaInformation("minCommunicationTime", minCommunicationTime);
        metaInformation.setMetaInformation("maxCommunicationTime", maxCommunicationTime);
        metaInformation.setMetaInformation("spreadCommunicationTime", spreadCommunicationTime);

        return metaInformation;
    }
}
