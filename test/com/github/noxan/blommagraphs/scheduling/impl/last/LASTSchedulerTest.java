package com.github.noxan.blommagraphs.scheduling.impl.last;

import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraphNode;
import org.junit.Assert
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LASTSchedulerTest {
    private LASTScheduler lastScheduler;

    @Before
    public void initialize() {
        lastScheduler = new LASTScheduler();
    }

    @Test
    public void schedule(){
        //coming soon
    }

    @Test
    public void calcNode() {
        //coming soon
    }

    @Test
    public void caldDEdge() {
        //coming soon
    }

    @Test
    public void calcStrength() {
        int id = 1;
        int computationTime = 1;
        int cpuId = 1;
        double expected = 1.0;

        TaskGraphNode taskGraphNode = new DefaultTaskGraphNode(id, computationTime);
        LASTNode lastNode = new LASTNode(taskGraphNode);

        //Assert.assertEquals(expected, lastScheduler.(lastNode, cpuId));
    }
}
