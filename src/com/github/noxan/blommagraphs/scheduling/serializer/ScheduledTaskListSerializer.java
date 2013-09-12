package com.github.noxan.blommagraphs.scheduling.serializer;


import java.util.List;

import com.github.noxan.blommagraphs.scheduling.ScheduledTask;


public interface ScheduledTaskListSerializer {
    /**
     * Serializes a list of ScheduledTasks.
     * 
     * @param scheduledTaskList List of ScheduledTasks ordered by their starting time.
     * @return String representation of the ScheduledTask List.
     */
    public String serialize(List<ScheduledTask> scheduledTaskList);
}
