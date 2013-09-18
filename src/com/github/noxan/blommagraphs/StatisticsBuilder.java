package com.github.noxan.blommagraphs;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.serializer.TaskGraphSerializer;
import com.github.noxan.blommagraphs.graphs.serializer.impl.STGSerializer;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.impl.dls.DynamicLevelScheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.genetic.GeneticScheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.last.LASTScheduler;
import com.github.noxan.blommagraphs.scheduling.stream.StreamScheduler;
import com.github.noxan.blommagraphs.scheduling.stream.impl.BasicStreamScheduler;
import com.github.noxan.blommagraphs.scheduling.stream.impl.CustomStreamScheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;
import com.github.noxan.blommagraphs.utils.TaskGraphFileUtils;


public class StatisticsBuilder {

    private final String statisticsFilePath = "export/statistics.html";

    private final int schedulerCount = 4;
    private final int taskGraphCount = 500;
    private final int taskGroupCount = 5;
    private final int cpuCount = 3;
    // Number of TaskGraph copies that are scheduled.
    private final int blockSize = 5;

    private List<List<List<Float>>> taskGraphStatistics;
    private List<List<List<Float>>> taskGroupStatistics;
    private List<List<Float>> schedulerStatistics;

    private StreamScheduler schedulers[];
    private SystemMetaInformation systemMetaInformation;

    private TaskGraphSerializer taskGraphSerializer;

    public enum Properties {
        ALGORITHM_DURATION, SCHEDULE_DURATION, NODE_COUNT, FILE_PATH, EDGE_COUNT, CP_LENGTH,
        SCHEDULE_CP_RATIO, SCHEDULE_CP_VARIANCE, THROUGHPUT, SINGLE_BLOCK_EXECUTION_TIME
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        StatisticsBuilder statBuilder = new StatisticsBuilder();
        statBuilder.buildStatistics();

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

        // Create Schedulers
        schedulers = new StreamScheduler[schedulerCount];
        schedulers[0] = new BasicStreamScheduler(new LASTScheduler());
        schedulers[1] = new BasicStreamScheduler(new DynamicLevelScheduler());
        schedulers[2] = new BasicStreamScheduler(new GeneticScheduler(new DynamicLevelScheduler()));
        schedulers[3] = new CustomStreamScheduler();

        systemMetaInformation = new DefaultSystemMetaInformation(cpuCount);

        taskGraphSerializer = new STGSerializer();
    }

    private void buildStatistics() throws IOException {
        File graphGroupsDirectory = new File("export/graphs");
        File[] graphDirectories = graphGroupsDirectory.listFiles();

        TaskGraph graph;
        // Following task graphs are used for scheduling.
        TaskGraph[] taskGraphs;
        ScheduledTaskList scheduledTaskList;

        for (File graphDirectory : graphDirectories) {
            for (File graphFile : graphDirectory.listFiles()) {

                // Get the current graph
                graph = TaskGraphFileUtils.readFile(graphFile.getAbsolutePath(),
                        taskGraphSerializer);
                // Create copies of this graph which are passed to the stream schedulers.
                taskGraphs = new TaskGraph[blockSize];
                for (int i = 0; i < blockSize; ++i)
                    taskGraphs[i] = graph.clone();

                for (StreamScheduler scheduler : schedulers) {
                    scheduledTaskList = scheduler.schedule(taskGraphs, systemMetaInformation);
                }
            }
        }
    }
}
