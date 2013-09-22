package com.github.noxan.blommagraphs.evaluation.helpers;


import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;


public class EvaluatedTask implements ScheduledTask {
    private int startTime;
    private int computationTime;
    private int communicationTime;
    private int cpuId;
    private TaskGraphNode task;

    // private long communicationTime;

    public EvaluatedTask(long startTime, long computationTime, long communicationTime, int cpuId,
            TaskGraphNode task) {
        this.startTime = (int) startTime;
        this.computationTime = (int) computationTime;
        this.communicationTime = (int) communicationTime;
        this.cpuId = cpuId;
        this.task = task;
    }

    @Override
    public int getStartTime() {
        return startTime;
    }

    @Override
    public int getComputationTime() {
        return computationTime;
    }

    @Override
    public int getCommunicationTime() {
        return communicationTime;
    }

    @Override
    public int getCpuId() {
        return cpuId;
    }

    @Override
    public TaskGraphNode getTaskGraphNode() {
        return task;
    }

    @Override
    public int getTaskId() {
        return task.getId();
    }

    @Override
    public int getFinishTime() {
        return startTime + computationTime;
    }

    @Override
    public void setCommunicationTime(int communicationTime) {
        this.communicationTime = computationTime;
    }

    @Override
    public void setCpuId(int cpuId) {
        this.cpuId = cpuId;
    }

    @Override
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    @Override
    public void setTaskGraphNode(TaskGraphNode node) {
        this.task = node;
    }

    @Override
    public int compareTo(ScheduledTask other) {
        return (int) (startTime - other.getStartTime());
    }
}
