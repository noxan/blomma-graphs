package com.github.noxan.blommagraphs;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.serializer.TaskGraphSerializer;
import com.github.noxan.blommagraphs.graphs.serializer.impl.STGSerializer;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.impl.dls.DynamicLevelScheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.genetic.GeneticScheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.last.LASTScheduler;
import com.github.noxan.blommagraphs.scheduling.stream.StreamScheduler;
import com.github.noxan.blommagraphs.scheduling.stream.impl.BasicStreamScheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;
import com.github.noxan.blommagraphs.utils.FileUtils;
import com.github.noxan.blommagraphs.utils.TaskGraphFileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class StatisticsBuilder {

    private class Statistic {
        private String filePath;
        private int nodeCount;
        private float edgeCount;
        private float cpDuration;
        private float algorithmDuration;
        private float singleBlockExecutionTime;
        private float scheduleCpRatio;
        private float scheduleCpVariance;
        private float throughput;
        private float averageCommunicationTime;
    };

    private final String statisticsFilePath = "export/statistics.html";

    // TODO: have to be 4! Just use 3 until CustomStreamscheduler is fixed :]
    private final int schedulerCount = 3;
    private final int taskGraphCount = 500;
    private final int taskGraphGroupSize = 100;
    private final int taskGroupCount = 5;
    private final int cpuCount = 3;
    // Number of TaskGraph copies that are scheduled.
    private final int blockSize = 5;

    private List<List<Statistic>> taskGraphStatistics;
    private List<List<Statistic>> taskGroupStatistics;
    private List<Statistic> schedulerStatistics;

    private StreamScheduler schedulers[];
    private SystemMetaInformation systemMetaInformation;

    private TaskGraphSerializer taskGraphSerializer;

    public enum Properties {
        NODE_COUNT, EDGE_COUNT, CP_LENGTH, ALGORITHM_DURATION, SCHEDULE_CP_RATIO,
        SCHEDULE_CP_VARIANCE, THROUGHPUT, SINGLE_BLOCK_EXECUTION_TIME,
        AVERAGE_COMMUNICATION_TIME
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        StatisticsBuilder statBuilder = new StatisticsBuilder();

        statBuilder.generateHTML();
        statBuilder.buildStatistics();
        statBuilder.generateHTML();
    }

    private StatisticsBuilder() {
        // Build lists
        taskGraphStatistics = new ArrayList<List<Statistic>>();
        taskGroupStatistics = new ArrayList<List<Statistic>>();
        schedulerStatistics = new ArrayList<Statistic>();

        for (int scheduler = 0; scheduler < schedulerCount; ++scheduler) {
            taskGraphStatistics.add(new ArrayList<Statistic>());
            taskGroupStatistics.add(new ArrayList<Statistic>());
            schedulerStatistics.add(new Statistic());

            for (int taskGraph = 0; taskGraph < taskGraphCount; ++taskGraph) {
                taskGraphStatistics.get(scheduler).add(new Statistic());
            }
            for (int taskGroup = 0; taskGroup < taskGroupCount; ++taskGroup) {
                taskGroupStatistics.get(scheduler).add(new Statistic());
            }
        }

        // Create Schedulers
        schedulers = new StreamScheduler[schedulerCount];
        schedulers[0] = new BasicStreamScheduler(new LASTScheduler());
        schedulers[1] = new BasicStreamScheduler(new DynamicLevelScheduler());
        schedulers[2] = new BasicStreamScheduler(new GeneticScheduler(new DynamicLevelScheduler()));
        // schedulers[3] = new CustomStreamScheduler();

        systemMetaInformation = new DefaultSystemMetaInformation(cpuCount);

        taskGraphSerializer = new STGSerializer();
    }

    /**
     * Calculate all statistics and build an html document.
     * 
     * @throws IOException
     */
    private void buildStatistics() throws IOException {
        buildTaskGraphStatistics();
        buildTaskGroupStatistics();
        buildSchedulerStatistics();

    }

    /**
     * Generates statistics for the taskGraphStatistics list by deserializing graphs from disk and
     * scheduling it with all schedulers.
     */
    private void buildTaskGraphStatistics() throws IOException {
        File graphGroupsDirectory = new File("export/graphs");
        File[] graphDirectories = graphGroupsDirectory.listFiles();

        TaskGraph graph;
        // Following task graphs are used for scheduling.
        TaskGraph[] taskGraphs;
        ScheduledTaskList scheduledTaskList;

        int taskGroupCounter = 0;

        for (File graphDirectory : graphDirectories) {
            File[] graphFiles = graphDirectory.listFiles();

            for (int graphId = 0; graphId < taskGraphGroupSize; ++graphId) {

                // Get the current graph
                System.out.println("Deserailizing ...");

                graph = TaskGraphFileUtils.readFile(graphFiles[graphId].getAbsolutePath(),
                        taskGraphSerializer);
                System.out.println("Done.\nGenerating task graph array...");

                // Create copies of this graph which are passed to the stream schedulers.
                taskGraphs = new TaskGraph[blockSize];
                for (int i = 0; i < blockSize; ++i)
                    taskGraphs[i] = graph.clone();

                System.out.println("Done.\nscheduling graph with " + graph.getNodeCount()
                        + " nodes...\n");

                for (int schedulerId = 0; schedulerId < schedulerCount; ++schedulerId) {
                    System.out.println("Calculate taskGraphStatistics here!");

                    Statistic currentStatistic = taskGraphStatistics.get(schedulerId).get(
                            taskGroupCounter * taskGraphGroupSize + graphId);

                    // Starttime measurement
                    long currentAlgorithmDuration = System.currentTimeMillis();
                    scheduledTaskList = schedulers[schedulerId].schedule(taskGraphs,
                            systemMetaInformation);
                    currentAlgorithmDuration = (System.currentTimeMillis() - currentAlgorithmDuration);
                    System.out.println("Algorithm duration: " + (float) currentAlgorithmDuration
                            / 1000);

                    // Calculate taskGraphStatistics here.
                    int criticalPathDuration = calcCriticalPathDuration(graph);

                    currentStatistic.nodeCount = graph.getNodeCount();
                    currentStatistic.edgeCount = graph.getEdgeCount();
                    currentStatistic.cpDuration = criticalPathDuration;
                    currentStatistic.algorithmDuration = currentAlgorithmDuration / 1000;
                    currentStatistic.scheduleCpRatio = (float) scheduledTaskList.getFinishTime()
                            / criticalPathDuration;
                    currentStatistic.scheduleCpVariance = criticalPathDuration
                            - (float) scheduledTaskList.getFinishTime();
                    currentStatistic.averageCommunicationTime = calcAverageCommunicationTime(scheduledTaskList);
                    currentStatistic.throughput = (float) blockSize
                            / scheduledTaskList.getFinishTime();
                    currentStatistic.singleBlockExecutionTime = scheduledTaskList.getFinishTime();
                }
            }

            taskGroupCounter++;
        }
    }

    /**
     * Generates statistics for the taskGroupStatistics list by using taskGraphStatistics.
     */
    private void buildTaskGroupStatistics() {

    }

    /**
     * Generates statistics for the schedulerStatistics list by using taskGroupStatistics.
     */
    private void buildSchedulerStatistics() {

    }

    /**
     * Calculates the critical path duration of a task graph.
     * 
     * @param taskGraph
     * @return Duration of the critical path.
     */

    private String generateTaskGraphStatisticsHTML() {
        return "HTML";
    }

    private String generateTaskGroupStatisticsHTML() {
        return "HTML";
    }

    private String generateSchedulerStatisticsHTML() {
        return "HTML";
    }

    private void generateHTML() throws IOException {
        String taskGraphStatisticsHTML = generateTaskGraphStatisticsHTML();
        String taskGroupStatisticsHTML = generateTaskGroupStatisticsHTML();
        String schedulerStatisticsHTML = generateSchedulerStatisticsHTML();
        String html = "<!DOCTYPE HTML>" +
                      "<html>" +
                      "     <head>" +
                      "         <title>BlommaGraphs - statistics.html</title>" +
                      "         <link rel=\"stylesheet\" media=\"screen\" href=\"../../ressources/bootstrap-3.0.0/dist/css/bootstrap.css\">" +
                      "         <meta charset=\"utf-8, initial-scale=1.0\">" +

                      "         <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->" +
                      "         <script src=\"//code.jquery.com/jquery.js\"></script>" +

                      "         <!-- Include all compiled plugins (below), or include individual files as needed -->" +
                      "         <script src=\"js/bootstrap.min.js\"></script>" +
                      "     </head>" +
                      "     <body>" +
                      "         <h1>BlommaGraphs - Statistics</h1>" +
                      "     </body>" +
                      "</html>";

        System.out.println("Generate statistics.html.");

        new File("export/statistics").mkdirs();
        FileUtils.writeFile("export/statistics/statistics.html", html);
    }

    private int calcCriticalPathDuration(TaskGraph taskGraph) {
        List<TaskGraphEdge> cpEdges = taskGraph.getCriticalPath();
        int duration = 0;
        for (TaskGraphEdge edge : cpEdges) {
            duration += edge.getNextNode().getComputationTime();
        }
        duration += cpEdges.get(0).getPrevNode().getComputationTime();
        return duration;
    }

    /**
     * Calculates the average communication time of a scheduled task list.
     * 
     * @param scheduledTaskList
     * @return The average communication time
     */
    private float calcAverageCommunicationTime(ScheduledTaskList scheduledTaskList) {
        int totalCommunicationTime = 0;
        for (ScheduledTask task : scheduledTaskList) {
            totalCommunicationTime += task.getCommunicationTime();
        }
        return (float) totalCommunicationTime / scheduledTaskList.size();
    }
}

