package com.github.noxan.blommagraphs.evaluation.impl;


import com.github.noxan.blommagraphs.evaluation.ScheduleSimulationWorker;


public class TimebasedScheduleSimulationWorker implements ScheduleSimulationWorker {
    @Override
    public void work(int workTime) {
        try {
            Thread.sleep(workTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
