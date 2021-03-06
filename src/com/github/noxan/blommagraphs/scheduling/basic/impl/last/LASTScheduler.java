package com.github.noxan.blommagraphs.scheduling.basic.impl.last;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.ContainsNoEdgeException;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.impl.DefaultScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class LASTScheduler implements Scheduler {
    private SystemMetaInformation systemInformation;
    private TaskGraph taskGraph;

    protected List<List<LASTNode>> groups;
    protected List<List<LASTNode>> frontiers;
    private Set<TaskGraphNode> nodeSet;

    @Override
    public ScheduledTaskList schedule(TaskGraph graph, SystemMetaInformation systemMetaInformation) {
        initialize(graph, systemMetaInformation);

        // Add the first node to cpu 0:
        LASTNode node = new LASTNode(taskGraph.getFirstNode());
        node.setCpuId(0);
        node.setStartTime(0);
        groups.get(0).add(node);

        while (groupsSize() != nodeSet.size()) {
            updateFrontiers();
            LASTNode highestNode = highestLastNodeByDNode();
            // add scheduled node to the calculated group
            findEarliestStartTimeCpu(highestNode);
            groups.get(highestNode.getCpuId()).add(highestNode);
        }

        ScheduledTaskList list = new DefaultScheduledTaskList(systemMetaInformation.getCpuCount());
        for (List<LASTNode> group : groups) {
            for (LASTNode listNode : group) {
                list.add(listNode);
            }
        }
        return list;
    }

    /**
     * Find the cpu which allows the earliest start time for node.
     * 
     * Hint: The cpu id as well as the earliest start time are saved in the node. This method
     * iterates over all cpus and searches the cpu which allowes the earliest start time for the
     * node. It also considers all dependencies so the task cannot start until all its dependencies
     * have finished.
     * 
     * @param node The LASTNode for which the optimal cpu is searched.
     * @return The id of the cpu.
     */
    public int findEarliestStartTimeCpu(LASTNode node) {
        node.setStartTime(0);
        node.setCpuId(0);

        List<LASTNode> group = null;
        // check first starttime for every cpu
        for (int currentCpuId = 0; currentCpuId < groups.size(); ++currentCpuId) {
            group = groups.get(currentCpuId);

            // get last task
            int firstStarttime = 0;
            if (group.size() > 0) {
                LASTNode lastTask = group.get(group.size() - 1);
                firstStarttime = lastTask.getStartTime() + lastTask.getComputationTime();
            }

            // check with the dependencies to other cpus
            int latestDependencyTime = 0;
            int actualCommunicationTime = 0;

            for (List<LASTNode> otherGroup : groups) {
                // just if its not the actual cpu
                if (otherGroup != group) {
                    int latestDependencyTimePerCpu = 0;
                    int currentCommunicationTimePerCpu = 0;

                    // check all dependent tasks
                    for (LASTNode task : otherGroup) {
                        int currentDependencyTimePerTask = 0;
                        int currentCommunicationTime = 0;

                        if (taskGraph
                                .containsEdge(task.getTaskGraphNode(), node.getTaskGraphNode()))
                            try {
                                // get dependency time for current task
                                currentCommunicationTime = taskGraph.findEdge(
                                        task.getTaskGraphNode(), node.getTaskGraphNode())
                                        .getCommunicationTime();

                                currentDependencyTimePerTask = task.getStartTime()
                                        + task.getComputationTime() + currentCommunicationTime;

                            } catch (ContainsNoEdgeException e) {
                                e.printStackTrace();
                            }

                        // get the latest dependency time per cpu
                        if (currentDependencyTimePerTask > latestDependencyTimePerCpu) {
                            latestDependencyTimePerCpu = currentDependencyTimePerTask;
                            currentCommunicationTimePerCpu = currentCommunicationTime;
                        }
                    }
                    // get the latest dependency time in total
                    if (latestDependencyTimePerCpu > latestDependencyTime) {
                        latestDependencyTime = latestDependencyTimePerCpu;
                        actualCommunicationTime = currentCommunicationTimePerCpu;
                    }
                }
            }
            // compare first start time with latest dependency time
            if (latestDependencyTime > firstStarttime) {
                firstStarttime = latestDependencyTime;
                
            } else if (latestDependencyTime < firstStarttime) {
                // calculate communication time that still has to be done.
                actualCommunicationTime = 0;
            } else {
                // calculate communication time that still has to be done.
                actualCommunicationTime = latestDependencyTime - firstStarttime;
            }
            if (currentCpuId == 0 || node.getStartTime() > firstStarttime) {
                node.setCommunicationTime(actualCommunicationTime);
                node.setCpuId(currentCpuId);
                node.setStartTime(firstStarttime);
            }
        }
        return node.getCpuId();
    }

    /**
     * 
     * @return
     */
    public int groupsSize() {
        int size = 0;

        for (int cpuId = 0; cpuId < groups.size(); ++cpuId) {
            size += groups.get(cpuId).size();
        }
        return size;
    }

    /**
     * Takes a TaskGraph and SystemMetaInformation and initializes the scheduler.
     * 
     * @param graph
     * @param systemMetaInformation
     */
    protected void initialize(TaskGraph graph, SystemMetaInformation systemMetaInformation) {
        systemInformation = systemMetaInformation;
        taskGraph = graph;
        int cpuCount = systemMetaInformation.getCpuCount();

        groups = new ArrayList<List<LASTNode>>();
        frontiers = new ArrayList<List<LASTNode>>();

        for (int i = 0; i < cpuCount; ++i) {
            groups.add(new ArrayList<LASTNode>());
            frontiers.add(new ArrayList<LASTNode>());
        }

        nodeSet = graph.getNodeSet();
    }

    /**
     * Update all frontiers for all cpus.
     * 
     * Adds a new LASTNode to a frontier if its task is adjacent to a scheduled task in the related
     * group. If the task is already scheduled (-> in the cpu group) it is not added.
     */
    protected void updateFrontiers() {
        for (List<LASTNode> frontier : frontiers) {
            frontier.clear();
        }

        LASTNode lastNode = null;
        List<LASTNode> group = null;

        for (TaskGraphNode graphNode : nodeSet) {
            if (!alreadyScheduled(graphNode) && hasDepenciesSatisfied(graphNode)) {
                for (int cpuId = 0; cpuId < groups.size(); ++cpuId) {
                    group = groups.get(cpuId);
                    for (int j = 0; j < group.size(); ++j) {
                        lastNode = group.get(j);

                        if (taskGraph.containsEdge(lastNode.getTaskGraphNode(), graphNode)
                                || taskGraph.containsEdge(graphNode, lastNode.getTaskGraphNode())) {
                            frontiers.get(cpuId).add(new LASTNode(graphNode));
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if a given TaskGraphNode was already scheduled.
     * 
     * @return
     */
    protected boolean alreadyScheduled(TaskGraphNode taskGraphNode) {
        for (List<LASTNode> group : groups) {
            if (containsTask(group, taskGraphNode))
                return true;
        }
        return false;
    }

    /**
     * This method checks if all dependencies of a TaskGraphNode are satisfied (parent tasks have
     * already been scheduled to cpus).
     * 
     * @param taskGraphNode
     * @return True if dependencies satisfied otherwise false
     */
    protected boolean hasDepenciesSatisfied(TaskGraphNode taskGraphNode) {
        for (TaskGraphNode dependency : taskGraphNode.getPrevNodes()) {
            if (!alreadyScheduled(dependency))
                return false;
        }
        return true;
    }

    /**
     * Checks if a list contains a LASTNode which references given TaskGraphNode.
     * 
     * @param lastNodeList The list that is checked.
     * @param taskGraphNode The TaskGraphNode that's searched for.
     * @return
     */
    protected boolean containsTask(List<LASTNode> lastNodeList, TaskGraphNode taskGraphNode) {
        for (LASTNode lastNode : lastNodeList) {
            if (lastNode.getTaskGraphNode() == taskGraphNode)
                return true;
        }
        return false;
    }

    /**
     * Find the LASTNode with highest D_NODE value of all frontiers.
     * 
     * This method calculates the D_NODE value for every LASTNode in all frontiers and returns the
     * LASTNode with the highest D_NODE value. If all frontiers are empty, it will returns null.
     * 
     * @return LASTNode with highest D_NODE value or null if no LASTNode can be found.
     */
    protected LASTNode highestLastNodeByDNode() {
        LASTNode maxNode = null;

        for (List<LASTNode> frontier : frontiers) {
            for (LASTNode currNode : frontier) {
                currNode.setDNode(calcDNode(currNode));

                if (maxNode == null || maxNode.getDNode() < currNode.getDNode())
                    maxNode = currNode;
            }
        }
        return maxNode;
    }

    /**
     * This method compares the strengths of the delivered node from the frontiers and returns the
     * destination cpu where the strength is greater.
     * 
     * @param node
     * @return
     */
    protected int findCpuForNode(LASTNode node) {
        int cpuId = 0;
        float strength = 0f;
        ArrayList<Integer> frontierList = new ArrayList<Integer>();

        for (int i = 0; i < systemInformation.getCpuCount(); i++) {
            for (int j = 0; j < frontiers.get(i).size(); j++) {
                if (frontiers.get(i).get(j) == node) {
                    frontierList.add(i);
                    break;
                }
            }
        }

        for (int i = 0; i < frontierList.size(); i++) {
            float strengthFrontier = calcStrength(node, frontierList.get(i));

            if (strengthFrontier > strength) {
                strength = strengthFrontier;
                cpuId = frontierList.get(i);
            }
        }
        return cpuId;
    }

    /**
     * Calculates the D_NODE value used to select the next task that is scheduled.
     * 
     * @param node The node for which the D_NODE value is calculated.
     * @return D_NODE value as float.
     */
    protected float calcDNode(LASTNode node) {
        int prevNumeratorSum = 0;
        int prevDenominatorSum = 0;
        int nextNumeratorSum = 0;
        int nextDenominatorSum = 0;

        int communicationTime = 0;

        for (List<LASTNode> group : groups) {
            for (LASTNode prevNode : group) {
                if (taskGraph.containsEdge(prevNode.getTaskGraphNode(), node.getTaskGraphNode())) {
                    try {
                        communicationTime = taskGraph.findEdge(prevNode.getTaskGraphNode(),
                                node.getTaskGraphNode()).getCommunicationTime();
                        prevNumeratorSum += communicationTime * calcDEdge(prevNode, node);
                    } catch (ContainsNoEdgeException e) {
                        e.printStackTrace();
                    }
                }
            }

            for (LASTNode nextNode : group) {
                if (taskGraph.containsEdge(node.getTaskGraphNode(), nextNode.getTaskGraphNode())) {
                    try {
                        communicationTime = taskGraph.findEdge(node.getTaskGraphNode(),
                                nextNode.getTaskGraphNode()).getCommunicationTime();

                        nextNumeratorSum += communicationTime * calcDEdge(node, nextNode);
                    } catch (ContainsNoEdgeException e) {
                        e.printStackTrace();
                    }
                }
            }
        } // for (group : groups)

        // Calculate denomiator sums
        for (TaskGraphEdge edge : node.getTaskGraphNode().getPrevEdges())
            prevDenominatorSum += edge.getCommunicationTime();
        for (TaskGraphEdge edge : node.getTaskGraphNode().getNextEdges())
            nextDenominatorSum += edge.getCommunicationTime();

        double result = ((double) (prevNumeratorSum + nextNumeratorSum))
                / ((prevDenominatorSum + nextDenominatorSum));

        return (float) result - node.getTaskGraphNode().getDeadLine();
    }

    /**
     * Calculates the D_EDGE value which says if the edge between node1 and node2 is defined. An
     * edge is defined if either node1 or node2 is already scheduled on a CPU.
     * 
     * @param node1
     * @param node2
     * @return 1 if the edge is defined or 0 if it's not defined.
     */
    protected int calcDEdge(LASTNode node1, LASTNode node2) {
        // saves if node1 or node2 is scheduled.
        int node1scheduled = 0;
        int node2scheduled = 0;

        // Iterate through groups and check if node1 or node2 is included.
        for (List<LASTNode> group : groups) {
            if (group.contains(node1)) {
                node1scheduled = 1;
            }
            if (group.contains(node2)) {
                node2scheduled = 1;
            }
        }

        // If either node1 or node2 is scheduled, return 1, else return 0.
        return node1scheduled ^ node2scheduled;
    }

    /**
     * Calculates how much node1 is connected to a cpu. strength = sum(communicationTime (node1,
     * lastNodePrevNodes.get(i)) / computiationTime(node1)) + sum(communicationTime (node1,
     * lastNodeNextNodes.get(i)) / computiationTime(node1)) - dif(computiationTime(node1) /
     * communicationTime (node1, lastNodeNextNodes.get(i))
     * 
     * @param node1
     * @param cpuId The id of the cpu.
     * @return STRENGTH of node1 related to cpu.
     */
    protected float calcStrength(LASTNode node1, int cpuId) {
        int computationTimeNode1 = node1.getTaskGraphNode().getComputationTime();
        double strength = 0.0;

        // Lists of the previous and following nodes of the node1.
        ArrayList<TaskGraphNode> lastNodePrevNodesAll = new ArrayList<TaskGraphNode>(node1
                .getTaskGraphNode().getPrevNodes());
        ArrayList<TaskGraphNode> lastNodeNextNodesAll = new ArrayList<TaskGraphNode>(node1
                .getTaskGraphNode().getNextNodes());

        ArrayList<TaskGraphNode> lastNodePrevNodes = new ArrayList<TaskGraphNode>();
        ArrayList<TaskGraphNode> lastNodeNextNodes = new ArrayList<TaskGraphNode>();

        for (int i = 0; i < lastNodePrevNodesAll.size(); i++) {
            for (int j = 0; j < groups.get(cpuId).size(); j++) {
                if (lastNodePrevNodesAll.get(i) == groups.get(cpuId).get(j).getTaskGraphNode())
                    lastNodePrevNodes.add(lastNodePrevNodesAll.get(i));
            }
        }
        for (int i = 0; i < lastNodeNextNodesAll.size(); i++) {
            for (int j = 0; j < groups.get(cpuId).size(); j++) {
                if (lastNodeNextNodesAll.get(i) == groups.get(cpuId).get(j).getTaskGraphNode())
                    lastNodeNextNodes.add(lastNodeNextNodesAll.get(i));
            }
        }

        // If there are no nodes in the group then there are no dependencies.
        if (lastNodeNextNodes.size() == 0 && lastNodePrevNodes.size() == 0) {
            return 0f;
        }

        // Iterate through the lastNodePrevNodes and calculate the first sum.
        for (int i = 0; i < lastNodePrevNodes.size(); i++) {
            try {
                strength += ((double) (taskGraph.findEdge(lastNodePrevNodes.get(i),
                        node1.getTaskGraphNode()).getCommunicationTime()) / computationTimeNode1);
            } catch (ContainsNoEdgeException e) {
                e.printStackTrace();
            }
        }

        // Iterate through the lastNodeNextNodes and calculate the second sum.
        for (int i = 0; i < lastNodeNextNodes.size(); i++) {
            try {
                strength += ((double) (taskGraph.findEdge(node1.getTaskGraphNode(),
                        lastNodeNextNodes.get(i)).getCommunicationTime()) / computationTimeNode1);
            } catch (ContainsNoEdgeException e) {
                e.printStackTrace();
            }
        }

        // Iterate through the lastNodeNextNodes and calculate the difference.
        for (int i = 0; i < lastNodeNextNodes.size(); i++) {
            if (lastNodeNextNodes.size() == 0) {
                strength -= 0;
            } else {
                try {
                    strength -= (computationTimeNode1 / (double) (taskGraph.findEdge(
                            node1.getTaskGraphNode(), lastNodeNextNodes.get(i))
                            .getCommunicationTime()));
                } catch (ContainsNoEdgeException e) {
                    e.printStackTrace();
                }
            }
        }
        return (float) strength;
    }

    @Override
    public String getName() {
        return "last";
    }
}
