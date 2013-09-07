package com.github.noxan.blommagraphs.scheduling.impl.genetic;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.Scheduler;
import com.github.noxan.blommagraphs.scheduling.impl.genetic.chromosome.AbstractChromosome;
import com.github.noxan.blommagraphs.scheduling.impl.genetic.chromosome.ProcessorChromosome;
import com.github.noxan.blommagraphs.scheduling.impl.genetic.chromosome.RandomChromosome;
import com.github.noxan.blommagraphs.scheduling.impl.genetic.chromosome.ScheduledChromosome;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class GeneticScheduler implements Scheduler {
    private SystemMetaInformation metaInformation;
    private TaskGraph taskGraph;

    private Scheduler initialScheduler;
    private ScheduledTaskList initialTaskSchedule;

    private int randomPopulationSize = 10;
    private float elitismRatio = 0.1f;
    private int generationCount = 10;

    private List<AbstractChromosome> elitismPopulation;
    private Set<AbstractChromosome> matingPopulation;

    public GeneticScheduler(Scheduler initialScheduler) {
        this.initialScheduler = initialScheduler;
    }

    public GeneticScheduler(ScheduledTaskList initialTaskSchedule) {
        this.initialTaskSchedule = initialTaskSchedule;
    }

    private void initialize() {
        if (initialTaskSchedule == null) {
            initialTaskSchedule = initialScheduler.schedule(taskGraph, metaInformation);
        }

        Set<AbstractChromosome> population = new HashSet<AbstractChromosome>();

        // initial schedule based chromosome
        population.add(new ScheduledChromosome(metaInformation.getProcessorCount(), taskGraph,
                initialTaskSchedule));
        // processor based chromosomes
        for (int processorId = 0; processorId < metaInformation.getProcessorCount(); processorId++) {
            population.add(new ProcessorChromosome(metaInformation.getProcessorCount(), taskGraph,
                    processorId));
        }
        // random chromosomes
        for (int i = 0; i < randomPopulationSize; i++) {
            population.add(new RandomChromosome(metaInformation.getProcessorCount(), taskGraph));
        }

        elitismPopulation = new ArrayList<AbstractChromosome>();
        matingPopulation = new HashSet<AbstractChromosome>();

        splitPopulation(population);
    }

    private void splitPopulation(Set<AbstractChromosome> population) {
        List<AbstractChromosome> sortedPopulation = new ArrayList<AbstractChromosome>(population);
        Collections.sort(sortedPopulation);

        int populationSize = sortedPopulation.size();

        elitismPopulation.clear();
        matingPopulation.clear();

        for (int index = 0; index < populationSize; index++) {
            AbstractChromosome chromosome = sortedPopulation.get(index);

            if (elitismRatio >= (float) index / (float) populationSize) {
                elitismPopulation.add(chromosome);
            } else {
                matingPopulation.add(chromosome);
            }
        }
    }

    private void nextGeneration() {
        // swap mutation
        for (AbstractChromosome chromosome : matingPopulation) {
            if (Math.random() > 0.5f) {
                chromosome.swapMutate();
            }
        }

        Set<AbstractChromosome> nextPopulation = new HashSet<AbstractChromosome>();
        nextPopulation.addAll(elitismPopulation);
        nextPopulation.addAll(matingPopulation);
        splitPopulation(nextPopulation);
    }

    @Override
    public ScheduledTaskList schedule(TaskGraph taskGraph, SystemMetaInformation metaInformation) {
        this.metaInformation = metaInformation;
        this.taskGraph = taskGraph;

        initialize();

        for (int generation = 0; generation < generationCount; generation++) {
            nextGeneration();
        }

        Collections.sort(elitismPopulation);
        AbstractChromosome solutionChromosome = elitismPopulation.get(0);

        ScheduledTaskList solutionScheduledTaskList = solutionChromosome.decode();

        return solutionScheduledTaskList;
    }
}
