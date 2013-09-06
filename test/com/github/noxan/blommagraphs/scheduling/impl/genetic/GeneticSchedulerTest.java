package com.github.noxan.blommagraphs.scheduling.impl.genetic;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.Scheduler;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTask;
import com.github.noxan.blommagraphs.scheduling.system.AbstractSystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class GeneticSchedulerTest {
    private TaskGraph taskGraph;
    private SystemMetaInformation metaInformation;

    private Scheduler scheduler;

    @Before
    public void initialize() {
        taskGraph = new DefaultTaskGraph();

        TaskGraphNode[] nodes = new TaskGraphNode[5];
        nodes[0] = taskGraph.insertNode(taskGraph.getFirstNode(), 0, taskGraph.getLastNode(), 0, 2);
        nodes[1] = taskGraph.insertNode(nodes[0], 2, taskGraph.getLastNode(), 0, 5);
        nodes[2] = taskGraph.insertNode(nodes[0], 1, taskGraph.getLastNode(), 0, 3);
        nodes[3] = taskGraph.insertNode(nodes[1], 3, taskGraph.getLastNode(), 0, 10);
        nodes[4] = taskGraph.insertNode(nodes[2], 1, taskGraph.getLastNode(), 0, 8);

        List<ScheduledTask> taskList = new ArrayList<ScheduledTask>();

        taskList.add(new DefaultScheduledTask(0, 0, 0, nodes[0]));
        taskList.add(new DefaultScheduledTask(1, 0, 0, nodes[1]));
        taskList.add(new DefaultScheduledTask(5, 0, 0, nodes[2]));
        taskList.add(new DefaultScheduledTask(6, 0, 0, nodes[3]));
        taskList.add(new DefaultScheduledTask(7, 1, 0, nodes[4]));

        metaInformation = new AbstractSystemMetaInformation(2) {
        };

        scheduler = new GeneticScheduler(taskList);
    }

    @Test
    public void testGeneticScheduler() {
        List<ScheduledTask> scheduledTasks = scheduler.schedule(metaInformation, taskGraph);

        Assert.assertEquals(scheduledTasks.size(), taskGraph.getNodeCount() - 2);
    }
}
