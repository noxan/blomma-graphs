package com.github.noxan.blommagraphs.scheduling.impl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;


public class DefaultScheduledTaskList extends ArrayList<ScheduledTask> implements ScheduledTaskList {
    private static final long serialVersionUID = -3333940630170033338L;

    @Override
    public boolean isTaskOnProcessor(int processorId, int taskId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void add(int index, ScheduledTask element) {
        super.add(index, element);
        Collections.sort(this);
    }

    @Override
    public boolean add(ScheduledTask e) {
        boolean result = super.add(e);
        Collections.sort(this);
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends ScheduledTask> c) {
        boolean result = super.addAll(c);
        Collections.sort(this);
        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends ScheduledTask> c) {
        boolean result = super.addAll(index, c);
        Collections.sort(this);
        return result;
    }
}
