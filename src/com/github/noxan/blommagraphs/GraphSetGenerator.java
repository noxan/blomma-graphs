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


/**
 * Generates a set of graphs.
 * 
 * Sizes: 10, 50, 100, 300, 500<br />
 * Number of graphs per size: 100
 * 
 */
public class GraphSetGenerator {
    public static void main(String[] args) {
        // settings
        String rootFolder = "export/";
        String graphsFolder = "";
        int numberOfGraphs = 100;
        // graph generator settings
        int numberOfNodes[] = { 10, 50, 100, 300, 500 };
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
        System.out.println(String.format("Writing exports to folder: %s", rootFolder));
        new File(rootFolder).mkdir();

        TaskGraphGenerator taskGraphGenerator = new DefaultTaskGraphGenerator();
        try {
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

        for (int i = 0; i < numberOfNodes.length; ++i) {
            graphsFolder = Integer.toString(numberOfNodes[i]);
            new File(rootFolder + "/" + graphsFolder).mkdir();

            try {
                taskGraphGenerator.setNumberOfNodes(numberOfNodes[i]);
            } catch (OutOfRangeException outOfRangeException) {
                System.err.println("Missconfigured generator: " + outOfRangeException.getMessage());
            } catch (GeneratorException generatorException) {
                System.err.println("Missconfigured generator: " + generatorException.getMessage());
            }

            for (int j = 0; j < numberOfGraphs; j++) {
                taskGraphGenerator.generateSeed();
                TaskGraph taskGraph = taskGraphGenerator.generator();

                String pathname = String.format("%s/%s/%d.stg", rootFolder, graphsFolder, j);
                try {
                    TaskGraphFileUtils.writeFile(pathname, taskGraph, serializer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Done");
    }
}
