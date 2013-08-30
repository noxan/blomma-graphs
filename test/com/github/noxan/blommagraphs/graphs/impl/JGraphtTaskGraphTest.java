package com.github.noxan.blommagraphs.graphs.impl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.DuplicateEdgeException;


@RunWith(JUnit4.class)
public class JGraphtTaskGraphTest {
    private TaskGraph taskGraph;

    @Before
    public void initialize() {
        taskGraph = new JGraphtTaskGraph();
    }

    @Test
    public void testInsertNode() {
        taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(), 1, 1);
    }

    @Test(expected = DuplicateEdgeException.class)
    public void testInsertDuplicateEdge() throws DuplicateEdgeException {
        taskGraph.insertEdge(taskGraph.getFirstNode(), taskGraph.getLastNode(), 2);
    }

    @Test
    public void testGetNodeSet() {
        Assert.assertEquals(taskGraph.getNodeSet().size(), 2);
    }

    @Test
    public void testGetEdgeSet() {
        Assert.assertEquals(taskGraph.getEdgeSet().size(), 1);
    }

    @Test
    public void testNodeGetEdgeSet() {
        TaskGraphNode node = taskGraph.insertNode(taskGraph.getFirstNode(), 1,
                taskGraph.getLastNode(), 1, 1);
        Assert.assertEquals(node.getPrevEdges().size(), 1);
    }

    @Test
    public void testFirstAndLastNodeIds() {
        Assert.assertEquals(taskGraph.getFirstNode().getId(), 0);
        Assert.assertEquals(taskGraph.getLastNode().getId(), 1);
    }

    @Test
    public void testFirstAndLastNodeIdsWithInsert() {
        taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(), 1, 1);
        Assert.assertEquals(taskGraph.getFirstNode().getId(), 0);
        Assert.assertEquals(taskGraph.getLastNode().getId(), 2);
    }

    @Test
    public void testNodeComparable() {
        Assert.assertTrue(taskGraph.getFirstNode().compareTo(taskGraph.getLastNode()) < 0);
        Assert.assertEquals(taskGraph.getFirstNode().compareTo(taskGraph.getFirstNode()), 0);
        Assert.assertTrue(taskGraph.getLastNode().compareTo(taskGraph.getFirstNode()) > 0);
    }

    @Test
    public void testGetLayerCount() {
        TaskGraphNode taskGraphNode = taskGraph.insertNode(taskGraph.getFirstNode(), 1,
                taskGraph.getLastNode(), 1, 1);
        taskGraph.insertNode(taskGraphNode, 1, taskGraph.getLastNode(), 1, 1);
        taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(), 1, 1);
        Assert.assertEquals(taskGraph.getLayerCount(), 4);
    }
}
