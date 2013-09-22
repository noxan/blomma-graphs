package com.github.noxan.blommagraphs.evaluation.impl;


import java.util.ArrayList;
import java.util.List;

import com.github.noxan.blommagraphs.evaluation.ScheduleSimulationWorker;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTaskList;


public class PracticalScheduleSimulator {
    public ScheduledTaskList simulateExecution(ScheduledTaskList scheduledTaskList,
            Class<? extends ScheduleSimulationWorker> workerClass) {
        PracticalScheduleWorker workers[] = new PracticalScheduleWorker[scheduledTaskList
                .getCpuCount()];
        Thread[] threads = new Thread[scheduledTaskList.getCpuCount()];

        ScheduledTaskList simulatedScheduledTaskList = new DefaultScheduledTaskList(
                scheduledTaskList.getCpuCount());

        List<List<ScheduledTask>> processorScheduledTaskList = new ArrayList<List<ScheduledTask>>();
        for (int cpuId = 0; cpuId < scheduledTaskList.getCpuCount(); cpuId++) {
            processorScheduledTaskList.add(new ArrayList<ScheduledTask>());
        }
        for (ScheduledTask task : scheduledTaskList) {
            processorScheduledTaskList.get(task.getCpuId()).add(task);
        }

        for (int cpuId = 0; cpuId < scheduledTaskList.getCpuCount(); cpuId++) {
            workers[cpuId] = new PracticalScheduleWorker(processorScheduledTaskList.get(cpuId),
                    simulatedScheduledTaskList, workerClass);
            threads[cpuId] = new Thread(workers[cpuId]);
        }

        // start workers
        for (int cpuId = 0; cpuId < scheduledTaskList.getCpuCount(); cpuId++) {
            threads[cpuId].start();
        }

        // wait for all workers to finish
        for (Thread thread : threads) {
            try {
                thread.join();
                System.out.println("Worker finished...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return simulatedScheduledTaskList;
    }
}
