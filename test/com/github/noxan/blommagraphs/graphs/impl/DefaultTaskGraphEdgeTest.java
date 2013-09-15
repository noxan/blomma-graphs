package com.github.noxan.blommagraphs.graphs.impl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class DefaultTaskGraphEdgeTest {
    private TaskGraphNode node0;
    private TaskGraphNode node1;
    private TaskGraphEdge edge;

    @Before
    public void initialize() {
        node0 = new DefaultTaskGraphNode(0, 0);
        node1 = new DefaultTaskGraphNode(1, 0);
        edge = new DefaultTaskGraphEdge(node0, node1, 5);
    }

    @Test
    public void testCommunicationTime() {
        Assert.assertEquals(5, edge.getCommunicationTime());
        ((DefaultTaskGraphEdge) edge).setCommunicationTime(17);
        Assert.assertEquals(17, edge.getCommunicationTime());
    }

    @Test
    public void testPrevNode() {
        Assert.assertEquals(node0, edge.getPrevNode());
    }

    @Test
    public void testNextNode() {
        Assert.assertEquals(node1, edge.getNextNode());
    }
}
