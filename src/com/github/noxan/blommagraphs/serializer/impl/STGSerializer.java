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
        return stringBuffer.toString();
    }

    @Override
    public TaskGraph deserialize(String graphString) {
        // TODO Auto-generated method stub
        return new DefaultTaskGraph();
    }
}
