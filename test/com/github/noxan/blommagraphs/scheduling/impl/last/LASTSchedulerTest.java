package com.github.noxan.blommagraphs.scheduling.impl.last;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;


@RunWith(JUnit4.class)
public class LASTSchedulerTest {
    private LASTScheduler lastScheduler;
    private TaskGraph graph;
    private TaskGraphNode graphNodes[];

    @Before
    public void initialize() {
        lastScheduler = new LASTScheduler();
        graphNodes = new TaskGraphNode[5];

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
        graphNodes[4] = graph.insertNode(graphNodes[2], 10, graph.getLastNode(), 5, 40);
    }

    @Test
    public void scheduleTest() {
        Assert.fail("Not implemented yet");
    }

    @Test
    public void calcDNodeTest() {
        Assert.fail("Not implemented yet");
    }

    @Test
    public void calcDEdgeTest() {
        Assert.fail("Not implemented yet");
    }

    @Test
    public void calcStrengthTest() {
        List<List<LASTNode>> group1 = new ArrayList<List<LASTNode>>();
        group1.add();

        int id = 1;
        int computationTime = 1;
        int cpuId = 1;
        double expected = 1.0;

        TaskGraphNode taskGraphNode = new DefaultTaskGraphNode(id, computationTime);
        LASTNode lastNode = new LASTNode(taskGraphNode);

        // Assert.assertEquals(expected, lastScheduler.(lastNode, cpuId));
    }
}
