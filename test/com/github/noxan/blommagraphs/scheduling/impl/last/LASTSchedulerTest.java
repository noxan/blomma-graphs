package com.github.noxan.blommagraphs.scheduling.impl.last;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
        Assert.fail("Not implemented yet");
    }

    @Test
    public void calcDNodeTest() {
        LASTNode lastNode0 = new LASTNode(graphNodes[0]);
        LASTNode lastNode1 = new LASTNode(graphNodes[1]);
        LASTNode lastNode2 = new LASTNode(graphNodes[2]);

        lastScheduler.groups.get(0).add(lastNode0);

        System.out.println(graphNodes[4].getComputationTime());
        Assert.assertEquals(0.5f, lastScheduler.calcDNode(lastNode1));
        Assert.assertEquals(0.3333333333333333f, lastScheduler.calcDNode(lastNode2));
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
        float expected = 0f;

        lastScheduler.groups.get(cpuId).add(lastNodes[0]);
        lastScheduler.groups.get(cpuId).add(lastNodes[1]);

        Assert.assertEquals(expected, lastScheduler.calcStrength(lastNodes[3], cpuId));
    }
}
