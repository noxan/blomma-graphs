package com.github.noxan.blommagraphs.graphs.factories.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.factories.GraphFactory;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;


public class JGraphtTaskGraphFactory implements GraphFactory {
    @Override
    public TaskGraph makeGraph() {
        TaskGraph taskGraph = new JGraphtTaskGraph();

        return taskGraph;
    }
}
