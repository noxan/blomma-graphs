package com.github.noxan.blommagraphs.serializer.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;
import com.github.noxan.blommagraphs.serializer.TaskGraphSerializer;


public class STGSerializer implements TaskGraphSerializer {
    @Override
    public String serialize(TaskGraph graph) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public TaskGraph deserialize(String graphString) {
        // TODO Auto-generated method stub
        return new DefaultTaskGraph();
    }
}
