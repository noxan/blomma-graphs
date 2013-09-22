package com.github.noxan.blommagraphs.scheduling.basic.impl.genetic;


import com.github.noxan.blommagraphs.scheduling.basic.impl.last.LASTScheduler;


public class GeneticLASTScheduler extends GeneticScheduler {
    public GeneticLASTScheduler() {
        super(new LASTScheduler());
    }
}
