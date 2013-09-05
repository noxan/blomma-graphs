package com.github.noxan.blommagraphs.serializer.impl;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;


public class STGSerializerTest {
    private STGSerializer stgSerializer;
    private TaskGraph testGraph1;
    private String testGraph1String;

    @Before
    public void setUp() throws Exception {
        stgSerializer = new STGSerializer();

        testGraph1 = new JGraphtTaskGraph();
        TaskGraphNode node1 = testGraph1.insertNode(testGraph1.getFirstNode(), 1,
                testGraph1.getLastNode(), 11, 1);
        TaskGraphNode node2 = testGraph1.insertNode(testGraph1.getFirstNode(), 2,
                testGraph1.getLastNode(), 3, 2);
        testGraph1.insertNode(testGraph1.getFirstNode(), 3, node2, 3, 3);
        testGraph1.insertNode(node1, 15, testGraph1.getLastNode(), 16, 4);
        testGraph1.insertNode(node1, 21, node2, 23, 5);

        testGraph1String = "7\n" + "0 1 0\n" + "1 1 1\n" + "\t0 1\n" + "2 2 2\n" + "\t3 3\n"
                + "\t5 23\n" + "3 3 1\n" + "\t0 3\n" + "4 4 1\n" + "\t1 15\n" + "5 5 1\n"
                + "\t1 21\n" + "6 1 2\n" + "\t2 3\n" + "\t4 16\n"
                + "\n# BlommaGraphs:\tthis is a Standard Task Graph project file\n"
                + "# edgesPerNodeRatio:\t1\n" + "# layerCount:\t4\n" + "# edgeCount:\t8\n"
                + "# nodeCount:\t7\n" + "# dummyEdgeCount:\t4\n" + "# dummyNodeCount:\t2\n";
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSerialize() {
        String testGraph1ActualString = stgSerializer.serialize(testGraph1);
        Assert.assertEquals(testGraph1String, testGraph1ActualString);
    }

    @Test
    public void testDeserialize() {
        TaskGraph graph = stgSerializer.deserialize(testGraph1String);
        Assert.assertEquals(testGraph1String, stgSerializer.serialize(graph));
    }
}
