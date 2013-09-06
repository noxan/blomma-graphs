package com.github.noxan.blommagraphs.scheduling;


public interface ScheduledTaskList {
    public boolean isTaskOnProcessor(int processorId, int taskId);
}
