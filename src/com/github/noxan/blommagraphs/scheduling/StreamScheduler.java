package com.github.noxan.blommagraphs.scheduling;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public interface StreamScheduler {
    public ScheduledTaskList schedule(TaskGraph taskGraphs[], SystemMetaInformation systemInfo,
            Scheduler scheduler);
}
