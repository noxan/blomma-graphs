package com.github.noxan.blommagraphs.graphs.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.ContainsNoEdgeException;
import com.github.noxan.blommagraphs.graphs.exceptions.DuplicateEdgeException;
import com.github.noxan.blommagraphs.graphs.meta.TaskGraphMetaInformation;
import com.github.noxan.blommagraphs.graphs.meta.impl.DefaultTaskGraphMetaInformation;
import com.github.noxan.blommagraphs.utils.Tuple;


public class JGraphtTaskGraph implements TaskGraph {
    private TaskGraphMetaInformation metaInformation;

    private SimpleDirectedWeightedGraph<TaskGraphNode, TaskGraphEdge> graph;

    private TaskGraphNode firstNode;
    private TaskGraphNode lastNode;

    private int cp_time;
    private ArrayList<TaskGraphEdge> cp_edgeList;

    public JGraphtTaskGraph() {
        this(new DefaultTaskGraphMetaInformation());
    }

    public JGraphtTaskGraph(TaskGraphMetaInformation metaInformation) {
        this.metaInformation = metaInformation;

        graph = new SimpleDirectedWeightedGraph<TaskGraphNode, TaskGraphEdge>(
                JGraphtTaskGraphEdge.class);

        firstNode = new JGraphtTaskGraphNode(graph, this, 0, 1);
        graph.addVertex(firstNode);

        lastNode = new JGraphtTaskGraphNode(graph, this, 1, 1);
        graph.addVertex(lastNode);

        TaskGraphEdge edge = graph.addEdge(firstNode, lastNode);
        graph.setEdgeWeight(edge, 1);

    }

    @Override
    public TaskGraphNode getFirstNode() {
        return firstNode;
    }

    @Override
    public TaskGraphNode getLastNode() {
        return lastNode;
    }

    @Override
    public int getLayerCount() {
        return firstNode.getBottomLayerCount() + 1;
    }

    @Override
    public int getEdgeCount() {
        return graph.edgeSet().size();
    }

    @Override
    public int getNodeCount() {
        return graph.vertexSet().size();
    }

    @Override
    public List<TaskGraphEdge> getCriticalPath() {
        // return criticalPath1(this.firstNode, 0, new
        // ArrayList<TaskGraphEdge>()).getKey();
        return criticalPath(this.firstNode, 0, new ArrayList<TaskGraphEdge>());
    }

    private List<TaskGraphEdge> criticalPath(TaskGraphNode node, int time,
            ArrayList<TaskGraphEdge> currentEdgeList) {
        int maxTime = time + node.getComputationTime();

        ArrayList<TaskGraphEdge> taskGraphEdge = new ArrayList<TaskGraphEdge>();

        taskGraphEdge.addAll(graph.outgoingEdgesOf(node));
        for (int i = 0; i < taskGraphEdge.size(); i++) {
            ArrayList<TaskGraphEdge> currentEdgeListCopy = new ArrayList<TaskGraphEdge>();
            for (int j = 0; j < currentEdgeList.size(); j++) {
                currentEdgeListCopy.add(currentEdgeList.get(j));
            }
            currentEdgeListCopy.add(taskGraphEdge.get(i));
            criticalPath(graph.getEdgeTarget(taskGraphEdge.get(i)), maxTime
                    + taskGraphEdge.get(i).getCommunicationTime(), currentEdgeListCopy);

            if (maxTime > this.cp_time) {
                this.cp_time = maxTime;
                this.cp_edgeList = currentEdgeListCopy;
            }
        }
        return this.cp_edgeList;
    }

    @SuppressWarnings("unused")
    private Tuple<ArrayList<TaskGraphEdge>, Integer> criticalPath1(TaskGraphNode node,
            int currentTime, ArrayList<TaskGraphEdge> currentEdgePath) {
        // if node is last node return path and time
        if (node.equals(this.getLastNode())) {
            return new Tuple<ArrayList<TaskGraphEdge>, Integer>(currentEdgePath, currentTime);
        }

        // List of out going edges of this node
        ArrayList<TaskGraphEdge> outgoingEdges = new ArrayList<TaskGraphEdge>();
        outgoingEdges.addAll(graph.outgoingEdgesOf(node));

        // List to compare paths by cost
        ArrayList<Tuple<ArrayList<TaskGraphEdge>, Integer>> currentPathList = new ArrayList<Tuple<ArrayList<TaskGraphEdge>, Integer>>();

        // current time till this node
        currentTime += node.getComputationTime();

        // go through the list of outgoing edges and call this function again
        // for every edge
        for (int i = 0; i < node.getNextEdgeCount(); i++) {
            ArrayList<TaskGraphEdge> nextEdgePath = new ArrayList<TaskGraphEdge>();
            for (TaskGraphEdge edge : currentEdgePath) {
                nextEdgePath.add(edge);
            }
            nextEdgePath.add(outgoingEdges.get(i));
            Tuple<ArrayList<TaskGraphEdge>, Integer> path = criticalPath1(
                    graph.getEdgeTarget(outgoingEdges.get(i)), currentTime
                            + outgoingEdges.get(i).getCommunicationTime(), nextEdgePath);
            currentPathList.add(path);
        }

        // compare all returned paths and return the one which takes the most
        // time
        int max = 0;
        Tuple<ArrayList<TaskGraphEdge>, Integer> currentMaxPair = null;
        for (Tuple<ArrayList<TaskGraphEdge>, Integer> pair : currentPathList) {
            if (pair.getSecond() > max) {
                max = pair.getSecond();
                currentMaxPair = pair;
            }
        }
        return currentMaxPair;
    }

    @Override
    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime) {
        return insertNode(prevNode, prevCommunicationTime, nextNode, nextCommunicationTime,
                computationTime, false);
    }

    @Override
    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime,
            boolean keepExistingEdge) {
        int lastId = lastNode.getId();
        ((JGraphtTaskGraphNode) lastNode).setId(lastId + 1);
        TaskGraphNode node = new JGraphtTaskGraphNode(graph, this, lastId, computationTime);
        graph.addVertex(node);

        TaskGraphEdge prevEdge = graph.addEdge(prevNode, node);
        graph.setEdgeWeight(prevEdge, prevCommunicationTime);

        TaskGraphEdge nextEdge = graph.addEdge(node, nextNode);
        graph.setEdgeWeight(nextEdge, nextCommunicationTime);

        if (!keepExistingEdge) {
            graph.removeAllEdges(prevNode, nextNode);
        }

        return node;
    }

    @Override
    public TaskGraphEdge insertEdge(TaskGraphNode prevNode, TaskGraphNode nextNode,
            int communicationTime) throws DuplicateEdgeException {

        if (graph.containsEdge(prevNode, nextNode)) {
            throw new DuplicateEdgeException();
        }

        TaskGraphEdge edge = graph.addEdge(prevNode, nextNode);
        graph.setEdgeWeight(edge, communicationTime);

        return edge;
    }

    @Override
    public TaskGraphEdge deleteEdge(TaskGraphNode prevNode, TaskGraphNode nextNode) {
        return graph.removeEdge(prevNode, nextNode);
    }

    @Override
    public void modifyEdge(TaskGraphNode prevNode, TaskGraphNode nextNode, int newCommunicationTime) {
        TaskGraphEdge edge = graph.getEdge(prevNode, nextNode);

        graph.setEdgeWeight(edge, newCommunicationTime);
    }

    @Override
    public Set<TaskGraphEdge> getEdgeSet() {
        return graph.edgeSet();
    }

    @Override
    public Set<TaskGraphNode> getNodeSet() {
        return graph.vertexSet();
    }

    @Override
    public boolean containsEdge(TaskGraphNode prevNode, TaskGraphNode nextNode) {
        return graph.containsEdge(prevNode, nextNode);
    }

    @Override
    public TaskGraphEdge findEdge(TaskGraphNode prevNode, TaskGraphNode nextNode)
            throws ContainsNoEdgeException {
        if (graph.getEdge(prevNode, nextNode).equals(null)) {
            throw new ContainsNoEdgeException();
        }
        return graph.getEdge(prevNode, nextNode);
    }

    @Override
    public Map<String, Object> getMetaInformation() {
        metaInformation.setMetaInformation("nodeCount", getNodeCount());
        metaInformation.setMetaInformation("dummyNodeCount", 2);
        metaInformation.setMetaInformation("edgeCount", getEdgeCount());
        metaInformation.setMetaInformation("dummyEdgeCount", getFirstNode().getNextEdgeCount()
                + getLastNode().getPrevEdgeCount());
        metaInformation.setMetaInformation("layerCount", getLayerCount());
        metaInformation.setMetaInformation("edgesPerNodeRatio", getEdgeCount() / getNodeCount());

        return metaInformation.getMetaInformation();
    }
}
