package com.github.noxan.blommagraphs.serializer;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;


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
