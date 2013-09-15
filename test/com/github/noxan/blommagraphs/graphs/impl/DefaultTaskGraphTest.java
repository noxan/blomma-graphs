package com.github.noxan.blommagraphs.graphs.impl;


import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.ContainsNoEdgeException;
import com.github.noxan.blommagraphs.graphs.exceptions.DuplicateEdgeException;


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
    public void testGetLayerCountWithEmptyGraph() {
        Assert.assertEquals(taskGraph.getLayerCount(), 2);
    }

    @Test
    public void testGetLayerCountWithSimpleGraph() {
        taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(), 1, 1);
        Assert.assertEquals(taskGraph.getLayerCount(), 3);
    }

    @Test
    public void testGetLayerCountWithListLikeGraph() {
        TaskGraphNode prevNode = taskGraph.getFirstNode();
        for (int i = 0; i < 10; i++) {
            prevNode = taskGraph.insertNode(prevNode, 1, taskGraph.getLastNode(), 1, 1);
        }
        Assert.assertEquals(taskGraph.getLayerCount(), 12);
    }

    @Test
    public void testGetLayerCountWithMultipleListLikeGraphs() {
        for (int j = 0; j < 4; j++) {
            TaskGraphNode prevNode = taskGraph.getFirstNode();
            for (int i = 0; i < 10; i++) {
                prevNode = taskGraph.insertNode(prevNode, 1, taskGraph.getLastNode(), 1, 1);
            }
        }
        Assert.assertEquals(taskGraph.getLayerCount(), 12);
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
    public void testInsertEdgeToSimpleGraph() {
        taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(), 1, 1);

        Assert.assertEquals(taskGraph.getFirstNode().getNextEdgeCount(), 1);
        Assert.assertEquals(taskGraph.getLastNode().getPrevEdgeCount(), 1);

        try {
            taskGraph.insertEdge(taskGraph.getFirstNode(), taskGraph.getLastNode(), 5);
        } catch (DuplicateEdgeException e) {
            Assert.fail("The edge between startNode and endNode has not been removed.");
        }

        Assert.assertEquals(taskGraph.getFirstNode().getNextEdgeCount(), 2);
        Assert.assertEquals(taskGraph.getLastNode().getPrevEdgeCount(), 2);
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

    @Test
    public void testGetNodeSetWithEmptyGraph() {
        Assert.assertEquals(taskGraph.getNodeSet().size(), 2);
    }

    @Test
    public void testGetNodeSetWithSimpleGraph() {
        taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(), 1, 1);
        Assert.assertEquals(taskGraph.getNodeSet().size(), 3);
    }

    @Test
    public void testContainsEdge() {
        Assert.assertTrue(taskGraph.containsEdge(taskGraph.getFirstNode(), taskGraph.getLastNode()));
    }

    @Test
    public void testContainsEdgeFails() {
        Assert.assertFalse(taskGraph.containsEdge(taskGraph.getLastNode(), taskGraph.getFirstNode()));
    }

    @Test
    public void testMergeGraph() throws ContainsNoEdgeException {
        TaskGraph srcGraph1 = new JGraphtTaskGraph();
        srcGraph1.insertNode(srcGraph1.getFirstNode(), 11, srcGraph1.getLastNode(), 12, 20);

        TaskGraph srcGraph2 = new JGraphtTaskGraph();
        srcGraph2.insertNode(srcGraph2.getFirstNode(), 900, srcGraph2.getLastNode(), 910, 40);

        taskGraph
                .mergeGraph(srcGraph1, taskGraph.getFirstNode(), 111, taskGraph.getLastNode(), 222);
        taskGraph
                .mergeGraph(srcGraph2, taskGraph.getFirstNode(), 333, taskGraph.getLastNode(), 444);

        // Check edges and nodes
        Assert.assertEquals(8, taskGraph.getNodeCount());
        Assert.assertEquals(9, taskGraph.getEdgeCount());

        Assert.assertEquals(taskGraph.getFirstNode().getNextEdgeCount(), 3);
        Assert.assertEquals(taskGraph.getLastNode().getPrevEdgeCount(), 3);

        // Check ids
        Assert.assertEquals(0, taskGraph.getFirstNode().getId());
        Assert.assertEquals(7, taskGraph.getLastNode().getId());

        // Check for unique ids
        Set<Integer> nodeIdSet = new HashSet<Integer>();
        for (TaskGraphNode node : taskGraph.getNodeSet()) {
            nodeIdSet.add(node.getId());
        }
        Assert.assertEquals(nodeIdSet.size(), taskGraph.getNodeCount());
    }
}
