package com.github.noxan.blommagraphs.evaluation;


import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;


public interface ScheduleSimulator {
    public ScheduledTaskList simulateExecution(ScheduledTaskList scheduledTaskList);
}
