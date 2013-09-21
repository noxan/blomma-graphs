package com.github.noxan.blommagraphs.scheduling.basic.impl.genetic;


import com.github.noxan.blommagraphs.scheduling.basic.impl.dls.DynamicLevelScheduler;


public class GeneticDLSScheduler extends GeneticScheduler {
    public GeneticDLSScheduler() {
        super(new DynamicLevelScheduler());
    }
}
