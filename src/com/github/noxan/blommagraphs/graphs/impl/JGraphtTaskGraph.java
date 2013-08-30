package com.github.noxan.blommagraphs.graphs.impl;


import java.util.ArrayList;
import java.util.Set;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.DuplicateEdgeException;


public class JGraphtTaskGraph implements TaskGraph {
    private SimpleDirectedWeightedGraph<TaskGraphNode, TaskGraphEdge> graph;

    private TaskGraphNode firstNode;
    private TaskGraphNode lastNode;
    /**
     * global auxiliary variable for the layerCake function
     */
    private int layer;

    public JGraphtTaskGraph() {
        graph = new SimpleDirectedWeightedGraph<TaskGraphNode, TaskGraphEdge>(
                JGraphtTaskGraphEdge.class);

        firstNode = new JGraphtTaskGraphNode(graph, 0, 1);
        graph.addVertex(firstNode);

        lastNode = new JGraphtTaskGraphNode(graph, 1, 1);
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
        return layerCake(0, firstNode);
    }

    /**
     * recursive function to determine the layers of the graph
     * 
     * @param layer
     * @param vertex
     * @return
     */
    private int layerCake(int layer, TaskGraphNode vertex) {
        int max = layer;
        ArrayList<TaskGraphEdge> defWeiEdg = new ArrayList<TaskGraphEdge>();

        defWeiEdg.addAll(graph.edgesOf(vertex));
        for (int j = 0; j < defWeiEdg.size(); j++) {
            if (graph.getEdgeTarget(defWeiEdg.get(j)) == null
                    || graph.getEdgeTarget(defWeiEdg.get(j)) == vertex) {
                this.layer = layer;
                continue;
            } else {
                layer += 1;
                layerCake(layer, graph.getEdgeTarget(defWeiEdg.get(j)));
            }
            if (layer > this.layer) {
                this.layer = layer;
            }

        }
        max = this.layer;
        return max;
    }

    @Override
    public int getEdgeCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNodeCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime) {
        return insertNode(prevNode, prevCommunicationTime, nextNode, nextCommunicationTime,
                computationTime, true);
    }

    @Override
    public TaskGraphNode insertNode(TaskGraphNode prevNode, int prevCommunicationTime,
            TaskGraphNode nextNode, int nextCommunicationTime, int computationTime,
            boolean keepExistingEdge) {
        int lastId = lastNode.getId();
        ((JGraphtTaskGraphNode) lastNode).setId(lastId + 1);
        TaskGraphNode node = new JGraphtTaskGraphNode(graph, lastId, computationTime);
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
}
