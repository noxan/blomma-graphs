package com.github.noxan.blommagraphs.graphs.impl;


import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.ContainsNoEdgeException;
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
            TaskGraphNode node = taskGraph.insertNode(taskGraph.getFirstNode(), 0,
                    taskGraph.getLastNode(), 0, 1);
            TaskGraphNode nextnode = taskGraph.insertNode(node, 1 + i, taskGraph.getLastNode(), 0,
                    1);
            nodeList.add(node);
            nodeList.add(nextnode);
        }
        try {
            taskGraph.insertEdge(nodeList.get(0), nodeList.get(3), 4);
            taskGraph.insertEdge(nodeList.get(2), nodeList.get(5), 5);
        } catch (DuplicateEdgeException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(3, taskGraph.getCriticalPath().size());

        ArrayList<TaskGraphEdge> edgeList = new ArrayList<TaskGraphEdge>();
        try {
            edgeList.add(taskGraph.findEdge(taskGraph.getFirstNode(), nodeList.get(2)));
            edgeList.add(taskGraph.findEdge(nodeList.get(2), nodeList.get(5)));
            edgeList.add(taskGraph.findEdge(nodeList.get(5), taskGraph.getLastNode()));
        } catch (ContainsNoEdgeException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(edgeList, taskGraph.getCriticalPath());
    }

    @Test
    public void testMergeGraph() throws ContainsNoEdgeException {
        TaskGraph srcGraph1 = new JGraphtTaskGraph();
        TaskGraphNode src1Node = srcGraph1.insertNode(srcGraph1.getFirstNode(), 11,
                srcGraph1.getLastNode(), 12, 20);

        TaskGraph srcGraph2 = new JGraphtTaskGraph();
        TaskGraphNode src2Node = srcGraph2.insertNode(srcGraph2.getFirstNode(), 900,
                srcGraph2.getLastNode(), 910, 40);

        taskGraph
                .mergeGraph(srcGraph1, taskGraph.getFirstNode(), 111, taskGraph.getLastNode(), 222);
        taskGraph
                .mergeGraph(srcGraph2, taskGraph.getFirstNode(), 333, taskGraph.getLastNode(), 444);

        // Check edges and nodes
        Assert.assertEquals(8, taskGraph.getNodeCount());
        Assert.assertEquals(9, taskGraph.getEdgeCount());

        Assert.assertTrue(taskGraph.containsEdge(taskGraph.getFirstNode(), taskGraph.getLastNode()));
        Assert.assertTrue(taskGraph.containsEdge(taskGraph.getFirstNode(), taskGraph.getLastNode()));
        Assert.assertEquals(111,
                taskGraph.findEdge(taskGraph.getFirstNode(), srcGraph1.getFirstNode())
                        .getCommunicationTime());
        Assert.assertEquals(444,
                taskGraph.findEdge(srcGraph2.getLastNode(), taskGraph.getLastNode())
                        .getCommunicationTime());

        // Check node ids
        Assert.assertEquals(0, taskGraph.getFirstNode().getId());
        Assert.assertEquals(7, taskGraph.getLastNode().getId());

        Assert.assertEquals(1, srcGraph1.getFirstNode().getId());
        Assert.assertEquals(3, srcGraph1.getLastNode().getId());
        Assert.assertEquals(2, src1Node.getId());

        Assert.assertEquals(4, srcGraph2.getFirstNode().getId());
        Assert.assertEquals(6, srcGraph2.getLastNode().getId());
        Assert.assertEquals(5, src2Node.getId());

    }
}
