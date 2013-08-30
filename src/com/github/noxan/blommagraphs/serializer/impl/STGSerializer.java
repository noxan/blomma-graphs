package com.github.noxan.blommagraphs.serializer.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.impl.DefaultTaskGraph;
import com.github.noxan.blommagraphs.serializer.TaskGraphSerializer;


public class STGSerializer implements TaskGraphSerializer {
    @Override
    public String serialize(TaskGraph graph) {
        StringBuffer stringBuffer = new StringBuffer();

        // Iterate through nodes
        for (TaskGraphNode node : graph.getNodeSet()) {
            stringBuffer.append(String.format("(%i) (%i) (%i)\n", node.getID(),
                    node.getComputationTime(), node.getPrevNodeCount()));

            // Iterate through dependencies of node
            for (TaskGraphEdge edge : node.getPrevEdges()) {
                String.format("\t(%i) (%i)\n", edge.getPrevNode().getID(),
                        edge.getCommunicationTime());
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
