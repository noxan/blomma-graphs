package com.github.noxan.blommagraphs.scheduling.impl.last;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraphNode;


@RunWith(JUnit4.class)
public class LASTSchedulerTest extends LASTScheduler {
    private LASTScheduler lastScheduler;

    @Before
    public void initialize() {
        lastScheduler = new LASTScheduler();
    }

    @Test
    public void schedule() {
        // coming soon
    }

    @Test
    public void calcDNode() {
        // coming soon
    }

    @Test
    public void calcDEdge() {
        // coming soon
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
