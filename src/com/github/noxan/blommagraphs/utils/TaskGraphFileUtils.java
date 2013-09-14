package com.github.noxan.blommagraphs.utils;


import java.io.IOException;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.serializer.TaskGraphSerializer;


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
        FileUtils.writeFile(pathname, serializer.serialize(graph));
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
        String graphString = FileUtils.readFile(pathname);
        return deserializer.deserialize(graphString.toString());
    }
}
