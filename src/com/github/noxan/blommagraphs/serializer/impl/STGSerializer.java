package com.github.noxan.blommagraphs.serializer.impl;


import java.util.ArrayList;
import java.util.List;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;
import com.github.noxan.blommagraphs.serializer.TaskGraphSerializer;


public class STGSerializer implements TaskGraphSerializer {
    @Override
    public String serialize(TaskGraph graph) {
        StringBuffer stringBuffer = new StringBuffer();
        List<TaskGraphNode> nodeList = new ArrayList<TaskGraphNode>(graph.getNodeSet());
        java.util.Collections.sort(nodeList);

        // Add number of nodes
        stringBuffer.append(nodeList.size()).append("\n");

        // Iterate through nodes
        for (TaskGraphNode node : nodeList) {
            stringBuffer.append(String.format("%d %d %d\n", node.getId(),
                    node.getComputationTime(), node.getPrevNodeCount()));

            // Iterate through dependencies of node
            for (TaskGraphEdge edge : node.getPrevEdges()) {
                stringBuffer.append(String.format("\t%d %d\n", edge.getPrevNode().getId(),
                        edge.getCommunicationTime()));
            }
        }
        stringBuffer.append("\n").append(generateMetaInfo(graph));
        return stringBuffer.toString();
    }

    @Override
    public TaskGraph deserialize(String graphString) {
        // TODO Auto-generated method stub
        return new DefaultTaskGraph();
    }

    private String generateMetaInfo(TaskGraph graph) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("# BlommaGraphs:\tthis is a Standard Task Graph project file\n");
        // Nodes
        stringBuffer
                .append(String.format("# Tasks:\t%d (+ dummy tasks: 2)\n", graph.getNodeCount()));
        // Layers
        stringBuffer.append(String.format("# Layers:\t%d\n", graph.getLayerCount()));
        // Edges
        stringBuffer.append(String.format("# Edges:\t%d (+ dummy edges: %d)\n",
                graph.getEdgeCount(), graph.getFirstNode().getNextEdgeCount()
                        + graph.getLastNode().getPrevEdgeCount()));
        return stringBuffer.toString();
    }
}
