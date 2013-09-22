package com.github.noxan.blommagraphs.scheduling.serializer;


import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;


public interface ScheduledTaskListSerializer {
    /**
     * Serializes a list of ScheduledTasks.
     * 
     * @param scheduledTaskList List of ScheduledTasks ordered by their starting
     *            time.
     * @return String representation of the ScheduledTask List.
     */
    public String serialize(ScheduledTaskList scheduledTaskList);
}
