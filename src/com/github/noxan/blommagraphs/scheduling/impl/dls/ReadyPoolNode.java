package com.github.noxan.blommagraphs.scheduling.impl.dls;

import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.utils.Tuple;

import java.util.ArrayList;


public class ReadyPoolNode {
    private TaskGraphNode node;
    private ArrayList<Integer> earliestStarttime = new ArrayList<Integer>();
    private int bLevel;

    public ReadyPoolNode(TaskGraphNode node, int bLevel, int numberOfCpu) {
        this.node = node;
        this.bLevel = bLevel;
        for (int i = 0; i < numberOfCpu; i++) {
            this.earliestStarttime.add(i, Integer.MAX_VALUE);
        }
    }

    public Tuple<Integer, Integer> getMaxDynamicLevel() {
        int max = getDynamicLevel(0), cpuId = 0;
        for (int i = 1; i < earliestStarttime.size(); i++) {
            if (getDynamicLevel(i) > max){
                max = getDynamicLevel(i);
                cpuId = i;
            }
        }
        return new Tuple<Integer, Integer>(max, cpuId);
    }

    public int getDynamicLevel(int cpuId) {
        return bLevel-earliestStarttime.get(cpuId);
    }

    public int getEarliestStarttime(int cpuId) {
        return this.earliestStarttime.get(cpuId);
    }

    public void setEarliestStarttime(int cpuId, int earliestStarttime) {
        this.earliestStarttime.set(cpuId, earliestStarttime);
    }

    public TaskGraphNode getNode() {
        return node;
    }
}
