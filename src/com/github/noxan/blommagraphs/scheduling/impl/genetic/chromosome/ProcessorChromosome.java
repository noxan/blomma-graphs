package com.github.noxan.blommagraphs.scheduling.impl.genetic.chromosome;


import java.util.Iterator;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class ProcessorChromosome extends AbstractChromosome {
    public ProcessorChromosome(int numberOfProcessors, TaskGraph taskGraph, int processorId) {
        super(numberOfProcessors, taskGraph);

        Iterator<TaskGraphNode> it = taskGraph.getNodeSet().iterator();
        while (it.hasNext()) {
            addTaskToProcessor(processorId, it.next());
        }
    }

    @Override
    public String toString() {
        return "Processor" + super.toString();
    }
}
