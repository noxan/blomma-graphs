package com.github.noxan.blommagraphs.scheduling.serializer.impl;


import java.util.List;

import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;


public class ExtendedScheduledTaskListSerializer implements ScheduledTaskListSerializer {
    @Override
    public String serialize(List<ScheduledTask> scheduledTaskList) {
        StringBuffer string = new StringBuffer();

        for (ScheduledTask task : scheduledTaskList) {
            string.append(String.format("%d\t%d\t%d\t%d\t%d\n", task.getStartTime(),
                    task.getCpuId(), task.getTaskId(), task.getComputationTime(),
                    task.getCommunicationTime()));
        }
        // Add meta info here, if necessary.
        return string.toString();
    }
}
