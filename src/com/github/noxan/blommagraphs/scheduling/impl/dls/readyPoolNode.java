package com.github.noxan.blommagraphs.scheduling.impl.dls;

import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.utils.Tuple;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Freax
 * Date: 07.09.13
 * Time: 12:07
 * To change this template use File | Settings | File Templates.
 */
public class readyPoolNode {
    private TaskGraphNode node;
    private ArrayList<Integer> earliestStarttime = new ArrayList<Integer>();
    private int bLevel;

    public readyPoolNode(TaskGraphNode node, int bLevel) {
        this.node = node;
        this.bLevel = bLevel;
    }

    public Tuple<Integer, Integer> getMaxDynamicLevel() {
        int max = 0, cpuId = 0, temp;
        for (int i = 0; i < earliestStarttime.size(); i++) {
            if ((temp = earliestStarttime.get(i)) > max){
                max = temp;
                cpuId = i;
            }
        }
        return new Tuple<Integer, Integer>(max, cpuId);
    }

    public int getDynamicLevel(int cpuId) {
        return bLevel-earliestStarttime.get(cpuId);
    }

    public void setEarliestStarttime(int cpuId, int earliestStarttime) {
        this.earliestStarttime.set(cpuId, earliestStarttime);
    }

    public TaskGraphNode getNode() {
        return node;
    }
}
