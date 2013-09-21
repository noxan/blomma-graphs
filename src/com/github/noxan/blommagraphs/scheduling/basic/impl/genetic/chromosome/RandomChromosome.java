package com.github.noxan.blommagraphs.scheduling.basic.impl.genetic.chromosome;


import java.util.Random;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class RandomChromosome extends AbstractChromosome {
    public RandomChromosome(int cpuCount, TaskGraph taskGraph) {
        super(cpuCount, taskGraph);

        Random random = new Random(System.nanoTime());

        for (TaskGraphNode taskNode : taskGraph.getNodeSet()) {
            int cpuId = random.nextInt(cpuCount);
            addTaskToCpu(cpuId, taskNode);
        }
    }

    @Override
    public String toString() {
        return "Random" + super.toString();
    }
}
