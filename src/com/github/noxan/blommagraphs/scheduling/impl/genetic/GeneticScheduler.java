package com.github.noxan.blommagraphs.scheduling.impl.genetic;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.Scheduler;
import com.github.noxan.blommagraphs.scheduling.impl.genetic.utils.Chromosome;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class GeneticScheduler implements Scheduler {
    private SystemMetaInformation metaInformation;
    private TaskGraph taskGraph;

    private Scheduler initialScheduler;
    private List<ScheduledTask> initialTaskSchedule;

    private float elitismRatio = 0.1f;
    private int generationCount = 50;

    private List<Chromosome> elitismPopulation;
    private Set<Chromosome> matingPopulation;

    public GeneticScheduler(Scheduler initialScheduler) {
        this.initialScheduler = initialScheduler;
    }

    public GeneticScheduler(List<ScheduledTask> initialTaskSchedule) {
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
            Chromosome processorChromosome = new Chromosome(metaInformation.getProcessorCount(),
                    taskGraph);

            Collections.shuffle(initialTaskSchedule);

            Iterator<ScheduledTask> it = initialTaskSchedule.iterator();
            while (it.hasNext()) {
                processorChromosome.addTaskToProcessor(processorId, it.next().getTaskGraphNode());
            }

            population.add(processorChromosome);
        }

        elitismPopulation = new ArrayList<Chromosome>();
        matingPopulation = new HashSet<Chromosome>();

        splitPopulation(population);
    }

    private void splitPopulation(Set<Chromosome> population) {
        List<Chromosome> sortedPopulation = new ArrayList<Chromosome>(population);
        Collections.sort(sortedPopulation);

        int populationSize = sortedPopulation.size();

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
    }

    @Override
    public List<ScheduledTask> schedule(SystemMetaInformation metaInformation, TaskGraph taskGraph) {
        this.metaInformation = metaInformation;
        this.taskGraph = taskGraph;

        initialize();

        for (int generation = 0; generation < generationCount; generation++) {
            nextGeneration();
        }

        Chromosome solutionChromosome = elitismPopulation.get(0);

        ScheduledTaskList solutionScheduledTaskList = solutionChromosome.decode();

        System.out.println(solutionScheduledTaskList.getFinishTime());

        return solutionScheduledTaskList;
    }
}
