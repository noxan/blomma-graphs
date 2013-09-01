package com.github.noxan.blommagraphs;


import java.io.File;
import java.io.IOException;

import com.github.noxan.blommagraphs.generator.TaskGraphGenerator;
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

        // logging
        System.out.println(String.format("Writing exports to folder: %s", folderName));
        new File(folderName).mkdir();

        System.out.println(String.format("Generating %d graphs...", numberOfGraphs));

        TaskGraphGenerator taskGraphGenerator = new DefaultTaskGraphGenerator();
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
