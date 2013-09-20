package com.github.noxan.blommagraphs.evaluation.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.generator.TaskGraphGenerator;
import com.github.noxan.blommagraphs.generator.impl.DefaultTaskGraphGenerator;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.dls.DynamicLevelScheduler;
import com.github.noxan.blommagraphs.scheduling.serializer.impl.DefaultScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;


public class PracticalScheduleSimulatorTest {
    private PracticalScheduleSimulator scheduleSimulator;
    private ScheduledTaskList scheduledTasksList;

    @Before
    public void initialize() {
        scheduleSimulator = new PracticalScheduleSimulator();
        SystemMetaInformation systemMetaInformation = new DefaultSystemMetaInformation(2);
        TaskGraphGenerator taskGraphGenerator = new DefaultTaskGraphGenerator();
        Scheduler scheduler = new DynamicLevelScheduler();

        scheduledTasksList = scheduler.schedule(taskGraphGenerator.generator(),
                systemMetaInformation);
    }

    @Test
    public void testSimulateExecution() throws Exception {
        System.out.println(new DefaultScheduledTaskListSerializer()
                .serialize(this.scheduledTasksList));

        Map<Long, TaskGraphNode> executedScheduledTaskList = scheduleSimulator
                .simulateExecution(scheduledTasksList);

        List<Long> timeList = new ArrayList<Long>(executedScheduledTaskList.keySet());
        Collections.sort(timeList);

        for (Long time : timeList) {
            System.out.println(time + "\t" + executedScheduledTaskList.get(time));
        }
    }
}
