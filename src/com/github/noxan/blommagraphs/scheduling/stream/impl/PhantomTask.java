package com.github.noxan.blommagraphs.scheduling.stream.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraphNode;

public class PhantomTask {
    private int cpuId;
    private TaskGraphNode taskGraphNode;
    private int gap;

    public PhantomTask(int cpuId, TaskGraphNode taskGraphNode, int gap) {
        this.cpuId = cpuId;
        this.taskGraphNode = taskGraphNode;
        this.gap = gap;
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
}
