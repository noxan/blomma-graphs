package com.github.noxan.blommagraphs.scheduling.stream.impl;


import java.util.ArrayList;
import java.util.List;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.stream.StreamScheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class CustomStreamScheduler implements StreamScheduler {
    @Override
    public ScheduledTaskList schedule(TaskGraph[] taskGraphs, SystemMetaInformation systemInfo,
            Scheduler scheduler) {
        List<TaskGraphNode> readyList = initializeReadyList(taskGraphs);
        ScheduledTaskList scheduledTaskList = new DefaultScheduledTaskList(
                systemInfo.getProcessorCount());

        return null;
    }

    private List<TaskGraphNode> initializeReadyList(TaskGraph[] taskGraphs) {
        List<TaskGraphNode> readyList = new ArrayList<TaskGraphNode>();

        for (TaskGraph taskGraph : taskGraphs) {
            readyList.add(taskGraph.getFirstNode());
        }

        return readyList;
    }

    private void updateReadyList(List<TaskGraphNode> readyList,
            ScheduledTaskList scheduledTaskList, TaskGraphNode lastScheduledNode) {
        for (TaskGraphNode nextNode : lastScheduledNode.getNextNodes()) {
            boolean isReady = true;
            for (TaskGraphNode dependency : nextNode.getPrevNodes()) {
                if (!scheduledTaskList.contains(dependency)) {
                    isReady = false;
                    break;
                }
            }
            if (isReady) {
                readyList.add(nextNode);
            }
        }
    }
}
