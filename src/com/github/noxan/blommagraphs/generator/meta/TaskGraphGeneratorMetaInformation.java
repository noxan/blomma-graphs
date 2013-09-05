package com.github.noxan.blommagraphs.generator.meta;


public class TaskGraphGeneratorMetaInformation {
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

    public TaskGraphGeneratorMetaInformation(long seed, int numberOfNodes, int minIncomingEdges,
            int maxIncomingEdges, int spreadEdges, int minComputationTime, int maxComputationTime,
            int spreadComputationTime, int minCommunicationTime, int maxCommunicationTime,
            int spreadCommunicationTime) {
        this.seed = seed;
        this.numberOfNodes = numberOfNodes;
        this.minIncomingEdges = minIncomingEdges;
        this.maxIncomingEdges = maxIncomingEdges;
        this.spreadEdges = spreadEdges;
        this.minComputationTime = minComputationTime;
        this.maxComputationTime = maxComputationTime;
        this.spreadComputationTime = spreadComputationTime;
        this.minCommunicationTime = minCommunicationTime;
        this.maxCommunicationTime = maxCommunicationTime;
        this.spreadCommunicationTime = spreadCommunicationTime;
    }

    public long getSeed() {
        return seed;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public int getMinIncomingEdges() {
        return minIncomingEdges;
    }

    public int getMaxIncomingEdges() {
        return maxIncomingEdges;
    }

    public int getSpreadEdges() {
        return spreadEdges;
    }

    public int getMinComputationTime() {
        return minComputationTime;
    }

    public int getMaxComputationTime() {
        return maxComputationTime;
    }

    public int getSpreadComputationTime() {
        return spreadComputationTime;
    }

    public int getMinCommunicationTime() {
        return minCommunicationTime;
    }

    public int getMaxCommunicationTime() {
        return maxCommunicationTime;
    }

    public int getSpreadCommunicationTime() {
        return spreadCommunicationTime;
    }
}
