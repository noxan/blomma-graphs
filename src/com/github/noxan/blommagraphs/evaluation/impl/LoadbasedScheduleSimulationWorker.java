package com.github.noxan.blommagraphs.evaluation.impl;


import com.github.noxan.blommagraphs.evaluation.ScheduleSimulationWorker;


public class LoadbasedScheduleSimulationWorker implements ScheduleSimulationWorker {
    @Override
    public void work(int workTime) {
        for (int i = 0; i < workTime * 50; i++) {
            for (int j = Integer.MIN_VALUE; j < Integer.MAX_VALUE; j++) {
            }
        }
    }
}
