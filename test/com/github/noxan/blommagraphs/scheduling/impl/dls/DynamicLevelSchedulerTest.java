package com.github.noxan.blommagraphs.scheduling.impl.dls;

import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;



@RunWith(JUnit4.class)
public class DynamicLevelSchedulerTest {
    DynamicLevelScheduler dynamicLevelScheduler;
    TaskGraph taskGraph;

    @Before
    public void initialize() {
        dynamicLevelScheduler = new DynamicLevelScheduler();
        taskGraph = new JGraphtTaskGraph();
    }

    @Test
    public void testSchedule() {
        for (int i = 0; i < 2; i++) {
            TaskGraphNode tempNode1 = taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(), 1, 3);
            TaskGraphNode tempNode2 = taskGraph.insertNode(tempNode1, 1, taskGraph.getLastNode(), 1, 3);
            if (i == 0) {
                taskGraph.insertNode(tempNode1, 1, tempNode2, 1, 3);
            }
        }
        SystemMetaInformation systemMetaInformation = new DefaultSystemMetaInformation(2);
        ScheduledTaskList scheduledTaskList = dynamicLevelScheduler.schedule(taskGraph, systemMetaInformation);
        System.out.println();
        System.out.println("============OUTPUT===============");
        for (ScheduledTask scheduledTask : scheduledTaskList) {
            System.out.println("ST: " + scheduledTask.getStartTime() + " CPU_ID: " + scheduledTask.getCpuId() + " NODE_ID: " + scheduledTask.getTaskId());
        }
    }

    @Test
    public void testIsReadyNode() {

    }
}
