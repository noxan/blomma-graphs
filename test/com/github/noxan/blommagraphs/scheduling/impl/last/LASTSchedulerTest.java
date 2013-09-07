package com.github.noxan.blommagraphs.scheduling.impl.last;


import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;


@RunWith(JUnit4.class)
public class LASTSchedulerTest extends LASTScheduler {
    // private LASTScheduler lastScheduler;
    private TaskGraph graph;

    @Before
    public void initialize() {
        // lastScheduler = new LASTScheduler();

        // Building up an example graph statet in
        // http://www.eng.auburn.edu/files/acad_depts/csse/csse_technical_reports/CSSE91-14.pdf
        // on page 8.
        graph = new JGraphtTaskGraph();
        graph.getFirstNode().setComputationTime(10);
        graph.getLastNode().setComputationTime(50);
        graph.insertNode(graph.getFirstNode(), 5, graph.getLastNode(), 5, 20);
        TaskGraphNode node3 = graph
                .insertNode(graph.getFirstNode(), 5, graph.getLastNode(), 50, 30);
        graph.insertNode(node3, 10, graph.getLastNode(), 5, 40);
    }

    @Test
    public void schedule() {
        Assert.fail("Not implemented yet");
    }

    @Test
    public void calcDNode() {
        Assert.fail("Not implemented yet");
    }

    @Test
    public void calcDEdge() {
        Assert.fail("Not implemented yet");
    }

    @Test
    public void calcStrength() {
        int id = 1;
        int computationTime = 1;
        int cpuId = 1;
        double expected = 1.0;

        TaskGraphNode taskGraphNode = new DefaultTaskGraphNode(id, computationTime);
        LASTNode lastNode = new LASTNode(taskGraphNode);

        // Assert.assertEquals(expected, lastScheduler.(lastNode, cpuId));
    }
}
