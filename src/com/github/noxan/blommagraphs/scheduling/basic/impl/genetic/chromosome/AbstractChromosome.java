package com.github.noxan.blommagraphs.scheduling.basic.impl.genetic.chromosome;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTask;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTaskList;


public abstract class AbstractChromosome implements Chromosome {
    private List<List<TaskGraphNode>> genes;

    private int cpuCount;
    private TaskGraph taskGraph;

    public AbstractChromosome(int cpuCount, TaskGraph taskGraph) {
        this.cpuCount = cpuCount;
        this.taskGraph = taskGraph;
        genes = new ArrayList<List<TaskGraphNode>>();
        for (int i = 0; i < cpuCount; i++) {
            genes.add(new ArrayList<TaskGraphNode>());
        }
    }

    protected void addTaskToCpu(int cpu, TaskGraphNode task) {
        genes.get(cpu).add(task);
    }

    public int getCpuForTask(TaskGraphNode task) {
        for (int cpuId = 0; cpuId < genes.size(); cpuId++) {
            if (genes.get(cpuId).contains(task)) {
                return cpuId;
            }
        }
        return -1; // TODO: should not happen but not nice
    }

    private List<TaskGraphNode> createReadyTaskList(TaskGraph taskGraph,
            ScheduledTaskList scheduledTaskList) {
        List<TaskGraphNode> readyList = new ArrayList<TaskGraphNode>();

        for (TaskGraphNode taskNode : taskGraph.getNodeSet()) {
            // just unscheduled tasks
            if (!scheduledTaskList.containsTask(taskNode.getId())) {
                boolean taskReady = true;
                // check all dependencies
                for (TaskGraphNode prevTaskNode : taskNode.getPrevNodes()) {
                    // break if previous task has not been scheduled
                    if (!scheduledTaskList.containsTask(prevTaskNode.getId())) {
                        taskReady = false;
                        break;
                    }
                }
                if (taskReady) {
                    readyList.add(taskNode);
                }
            }
        }

        Collections.sort(readyList);
        return readyList;
    }

    private void decode(TaskGraphNode taskNode, ScheduledTaskList scheduledTaskList,
            Set<TaskGraphNode> unscheduledNodes) {

        if (unscheduledNodes.remove(taskNode)) {
            // schedule current task node
            int cpuId = getCpuForTask(taskNode);
            int communicationTime = 0;
            int startTime = 0;

            // iterate over all previous nodes (dependencies) and determine maximum of the sums of
            // finishTime and communicationTime
            Set<TaskGraphEdge> prevEdges = taskNode.getPrevEdges();
            Iterator<TaskGraphEdge> it = prevEdges.iterator();
            // set startTime to MAX_INT to use the value of the first iteration for sure
            if (it.hasNext()) {
                startTime = Integer.MAX_VALUE;
            }
            // search for best connection to dependencies
            while (it.hasNext()) {
                int tempCommunicationTime = 0;
                int tempFinishTime = 0;

                TaskGraphEdge prevEdge = it.next();
                TaskGraphNode prevNode = prevEdge.getPrevNode();
                ScheduledTask prevScheduledTask = scheduledTaskList.getTaskById(prevNode.getId());
                // just if previous task is scheduled on another CPU, else it stays zero
                if (prevScheduledTask.getCpuId() != cpuId) {
                    tempCommunicationTime = prevEdge.getCommunicationTime();
                }
                tempFinishTime = prevScheduledTask.getFinishTime() + tempCommunicationTime;

                // use minimum startTime for the next task
                if (tempFinishTime < startTime) {
                    // check if CPU is free
                    ScheduledTask prevTaskOnCpu = scheduledTaskList
                            .getLastScheduledTaskOnCpu(cpuId);
                    if (prevTaskOnCpu == null || prevTaskOnCpu.getFinishTime() <= tempFinishTime) {
                        startTime = tempFinishTime;
                    } else {
                        startTime = prevTaskOnCpu.getFinishTime() + communicationTime;
                    }
                    communicationTime = tempCommunicationTime;
                }
            }

            scheduledTaskList.add(new DefaultScheduledTask(startTime, cpuId, communicationTime,
                    taskNode));
        }

        // select next task node
        List<TaskGraphNode> readyList = createReadyTaskList(taskGraph, scheduledTaskList);
        Iterator<TaskGraphNode> it = readyList.iterator();
        while (it.hasNext()) {
            TaskGraphNode nextTaskNode = it.next();
            if (unscheduledNodes.contains(nextTaskNode)) {
                decode(nextTaskNode, scheduledTaskList, unscheduledNodes);
            }
        }
    }

    @Override
    public ScheduledTaskList decode() {
        ScheduledTaskList scheduledTaskList = new DefaultScheduledTaskList(cpuCount);

        Set<TaskGraphNode> unscheduledNodes = taskGraph.getNodeSet();

        decode(taskGraph.getFirstNode(), scheduledTaskList, unscheduledNodes);

        // TODO: maybe add a readyNode list to select from?!

        return scheduledTaskList;
    }

    @Override
    public void swapMutate() {
        Random random = new Random(System.nanoTime());

        // search only in lists with elements
        List<Integer> validGeneList = new ArrayList<Integer>();
        for (int index = 0; index < genes.size(); index++) {
            if (genes.get(index).size() > 0) {
                validGeneList.add(index);
            }
        }

        int x1 = validGeneList.get(random.nextInt(validGeneList.size()));
        int y1 = random.nextInt(genes.get(x1).size());
        TaskGraphNode value1 = genes.get(x1).remove(y1);

        // search only in lists with elements
        validGeneList.clear();
        for (int index = 0; index < genes.size(); index++) {
            if (genes.get(index).size() > 0) {
                validGeneList.add(index);
            }
        }

        int x2 = validGeneList.get(random.nextInt(validGeneList.size()));
        int y2 = random.nextInt(genes.get(x2).size());
        TaskGraphNode value2 = genes.get(x2).remove(y2);

        if (y1 > genes.get(x1).size()) {
            genes.get(x1).add(value2);
        } else {
            genes.get(x1).add(y1, value2);
        }

        if (y1 > genes.get(x2).size()) {
            genes.get(x2).add(value1);
        } else {
            genes.get(x2).add(y2, value1);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Chromosome[");
        sb.append(genes.size());
        sb.append("]");

        int p = 0;
        for (List<TaskGraphNode> taskGraphNodeList : genes) {
            sb.append("\np");
            sb.append(p++);
            sb.append(" (");
            for (TaskGraphNode taskGraphNode : taskGraphNodeList) {
                sb.append(taskGraphNode.getId());
                sb.append(" ");
            }
            sb.append(")");
        }

        return sb.toString();
    }

    @Override
    public int compareTo(Chromosome other) {
        return decode().getFinishTime() - other.decode().getFinishTime();
    }
}
