package com.github.noxan.blommagraphs.scheduling.impl.last;


import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class LASTNode {
    public enum Status {
        FRONTIER, SCHEDULED
    }

    private TaskGraphNode taskGraphNode;
    private float dNode;
    private Status status;
    private int cpuId;

    public LASTNode(TaskGraphNode taskGraphNode) {
        this.taskGraphNode = taskGraphNode;
    }

    public TaskGraphNode getTaskGraphNode() {
        return taskGraphNode;
    }

    public float getDNode() {
        return dNode;
    }

    public void setDNode(float dNode) {
        this.dNode = dNode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getCpuId() {
        return cpuId;
    }

    public void setCpuId(int cpuId) {
        this.cpuId = cpuId;
    }
}
