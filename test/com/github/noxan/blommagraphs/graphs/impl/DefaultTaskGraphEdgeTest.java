package com.github.noxan.blommagraphs.graphs.impl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class DefaultTaskGraphEdgeTest {
    private TaskGraphEdge edge;

    @Before
    public void initialize() {
        TaskGraphNode node1 = new DefaultTaskGraphNode(0, 0);
        TaskGraphNode node2 = new DefaultTaskGraphNode(1, 0);
        edge = new DefaultTaskGraphEdge(node1, node2, 5);
    }

    @Test
    public void testCommunicationTime() {
        Assert.assertEquals(5, edge.getCommunicationTime());
        ((DefaultTaskGraphEdge) edge).setCommunicationTime(17);
        Assert.assertEquals(17, edge.getCommunicationTime());
    }
}
