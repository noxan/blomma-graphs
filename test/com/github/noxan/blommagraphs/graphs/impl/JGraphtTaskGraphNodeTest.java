package com.github.noxan.blommagraphs.graphs.impl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class JGraphtTaskGraphNodeTest {
    private TaskGraph taskGraph;
    private TaskGraphNode taskGraphNode;

    @Before
    public void initialize() {
        taskGraph = new JGraphtTaskGraph();
    }

    @Test
    public void testGetBottomLayerCount() {
        taskGraphNode = taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(),
                1, 1);
        Assert.assertEquals(1, taskGraphNode.getBottomLayerCount());
    }

    @Test
    public void testGetTopLayerCount() {
        taskGraphNode = taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(),
                1, 1);
        Assert.assertEquals(1, taskGraphNode.getTopLayerCount());
    }

    @Test
    public void testGetBottomLayerCount2() {
        taskGraphNode = taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(),
                1, 1);
        taskGraph.insertNode(taskGraphNode, 1, taskGraph.getLastNode(), 1, 1);
        Assert.assertEquals(2, taskGraphNode.getBottomLayerCount());
    }

    @Test
    public void testGetTopLayerCount2() {
        taskGraphNode = taskGraph.insertNode(taskGraph.getFirstNode(), 1, taskGraph.getLastNode(),
                1, 1);
        taskGraph.insertNode(taskGraphNode, 1, taskGraph.getLastNode(), 1, 1);
        Assert.assertEquals(1, taskGraphNode.getTopLayerCount());
    }

    @Test
    public void testGetBottomLayerCount3() {
        taskGraphNode = taskGraph.getFirstNode();
        TaskGraphNode taskGraphNode2 = taskGraph.getLastNode();
        for (int i = 0; i < 5; i++) {
            taskGraphNode = taskGraph.insertNode(taskGraphNode, 1, taskGraph.getLastNode(), 1, 1);
            if (i == 1) {
                taskGraphNode2 = taskGraphNode;
            }
        }
        Assert.assertEquals(4, taskGraphNode2.getBottomLayerCount());
    }

    @Test
    public void testGetTopLayerCount3() {
        taskGraphNode = taskGraph.getFirstNode();
        TaskGraphNode taskGraphNode2 = taskGraph.getLastNode();
        for (int i = 0; i < 5; i++) {
            taskGraphNode = taskGraph.insertNode(taskGraphNode, 1, taskGraph.getLastNode(), 1, 1);
            if (i == 1) {
                taskGraphNode2 = taskGraphNode;
            }
        }
        Assert.assertEquals(2, taskGraphNode2.getTopLayerCount());
    }

    @Test
    public void testGetStaticBLevel() {
        TaskGraphNode node1 = taskGraph.getLastNode();

        for (int i = 0; i < 2; i++) {
            TaskGraphNode tempNode1 = taskGraph.insertNode(taskGraph.getFirstNode(), 0, taskGraph.getLastNode(), 0, 3);
            TaskGraphNode tempNode2 = taskGraph.insertNode(tempNode1, 0, taskGraph.getLastNode(), 0, 3);
            if (i == 0) {
                node1 = taskGraph.insertNode(tempNode1, 0, tempNode2, 0, 3);
            }
        }
        Assert.assertEquals(7, node1.getStaticBLevel());
    }
}
