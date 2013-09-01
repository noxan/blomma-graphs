package com.github.noxan.blommagraphs.serializer.impl;


import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class STGNodeTest {
    private STGNode node1;

    @Before
    public void setUp() throws Exception {
        node1 = new STGNode();
        node1.setId(1);
        node1.setComputationcosts(10);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSTGNode() {
        node1 = new STGNode(2, 20);
        Assert.assertEquals(2, node1.id);
        Assert.assertEquals(20, node1.getComputationcosts());
    }

    @Test
    public void testGetDependencyCount() {
        Assert.assertEquals(0, node1.getDependencyCount());
        node1.addDependency(1, 10);
        Assert.assertEquals(1, node1.getDependencyCount());
        node1.addDependency(2, 10);
        node1.addDependency(3, 10);
        Assert.assertEquals(3, node1.getDependencyCount());
    }

    @Test
    public void testHasDependencyOn() {
        Assert.assertFalse(node1.hasDependencyOn(1));
        node1.addDependency(1, 10);
        Assert.assertTrue(node1.hasDependencyOn(1));
    }

    @Test
    public void testAddDependency() {
        Map<Integer, Integer> expectedDependencyMap = new HashMap<Integer, Integer>();
        expectedDependencyMap.put(1, 10);
        expectedDependencyMap.put(2, 200);
        node1.addDependency(1, 10);
        node1.addDependency(2, 200);
        Assert.assertEquals(expectedDependencyMap, node1.getDependencies());

    }

    @Test
    public void testToString() {
        String expectedString = "1 10 2\n\t1 333\n\t2 666\n";
        node1.addDependency(1, 333);
        node1.addDependency(2, 666);
        Assert.assertEquals(expectedString, node1.toString());

    }

}
