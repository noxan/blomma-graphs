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
        this.node = graphNode;
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

    public TaskGraphNode getTaskGraphNode() {
        return node;
    }

    @Override
    public int getComputationTime() {
        return node.getComputationTime();
    }

    @Override
    public int getTaskId() {
        return node.getId();
    }

}
