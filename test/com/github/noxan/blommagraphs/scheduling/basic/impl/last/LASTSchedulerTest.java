package com.github.noxan.blommagraphs.scheduling.basic.impl.last;


import junit.framework.Assert;

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
public class LASTSchedulerTest {
    private LASTScheduler lastScheduler;
    private SystemMetaInformation systemInformation;
    private TaskGraph graph;
    private TaskGraphNode graphNodes[];
    private LASTNode lastNodes[];

    @Before
    public void initialize() {
        systemInformation = new DefaultSystemMetaInformation(2);

        lastScheduler = new LASTScheduler();
        graphNodes = new TaskGraphNode[5];
        lastNodes = new LASTNode[5];

        // Building up an example graph statet in
        // http://www.eng.auburn.edu/files/acad_depts/csse/csse_technical_reports/CSSE91-14.pdf
        // on page 8.
        graph = new JGraphtTaskGraph();
        graphNodes[0] = graph.getFirstNode();
        graphNodes[0].setComputationTime(10);

        graphNodes[4] = graph.getLastNode();
        graphNodes[4].setComputationTime(50);

        graphNodes[1] = graph.insertNode(graph.getFirstNode(), 5, graph.getLastNode(), 5, 20);
        graphNodes[2] = graph.insertNode(graph.getFirstNode(), 5, graph.getLastNode(), 50, 30);
        graphNodes[3] = graph.insertNode(graphNodes[2], 10, graph.getLastNode(), 5, 40);

        // Generate last nodes
        for (int i = 0; i < graphNodes.length; ++i)
            lastNodes[i] = new LASTNode(graphNodes[i]);

        lastScheduler.initialize(graph, systemInformation);
    }

    @Test
    public void scheduleTest() {
        String expectedString = "0 0 0\n10 0 1\n15 1 2\n45 1 3\n85 1 4\n";
        ScheduledTaskListSerializer serializer = new DefaultScheduledTaskListSerializer();

        ScheduledTaskList list = lastScheduler.schedule(graph, systemInformation);
        Assert.assertEquals(expectedString, serializer.serialize(list));
    }

    @Test
    public void updateFrontiersTest() {
        lastScheduler.groups.get(0).add(lastNodes[0]);
        lastScheduler.updateFrontiers();

        Assert.assertEquals(0, lastScheduler.frontiers.get(1).size());
        Assert.assertEquals(2, lastScheduler.frontiers.get(0).size());

        Assert.assertEquals(graphNodes[1], lastScheduler.frontiers.get(0).get(0).getTaskGraphNode());
        Assert.assertEquals(graphNodes[2], lastScheduler.frontiers.get(0).get(1).getTaskGraphNode());
    }

    @Test
    public void containsTaskTest() {
        lastScheduler.frontiers.get(0).add(lastNodes[0]);
        Assert.assertTrue(lastScheduler.containsTask(lastScheduler.frontiers.get(0), graphNodes[0]));
        Assert.assertFalse(lastScheduler.containsTask(lastScheduler.frontiers.get(0), graphNodes[1]));
    }

    @Test
    public void highestLastNodeByDNodeTest() {
        lastScheduler.groups.get(0).add(lastNodes[0]);
        lastScheduler.frontiers.get(1).add(lastNodes[1]);
        lastScheduler.frontiers.get(0).add(lastNodes[2]);
        Assert.assertEquals(lastNodes[1], lastScheduler.highestLastNodeByDNode());
    }

    @Test
    public void calcDNodeTest() {
        lastScheduler.groups.get(0).add(lastNodes[0]);

        Assert.assertEquals(0.5f, lastScheduler.calcDNode(lastNodes[1]));
        Assert.assertEquals(0.3333333333333333f, lastScheduler.calcDNode(lastNodes[2]));
    }

    @Test
    public void calcDEdgeTest() {
        Assert.assertEquals(0, lastScheduler.calcDEdge(lastNodes[0], lastNodes[1]));
        Assert.assertEquals(0, lastScheduler.calcDEdge(lastNodes[1], lastNodes[0]));

        lastScheduler.groups.get(0).add(lastNodes[1]);
        Assert.assertEquals(1, lastScheduler.calcDEdge(lastNodes[0], lastNodes[1]));
        Assert.assertEquals(1, lastScheduler.calcDEdge(lastNodes[1], lastNodes[0]));
        Assert.assertEquals(1, lastScheduler.calcDEdge(lastNodes[4], lastNodes[1]));

        lastScheduler.groups.get(0).add(lastNodes[0]);
        Assert.assertEquals(0, lastScheduler.calcDEdge(lastNodes[0], lastNodes[0]));
        Assert.assertEquals(0, lastScheduler.calcDEdge(lastNodes[1], lastNodes[0]));
    }

    @Test
    public void calcStrengthTest() {
        int cpuId = 0;

        lastScheduler.groups.get(cpuId).add(lastNodes[0]);
        lastScheduler.groups.get(cpuId).add(lastNodes[1]);

        // First test.
        Assert.assertEquals(0.16666667f, lastScheduler.calcStrength(lastNodes[2], cpuId));

        // Second test.
        Assert.assertEquals(0f, lastScheduler.calcStrength(lastNodes[3], cpuId));

        // Second test.
        Assert.assertEquals(0.1f, lastScheduler.calcStrength(lastNodes[4], cpuId));

    }
}
