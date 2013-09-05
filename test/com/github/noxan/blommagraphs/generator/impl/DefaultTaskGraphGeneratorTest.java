package com.github.noxan.blommagraphs.generator.impl;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.noxan.blommagraphs.generator.TaskGraphGenerator;
import com.github.noxan.blommagraphs.generator.exceptions.BoundaryConflictException;
import com.github.noxan.blommagraphs.generator.exceptions.OutOfRangeException;


@RunWith(JUnit4.class)
public class DefaultTaskGraphGeneratorTest {
    private TaskGraphGenerator taskGraphGenerator;

    @Before
    public void initialize() {
        taskGraphGenerator = new DefaultTaskGraphGenerator();
    }

    @Test(expected = OutOfRangeException.class)
    public void setNumberOfNodesNegativ() throws BoundaryConflictException, OutOfRangeException {
        int numberOfNodes = -1;
        taskGraphGenerator.setNumberOfNodes(numberOfNodes);
    }

    @Test(expected = OutOfRangeException.class)
    public void setMinIncomingEdgesNegativ() throws BoundaryConflictException, OutOfRangeException {
        int minIncomingEdges = -1;
        taskGraphGenerator.setMinIncomingEdges(minIncomingEdges);
    }

    @Test(expected = OutOfRangeException.class)
    public void setMinIncomingEdgesZero() throws BoundaryConflictException, OutOfRangeException {
        int minIncomingEdges = 0;
        taskGraphGenerator.setMinIncomingEdges(minIncomingEdges);
    }

    @Test(expected = OutOfRangeException.class)
    public void setMaxIncomingEdgesNegativ() throws BoundaryConflictException, OutOfRangeException {
        int maxIncomingEdges = -1;
        taskGraphGenerator.setMaxIncomingEdges(maxIncomingEdges);
    }

    @Test(expected = OutOfRangeException.class)
    public void setMaxIncomingEdgesZero() throws BoundaryConflictException, OutOfRangeException {
        int maxIncomingEdges = 0;
        taskGraphGenerator.setMaxIncomingEdges(maxIncomingEdges);
    }

    @Test(expected = BoundaryConflictException.class)
    public void setMinIncomingEdgesHigherThanMaxIncomingEdges() throws BoundaryConflictException,
            OutOfRangeException {
        int minIncomingEdges = 100;
        int maxIncomingEdges = 5;
        taskGraphGenerator.setMaxIncomingEdges(maxIncomingEdges);
        taskGraphGenerator.setMinIncomingEdges(minIncomingEdges);
    }

    @Test(expected = BoundaryConflictException.class)
    public void setMaxIncomingEdgesLessThanMinIncomingEdges() throws BoundaryConflictException,
            OutOfRangeException {
        int minIncomingEdges = 100;
        int maxIncomingEdges = 5;
        taskGraphGenerator.setMinIncomingEdges(minIncomingEdges);
        taskGraphGenerator.setMaxIncomingEdges(maxIncomingEdges);
    }

    @Test(expected = OutOfRangeException.class)
    public void setMinComputationTimeNegativ() throws BoundaryConflictException,
            OutOfRangeException {
        int minComputationTime = -1;
        taskGraphGenerator.setMinComputationTime(minComputationTime);
    }

    @Test(expected = OutOfRangeException.class)
    public void setMinComputationTimeZero() throws BoundaryConflictException, OutOfRangeException {
        int minComputationTime = 0;
        taskGraphGenerator.setMinComputationTime(minComputationTime);
    }

    @Test(expected = OutOfRangeException.class)
    public void setMaxComputationTimeNegativ() throws BoundaryConflictException,
            OutOfRangeException {
        int maxComputationTime = -1;
        taskGraphGenerator.setMaxComputationTime(maxComputationTime);
    }

    @Test(expected = OutOfRangeException.class)
    public void setMaxComputationTimeZero() throws BoundaryConflictException, OutOfRangeException {
        int maxComputationTime = 0;
        taskGraphGenerator.setMaxComputationTime(maxComputationTime);
    }

    @Test(expected = BoundaryConflictException.class)
    public void setMinComputationTimeHigherThanMaxComputationTime()
            throws BoundaryConflictException, OutOfRangeException {
        int minComputationTime = 100;
        int maxComputationTime = 5;
        taskGraphGenerator.setMaxComputationTime(maxComputationTime);
        taskGraphGenerator.setMinComputationTime(minComputationTime);
    }

    @Test(expected = BoundaryConflictException.class)
    public void setMaxComputationTimeLessThanMinComputationTime() throws BoundaryConflictException,
            OutOfRangeException {
        int minComputationTime = 100;
        int maxComputationTime = 5;
        taskGraphGenerator.setMinComputationTime(minComputationTime);
        taskGraphGenerator.setMaxComputationTime(maxComputationTime);
    }

    @Test(expected = OutOfRangeException.class)
    public void setMinCommunicationTimeNegativ() throws BoundaryConflictException,
            OutOfRangeException {
        int minCommunicationTime = -1;
        taskGraphGenerator.setMinCommunicationTime(minCommunicationTime);
    }

    @Test(expected = OutOfRangeException.class)
    public void setMaxCommunicationTimeNegativ() throws BoundaryConflictException,
            OutOfRangeException {
        int maxCommunicationTime = -1;
        taskGraphGenerator.setMaxCommunicationTime(maxCommunicationTime);
    }

    @Test(expected = BoundaryConflictException.class)
    public void setMinCommunicationTimeHigherThanMaxCommunicationTime()
            throws BoundaryConflictException, OutOfRangeException {
        int minCommunicationTime = 100;
        int maxCommunicationTime = 5;
        taskGraphGenerator.setMaxCommunicationTime(maxCommunicationTime);
        taskGraphGenerator.setMinCommunicationTime(minCommunicationTime);
    }

    @Test(expected = BoundaryConflictException.class)
    public void setMinCommunicationTimeLessThanMaxCommunicationTime()
            throws BoundaryConflictException, OutOfRangeException {
        int minCommunicationTime = 100;
        int maxCommunicationTime = 5;
        taskGraphGenerator.setMinCommunicationTime(minCommunicationTime);
        taskGraphGenerator.setMaxCommunicationTime(maxCommunicationTime);
    }
}
