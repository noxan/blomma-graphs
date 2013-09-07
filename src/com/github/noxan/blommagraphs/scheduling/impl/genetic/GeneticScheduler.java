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

    private Set<Chromosome> population;
    private Set<Chromosome> elitismPopulation;
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

        population = new HashSet<Chromosome>();

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

        elitismPopulation = new HashSet<Chromosome>();
        matingPopulation = new HashSet<Chromosome>();
    }

    private void chromosomeDecoding() {
        List<ScheduledTaskList> decodedChromosomeList = new ArrayList<ScheduledTaskList>();
        for (Chromosome chromosome : population) {
            ScheduledTaskList scheduledTaskList = chromosome.decode();

            System.out.println(scheduledTaskList.getFinishTime());

            decodedChromosomeList.add(scheduledTaskList);
        }
    }

    @Override
    public List<ScheduledTask> schedule(SystemMetaInformation metaInformation, TaskGraph taskGraph) {
        this.metaInformation = metaInformation;
        this.taskGraph = taskGraph;

        initialize();

        chromosomeDecoding();

        // TODO Auto-generated method stub
        return initialTaskSchedule;
    }
}
