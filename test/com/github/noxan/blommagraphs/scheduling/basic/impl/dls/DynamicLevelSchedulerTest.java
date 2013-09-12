package com.github.noxan.blommagraphs.scheduling.basic.impl.dls;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.serializer.impl.DefaultScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;


@RunWith(JUnit4.class)
public class DynamicLevelSchedulerTest {
    DynamicLevelScheduler dynamicLevelScheduler;
    TaskGraph taskGraph;
    SystemMetaInformation systemMetaInformation;

    @Before
    public void initialize() {
        int numberOfCpu = 2;
        int communicationCost = 2;

        dynamicLevelScheduler = new DynamicLevelScheduler();
        taskGraph = new JGraphtTaskGraph();
        systemMetaInformation = new DefaultSystemMetaInformation(numberOfCpu);

        for (int i = 0; i < 2; i++) {
            TaskGraphNode tempNode1 = taskGraph.insertNode(taskGraph.getFirstNode(), communicationCost, taskGraph.getLastNode(), communicationCost, 3);
            TaskGraphNode tempNode2 = taskGraph.insertNode(tempNode1, communicationCost, taskGraph.getLastNode(), communicationCost, 3);
            if (i == 0) {
                taskGraph.insertNode(tempNode1, communicationCost, tempNode2, communicationCost, 3);
            }
        }
    }

    @Test
    public void testSchedule() {
        ScheduledTaskListSerializer scheduledTaskListSerializer = new DefaultScheduledTaskListSerializer();
        String expected = "0 0 0\n1 0 1\n3 1 4\n4 0 3\n6 1 5\n7 0 2\n11 0 6\n";

        Assert.assertEquals(expected, scheduledTaskListSerializer.serialize(dynamicLevelScheduler.schedule(taskGraph, systemMetaInformation)));
    }

    @Test
    public void testIsReadyNode() {
        ScheduledTaskList scheduledTaskList = dynamicLevelScheduler.schedule(taskGraph, systemMetaInformation);
        TaskGraphNode node = scheduledTaskList.getLastScheduledTaskOnProcessor(0).getTaskGraphNode();

        Assert.assertTrue(dynamicLevelScheduler.isReadyNode(node));
    }
}
