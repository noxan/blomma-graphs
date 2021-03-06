package com.github.noxan.blommagraphs.scheduling.serializer.impl;


import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;


public class DefaultScheduledTaskListSerializer implements ScheduledTaskListSerializer {

    /**
     * @return String format: startTime cpuId nodeId
     */
    @Override
    public String serialize(ScheduledTaskList scheduledTaskList) {
        StringBuffer stringBuilder = new StringBuffer();

        for (ScheduledTask task : scheduledTaskList) {
            stringBuilder.append(String.format("%d %d %d\n", task.getStartTime(), task.getCpuId(),
                    task.getTaskId()));
        }
        // Add meta info here, if necessary.
        return stringBuilder.toString();
    }
}
