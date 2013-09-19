package com.github.noxan.blommagraphs.scheduling.impl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskListStatus;


public class DefaultScheduledTaskListTest {
    private TaskGraph taskGraph;
    private ScheduledTaskList scheduledTaskList;

    @Before
    public void initialize() {
        taskGraph = new DefaultTaskGraph();
        TaskGraphNode task0 = taskGraph.getFirstNode();
        TaskGraphNode task3 = taskGraph.getLastNode();
        TaskGraphNode task1 = taskGraph.insertNode(task0, 3, task3, 5, 4);
        TaskGraphNode task2 = taskGraph.insertNode(task0, 5, task3, 2, 8);
        taskGraph.resetDeadLine(20);
        scheduledTaskList = new DefaultScheduledTaskList(2);
        scheduledTaskList.add(new DefaultScheduledTask(0, 0, 0, task0));
        scheduledTaskList.add(new DefaultScheduledTask(1, 0, 0, task1));
        scheduledTaskList.add(new DefaultScheduledTask(1, 1, 0, task2));
        scheduledTaskList.add(new DefaultScheduledTask(9, 0, 0, task3));
    }

    @Test
    public void testValidate() {
        Assert.assertEquals(ScheduledTaskListStatus.VALID, scheduledTaskList.validate());
    }

    @Test
    public void testValidateFails() {
        taskGraph.resetDeadLine(5);
        Assert.assertEquals(ScheduledTaskListStatus.INVALID_DEADLINE, scheduledTaskList.validate());
    }
}
