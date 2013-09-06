package com.github.noxan.blommagraphs.scheduling.impl.last;


import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class LASTNode {
    private TaskGraphNode taskGraphNode;
    private float dNode;
    private int dEdge;
    private float strength;
    private Status status;

    public LASTNode(TaskGraphNode taskGraphNode) {
        this.taskGraphNode = taskGraphNode;
    }

    public TaskGraphNode getTaskGraphNode() {
        return taskGraphNode;
    }

    public float getdNode() {
        return dNode;
    }

    public void setdNode(float dNode) {
        this.dNode = dNode;
    }

    public int getdEdge() {
        return dEdge;
    }

    public void setdEdge(int dEdge) {
        this.dEdge = dEdge;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        FRONTIER, SCHEDULED
    }
}
