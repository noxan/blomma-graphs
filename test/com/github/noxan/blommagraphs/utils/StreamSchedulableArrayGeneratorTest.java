package com.github.noxan.blommagraphs.utils;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.factories.impl.JGraphtTaskGraphFactory;
import com.github.noxan.blommagraphs.graphs.serializer.TaskGraphSerializer;
import com.github.noxan.blommagraphs.graphs.serializer.impl.STGSerializer;


public class StreamSchedulableArrayGeneratorTest {
    TaskGraphSerializer serializer;

    @Before
    public void setUp() throws Exception {
        serializer = new STGSerializer();
    }

    @Test
    public void generateArrayTest() {
        TaskGraph graph = JGraphtTaskGraphFactory.makeGraph();
        int deadLines[] = { 10, 20, 30, 40 };

        TaskGraph[] graphs = StreamSchedulableArrayGenerator.generateArray(graph, deadLines);

        Assert.assertEquals(4, graphs.length);
        for (int i = 0; i < graphs.length; ++i) {
            Assert.assertTrue(serializer.serialize(graphs[i]).startsWith(
                    JGraphtTaskGraphFactory.getSTGSerializedGraph()));
            // Check if all graphs are independent instances
            for (int j = 0; j < graphs.length; ++j) {
                if (i != j)
                    Assert.assertFalse(graphs[i].equals(graphs[j]));
            }
        }
    }
}
