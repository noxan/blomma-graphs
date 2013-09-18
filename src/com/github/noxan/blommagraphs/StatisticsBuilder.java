package com.github.noxan.blommagraphs;


import java.util.ArrayList;
import java.util.List;


public class StatisticsBuilder {

    private final int schedulerCount = 4;
    private final int taskGraphCount = 500;
    private final int taskGroupCount = 5;

    private List<List<List<Float>>> taskGraphStatistics;
    private List<List<List<Float>>> taskGroupStatistics;
    private List<List<Float>> schedulerStatistics;

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    private StatisticsBuilder() {
        // Build lists
        taskGraphStatistics = new ArrayList<List<List<Float>>>();
        taskGroupStatistics = new ArrayList<List<List<Float>>>();
        schedulerStatistics = new ArrayList<List<Float>>();

        for (int scheduler = 0; scheduler < schedulerCount; ++scheduler) {
            taskGraphStatistics.add(new ArrayList<List<Float>>());
            taskGroupStatistics.add(new ArrayList<List<Float>>());
            schedulerStatistics.add(new ArrayList<Float>());

            for (int taskGraph = 0; taskGraph < taskGraphCount; ++taskGraph) {
                taskGraphStatistics.get(scheduler).add(new ArrayList<Float>());
            }
            for (int taskGroup = 0; taskGroup < taskGroupCount; ++taskGroup) {
                taskGroupStatistics.get(scheduler).add(new ArrayList<Float>());
            }
        }

    }
}
