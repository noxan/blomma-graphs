package com.github.noxan.blommagraphs.evaluation.impl;


import com.github.noxan.blommagraphs.evaluation.ScheduleSimulationWorker;
import com.github.noxan.blommagraphs.evaluation.ScheduleSimulator;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTaskList;


public class PracticalScheduleSimulator implements ScheduleSimulator {
    private ScheduleSimulationWorker[] workers;

    public PracticalScheduleSimulator() {
    }

    @Override
    public ScheduledTaskList simulateExecution(ScheduledTaskList scheduledTaskList) {
        workers = new ScheduleSimulationWorker[scheduledTaskList.getCpuCount()];

        for (int cpuId = 0; cpuId < workers.length; cpuId++) {
            workers[cpuId] = new TimebasedScheduleSimulationWorker();
        }

        ScheduledTaskList simulatedScheduledTaskList = new DefaultScheduledTaskList(workers.length);

        return simulatedScheduledTaskList;
    }
}
