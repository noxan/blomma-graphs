package com.github.noxan.blommagraphs.scheduling.impl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;


public class DefaultScheduledTaskList extends ArrayList<ScheduledTask> implements ScheduledTaskList {
    private static final long serialVersionUID = -3333940630170033338L;

    private int processorCount;

    public DefaultScheduledTaskList(int processorCount) {
        this.processorCount = processorCount;
    }

    @Override
    public int getProcessorCount() {
        return processorCount;
    }

    @Override
    public boolean isTaskOnProcessor(int processorId, int taskId) {
        Iterator<ScheduledTask> it = iterator();
        while (it.hasNext()) {
            ScheduledTask scheduledTask = it.next();
            if (scheduledTask.getTaskId() == taskId && scheduledTask.getCpuId() == processorId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ScheduledTask getLastScheduledTaskOnProcessor(int processorId) {
        ScheduledTask lastTask = null;

        Iterator<ScheduledTask> it = iterator();
        while (it.hasNext()) {
            ScheduledTask task = it.next();
            if (task.getCpuId() == processorId
                    && (lastTask == null || task.getStartTime() > lastTask.getStartTime())) {
                lastTask = task;
            }
        }

        return lastTask;
    }

    @Override
    public int getFinishTime() {
        int maxFinishTime = -1;

        for (int processorId = 0; processorId < processorCount; processorId++) {
            ScheduledTask scheduledTask = getLastScheduledTaskOnProcessor(processorId);
            if (scheduledTask != null) {
                int finishTime = scheduledTask.getFinishTime();
                if (finishTime > maxFinishTime) {
                    maxFinishTime = finishTime;
                }
            }
        }

        return maxFinishTime;
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
