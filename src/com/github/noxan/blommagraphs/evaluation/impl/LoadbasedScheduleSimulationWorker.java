package com.github.noxan.blommagraphs.evaluation.impl;


import com.github.noxan.blommagraphs.evaluation.ScheduleSimulationWorker;


public class LoadbasedScheduleSimulationWorker implements ScheduleSimulationWorker {
    private static int calcFibonacci(int n) {
        int result = 1;
        int previous = -1;
        int sum = 0;

        for (int i = 0; i <= n; i++) {
            sum = previous + result;
            previous = result;
            result = sum;
        }
        return result;
    }

    @Override
    public void work(int workTime) {
        for (int j = 0; j < workTime; j++) {
            System.out.println(calcFibonacci(50000000));
        }
    }
}
