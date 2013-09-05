package com.github.noxan.blommagraphs.graphs.impl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
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
}
