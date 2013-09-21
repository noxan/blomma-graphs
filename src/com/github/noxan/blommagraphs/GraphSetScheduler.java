package com.github.noxan.blommagraphs;


import java.io.File;
import java.io.IOException;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.serializer.TaskGraphSerializer;
import com.github.noxan.blommagraphs.graphs.serializer.impl.STGSerializer;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.genetic.GeneticScheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.last.LASTScheduler;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.serializer.impl.DefaultScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;
import com.github.noxan.blommagraphs.utils.FileUtils;
import com.github.noxan.blommagraphs.utils.TaskGraphFileUtils;


/**
 * Schedules all graphs created by GraphSetGenerator.
 * 
 * This class schedules all graphs created by GraphSetGenerator. Schedulers used:
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
        File[] graphFolders = new File(rootGraphFolder).listFiles();

        String schedulesExportFolder = "export/schedules";
        new File(schedulesExportFolder).mkdirs();

        SystemMetaInformation systemMetaInformation = new DefaultSystemMetaInformation(2);
        TaskGraphSerializer serializer = new STGSerializer();
        ScheduledTaskListSerializer scheduledSerializer = new DefaultScheduledTaskListSerializer();

        Scheduler[] schedulers = { new GeneticScheduler(new LASTScheduler()) };

        for (Scheduler scheduler : schedulers) {
            System.out.println("Performing schedules for " + scheduler.getName() + " scheduler...");
            String schedulerExportFolder = schedulesExportFolder + "/" + scheduler.getName();
            new File(schedulerExportFolder).mkdir();

            for (File graphFolder : graphFolders) {
                File graphSetExportFolder = new File(schedulerExportFolder + "/"
                        + graphFolder.getName());
                graphSetExportFolder.mkdir();
                System.out.println("Performing schedules for folder " + graphSetExportFolder);

                long startTime = System.currentTimeMillis();

                File graphFiles[] = graphFolder.listFiles();
                for (int i = 0; i < graphFiles.length; i++) {
                    File graphFile = graphFiles[i];

                    TaskGraph taskGraph = TaskGraphFileUtils.readFile(graphFile.getAbsolutePath(),
                            serializer);

                    ScheduledTaskList scheduledTaskList = scheduler.schedule(taskGraph,
                            systemMetaInformation);
                    String scheduledGraphString = scheduledSerializer.serialize(scheduledTaskList);

                    String exportPathname = graphSetExportFolder + "/" + i + ".sgf";

                    FileUtils.writeFile(exportPathname, scheduledGraphString);
                }

                System.out.println("Done in " + (System.currentTimeMillis() - startTime) + ".");
            }
        }
    }
}
