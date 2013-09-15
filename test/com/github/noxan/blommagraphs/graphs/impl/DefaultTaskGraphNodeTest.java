package com.github.noxan.blommagraphs.graphs.impl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


@RunWith(JUnit4.class)
public class DefaultTaskGraphNodeTest {
    private TaskGraph taskGraph;

    @Before
    public void initialize() {
        taskGraph = new DefaultTaskGraph();
    }

    @Test
    public void testGetBottomLayerCountWithSimpleGraph() {
        TaskGraphNode taskGraphNode = taskGraph.insertNode(taskGraph.getFirstNode(), 1,
                taskGraph.getLastNode(), 1, 1);
        Assert.assertEquals(1, taskGraphNode.getBottomLayerCount());
    }

    @Test
    public void testGetTopLayerCountWithSimpleGraph() {
        TaskGraphNode taskGraphNode = taskGraph.insertNode(taskGraph.getFirstNode(), 1,
                taskGraph.getLastNode(), 1, 1);
        Assert.assertEquals(1, taskGraphNode.getTopLayerCount());
    }

    @Test
    public void testGetBottomLayerCountWithTwoNodesGraph() {
        TaskGraphNode taskGraphNode = taskGraph.insertNode(taskGraph.getFirstNode(), 1,
                taskGraph.getLastNode(), 1, 1);
        taskGraph.insertNode(taskGraphNode, 1, taskGraph.getLastNode(), 1, 1);
        Assert.assertEquals(2, taskGraphNode.getBottomLayerCount());
    }

    @Test
    public void testGetTopLayerCountWithTwoNodesGraph() {
        TaskGraphNode taskGraphNode = taskGraph.insertNode(taskGraph.getFirstNode(), 1,
                taskGraph.getLastNode(), 1, 1);
        taskGraph.insertNode(taskGraphNode, 1, taskGraph.getLastNode(), 1, 1);
        Assert.assertEquals(1, taskGraphNode.getTopLayerCount());
    }

    @Test
    public void testGetBottomLayerCountWithListLikeGraph() {
        TaskGraphNode taskGraphNode = taskGraph.getFirstNode();
        TaskGraphNode testingGraphNode = taskGraph.getLastNode();
        for (int i = 0; i < 5; i++) {
            taskGraphNode = taskGraph.insertNode(taskGraphNode, 1, taskGraph.getLastNode(), 1, 1);
            if (i == 1) {
                testingGraphNode = taskGraphNode;
            }
        }
        Assert.assertEquals(4, testingGraphNode.getBottomLayerCount());
    }

    @Test
    public void testGetTopLayerCountWithListLikeGraph() {
        TaskGraphNode taskGraphNode = taskGraph.getFirstNode();
        TaskGraphNode testingGraphNode = taskGraph.getLastNode();
        for (int i = 0; i < 5; i++) {
            taskGraphNode = taskGraph.insertNode(taskGraphNode, 1, taskGraph.getLastNode(), 1, 1);
            if (i == 1) {
                testingGraphNode = taskGraphNode;
            }
        }
        Assert.assertEquals(2, testingGraphNode.getTopLayerCount());
    }

    @Test
    public void testGetStaticBLevel() {
        TaskGraphNode node1 = taskGraph.getLastNode();

        for (int i = 0; i < 2; i++) {
            TaskGraphNode tempNode1 = taskGraph.insertNode(taskGraph.getFirstNode(), 0,
                    taskGraph.getLastNode(), 0, 3);
            TaskGraphNode tempNode2 = taskGraph.insertNode(tempNode1, 0, taskGraph.getLastNode(),
                    0, 3);
            if (i == 0) {
                node1 = taskGraph.insertNode(tempNode1, 0, tempNode2, 0, 3);
            }
        }
        Assert.assertEquals(7, node1.getStaticBLevel());
    }

    @Test
    public void testFindPrevEdge() {
        TaskGraphNode node = taskGraph.insertNode(taskGraph.getFirstNode(), 2,
                taskGraph.getLastNode(), 4, 3);

        TaskGraphEdge edge = node.findPrevEdge(taskGraph.getFirstNode());
        Assert.assertNotNull(edge);
        Assert.assertEquals(taskGraph.getFirstNode(), edge.getPrevNode());
        Assert.assertEquals(node, edge.getNextNode());
    }

    @Test
    public void testFindPrevEdgeNull() {
        TaskGraphNode node = taskGraph.insertNode(taskGraph.getFirstNode(), 2,
                taskGraph.getLastNode(), 4, 3);

        TaskGraphEdge edge = node.findPrevEdge(taskGraph.getLastNode());
        Assert.assertNull(edge);
    }

    @Test
    public void testFindNextEdge() {
        TaskGraphNode node = taskGraph.insertNode(taskGraph.getFirstNode(), 2,
                taskGraph.getLastNode(), 4, 3);

        TaskGraphEdge edge = node.findNextEdge(taskGraph.getLastNode());
        Assert.assertNotNull(edge);
        Assert.assertEquals(node, edge.getPrevNode());
        Assert.assertEquals(taskGraph.getLastNode(), edge.getNextNode());
    }

    @Test
    public void testFindNextEdgeNull() {
        TaskGraphNode node = taskGraph.insertNode(taskGraph.getFirstNode(), 2,
                taskGraph.getLastNode(), 4, 3);

        TaskGraphEdge edge = node.findNextEdge(taskGraph.getFirstNode());
        Assert.assertNull(edge);
    }

    @Test
    public void testSetComputationTime() {
        TaskGraphNode node = new DefaultTaskGraphNode(0, 64);
        Assert.assertEquals(64, node.getComputationTime());
        node.setComputationTime(489);
        Assert.assertEquals(489, node.getComputationTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetIllegalComputationTime() {
        TaskGraphNode node = new DefaultTaskGraphNode(0, 15);
        node.setComputationTime(-12);
        Assert.assertEquals(15, node.getComputationTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetZeroComputationTime() {
        TaskGraphNode node = new DefaultTaskGraphNode(0, 13);
        node.setComputationTime(0);
        Assert.assertEquals(13, node.getComputationTime());
    }

    @Test
    public void testToString() {
        TaskGraphNode node = taskGraph.insertNode(taskGraph.getFirstNode(), 24,
                taskGraph.getLastNode(), 64, 31);

        String result = node.toString();
        Assert.assertTrue(result
                .startsWith("com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraphNode@"));

        Assert.assertTrue(result.endsWith("[1]"));
    }
}
