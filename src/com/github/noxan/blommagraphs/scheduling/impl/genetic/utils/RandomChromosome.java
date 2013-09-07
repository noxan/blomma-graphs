package com.github.noxan.blommagraphs.scheduling.impl.genetic.utils;


import java.util.Random;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


public class RandomChromosome extends Chromosome {
    public RandomChromosome(int numberOfProcessors, TaskGraph taskGraph) {
        super(numberOfProcessors, taskGraph);

        Random random = new Random(System.nanoTime());

        for (TaskGraphNode taskNode : taskGraph.getNodeSet()) {
            int processorId = random.nextInt(numberOfProcessors);
            addTaskToProcessor(processorId, taskNode);
        }
    }
}
