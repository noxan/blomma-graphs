package com.github.noxan.blommagraphs.utils;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.serializer.TaskGraphSerializer;
import com.github.noxan.blommagraphs.graphs.serializer.impl.STGSerializer;


public class StreamSchedulableArrayGenerator {

    /**
     * @param args
     */
    public TaskGraph[] generateArray(TaskGraph graph, int[] deadLines) {
        TaskGraph graphs[] = new TaskGraph[deadLines.length];

        // Ugly workaround: use serialize -> deserialize to get new graph instance. That's supposed
        // to be replaced when a deep-copy method for TaskGraph is implemented.
        TaskGraphSerializer serializer = new STGSerializer();
        String serializedGraph = serializer.serialize(graph);

        for (int i = 0; i < deadLines.length; ++i) {
            graphs[i] = serializer.deserialize(serializedGraph);
            graphs[i].resetDeadLine(deadLines[i]);
        }

        return graphs;
    }

}
