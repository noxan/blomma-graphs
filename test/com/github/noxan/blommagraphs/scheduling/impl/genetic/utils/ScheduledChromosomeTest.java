package com.github.noxan.blommagraphs.scheduling.impl.genetic.utils;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTask;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.impl.genetic.chromosome.Chromosome;
import com.github.noxan.blommagraphs.scheduling.impl.genetic.chromosome.ScheduledChromosome;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.serializer.impl.ExtendedScheduledTaskListSerializer;


public class ScheduledChromosomeTest {
    private TaskGraph taskGraph;
    private ScheduledTaskList scheduledTaskList;

    @Before
    public void initialize() {
        taskGraph = new DefaultTaskGraph();

        TaskGraphNode[] nodes = new TaskGraphNode[5];
        nodes[0] = taskGraph.insertNode(taskGraph.getFirstNode(), 0, taskGraph.getLastNode(), 0, 2); // t1
        nodes[1] = taskGraph.insertNode(nodes[0], 2, taskGraph.getLastNode(), 0, 5); // t2
        nodes[2] = taskGraph.insertNode(nodes[0], 1, taskGraph.getLastNode(), 0, 3); // t3
        nodes[3] = taskGraph.insertNode(nodes[1], 3, taskGraph.getLastNode(), 0, 10); // t4
        nodes[4] = taskGraph.insertNode(nodes[2], 1, taskGraph.getLastNode(), 0, 8); // t5

        scheduledTaskList = new DefaultScheduledTaskList(2);

        scheduledTaskList.add(new DefaultScheduledTask(0, 0, 0, taskGraph.getFirstNode())); // t0
        scheduledTaskList.add(new DefaultScheduledTask(1, 0, 0, nodes[0])); // t1
        scheduledTaskList.add(new DefaultScheduledTask(3, 0, 0, nodes[1])); // t2
        scheduledTaskList.add(new DefaultScheduledTask(3, 0, 0, nodes[2])); // t3
        scheduledTaskList.add(new DefaultScheduledTask(7, 0, 0, nodes[3])); // t4
        scheduledTaskList.add(new DefaultScheduledTask(8, 1, 1, nodes[4])); // t5
        scheduledTaskList.add(new DefaultScheduledTask(16, 0, 0, taskGraph.getLastNode())); // t6
    }

    @Test
    public void testScheduledChromosomeDecode() {
        Chromosome chromosome = new ScheduledChromosome(2, taskGraph, scheduledTaskList);

        ScheduledTaskList taskList = chromosome.decode();
        Assert.assertEquals(taskList.size(), taskGraph.getNodeCount());
        Assert.assertEquals(taskList.size(), scheduledTaskList.size());

        Assert.assertEquals(taskList.getFinishTime(), 22);

        ScheduledTaskListSerializer serializer = new ExtendedScheduledTaskListSerializer();
        Assert.assertEquals(
                serializer.serialize(taskList),
                "sT\tp\ttask\tcompT\tcommT\n\n0\t0\t0\t1\t0\n1\t0\t1\t2\t0\n3\t0\t2\t5\t0\n8\t0\t3\t3\t0\n11\t0\t4\t10\t0\n12\t1\t5\t8\t1\n21\t0\t6\t1\t0\n");
    }
}
