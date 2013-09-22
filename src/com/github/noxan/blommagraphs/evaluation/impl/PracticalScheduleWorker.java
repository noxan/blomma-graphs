package com.github.noxan.blommagraphs.evaluation.impl;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.github.noxan.blommagraphs.evaluation.ScheduleSimulationWorker;
import com.github.noxan.blommagraphs.evaluation.helpers.EvaluatedTask;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;


public class PracticalScheduleWorker implements Runnable {
    private List<ScheduledTask> list;
    private ScheduledTaskList simulatedScheduledTaskList;
    private ScheduleSimulationWorker worker;

    public PracticalScheduleWorker(List<ScheduledTask> list,
            ScheduledTaskList simulatedScheduledTaskList,
            Class<? extends ScheduleSimulationWorker> workerClass) {
        this.list = list;
        this.simulatedScheduledTaskList = simulatedScheduledTaskList;

        try {
            Constructor<? extends ScheduleSimulationWorker> constructor = workerClass
                    .getConstructor();
            this.worker = constructor.newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private boolean isDependencyReady(TaskGraphNode dependency) {
        for (ScheduledTask evaluatedTask : simulatedScheduledTaskList) {
            if (evaluatedTask.getTaskGraphNode() == dependency) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        long workerStartTime = System.currentTimeMillis();
        System.out.println("Worker starting...");
        while (list.size() > 0) {
            ScheduledTask task = list.get(0);
            boolean ready = true;
            synchronized (simulatedScheduledTaskList) {
                for (TaskGraphNode dependency : task.getTaskGraphNode().getPrevNodes()) {
                    if (!isDependencyReady(dependency)) {
                        ready = false;
                        break;
                    }
                }
            }
            if (ready) {
                System.out.println("Start task " + task.getTaskId());
                long taskStartTime = System.currentTimeMillis();
                worker.work(task.getCommunicationTime());
                long afterCommunicationTime = System.currentTimeMillis();
                list.remove(task);
                worker.work(task.getComputationTime());
                System.out.println("Finish task " + task.getTaskId());

                long startTime = afterCommunicationTime - workerStartTime;
                long computationTime = System.currentTimeMillis() - afterCommunicationTime;
                long communicationTime = afterCommunicationTime - taskStartTime;

                synchronized (simulatedScheduledTaskList) {
                    simulatedScheduledTaskList.add(new EvaluatedTask(startTime / 20,
                            computationTime / 20, communicationTime / 20, task.getCpuId(), task
                                    .getTaskGraphNode()));
                }
            }
        }
    }
}
