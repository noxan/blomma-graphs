package com.github.noxan.blommagraphs;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.factories.impl.DefaultTaskGraphFactory;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.last.LASTScheduler;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.serializer.impl.HTMLSerializer;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;
import com.github.noxan.blommagraphs.utils.FileUtils;

import java.io.IOException;


public class GraphVisualizerHTML {

    public static void main(String[] args) throws IOException {
        TaskGraph taskGraph = DefaultTaskGraphFactory.makeGraph();
        SystemMetaInformation systemMetaInformation = new DefaultSystemMetaInformation(3);

        Scheduler scheduler = new LASTScheduler();
        ScheduledTaskList scheduledTaskList = scheduler.schedule(taskGraph, systemMetaInformation);

        ScheduledTaskListSerializer scheduledTaskListSerializer = new HTMLSerializer();
        FileUtils.writeFile("./serialized.html",
                scheduledTaskListSerializer.serialize(scheduledTaskList));

        System.out.println("Done.");

    }
}
