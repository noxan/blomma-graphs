package com.github.noxan.blommagraphs.scheduling.basic.impl.last;


import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTask;


public class LASTNode extends DefaultScheduledTask {
    public enum Status {
        FRONTIER, SCHEDULED
    }

    private float dNode;
    private Status status;

    public LASTNode(TaskGraphNode taskGraphNode) {
        super(taskGraphNode);
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
}
