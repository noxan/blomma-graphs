package com.github.noxan.blommagraphs.scheduling.impl;


import java.util.ArrayList;

import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;


public class DefaultScheduledTaskList extends ArrayList<ScheduledTask> implements ScheduledTaskList {
    private static final long serialVersionUID = -3333940630170033338L;

    @Override
    public boolean isTaskOnProcessor(int processorId, int taskId) {
        // TODO Auto-generated method stub
        return false;
    }
}
