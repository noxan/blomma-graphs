package com.github.noxan.blommagraphs;


import java.io.IOException;

import com.github.noxan.blommagraphs.generator.TaskGraphGenerator;
import com.github.noxan.blommagraphs.generator.impl.DefaultTaskGraphGenerator;
import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.last.LASTScheduler;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.serializer.impl.HTMLSerializer;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;
import com.github.noxan.blommagraphs.utils.FileUtils;


public class GraphVisualizerHTML {

    public static void main(String[] args) throws IOException {
        TaskGraphGenerator taskGraphGenerator = new DefaultTaskGraphGenerator();
        TaskGraph taskGraph = taskGraphGenerator.generator();
        SystemMetaInformation systemMetaInformation = new DefaultSystemMetaInformation(4);
        Scheduler scheduler = new LASTScheduler();
        ScheduledTaskList scheduledTaskList = scheduler.schedule(taskGraph, systemMetaInformation);
        ScheduledTaskListSerializer scheduledTaskListSerializer = new HTMLSerializer();
        FileUtils.writeFile(".", scheduledTaskListSerializer.serialize(scheduledTaskList));

    }
}
