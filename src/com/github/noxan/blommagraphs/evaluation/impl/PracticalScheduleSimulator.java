package com.github.noxan.blommagraphs.evaluation.impl;


import com.github.noxan.blommagraphs.evaluation.ScheduleSimulationWorker;
import com.github.noxan.blommagraphs.evaluation.ScheduleSimulator;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTaskList;


public class PracticalScheduleSimulator implements ScheduleSimulator {
    private ScheduleSimulationWorker worker;
    private ScheduleSimulationRunnable[] runnable;

    public PracticalScheduleSimulator(ScheduleSimulationWorker worker) {
        this.worker = worker;
    }

    @Override
    public ScheduledTaskList simulateExecution(ScheduledTaskList scheduledTaskList) {
        this.runnable = new ScheduleSimulationRunnable[scheduledTaskList.getCpuCount()];

        for (int cpuId = 0; cpuId < scheduledTaskList.getCpuCount(); cpuId++) {
            this.runnable[cpuId] = new ScheduleSimulationRunnable(worker);
            Thread thread = new Thread(this.runnable[cpuId]);
            thread.start();
        }

        ScheduledTaskList simulatedScheduledTaskList = new DefaultScheduledTaskList(scheduledTaskList.getCpuCount());



        return simulatedScheduledTaskList;
    }
}
