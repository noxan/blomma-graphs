package com.github.noxan.blommagraphs.scheduling.stream.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.stream.StreamScheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class DefaultStreamScheduler implements StreamScheduler {

    @Override
    public ScheduledTaskList schedule(TaskGraph[] taskGraphs, SystemMetaInformation systemInfo,
            Scheduler scheduler) {

        TaskGraph graph = new JGraphtTaskGraph();
        for (int i = 0; i < taskGraphs.length; ++i) {
            graph.mergeGraph(taskGraphs[i], graph.getFirstNode(), 1, graph.getLastNode(), 1);
        }
        return scheduler.schedule(graph, systemInfo);
    }
}
