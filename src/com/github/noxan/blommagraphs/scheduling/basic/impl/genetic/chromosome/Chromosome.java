package com.github.noxan.blommagraphs.scheduling.basic.impl.genetic.chromosome;


import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;


public interface Chromosome extends Comparable<Chromosome> {
    public ScheduledTaskList decode();

    public void swapMutate();
}
