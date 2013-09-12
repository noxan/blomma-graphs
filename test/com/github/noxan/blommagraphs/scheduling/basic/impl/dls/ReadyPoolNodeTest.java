package com.github.noxan.blommagraphs.scheduling.basic.impl.dls;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;


@RunWith(JUnit4.class)
public class ReadyPoolNodeTest {
    TaskGraph graph;
    TaskGraphNode node;
    ReadyPoolNode poolNode;

    @Before
    public void initialize() {
        int numberOfCpu = 2;
        graph = new JGraphtTaskGraph();
        node = graph.insertNode(graph.getFirstNode(), 1, graph.getLastNode(), 1, 2);
        poolNode = new ReadyPoolNode(node, node.getStaticBLevel(), numberOfCpu);
        poolNode.setEarliestStarttime(0, 5);
        poolNode.setEarliestStarttime(1, 8);
    }

    @Test
    public void testGetMaxDynamicLevel() {
        Assert.assertEquals(-2, (Object) poolNode.getMaxDynamicLevel().getFirst());
        Assert.assertEquals(0, (Object) poolNode.getMaxDynamicLevel().getSecond());
    }

    @Test
    public void testGetDynamicLevel() {
        Assert.assertEquals(-2, poolNode.getDynamicLevel(0));
    }

    @Test
    public void testGetEarliestStarttime() {
        Assert.assertEquals(5, poolNode.getEarliestStarttime(0));
    }

    @Test
    public void testSetEarliestStarttime() {
        poolNode.setEarliestStarttime(0, 11);

        Assert.assertEquals(11, poolNode.getEarliestStarttime(0));
    }

    @Test
    public void testGetNode() {
        Assert.assertEquals(node, poolNode.getNode());
    }
}
