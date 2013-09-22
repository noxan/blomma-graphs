package com.github.noxan.blommagraphs.evaluation.impl;


import java.util.List;

import com.github.noxan.blommagraphs.evaluation.ScheduleSimulationWorker;
import com.github.noxan.blommagraphs.evaluation.helpers.EvaluatedTask;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;


public class PracticalScheduleWorker implements Runnable {
    private List<ScheduledTask> list;
    private List<EvaluatedTask> simulatedScheduledTaskList;
    private ScheduleSimulationWorker worker;

    public PracticalScheduleWorker(List<ScheduledTask> list,
            List<EvaluatedTask> simulatedScheduledTaskList) {
        this.list = list;
        this.simulatedScheduledTaskList = simulatedScheduledTaskList;
        this.worker = new TimebasedScheduleSimulationWorker();
    }

    private boolean isDependencyReady(TaskGraphNode dependency) {
        for (EvaluatedTask evaluatedTask : simulatedScheduledTaskList) {
            if (evaluatedTask.getTask() == dependency) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        System.out.println("Worker starting...");
        while (list.size() > 0) {
            ScheduledTask task = list.get(0);
            boolean ready = true;
            for (TaskGraphNode dependency : task.getTaskGraphNode().getPrevNodes()) {
                if (!simulatedScheduledTaskList.containsValue(dependency)) {
                    ready = false;
                }
            }
            if (ready) {
                System.out.println("Start task " + task.getTaskId());
                list.remove(task);
                worker.work(task.getComputationTime() + task.getCommunicationTime());
                System.out.println("Finish task " + task.getTaskId());
                simulatedScheduledTaskList.put(System.nanoTime() - startTime,
                        task.getTaskGraphNode());
            }
        }
    }
}
