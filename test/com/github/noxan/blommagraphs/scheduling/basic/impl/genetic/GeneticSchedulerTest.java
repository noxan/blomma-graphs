package com.github.noxan.blommagraphs.scheduling.basic.impl.genetic;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTask;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.serializer.impl.ExtendedScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;


public class GeneticSchedulerTest {
    private TaskGraph taskGraph;
    private SystemMetaInformation metaInformation;

    private Scheduler scheduler;

    @Before
    public void initialize() {
        taskGraph = new DefaultTaskGraph();

        TaskGraphNode[] nodes = new TaskGraphNode[5];
        nodes[0] = taskGraph.insertNode(taskGraph.getFirstNode(), 0, taskGraph.getLastNode(), 0, 2); // t1
        nodes[1] = taskGraph.insertNode(nodes[0], 2, taskGraph.getLastNode(), 0, 5); // t2
        nodes[2] = taskGraph.insertNode(nodes[0], 1, taskGraph.getLastNode(), 0, 3); // t3
        nodes[3] = taskGraph.insertNode(nodes[1], 3, taskGraph.getLastNode(), 0, 10); // t4
        nodes[4] = taskGraph.insertNode(nodes[2], 1, taskGraph.getLastNode(), 0, 8); // t5

        // STGSerializer serializer = new STGSerializer();
        // System.out.println(serializer.serialize(taskGraph));

        ScheduledTaskList taskList = new DefaultScheduledTaskList(2);

        taskList.add(new DefaultScheduledTask(0, 0, 0, taskGraph.getFirstNode())); // t0
        taskList.add(new DefaultScheduledTask(1, 0, 0, nodes[0])); // t1
        taskList.add(new DefaultScheduledTask(3, 0, 0, nodes[1])); // t2
        taskList.add(new DefaultScheduledTask(3, 0, 0, nodes[2])); // t3
        taskList.add(new DefaultScheduledTask(7, 0, 0, nodes[3])); // t4
        taskList.add(new DefaultScheduledTask(8, 1, 1, nodes[4])); // t5
        taskList.add(new DefaultScheduledTask(16, 0, 0, taskGraph.getLastNode())); // t6

        metaInformation = new DefaultSystemMetaInformation(2);

        scheduler = new GeneticScheduler(taskList);
    }

    @Test
    public void testGeneticScheduler() {
        ScheduledTaskList scheduledTasks = scheduler.schedule(taskGraph, metaInformation);

        ScheduledTaskListSerializer scheduledSerializer = new ExtendedScheduledTaskListSerializer();
        System.out.println(scheduledSerializer.serialize(scheduledTasks));

        Assert.assertEquals(scheduledTasks.size(), taskGraph.getNodeCount());
    }
}
