package com.github.noxan.blommagraphs.scheduling.serializer.impl;


import java.util.List;

import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;


public class DefaultScheduledTaskListSerializer implements ScheduledTaskListSerializer {

    /**
     * @return String format: startTime cpuId nodeId
     */
    @Override
    public String serialize(List<ScheduledTask> scheduledTaskList) {
        StringBuffer string = new StringBuffer();

        for (ScheduledTask task : scheduledTaskList) {
            string.append(String.format("%d %d %d\n", task.getStartTime(), task.getCpuId(),
                    task.getTaskId()));
        }
        // Add meta info here, if necessary.
        return string.toString();
    }
}
