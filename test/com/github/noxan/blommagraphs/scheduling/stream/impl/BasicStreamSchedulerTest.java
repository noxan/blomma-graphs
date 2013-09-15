package com.github.noxan.blommagraphs.scheduling.stream.impl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.factories.impl.JGraphtTaskGraphFactory;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.impl.last.LASTScheduler;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.serializer.impl.DefaultScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.stream.StreamScheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;
import com.github.noxan.blommagraphs.utils.StreamSchedulableArrayGenerator;


public class BasicStreamSchedulerTest {

    ScheduledTaskListSerializer scheduledTaskListSerialzer;
    StreamScheduler basicStreamScheduler;
    SystemMetaInformation sysInfo;
    int deadLines[];
    TaskGraph taskGraphs[];

    @Before
    public void setUp() throws Exception {
        scheduledTaskListSerialzer = new DefaultScheduledTaskListSerializer();
        basicStreamScheduler = new BasicStreamScheduler(new LASTScheduler());
        sysInfo = new DefaultSystemMetaInformation(3);

        deadLines = new int[2];
        deadLines[0] = 10;
        deadLines[1] = 20;

        taskGraphs = StreamSchedulableArrayGenerator.generateArray(
                JGraphtTaskGraphFactory.makeGraph(), deadLines);
    }

    @Test
    public void testSchedule() {
        Assert.fail("Not yet implemented");
    }

}