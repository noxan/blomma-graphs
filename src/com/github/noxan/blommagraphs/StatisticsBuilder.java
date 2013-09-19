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
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;
import com.github.noxan.blommagraphs.utils.TaskGraphFileUtils;


public class StatisticsBuilder {

    private final String statisticsFilePath = "export/statistics.html";

    // TODO: have to be 4! Just use 3 until CustomStreamscheduler is fixed :]
    private final int schedulerCount = 3;
    private final int taskGraphCount = 500;
    private final int taskGraphGroupSize = 100;
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
        FILE_PATH, NODE_COUNT, EDGE_COUNT, CP_LENGTH, ALGORITHM_DURATION, SCHEDULE_DURATION,
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
        // schedulers[3] = new CustomStreamScheduler();

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

                    List<Float> propertiesList = taskGraphStatistics.get(schedulerId).get(
                            taskGroupCounter * taskGraphGroupSize + graphId);

                    // Starttime measurement
                    long currentAlgorithmDuration = System.currentTimeMillis();
                    scheduledTaskList = schedulers[schedulerId].schedule(taskGraphs,
                            systemMetaInformation);
                    currentAlgorithmDuration = (System.currentTimeMillis() - currentAlgorithmDuration);
                    System.out.println("Algorithm duration: " + (float) currentAlgorithmDuration
                            / 1000);

                    // Calculate taskGraphStatistics here.
                    propertiesList.clear();
                    for (int propertyCounter = 0; propertyCounter < Properties.values().length; ++propertyCounter)
                        propertiesList.add(0.0f);

                    propertiesList.set(Properties.FILE_PATH.ordinal(), 0.0f);
                    propertiesList.set(Properties.NODE_COUNT.ordinal(), 0.0f);
                    propertiesList.set(Properties.EDGE_COUNT.ordinal(), 0.0f);
                    propertiesList.set(Properties.CP_LENGTH.ordinal(), 0.0f);
                    propertiesList.set(Properties.ALGORITHM_DURATION.ordinal(),
                            (float) currentAlgorithmDuration / 1000);
                    propertiesList.set(Properties.SCHEDULE_DURATION.ordinal(), 0.0f);
                    propertiesList.set(Properties.SCHEDULE_CP_RATIO.ordinal(), 0.0f);
                    propertiesList.set(Properties.SCHEDULE_CP_VARIANCE.ordinal(), 0.0f);
                    propertiesList.set(Properties.THROUGHPUT.ordinal(), 0.0f);
                    propertiesList.set(Properties.SINGLE_BLOCK_EXECUTION_TIME.ordinal(), 0.0f);
                }
            }

            taskGroupCounter++;
        }
    }
}
