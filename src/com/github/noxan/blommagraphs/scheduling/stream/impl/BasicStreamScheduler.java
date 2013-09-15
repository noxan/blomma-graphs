package com.github.noxan.blommagraphs.scheduling.stream.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.stream.StreamScheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class BasicStreamScheduler implements StreamScheduler {
    Scheduler scheduler;

    public BasicStreamScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public ScheduledTaskList schedule(TaskGraph[] taskGraphs, SystemMetaInformation systemInfo) {

        TaskGraph graph = new DefaultTaskGraph();
        for (int i = 0; i < taskGraphs.length; ++i) {
            graph.mergeGraph(taskGraphs[i], graph.getFirstNode(), 1, graph.getLastNode(), 1);
        }
        graph.deleteEdge(graph.getFirstNode(), graph.getLastNode());
        return scheduler.schedule(graph, systemInfo);
    }
}
