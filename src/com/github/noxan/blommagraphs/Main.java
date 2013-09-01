package com.github.noxan.blommagraphs;


import java.io.File;
import java.io.IOException;

import com.github.noxan.blommagraphs.generator.TaskGraphGenerator;
import com.github.noxan.blommagraphs.generator.exceptions.GeneratorException;
import com.github.noxan.blommagraphs.generator.exceptions.OutOfRangeException;
import com.github.noxan.blommagraphs.generator.impl.DefaultTaskGraphGenerator;
import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.serializer.TaskGraphSerializer;
import com.github.noxan.blommagraphs.serializer.impl.STGSerializer;
import com.github.noxan.blommagraphs.utils.TaskGraphFileUtils;


public class Main {
    public static void main(String[] args) {
        System.out.println("blomma-graphs");

        // settings
        String folderName = "export/";
        int numberOfGraphs = 10;
        // graph generator settings
        int numberOfNodes = 10;
        int minIncomgEdges = 1;
        int maxIncomingEdges = 2;
        int spreadEdges = 1;
        int minComputationTime = 1;
        int maxComputationTime = 10;
        int spreadComputationTime = 1;
        int minCommunicationTime = 1;
        int maxCommunicationTime = 10;
        int spreadCommunicationTime = 1;

        // logging
        System.out.println(String.format("Writing exports to folder: %s", folderName));
        new File(folderName).mkdir();

        System.out.println(String.format("Number of nodes: %d", numberOfNodes));

        TaskGraphGenerator taskGraphGenerator = new DefaultTaskGraphGenerator();
        try {
            taskGraphGenerator.setNumberOfNodes(numberOfNodes);
            taskGraphGenerator.setMinIncomingEdges(minIncomgEdges);
            taskGraphGenerator.setMaxIncomingEdges(maxIncomingEdges);
            taskGraphGenerator.setSpreadEdges(spreadEdges);
            taskGraphGenerator.setMinComputationTime(minComputationTime);
            taskGraphGenerator.setMaxComputationTime(maxComputationTime);
            taskGraphGenerator.setSpreadComputationTime(spreadComputationTime);
            taskGraphGenerator.setMinCommunicationTime(minCommunicationTime);
            taskGraphGenerator.setMaxCommunicationTime(maxCommunicationTime);
            taskGraphGenerator.setSpreadCommunicationTime(spreadCommunicationTime);
        } catch (OutOfRangeException outOfRangeException) {
            System.err.println("Missconfigured generator: " + outOfRangeException.getMessage());
        } catch (GeneratorException generatorException) {
            System.err.println("Missconfigured generator: " + generatorException.getMessage());
        }

        System.out.println(String.format("Generating %d graphs...", numberOfGraphs));

        TaskGraphSerializer serializer = new STGSerializer();

        for (int i = 0; i < numberOfGraphs; i++) {
            TaskGraph taskGraph = taskGraphGenerator.generator();

            String pathname = String.format("%s/graph%d.stg", folderName, i);
            try {
                TaskGraphFileUtils.writeFile(pathname, taskGraph, serializer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Done");
    }
}
