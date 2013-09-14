package com.github.noxan.blommagraphs.scheduling.stream.impl;


import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;
import com.github.noxan.blommagraphs.scheduling.stream.StreamScheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;


public class CustomStreamSchedulerTest {
    private TaskGraph[] taskGraphs;

    @Before
    public void initialize() {
        TaskGraph taskGraph = new DefaultTaskGraph();
        taskGraph.insertNode(taskGraph.getFirstNode(), 3, taskGraph.getLastNode(), 2, 5);

        List<Integer> deadlines = new ArrayList<Integer>();
        deadlines.add(5);
        deadlines.add(10);
        deadlines.add(15);

        taskGraphs = new TaskGraph[deadlines.size()];

        for (int i = 0; i < deadlines.size(); i++) {
            TaskGraph taskGraphClone = taskGraph.clone();
            taskGraphClone.getLastNode().setDeadLine(deadlines.get(i));
            taskGraphs[i] = taskGraphClone;
        }
    }

    @Test
    public void testCustomStreamScheduler() {
        StreamScheduler customStreamScheduler = new CustomStreamScheduler();
        SystemMetaInformation systemMetaInformation = new DefaultSystemMetaInformation(2);

        System.out.println(customStreamScheduler.schedule(taskGraphs, systemMetaInformation, null));
    }
}
