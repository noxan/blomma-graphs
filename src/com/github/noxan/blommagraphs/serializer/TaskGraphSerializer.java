package com.github.noxan.blommagraphs.serializer;


import com.github.noxan.blommagraphs.graphs.TaskGraph;


public interface TaskGraphSerializer {
    public String serialize(TaskGraph graph);

    public TaskGraph deserialize(String graphString);
}
