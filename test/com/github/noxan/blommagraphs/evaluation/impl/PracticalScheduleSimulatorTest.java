package com.github.noxan.blommagraphs.evaluation.impl;

import com.github.noxan.blommagraphs.evaluation.ScheduleSimulator;
import com.github.noxan.blommagraphs.generator.TaskGraphGenerator;
import com.github.noxan.blommagraphs.generator.impl.DefaultTaskGraphGenerator;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.dls.DynamicLevelScheduler;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.serializer.impl.DefaultScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;
import org.junit.Before;
import org.junit.Test;


public class PracticalScheduleSimulatorTest {
    private ScheduleSimulator scheduleSimulator;
    private ScheduledTaskList scheduledTasksList;

    @Before
    public void initialize() {
        scheduleSimulator = new PracticalScheduleSimulator(new TimebasedScheduleSimulationWorker());
        SystemMetaInformation systemMetaInformation = new DefaultSystemMetaInformation(2);
        TaskGraphGenerator taskGraphGenerator = new DefaultTaskGraphGenerator();
        Scheduler scheduler = new DynamicLevelScheduler();

        scheduledTasksList = scheduler.schedule(taskGraphGenerator.generator(), systemMetaInformation);
    }

    @Test
    public void testSimulateExecution() throws Exception {
        ScheduledTaskList executedScheduledTaskList = scheduleSimulator.simulateExecution(scheduledTasksList);

        ScheduledTaskListSerializer serializer = new DefaultScheduledTaskListSerializer();
        System.out.println(serializer.serialize(this.scheduledTasksList));
        System.out.println(serializer.serialize(executedScheduledTaskList));

    }
}
