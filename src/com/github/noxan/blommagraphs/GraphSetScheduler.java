package com.github.noxan.blommagraphs;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.serializer.TaskGraphSerializer;
import com.github.noxan.blommagraphs.graphs.serializer.impl.STGSerializer;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.genetic.GeneticDLSScheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.genetic.GeneticLASTScheduler;
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
    public static void main(String[] args) throws IOException {
        GraphSetScheduler graphSetScheduler = new GraphSetScheduler();
        graphSetScheduler.start();
    }

    private List<Class<? extends Scheduler>> schedulers;
    private SystemMetaInformation systemMetaInformation;

    public GraphSetScheduler() {
        schedulers = new ArrayList<Class<? extends Scheduler>>();
        schedulers.add(GeneticLASTScheduler.class);
        schedulers.add(GeneticDLSScheduler.class);

        systemMetaInformation = new DefaultSystemMetaInformation(2);
    }

    public void start() {
        // Get folders and paths
        String rootGraphFolder = "export/graphs";
        File[] graphFolders = new File(rootGraphFolder).listFiles();

        String schedulesExportFolder = "export/schedules";
        new File(schedulesExportFolder).mkdirs();

        for (Class<? extends Scheduler> schedulerClass : schedulers) {
            String schedulerName = schedulerClass.getSimpleName();

            String schedulerExportFolder = schedulesExportFolder + "/" + schedulerName;
            new File(schedulerExportFolder).mkdir();

            for (File graphFolder : graphFolders) {
                File graphSetExportFolder = new File(schedulerExportFolder + "/"
                        + graphFolder.getName());
                graphSetExportFolder.mkdir();

                File graphFiles[] = graphFolder.listFiles();
                GraphSetSchedulerWorker worker = new GraphSetSchedulerWorker(schedulerClass,
                        systemMetaInformation, graphSetExportFolder, graphFiles);
                new Thread(worker).start();
            }
        }
    }

    private class GraphSetSchedulerWorker implements Runnable {
        private Scheduler scheduler;
        private SystemMetaInformation systemMetaInformation;
        private File graphSetExportFolder;
        private File graphFiles[];

        private TaskGraphSerializer serializer;
        private ScheduledTaskListSerializer scheduledSerializer;

        public GraphSetSchedulerWorker(Class<? extends Scheduler> schedulerClass,
                SystemMetaInformation systemMetaInformation, File graphSetExportFolder,
                File graphFiles[]) {
            try {
                Constructor<? extends Scheduler> constructor = schedulerClass.getConstructor();
                this.scheduler = constructor.newInstance();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            this.systemMetaInformation = systemMetaInformation;
            this.graphSetExportFolder = graphSetExportFolder;
            this.graphFiles = graphFiles;
            serializer = new STGSerializer();
            scheduledSerializer = new DefaultScheduledTaskListSerializer();
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();

            System.out.println("Performing schedules for folder " + graphSetExportFolder);

            for (int i = 0; i < graphFiles.length; i++) {
                File graphFile = graphFiles[i];

                try {
                    TaskGraph taskGraph = TaskGraphFileUtils.readFile(graphFile.getAbsolutePath(),
                            serializer);

                    ScheduledTaskList scheduledTaskList = scheduler.schedule(taskGraph,
                            systemMetaInformation);
                    String scheduledGraphString = scheduledSerializer.serialize(scheduledTaskList);

                    String exportPathname = graphSetExportFolder + "/" + i + ".sgf";

                    FileUtils.writeFile(exportPathname, scheduledGraphString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Schedules for folder in " + graphSetExportFolder + " done in "
                    + (System.currentTimeMillis() - startTime));
        }
    }
}
