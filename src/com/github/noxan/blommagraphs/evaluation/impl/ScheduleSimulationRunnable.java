package com.github.noxan.blommagraphs.evaluation.impl;


import com.github.noxan.blommagraphs.evaluation.ScheduleSimulationWorker;

import java.util.ConcurrentModificationException;

public class ScheduleSimulationRunnable implements Runnable {
    private boolean run;
    private ScheduleSimulationWorker worker;
    private int time;
    private final Object object;

    public ScheduleSimulationRunnable(ScheduleSimulationWorker worker) {
        this.run = true;
        this.worker = worker;
        this.object = new Object();
    }

    @Override
    public void run() {
        while (run) {
            if (time > 0) {
                worker.work(time);
                synchronized (object) {
                    this.time = 0;
                }
            }
        }
    }

    public void stop() {
        this.run = false;
    }

    public void nextWork(int time) {
        if (time == 0) {
            synchronized (object) {
                this.time = time;
            }
        } else {
            throw new ConcurrentModificationException();
        }
    }
}
