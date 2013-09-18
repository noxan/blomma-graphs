package com.github.noxan.blommagraphs.scheduling.basic.impl.genetic.chromosome;


import java.util.Iterator;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class CpuChromosome extends AbstractChromosome {
    public CpuChromosome(int cpuCount, TaskGraph taskGraph, int cpuId) {
        super(cpuCount, taskGraph);

        Iterator<TaskGraphNode> it = taskGraph.getNodeSet().iterator();
        while (it.hasNext()) {
            addTaskToCpu(cpuId, it.next());
        }
    }

    @Override
    public String toString() {
        return "Cpu" + super.toString();
    }
}
