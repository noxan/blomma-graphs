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
    public void testInsertNodeInEmptyGraph() {
        TaskGraphNode node = taskGraph.insertNode(taskGraph.getFirstNode(), 1,
                taskGraph.getLastNode(), 1, 1);
        Assert.assertEquals(node.getPrevEdgeCount(), 1);
        Assert.assertEquals(node.getNextEdgeCount(), 1);
        TaskGraphNode prevNode = node.getPrevNodes().iterator().next();
        Assert.assertEquals(taskGraph.getFirstNode(), prevNode);
        TaskGraphNode nextNode = node.getNextNodes().iterator().next();
        Assert.assertEquals(taskGraph.getLastNode(), nextNode);
        Assert.assertEquals(taskGraph.getLastNode().getPrevNodeCount(), 1);
        Assert.assertEquals(taskGraph.getLastNode().getPrevNodes().iterator().next(), node);
        Assert.assertEquals(taskGraph.getFirstNode().getNextNodeCount(), 1);
        Assert.assertEquals(taskGraph.getFirstNode().getNextNodes().iterator().next(), node);
    }

    @Test
    public void testGetEdgeSetWithEmptyGraph() {
        Assert.assertEquals(taskGraph.getEdgeSet().size(), 1);
    }

    @Test
    public void testGetEdgeSetWithSimpleGraph() {
        taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(), 1, 1);
        Assert.assertEquals(taskGraph.getEdgeSet().size(), 2);
    }

    @Test
    public void testGetEdgeSetWithListLikeGraph() {
        TaskGraphNode prevNode = taskGraph.getFirstNode();
        for (int i = 0; i < 10; i++) {
            prevNode = taskGraph.insertNode(prevNode, 1, taskGraph.getLastNode(), 1, 1);
        }
        Assert.assertEquals(taskGraph.getEdgeSet().size(), 11);
    }

    @Test
    public void testGetEdgeSetWithMultipleListLikeGraphs() {
        for (int j = 0; j < 4; j++) {
            TaskGraphNode prevNode = taskGraph.getFirstNode();
            for (int i = 0; i < 10; i++) {
                prevNode = taskGraph.insertNode(prevNode, 1, taskGraph.getLastNode(), 1, 1);
            }
        }
        Assert.assertEquals(taskGraph.getEdgeSet().size(), 44);
    }
}
