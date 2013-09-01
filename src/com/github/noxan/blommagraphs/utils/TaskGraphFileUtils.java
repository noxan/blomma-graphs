package com.github.noxan.blommagraphs.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.serializer.TaskGraphSerializer;


/**
 * A helper class to provide basic file operations to import and export graphs.
 * 
 * @author noxan
 */
public class TaskGraphFileUtils {
    /**
     * Exports a single graph to a file with the given serializer.
     * 
     * @param pathname
     * @param graph
     * @param serializer
     * @throws IOException
     */
    public static void writeFile(String pathname, TaskGraph graph, TaskGraphSerializer serializer)
            throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(pathname));

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
        String graphString = serializer.serialize(graph);

        writer.write(graphString);

        writer.close();
        fos.close();
    }

    /**
     * Imports a graph from a file with the given deserializer.
     * 
     * @param pathname
     * @param deserializer
     * @return
     * @throws IOException
     */
    public static TaskGraph readFile(String pathname, TaskGraphSerializer deserializer)
            throws IOException {
        FileInputStream fis = new FileInputStream(new File(pathname));

        StringBuilder graphString = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        while ((line = reader.readLine()) != null) {
            graphString.append(line + "\n");
        }

        reader.close();
        fis.close();

        return deserializer.deserialize(graphString.toString());
    }
}
