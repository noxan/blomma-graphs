package com.github.noxan.blommagraphs.scheduling;


import java.util.List;


public interface ScheduledTaskList extends List<ScheduledTask> {
    public int getProcessorCount();

    public boolean isTaskOnProcessor(int processorId, int taskId);

    public ScheduledTask getLastScheduledTaskOnProcessor(int processorId);

    public int getFinishTime();

    public boolean containsTask(int taskId);
}
