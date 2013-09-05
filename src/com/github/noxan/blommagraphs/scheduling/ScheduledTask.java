package com.github.noxan.blommagraphs.scheduling;


import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public interface ScheduledTask {

    public void setStartTime(int startTime);

    public int getStartTime();

    public void setCpuId(int cpuId);

    public int getCpuId();

    public void setCommunicationTime(int communicationTime);

    public int getCommunicationTime();

    public int getComputationTime();

    public void setTaskGraphNode(TaskGraphNode node);

    public TaskGraphNode getTaskGraphNode();
}
