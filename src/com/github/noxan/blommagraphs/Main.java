package com.github.noxan.blommagraphs;


import java.io.IOException;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;
import com.github.noxan.blommagraphs.serializer.STGSerializer;
import com.github.noxan.blommagraphs.utils.TaskGraphFileUtils;


public class Main {
    public static void main(String[] args) {
        System.out.println("blomma-graphs");

        TaskGraph graph = new DefaultTaskGraph();
        try {
            TaskGraphFileUtils.writeFile("graph.stg", graph, new STGSerializer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
