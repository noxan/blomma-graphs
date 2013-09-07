package com.github.noxan.blommagraphs.scheduling.impl.genetic;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.Scheduler;
import com.github.noxan.blommagraphs.scheduling.impl.genetic.utils.Chromosome;
import com.github.noxan.blommagraphs.scheduling.impl.genetic.utils.ProcessorChromosome;
import com.github.noxan.blommagraphs.scheduling.impl.genetic.utils.RandomChromosome;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class GeneticScheduler implements Scheduler {
    private SystemMetaInformation metaInformation;
    private TaskGraph taskGraph;

    private Scheduler initialScheduler;
    private ScheduledTaskList initialTaskSchedule;

    private int randomPopulationSize = 10;
    private float elitismRatio = 0.1f;
    private int generationCount = 10;

    private List<Chromosome> elitismPopulation;
    private Set<Chromosome> matingPopulation;

    public GeneticScheduler(Scheduler initialScheduler) {
        this.initialScheduler = initialScheduler;
    }

    public GeneticScheduler(ScheduledTaskList initialTaskSchedule) {
        this.initialTaskSchedule = initialTaskSchedule;
    }

    private void initialize() {
        if (initialTaskSchedule == null) {
            initialTaskSchedule = initialScheduler.schedule(metaInformation, taskGraph);
        }

        Set<Chromosome> population = new HashSet<Chromosome>();

        Chromosome scheduledChromosome = new Chromosome(metaInformation.getProcessorCount(),
                taskGraph);

        Collections.sort(initialTaskSchedule);
        for (ScheduledTask task : initialTaskSchedule) {
            scheduledChromosome.addTaskToProcessor(task.getCpuId(), task.getTaskGraphNode());
        }

        population.add(scheduledChromosome);

        for (int processorId = 0; processorId < metaInformation.getProcessorCount(); processorId++) {
            population.add(new ProcessorChromosome(metaInformation.getProcessorCount(), taskGraph,
                    processorId));
        }

        for (int i = 0; i < randomPopulationSize; i++) {
            Chromosome randomChromosome = new RandomChromosome(metaInformation.getProcessorCount(),
                    taskGraph);

            population.add(randomChromosome);
        }

        elitismPopulation = new ArrayList<Chromosome>();
        matingPopulation = new HashSet<Chromosome>();

        splitPopulation(population);
    }

    private void splitPopulation(Set<Chromosome> population) {
        List<Chromosome> sortedPopulation = new ArrayList<Chromosome>(population);
        Collections.sort(sortedPopulation);

        int populationSize = sortedPopulation.size();

        elitismPopulation.clear();
        matingPopulation.clear();

        for (int index = 0; index < populationSize; index++) {
            Chromosome chromosome = sortedPopulation.get(index);

            if (elitismRatio >= index / populationSize) {
                elitismPopulation.add(chromosome);
            } else {
                matingPopulation.add(chromosome);
            }
        }
    }

    private void nextGeneration() {
        // swap mutation
        for (Chromosome chromosome : matingPopulation) {
            if (Math.random() > 0.5f) {
                chromosome.swapMutate();
            }
        }

        Set<Chromosome> nextPopulation = new HashSet<Chromosome>();
        nextPopulation.addAll(elitismPopulation);
        nextPopulation.addAll(matingPopulation);
        splitPopulation(nextPopulation);
        System.out.println("Chromosome count: " + nextPopulation.size());
        for (Chromosome chromosome : nextPopulation) {
            System.out.println(chromosome.toString());
            System.out.println(chromosome.decode().getFinishTime());
        }
    }

    @Override
    public ScheduledTaskList schedule(SystemMetaInformation metaInformation, TaskGraph taskGraph) {
        this.metaInformation = metaInformation;
        this.taskGraph = taskGraph;

        initialize();

        for (int generation = 0; generation < generationCount; generation++) {
            nextGeneration();
        }

        Collections.sort(elitismPopulation);
        Chromosome solutionChromosome = elitismPopulation.get(0);

        ScheduledTaskList solutionScheduledTaskList = solutionChromosome.decode();

        System.out.println();
        System.out.println("Solution chromosome: " + solutionChromosome.toString());
        System.out.println();
        System.out.println(solutionScheduledTaskList.getFinishTime());

        return solutionScheduledTaskList;
    }
}
