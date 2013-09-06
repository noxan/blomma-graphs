package com.github.noxan.blommagraphs.scheduling.impl.genetic.utils;


import java.util.ArrayList;
import java.util.List;


public class Chromosome {
    private List<List<Integer>> genes;

    public Chromosome(int numberOfCPUs) {
        genes = new ArrayList<List<Integer>>();
        for (int i = 0; i < numberOfCPUs; i++) {
            genes.add(new ArrayList<Integer>());
        }
    }

    public void addTaskToCPU(int cpu, int taskId) {
        genes.get(cpu).add(taskId);
    }
}
