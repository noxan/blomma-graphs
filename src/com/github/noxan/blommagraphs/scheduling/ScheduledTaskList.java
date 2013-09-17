package com.github.noxan.blommagraphs.scheduling;


import java.util.List;

import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public interface ScheduledTaskList extends List<ScheduledTask> {
    public int getProcessorCount();

    public ScheduledTask getScheduledTask(TaskGraphNode taskGraphNode);

    public ScheduledTask getTaskById(int taskId);

    public boolean isTaskOnProcessor(int processorId, int taskId);

    public ScheduledTask getLastScheduledTaskOnProcessor(int processorId);

    public int getFinishTime();

    public boolean containsTask(int taskId);

    public boolean containsTask(TaskGraphNode node);
}
