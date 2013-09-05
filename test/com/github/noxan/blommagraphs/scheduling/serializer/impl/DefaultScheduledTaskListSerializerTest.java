package com.github.noxan.blommagraphs.scheduling.serializer.impl;


import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTask;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;


public class DefaultScheduledTaskListSerializerTest {
    List<ScheduledTask> scheduledTaskList;
    TaskGraph graph;
    String expectedSerialization;
    ScheduledTaskListSerializer serializer;

    @Before
    public void setUp() throws Exception {
        scheduledTaskList = new ArrayList<>();
        serializer = new DefaultScheduledTaskListSerializer();
        graph = new JGraphtTaskGraph();
        TaskGraphNode node1 = graph.insertNode(graph.getFirstNode(), 1, graph.getLastNode(), 1, 11);
        TaskGraphNode node2 = graph.insertNode(node1, 2, graph.getLastNode(), 2, 22);
        TaskGraphNode node3 = graph.insertNode(node2, 3, graph.getLastNode(), 3, 33);

        ScheduledTask task = new DefaultScheduledTask();
        task.setStartTime(0);
        task.setTaskGraphNode(node1);
        task.setCommunicationTime(100);
        task.setCpuId(11);
        scheduledTaskList.add(task);

        task = new DefaultScheduledTask();
        task.setStartTime(0);
        task.setTaskGraphNode(node2);
        task.setCommunicationTime(200);
        task.setCpuId(22);
        scheduledTaskList.add(task);

        task = new DefaultScheduledTask();
        task.setStartTime(333);
        task.setTaskGraphNode(node3);
        task.setCommunicationTime(300);
        task.setCpuId(11);
        scheduledTaskList.add(task);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSerialize() {
        Assert.assertEquals("0 11 1\n0 22 2\n333 11 3\n", serializer.serialize(scheduledTaskList));
    }
}
