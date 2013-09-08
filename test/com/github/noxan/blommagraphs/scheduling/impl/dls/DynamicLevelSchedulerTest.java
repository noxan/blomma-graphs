package com.github.noxan.blommagraphs.scheduling.impl.dls;

import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;



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
        ScheduledTaskList scheduledTaskList = dynamicLevelScheduler.schedule(taskGraph, systemMetaInformation);

        System.out.println();
        System.out.println("============OUTPUT===============");
        for (ScheduledTask scheduledTask : scheduledTaskList) {
            System.out.println("ST: " + scheduledTask.getStartTime() + " CPU_ID: " + scheduledTask.getCpuId() + " NODE_ID: " + scheduledTask.getTaskId());
        }

        //ASSERT!

    }

    @Test
    public void testIsReadyNode() {
        ScheduledTaskList scheduledTaskList = dynamicLevelScheduler.schedule(taskGraph, systemMetaInformation);
        TaskGraphNode node = scheduledTaskList.getLastScheduledTaskOnProcessor(0).getTaskGraphNode();

        Assert.assertTrue(dynamicLevelScheduler.isReadyNode(node));
    }
}
