package com.github.noxan.blommagraphs.scheduling.stream.impl;


import java.util.ArrayList;
import java.util.List;

import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.factories.impl.DefaultTaskGraphFactory;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskListStatus;
import com.sun.jmx.remote.internal.ArrayQueue;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.serializer.impl.DefaultScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.stream.StreamScheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;


public class CustomStreamSchedulerTest {
    private ArrayList<TaskGraph> testGraphs;

    StreamScheduler customStreamScheduler = new CustomStreamScheduler();
    SystemMetaInformation systemMetaInformation = new DefaultSystemMetaInformation(2);
    ScheduledTaskListSerializer serializer = new DefaultScheduledTaskListSerializer();
    ScheduledTaskListStatus scheduledTaskListStatus;

    @Before
    public void initialize() {
        this.testGraphs = new ArrayList<>();
        this.customStreamScheduler = new CustomStreamScheduler();
        this.systemMetaInformation = new DefaultSystemMetaInformation(2);
        this.serializer = new DefaultScheduledTaskListSerializer();
        this.scheduledTaskListStatus = ScheduledTaskListStatus.VALID;


        TaskGraph taskGraph0 = new DefaultTaskGraph();
        taskGraph0.insertNode(taskGraph0.getFirstNode(), 3, taskGraph0.getLastNode(), 2, 6);
        taskGraph0.insertNode(taskGraph0.getFirstNode(), 2, taskGraph0.getLastNode(), 3, 6);
        this.testGraphs.add(taskGraph0);

        TaskGraph taskGraph1 = new DefaultTaskGraph();
        taskGraph1.insertNode(taskGraph1.getFirstNode(), 3, taskGraph1.getLastNode(), 3, 11);
        taskGraph1.insertNode(taskGraph1.getFirstNode(), 3, taskGraph1.getLastNode(), 3, 7);
        this.testGraphs.add(taskGraph1);

        TaskGraph taskGraph2 = new DefaultTaskGraph();
        taskGraph2.insertNode(taskGraph2.getFirstNode(), 5, taskGraph2.getLastNode(), 7, 4);
        taskGraph2.insertNode(taskGraph2.getFirstNode(), 6, taskGraph2.getLastNode(), 8, 4);
        this.testGraphs.add(taskGraph2);
    }

    @Test
    public void testCustomStreamSchedulerNormal() {
        ArrayList<ArrayList<Integer>> deadlines = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> deadlines0 = new ArrayList<Integer>();
        deadlines0.add(15);
        deadlines0.add(30);
        deadlines0.add(45);
        deadlines.add(deadlines0);
        ArrayList<Integer> deadlines1 = new ArrayList<Integer>();
        deadlines1.add(20);
        deadlines1.add(40);
        deadlines1.add(60);
        deadlines.add(deadlines1);
        ArrayList<Integer> deadlines2 = new ArrayList<Integer>();
        deadlines2.add(20);
        deadlines2.add(40);
        deadlines2.add(60);
        deadlines.add(deadlines2);
        for (int i = 0; i < this.testGraphs.size(); i++) {
            ScheduledTaskList scheduledTaskList = this.customStreamScheduler.schedule(
                    createTaskGraphArray(deadlines.get(i),this.testGraphs.get(i)),
                    this.systemMetaInformation);
            //System.out.print(this.serializer.serialize(scheduledTaskList));
            Assert.assertEquals(this.scheduledTaskListStatus, scheduledTaskList.validate());
        }
    }

    @Test
    public void testCustomStreamSchedulerCritical() {
        ArrayList<ArrayList<Integer>> deadlines = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> deadlines0 = new ArrayList<Integer>();
        deadlines0.add(10);
        deadlines0.add(17);
        deadlines0.add(24);
        deadlines.add(deadlines0);
        ArrayList<Integer> deadlines1 = new ArrayList<Integer>();
        deadlines1.add(15);
        deadlines1.add(30);
        deadlines1.add(34);
        deadlines.add(deadlines1);
        ArrayList<Integer> deadlines2 = new ArrayList<Integer>();
        deadlines2.add(10);
        deadlines2.add(10);
        deadlines2.add(20);
        deadlines.add(deadlines2);

        for (int i = 0; i < this.testGraphs.size(); i++) {
            ScheduledTaskList scheduledTaskList = customStreamScheduler.schedule(
                    createTaskGraphArray(deadlines.get(i),this.testGraphs.get(i)),
                    this.systemMetaInformation);
            //System.out.print(this.serializer.serialize(scheduledTaskList));
            Assert.assertEquals(this.scheduledTaskListStatus, scheduledTaskList.validate());
        }
    }

    // Takes too fucking long to wait for it
    //@Test
    public void testCustomStreamSchedulerTooShort() {
        ArrayList<ArrayList<Integer>> deadlines = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> deadlines0 = new ArrayList<Integer>();
        deadlines0.add(5);
        deadlines0.add(17);
        deadlines0.add(24);
        deadlines.add(deadlines0);
        ArrayList<Integer> deadlines1 = new ArrayList<Integer>();
        deadlines1.add(15);
        deadlines1.add(18);
        deadlines1.add(34);
        deadlines.add(deadlines1);
        ArrayList<Integer> deadlines2 = new ArrayList<Integer>();
        deadlines2.add(10);
        deadlines2.add(10);
        deadlines2.add(17);
        deadlines.add(deadlines2);

        for (int i = 0; i < deadlines.size(); i++) {
            ScheduledTaskList scheduledTaskList = this.customStreamScheduler.schedule(
                    createTaskGraphArray(deadlines.get(i),this.testGraphs.get(i)),
                    this.systemMetaInformation);
            Assert.assertEquals(this.scheduledTaskListStatus, scheduledTaskList.validate());
        }
    }

    @Test
    public void testCustomStreamSchedulerGenerator() {
        TaskGraph taskGraph = DefaultTaskGraphFactory.makeGraph();

        ArrayList<Integer> deadlines = new ArrayList<Integer>();
        deadlines.add(86);
        deadlines.add(112);

        ScheduledTaskList scheduledTaskList = this.customStreamScheduler.schedule(
                createTaskGraphArray(deadlines,taskGraph),
                this.systemMetaInformation);
        //System.out.print(this.serializer.serialize(scheduledTaskList));
        Assert.assertEquals(this.scheduledTaskListStatus, scheduledTaskList.validate());
    }

    private TaskGraph[] createTaskGraphArray(List<Integer> deadlines, TaskGraph taskGraph) {
        TaskGraph[] taskGraphs = new TaskGraph[deadlines.size()];
        for (int i = 0; i < deadlines.size(); i++) {
            TaskGraph taskGraphClone = taskGraph.clone();
            taskGraphClone.setDeadline(deadlines.get(i));
            taskGraphs[i] = taskGraphClone;
        }
        return taskGraphs;
    }
}
