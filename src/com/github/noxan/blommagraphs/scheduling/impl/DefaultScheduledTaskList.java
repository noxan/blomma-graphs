package com.github.noxan.blommagraphs.scheduling.impl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskListStatus;


public class DefaultScheduledTaskList extends ArrayList<ScheduledTask> implements ScheduledTaskList {
    private static final long serialVersionUID = -3333940630170033338L;

    private int cpuCount;

    public DefaultScheduledTaskList(int cpuCount) {
        this.cpuCount = cpuCount;
    }

    @Override
    public ScheduledTask getScheduledTask(TaskGraphNode taskGraphNode) {
        Iterator<ScheduledTask> it = iterator();
        while (it.hasNext()) {
            ScheduledTask scheduledTask = it.next();
            if (scheduledTask.getTaskGraphNode().equals(taskGraphNode)) {
                return scheduledTask;
            }
        }
        return null;
    }

    @Override
    public ScheduledTask getTaskById(int taskId) {
        Iterator<ScheduledTask> it = iterator();
        while (it.hasNext()) {
            ScheduledTask scheduledTask = it.next();
            if (scheduledTask.getTaskId() == taskId) {
                return scheduledTask;
            }
        }
        return null;
    }

    @Override
    public int getCpuCount() {
        return cpuCount;
    }

    @Override
    public boolean isTaskOnCpu(int cpuId, int taskId) {
        Iterator<ScheduledTask> it = iterator();
        while (it.hasNext()) {
            ScheduledTask scheduledTask = it.next();
            if (scheduledTask.getTaskId() == taskId && scheduledTask.getCpuId() == cpuId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsTask(int taskId) {
        Iterator<ScheduledTask> it = iterator();
        while (it.hasNext()) {
            if (it.next().getTaskId() == taskId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsTask(TaskGraphNode node) {
        Iterator<ScheduledTask> it = iterator();
        while (it.hasNext()) {
            if (it.next().getTaskGraphNode() == node) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ScheduledTask getLastScheduledTaskOnCpu(int cpuId) {
        ScheduledTask lastTask = null;

        Iterator<ScheduledTask> it = iterator();
        while (it.hasNext()) {
            ScheduledTask task = it.next();
            if (task.getCpuId() == cpuId
                    && (lastTask == null || task.getStartTime() > lastTask.getStartTime())) {
                lastTask = task;
            }
        }

        return lastTask;
    }

    @Override
    public int getFinishTime() {
        int maxFinishTime = -1;

        for (int cpuId = 0; cpuId < cpuCount; cpuId++) {
            ScheduledTask scheduledTask = getLastScheduledTaskOnCpu(cpuId);
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

    @Override
    public ScheduledTaskList getScheduledTasksOnCpu(int cpuId) {
        DefaultScheduledTaskList ScheduledCpuTasks = new DefaultScheduledTaskList(this.cpuCount);
        for (ScheduledTask task : this) {
            if (task.getCpuId() == cpuId) {
                ScheduledCpuTasks.add(task);
            }
        }
        return ScheduledCpuTasks;
    }

    public ScheduledTaskListStatus validate() {
        for (ScheduledTask task : this) {
            for (TaskGraphNode dependencyTask : task.getTaskGraphNode().getPrevNodes()) {
                if (!containsTask(dependencyTask)) {
                    return ScheduledTaskListStatus.INVALID_DEPENDENCY;
                }
                ScheduledTask dependencyScheduledTask = getScheduledTask(dependencyTask);
                if (task.getStartTime() < dependencyScheduledTask.getFinishTime()) {
                    return ScheduledTaskListStatus.INVALID_DEPENDENCY;
                }
            }
        }

        for (ScheduledTask task : this) {
            if (task.getTaskGraphNode().getDeadLine() < task.getFinishTime()) {
                return ScheduledTaskListStatus.INVALID_DEADLINE;
            }
        }

        return ScheduledTaskListStatus.VALID;
    }
}
