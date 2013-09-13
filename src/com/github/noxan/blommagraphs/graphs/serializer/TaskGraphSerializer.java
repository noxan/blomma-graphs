package com.github.noxan.blommagraphs.graphs.serializer;


import com.github.noxan.blommagraphs.graphs.TaskGraph;


public interface TaskGraphSerializer {
    /**
     * Serialize a graph into a string.
     * 
     * @param graph TaskGraph that is serialized.
     * @return String representation of serialized graph.
     */
    public String serialize(TaskGraph graph);

    /**
     * Deserialize a graph into a string.
     * 
     * @param graphString String thas is deserialized.
     * @return TaskGraph representation of deserialized graphString.
     */
    public TaskGraph deserialize(String graphString);
}
