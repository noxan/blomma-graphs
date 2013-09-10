package com.github.noxan.blommagraphs.scheduling;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public interface StreamScheduler {
    public ScheduledTaskList schedule(TaskGraph taskGraph, int graphCount,
            SystemMetaInformation systemInfo, Scheduler scheduler);
}
