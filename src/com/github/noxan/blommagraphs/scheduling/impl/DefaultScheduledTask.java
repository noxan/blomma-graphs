package com.github.noxan.blommagraphs.scheduling.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;


public class DefaultScheduledTask implements ScheduledTask {
    private int startTime;
    private int cpuId;
    private int communicationTime;
    private TaskGraphNode node;

    public DefaultScheduledTask() {
        this(null);
    }

    public DefaultScheduledTask(TaskGraphNode graphNode) {
        this(0, 0, 0, graphNode);
    }

    public DefaultScheduledTask(int startTime, int cpuId, int communicationTime, TaskGraphNode node) {
        setStartTime(startTime);
        setCpuId(cpuId);
        setCommunicationTime(communicationTime);
        setTaskGraphNode(node);
    }

    @Override
    public int getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    @Override
    public int getFinishTime() {
        return this.startTime + this.node.getComputationTime();
    }

    @Override
    public int getCpuId() {
        return cpuId;
    }

    @Override
    public void setCpuId(int cpuId) {
        this.cpuId = cpuId;
    }

    @Override
    public int getCommunicationTime() {
        return communicationTime;
    }

    @Override
    public void setCommunicationTime(int communicationTime) {
        this.communicationTime = communicationTime;
    }

    @Override
    public void setTaskGraphNode(TaskGraphNode node) {
        this.node = node;
    }

    @Override
    public int getComputationTime() {
        return node.getComputationTime();
    }

    @Override
    public int getTaskId() {
        return node.getId();
    }

    @Override
    public TaskGraphNode getTaskGraphNode() {
        return this.node;
    }

    public int compareTo(ScheduledTask other) {
        return getStartTime() - other.getStartTime();
    }
}
