package com.github.noxan.blommagraphs.scheduling.stream.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


/**
 * phantom task class to store the gap of every taskgraphnode on every cpu, its earliest start-time
 * and the communicationtime
 */
public class PhantomTask {
    private int cpuId;
    private TaskGraphNode taskGraphNode;
    private int gap;
    private int earliestStarttime;
    private int communicationTime;


    public PhantomTask(int cpuId, TaskGraphNode taskGraphNode, int gap, int earliestStarttime, int communicationTime) {
        this.cpuId = cpuId;
        this.taskGraphNode = taskGraphNode;
        this.gap = gap;
        this.earliestStarttime = earliestStarttime;
        this.communicationTime = communicationTime;
    }

    public int getCpuId() {
        return cpuId;
    }

    public TaskGraphNode getTaskGraphNode() {
        return taskGraphNode;
    }

    public int getGap() {
        return gap;
    }

    public int getEarliestStarttime() {
        return earliestStarttime;
    }

    public int getCommunicationTime() {
        return communicationTime;
    }
}
