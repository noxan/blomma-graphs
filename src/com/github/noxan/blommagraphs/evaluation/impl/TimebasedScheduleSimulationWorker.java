package com.github.noxan.blommagraphs.evaluation.impl;


import com.github.noxan.blommagraphs.evaluation.ScheduleSimulationWorker;


public class TimebasedScheduleSimulationWorker implements ScheduleSimulationWorker {
    @Override
    public void work(int workTime) {
        double startTime = System.nanoTime();
        while (System.nanoTime() - startTime < 20000000 * workTime) {
            Thread.yield();
        }
    }
}
