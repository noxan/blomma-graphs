package com.github.noxan.blommagraphs;


import java.io.File;
import java.io.IOException;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.scheduling.basic.impl.genetic.GeneticScheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.last.LASTScheduler;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.serializer.impl.DefaultScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;
import com.github.noxan.blommagraphs.serializer.TaskGraphSerializer;
import com.github.noxan.blommagraphs.serializer.impl.STGSerializer;
import com.github.noxan.blommagraphs.utils.FileUtils;
import com.github.noxan.blommagraphs.utils.TaskGraphFileUtils;


/**
 * Schedules all graphs created by GraphSetGenerator.
 * 
 * This class schedules all graphs created by GraphSetGenerator. Schedulers
 * used:
 * <ul>
 * <li>DLS</li>
 * <li>LAST</li>
 * <li>Genetic</li>
 * </ul>
 * 
 * @author namelessvoid
 * 
 */
public class GraphSetScheduler {

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // Get folders and paths
        String rootGraphFolder = "export/graphs";
        File rootFile = new File(rootGraphFolder);
        File graphFolders[] = rootFile.listFiles();
        File graphFiles[] = null;

        String lastSchedulFolder = "export/scheduled/last";
        String geneticSchedulFolder = "export/scheduled/genetic";
        new File(lastSchedulFolder).mkdirs();
        new File(geneticSchedulFolder).mkdirs();

        // Create schedulers
        LASTScheduler lastScheduler = new LASTScheduler();
        GeneticScheduler geneticScheduler = new GeneticScheduler(lastScheduler);

        String scheduledGraphString = null;

        TaskGraph taskGraph = null;
        SystemMetaInformation systemInfo = new DefaultSystemMetaInformation(2);
        TaskGraphSerializer serializer = new STGSerializer();
        ScheduledTaskListSerializer scheduledSerializer = new DefaultScheduledTaskListSerializer();

        // Deserialize all graphs, schedule them and write them back to disk.
        for (int i = 0; i < graphFolders.length; ++i) {
            graphFiles = graphFolders[i].listFiles();

            for (int j = 0; j < graphFiles.length; ++j) {
                // Create folder
                new File(lastSchedulFolder + "/" + Integer.toString(i)).mkdirs();

                // Deserialize
                taskGraph = TaskGraphFileUtils
                        .readFile(graphFiles[j].getAbsolutePath(), serializer);

                // Schedule & write back for LAST scheduler
                scheduledGraphString = scheduledSerializer.serialize(lastScheduler.schedule(
                        taskGraph, systemInfo));
                FileUtils.writeFile(
                        lastSchedulFolder + "/" + Integer.toString(i) + "/" + Integer.toString(j)
                                + ".sgf", scheduledGraphString);

                // Schedule & write back for genetic scheduler
                // scheduledGraphString =
                // scheduledSerializer.serialize(geneticScheduler.schedule(
                // taskGraph, systemInfo));
                // FileUtils.writeFile(geneticSchedulFolder +
                // Integer.toString(j),
                // scheduledGraphString);

            }
        }
    }
}
