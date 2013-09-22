package com.github.noxan.blommagraphs.scheduling;


import java.util.List;

import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public interface ScheduledTaskList extends List<ScheduledTask> {
    public int getCpuCount();

    public ScheduledTask getScheduledTask(TaskGraphNode taskGraphNode);

    public ScheduledTask getTaskById(int taskId);

    public boolean isTaskOnCpu(int cpuId, int taskId);

    public ScheduledTask getLastScheduledTaskOnCpu(int cpuId);

    public int getFinishTime();

    public boolean containsTask(int taskId);

    public boolean containsTask(TaskGraphNode node);

    public ScheduledTaskListStatus validate();

    public ScheduledTaskList getScheduledTasksOnCpu(int cpuId);
}
