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

        Chromosome scheduledChromosome = new Chromosome(metaInformation.getProcessorCount());

        Collections.sort(initialTaskSchedule);
        for (ScheduledTask task : initialTaskSchedule) {
            scheduledChromosome.addTaskToProcessor(task.getCpuId(), task.getTaskGraphNode());
        }

        population.add(scheduledChromosome);

        for (int processorId = 0; processorId < metaInformation.getProcessorCount(); processorId++) {
            Chromosome processorChromosome = new Chromosome(metaInformation.getProcessorCount());

            Collections.shuffle(initialTaskSchedule);

            Iterator<ScheduledTask> it = initialTaskSchedule.iterator();
            while (it.hasNext()) {
                processorChromosome.addTaskToProcessor(processorId, it.next().getTaskGraphNode());
            }

            population.add(processorChromosome);
        }
    }

    private void chromosomeDecoding() {
        List<ScheduledTaskList> decodedChromosomeList = new ArrayList<ScheduledTaskList>();

        for (Chromosome chromosome : population) {
            ScheduledTaskList scheduledTaskList = chromosome.decode(taskGraph);
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
