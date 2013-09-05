package com.github.noxan.blommagraphs.graphs.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.DuplicateEdgeException;

import java.util.ArrayList;


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
        TaskGraphNode taskGraphNode2 = taskGraph.insertNode(taskGraphNode, 1,
                taskGraph.getLastNode(), 1, 1);
        taskGraph.insertNode(taskGraphNode2, 1, taskGraph.getLastNode(), 1, 1);
        Assert.assertEquals(taskGraph.getLayerCount(), 5);
    }

    @Test
    public void testGetLayerCountForList() {
        TaskGraphNode prevNode = taskGraph.getFirstNode();
        for (int i = 0; i < 10; i++) {
            prevNode = taskGraph.insertNode(prevNode, 1, taskGraph.getLastNode(), 1, 1);
        }
        Assert.assertEquals(taskGraph.getNodeCount(), 12);
        Assert.assertEquals(taskGraph.getLayerCount(), 12);
    }

    @Test
    public void testGetLayerCount2() {
        TaskGraphNode prevNode = taskGraph.getFirstNode();
        for (int i = 0; i < 3; i++) {
            prevNode = taskGraph.insertNode(prevNode, 1, taskGraph.getLastNode(), 1, 1);
        }
        prevNode = taskGraph.getFirstNode();
        for (int i = 0; i < 5; i++) {
            prevNode = taskGraph.insertNode(prevNode, 1, taskGraph.getLastNode(), 1, 1);
        }
        Assert.assertEquals(taskGraph.getNodeCount(), 10);
        Assert.assertEquals(taskGraph.getLayerCount(), 7);
    }

    @Test
    public void testGetLayerCount3() {
        for (int i = 0; i < 3; i++) {
            taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(), 1, 1);
        }
        Assert.assertEquals(taskGraph.getNodeCount(), 5);
        Assert.assertEquals(taskGraph.getLayerCount(), 3);
    }

    @Test
    public void testGetLayerCount4() {
        TaskGraphNode node = taskGraph.insertNode(taskGraph.getFirstNode(), 1,
                taskGraph.getLastNode(), 1, 1);
        taskGraph.insertNode(taskGraph.getFirstNode(), 1, node, 1, 1);
        taskGraph.insertNode(taskGraph.getFirstNode(), 1, node, 1, 1);

        Assert.assertEquals(taskGraph.getNodeCount(), 5);
        Assert.assertEquals(taskGraph.getLayerCount(), 4);
    }

    @Test
    public void testGetLayerCount5() {
        for (int i = 0; i < 2; i++) {
            taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(), 1, 1);
        }
        Assert.assertEquals(taskGraph.getNodeCount(), 4);
        Assert.assertEquals(taskGraph.getLayerCount(), 3);
    }

    @Test
    public void testGetLayerCount6() {
        for (int i = 0; i < 4; i++) {
            taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(), 1, 1);
        }
        Assert.assertEquals(taskGraph.getNodeCount(), 6);
        Assert.assertEquals(taskGraph.getLayerCount(), 3);
    }

    @Test
    public void testgetCriticalPath() {
        ArrayList<TaskGraphNode> nodeList = new ArrayList<TaskGraphNode>();
        for (int i = 0; i < 3; i++) {
            TaskGraphNode node = taskGraph.insertNode(taskGraph.getFirstNode(), 0, taskGraph.getLastNode(), 0, 1);
            TaskGraphNode nextnode = taskGraph.insertNode(node, 1+i, taskGraph.getLastNode(), 0, 1);
            nodeList.add(node);
            nodeList.add(nextnode);
        }
        try {
            taskGraph.insertEdge(nodeList.get(0), nodeList.get(3), 4);
            taskGraph.insertEdge(nodeList.get(2), nodeList.get(5), 5);
        } catch (DuplicateEdgeException e) {
            e.printStackTrace();
        }
        ArrayList<TaskGraphEdge> expectedArray = new ArrayList<TaskGraphEdge>();
        Assert.assertEquals(4 ,taskGraph.getCriticalPath().size());
    }
}
