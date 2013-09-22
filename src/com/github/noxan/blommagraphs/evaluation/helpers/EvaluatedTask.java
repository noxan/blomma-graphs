package com.github.noxan.blommagraphs.evaluation.helpers;


import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class EvaluatedTask implements Comparable<EvaluatedTask> {
    private long startTime;
    private long computationTime;
    private long communicationTime;
    private int cpuId;
    private TaskGraphNode task;

    // private long communicationTime;

    public EvaluatedTask(long startTime, long computationTime, long communicationTime, int cpuId,
            TaskGraphNode task) {
        this.startTime = startTime;
        this.computationTime = computationTime;
        this.communicationTime = communicationTime;
        this.cpuId = cpuId;
        this.task = task;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getComputationTime() {
        return computationTime;
    }

    public long getCommunicationTime() {
        return communicationTime;
    }

    public int getCpuId() {
        return cpuId;
    }

    public TaskGraphNode getTask() {
        return task;
    }

    @Override
    public int compareTo(EvaluatedTask other) {
        return (int) (startTime - other.getStartTime());
    }
}
