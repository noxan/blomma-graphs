package com.github.noxan.blommagraphs.generator;

import com.github.noxan.blommagraphs.generator.exceptions.BoundaryConflictException;
import com.github.noxan.blommagraphs.generator.exceptions.OutOfRangeException;
import com.github.noxan.blommagraphs.graphs.TaskGraph;


public interface TaskGraphGenerator {

    public void setNumberOfNodes(int numberOfNodes) throws BoundaryConflictException, OutOfRangeException;

    public void setMinIncomingEdges(int minIncommingEdges) throws BoundaryConflictException, OutOfRangeException;

    public void setMaxIncomingEdges(int maxIncommingEdges) throws BoundaryConflictException, OutOfRangeException;

    public void setSpreadEdges(int spreadEges) throws OutOfRangeException;

    public void setMinComputationTime(int minComputationTime) throws BoundaryConflictException, OutOfRangeException;

    public void setMaxComputationTime(int maxComputationTime) throws BoundaryConflictException, OutOfRangeException;

    public void setSpreadComputationTime(int spreadComputationTime);

    public void setMinCommunicationTime(int minCommunicationTime) throws BoundaryConflictException, OutOfRangeException;

    public void setMaxCommunicationTime(int maxCommunicationTime) throws BoundaryConflictException, OutOfRangeException;

    public void setSpreadCommunicationTime(int spreadCommunicationTime);

    public TaskGraph generator();
}
