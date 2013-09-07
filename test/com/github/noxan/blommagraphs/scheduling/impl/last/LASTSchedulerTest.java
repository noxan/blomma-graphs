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
        LASTNode lastNode0 = new LASTNode(graphNodes[0]);
        LASTNode lastNode1 = new LASTNode(graphNodes[1]);
        LASTNode lastNode4 = new LASTNode(graphNodes[4]);

        Assert.assertEquals(0, lastScheduler.calcDEdge(lastNode0, lastNode1));
        Assert.assertEquals(0, lastScheduler.calcDEdge(lastNode1, lastNode0));

        lastScheduler.groups.get(0).add(lastNode1);
        Assert.assertEquals(1, lastScheduler.calcDEdge(lastNode0, lastNode1));
        Assert.assertEquals(1, lastScheduler.calcDEdge(lastNode1, lastNode0));
        Assert.assertEquals(1, lastScheduler.calcDEdge(lastNode4, lastNode1));

        lastScheduler.groups.get(0).add(lastNode0);
        Assert.assertEquals(0, lastScheduler.calcDEdge(lastNode0, lastNode0));
        Assert.assertEquals(0, lastScheduler.calcDEdge(lastNode1, lastNode0));
    }

    @Test
    public void calcStrengthTest() {
        int cpuId = 0;
        float expected = 0f;

        LASTNode node1 = new LASTNode(graphNodes[0]);
        LASTNode node2 = new LASTNode(graphNodes[1]);
        LASTNode testedNode = new LASTNode(graphNodes[3]);

        lastScheduler.groups.get(cpuId).add(node1);
        lastScheduler.groups.get(cpuId).add(node2);

        Assert.assertEquals(expected, lastScheduler.calcStrength(testedNode, cpuId));
    }
}
