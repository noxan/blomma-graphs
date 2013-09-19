package com.github.noxan.blommagraphs.scheduling.impl;


import org.junit.Before;
import org.junit.Test;

import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;


public class DefaultScheduledTaskListTest {
    private ScheduledTaskList scheduledTaskList;

    @Before
    public void initialize() {
        scheduledTaskList = new DefaultScheduledTaskList(4);
    }

    @Test
    public void testValidate() {
        scheduledTaskList.validate();
    }
}
