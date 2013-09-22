package com.github.noxan.blommagraphs.scheduling.stream;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public interface StreamScheduler {
    public ScheduledTaskList schedule(TaskGraph taskGraphs[], SystemMetaInformation systemInfo);
    
    public String getName();
}
