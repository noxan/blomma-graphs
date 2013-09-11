package com.github.noxan.blommagraphs.scheduling.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.Scheduler;
import com.github.noxan.blommagraphs.scheduling.StreamScheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class DefaultStreamScheduler implements StreamScheduler {

    @Override
    public ScheduledTaskList schedule(TaskGraph taskGraph, int[] deadLines,
            SystemMetaInformation systemInfo, Scheduler scheduler) {

        TaskGraph graph = new JGraphtTaskGraph();
        for (int i = 0; i < deadLines.length; ++i) {
            graph.mergeGraph(taskGraph, graph.getFirstNode(), 1, graph.getLastNode(), 1);
        }
        return scheduler.schedule(graph, systemInfo);
    }
}
