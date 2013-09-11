package com.github.noxan.blommagraphs.scheduling.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.Scheduler;
import com.github.noxan.blommagraphs.scheduling.StreamScheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class DefaultStreamScheduler implements StreamScheduler {

    @Override
    public ScheduledTaskList schedule(TaskGraph taskGraph, int graphCount, int[] deadlines,
            SystemMetaInformation systemInfo, Scheduler scheduler) {

        TaskGraph graph = new DefaultTaskGraph();
        for (int i = 0; i < graphCount; ++i) {
            graph.mergeGraph(taskGraph, graph.getFirstNode(), 1, graph.getLastNode(), 1);
        }
        return scheduler.schedule(graph, systemInfo);
    }
}
