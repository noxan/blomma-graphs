package com.github.noxan.blommagraphs.graphs.impl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


@RunWith(JUnit4.class)
public class DefaultTaskGraphTest {
    private TaskGraph taskGraph;

    @Before
    public void initialize() {
        taskGraph = new DefaultTaskGraph();
    }

    @Test
    public void testGetFirstNode() {
        if (taskGraph.getFirstNode() == null) {
            Assert.fail("First node is null.");
        }
        Assert.assertTrue(taskGraph.getFirstNode() instanceof TaskGraphNode);
        Assert.assertTrue(taskGraph.getFirstNode() instanceof DefaultTaskGraphNode);
    }

    @Test
    public void testGetLastNode() {
        if (taskGraph.getLastNode() == null) {
            Assert.fail("Last node is null.");
        }
        Assert.assertTrue(taskGraph.getLastNode() instanceof TaskGraphNode);
        Assert.assertTrue(taskGraph.getLastNode() instanceof DefaultTaskGraphNode);
    }

    @Test
    public void testGetEdgeSetWithEmptyGraph() {
        Assert.assertEquals(taskGraph.getEdgeSet().size(), 1);
    }
}
