package com.github.noxan.blommagraphs.graphs.factories.impl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.serializer.TaskGraphSerializer;
import com.github.noxan.blommagraphs.serializer.impl.STGSerializer;


public class JGraphtTaskGraphFactoryTest {
    TaskGraphSerializer serializer;

    @Before
    public void setUp() {
        serializer = new STGSerializer();
    }

    @Test
    public void test() {
        TaskGraph graph = JGraphtTaskGraphFactory.makeGraph();
        String expectedString = "10\n0 1 0\n1 10 1\n\t0 1\n2 5 3\n\t0 7\n\t1 5\n"
                + "\t3 5\n3 10 1\n\t0 1\n4 15 1\n\t0 1\n5 15 2\n\t3 6\n\t4 5\n"
                + "6 10 2\n\t2 4\n\t7 4\n7 20 1\n\t1 3\n8 10 2\n\t2 6\n\t5 3\n"
                + "9 1 3\n\t4 8\n\t6 1\n\t8 1\n";
        Assert.assertTrue(serializer.serialize(graph).startsWith(expectedString));
    }
}
