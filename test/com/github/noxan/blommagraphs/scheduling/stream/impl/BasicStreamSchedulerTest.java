package com.github.noxan.blommagraphs.scheduling.stream.impl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.factories.impl.DefaultTaskGraphFactory;
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
                DefaultTaskGraphFactory.makeGraph(), deadLines);
    }

    @Test
    public void testSchedule() {
        ScheduledTaskList scheduledList = basicStreamScheduler.schedule(taskGraphs, sysInfo);
        String expectedString = "0 0 0\n1 0 1\n2 0 2\n3 1 4\n3 2 5\n12 0 8\n17 1 3\n"
                + "19 2 6\n22 1 11\n23 1 12\n32 0 7\n33 1 18\n34 2 9\n42 0 14\n44 2 10\n"
                + "45 2 15\n52 0 13\n57 0 17\n60 2 16\n75 2 19\n85 2 20\n86 2 21\n";
        Assert.assertEquals(expectedString, scheduledTaskListSerialzer.serialize(scheduledList));
    }
}
