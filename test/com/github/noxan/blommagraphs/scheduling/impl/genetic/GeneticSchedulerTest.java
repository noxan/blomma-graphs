package com.github.noxan.blommagraphs.scheduling.impl.genetic;


import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.Scheduler;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTask;


public class GeneticSchedulerTest {
    private Scheduler scheduler;

    @Before
    public void initialize() {
        List<ScheduledTask> taskList = new ArrayList<ScheduledTask>();

        taskList.add(new DefaultScheduledTask());

        scheduler = new GeneticScheduler(taskList);
    }

    @Test
    public void test() {
        fail("Not yet implemented");
    }

}
