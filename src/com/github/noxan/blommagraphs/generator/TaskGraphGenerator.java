package com.github.noxan.blommagraphs.generator;

import com.github.noxan.blommagraphs.generator.exceptions.OutOfRangeException;
import com.github.noxan.blommagraphs.generator.exceptions.GeneratorException;



public interface TaskGraphGenerator {

    public void setNumberOfNodes(int numberOfNodes) throws OutOfRangeException;

    public void setMinIncommingEdges(int minIncommingEdges) throws GeneratorException;

    public void setMaxIncommingEdges(int maxIncommingEdges) throws GeneratorException;

    public void setSpreadEdges(int spreadEges) throws OutOfRangeException;

    public void setMinComputationTime(int minComputationTime) throws GeneratorException;

    public void setMaxComputationTime(int maxComputationTime) throws GeneratorException;

    public void setSpreadComputationTime(int spreadComputationTime);

    public void setMinCommunicationTime(int minCommunicationTime) throws GeneratorException;

    public void setMaxCommunicationTime(int maxCommunicationTime) throws GeneratorException;

    public void setSpreadCommunicationTime(int spreadCommunicationTime);

    public void generator();
}
