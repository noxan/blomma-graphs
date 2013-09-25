package com.github.noxan.blommagraphs.scheduling.basic.impl.dls;


import java.util.ArrayList;

import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.utils.Tuple;

/**
 * ready pool node to store b-level, earlieststarttime and communicationtime for every cpu
 */
public class ReadyPoolNode {
    private TaskGraphNode node;
    private ArrayList<Integer> earliestStarttime = new ArrayList<Integer>();
    private ArrayList<Integer> communicationTime = new ArrayList<Integer>();
    private int bLevel;

    /**
     * set default values for
     * earlieststarttime Integer.MAX_VALUE
     * communicationtime 0
     * @param node node to be set
     * @param bLevel blevel to be set
     * @param numberOfCpu numberofcpu
     */
    public ReadyPoolNode(TaskGraphNode node, int bLevel, int numberOfCpu) {
        this.node = node;
        this.bLevel = bLevel;
        for (int i = 0; i < numberOfCpu; i++) {
            this.earliestStarttime.add(i, Integer.MAX_VALUE);
            this.communicationTime.add(i, 0);
        }
    }

    /**
     *
     * @return maximum of all dynamiclevels of this readypoolnode
     */
    public Tuple<Integer, Integer> getMaxDynamicLevel() {
        int max = getDynamicLevel(0), cpuId = 0;
        for (int i = 1; i < earliestStarttime.size(); i++) {
            if (getDynamicLevel(i) > max) {
                max = getDynamicLevel(i);
                cpuId = i;
            }
        }
        return new Tuple<Integer, Integer>(max, cpuId);
    }

    /**
     *
     * @param cpuId cpuId depending to the dl
     * @return computed dynamiclevel
     */
    public int getDynamicLevel(int cpuId) {
        return bLevel - (earliestStarttime.get(cpuId) + this.getNode().getDeadLine());
    }

    /**
     *
     * @param cpuId cpuId depending to the earlieststarttime
     * @return starttime of the task on a cpu
     */
    public int getEarliestStarttime(int cpuId) {
        return this.earliestStarttime.get(cpuId);
    }

    /**
     *
     * @param cpuId cpuId depending to the earlieststarttime
     * @param earliestStarttime earlieststarttime to be set
     */
    public void setEarliestStarttime(int cpuId, int earliestStarttime) {
        this.earliestStarttime.set(cpuId, earliestStarttime);
    }

    /**
     *
     * @return Taskgraphnode
     */
    public TaskGraphNode getNode() {
        return node;
    }

    /**
     *
     * @param cpuId cpuId depending to the communicationtime
     * @return communictaiontime
     */
    public int getCommunicationTimes(int cpuId) {
        return communicationTime.get(cpuId);
    }

    /**
     *
     * @param cpuId  cpuId depending to communicationtime
     * @param communicationTime communicationtime to be set
     */
    public void setCommunicationTimes(int cpuId, int communicationTime) {
        this.communicationTime.set(cpuId, communicationTime);
    }
}
