package com.github.noxan.blommagraphs.serializer.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.DuplicateEdgeException;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;
import com.github.noxan.blommagraphs.serializer.TaskGraphSerializer;


/**
 * Serializer / deserializer for TaskGraph -> Standard Task Graph (STG) format.
 * 
 * @author namelessvoid
 * @since 2013-09-01
 */
public class STGSerializer implements TaskGraphSerializer {

    /**
     * Takes a TaskGraph and serialize it into a STG formated String.
     * 
     * @param graph The TaskGraph that is serialized.
     * @return String which contains the graph in STG format.
     */
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

    /**
     * Deserializes a string in STG format and generates a Taskgraph.
     * 
     * @param graphString String that contains a graph representation in STG
     *            format.
     * @return Deserialized TaskGraph representation of the given graphString.
     */
    @Override
    public TaskGraph deserialize(String graphString) {
        TaskGraph graph = new JGraphtTaskGraph();
        List<STGNode> nodeList = getSTGNodeList(graphString);
        int nodeCount = nodeList.size() - 1;

        // Set up map for organisazion of inserted TaskGraphNodes
        Map<Integer, TaskGraphNode> taskGraphNodeMap = new HashMap<Integer, TaskGraphNode>();
        taskGraphNodeMap.put(0, graph.getFirstNode());
        taskGraphNodeMap.put(nodeCount, graph.getLastNode());

        // First: insert all nodes except first and last node
        STGNode stgNode = null;
        for (int i = 1; i < nodeList.size() - 1; ++i) {
            stgNode = nodeList.get(i);

            TaskGraphNode taskGraphNode = graph.insertNode(taskGraphNodeMap.get(0), 0,
                    taskGraphNodeMap.get(nodeCount), 0, stgNode.computationcosts);
            taskGraphNodeMap.put(stgNode.getId(), taskGraphNode);
        }

        // Second: insert all dependencies, modify communication costs to first
        // node.
        for (int i = 1; i < nodeList.size() - 1; ++i) {
            stgNode = nodeList.get(i);
            for (int depKey : stgNode.getDependencies().keySet()) {
                // If node is connected to first node: update communication
                // costs.
                if (depKey == 0) {
                    graph.modifyEdge(taskGraphNodeMap.get(0), taskGraphNodeMap.get(i), stgNode
                            .getDependencies().get(0));
                } else {
                    // Add new edge
                    try {
                        graph.insertEdge(taskGraphNodeMap.get(depKey), taskGraphNodeMap.get(i),
                                stgNode.getDependencies().get(depKey));
                    } catch (DuplicateEdgeException e) {
                        e.printStackTrace();
                    }
                }
            }
            // Third: remove connection to first node if not necessary any more.
            if (!stgNode.dependencies.keySet().contains(0)) {
                graph.deleteEdge(taskGraphNodeMap.get(0), taskGraphNodeMap.get(stgNode.getId()));
            }
        }

        // Fourth: remove unnecessary connections of the last node
        stgNode = nodeList.get(nodeList.size() - 1);
        TaskGraphNode lastNode = graph.getLastNode();
        List<TaskGraphEdge> edges = new ArrayList<TaskGraphEdge>(lastNode.getPrevEdges());
        TaskGraphEdge edge = null;

        for (int i = 0; i < edges.size(); ++i) {
            edge = edges.get(i);
            if (!stgNode.getDependencies().keySet().contains(edge.getPrevNode().getId())) {
                // edges.remove(edge);
                graph.deleteEdge(edge.getPrevNode(), lastNode);
            } else {
                // Update costs
                graph.modifyEdge(edge.getPrevNode(), lastNode,
                        stgNode.getDependencies().get(edge.getPrevNode().getId()));
            }

        }

        return graph;
    }

    /**
     * Converts a given STG string into a list of STGNodes.
     * 
     * @param stgString The string in STG format.
     * @return List containing the STGNodes.
     */
    private List<STGNode> getSTGNodeList(String stgString) {
        List<STGNode> nodeList = new ArrayList<STGNode>();

        String[] lines = stgString.split("\\r?\\n");

        // Start with i = 1. First line holds number of nodes which is not of
        // interest here.
        for (int i = 1; i < lines.length; ++i) {
            int[] values = getLineValues(lines[i]);

            // When the first meta info line is reached, we are done.
            if (values == null)
                break;

            // set up new STGNode
            STGNode node = new STGNode(values[0], values[1]);
            // Create dependencies
            int dependencyCount = values[2];
            for (int j = 1; j <= dependencyCount; ++j) {
                values = getLineValues(lines[i + j]);
                // values[1] + "\n");
                node.addDependency(values[0], values[1]);
            }

            // Append new node to list
            nodeList.add(node);
            // Skip dependency lines
            i += dependencyCount;
        }
        return nodeList;
    }

    /**
     * Takes a whitespace separated String containing integers only and splits
     * it into integer values.
     * 
     * @param line A String that only consists of integers and white spaces.
     * @return Int array which contains the separated values in the line. Null
     *         if the line is not valid.
     */
    private int[] getLineValues(String line) {
        if (line.length() == 0)
            return null;

        String[] splitString = line.trim().split("\\s+");

        // TODO: Generic test if line does not start with an integer.
        if (splitString[0].compareTo("#") == 0)
            return null;

        int[] values = new int[splitString.length];

        for (int i = 0; i < splitString.length; ++i) {
            values[i] = Integer.parseInt(splitString[i]);
        }

        return values;
    }

    /**
     * Generates some meta information that is saved when serializing a
     * TaskGraph in STG format.
     * 
     * @param graph The graph for which the meta information is created.
     * @return String containing the meta information.
     */
    private String generateMetaInfo(TaskGraph graph) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("# BlommaGraphs:\tthis is a Standard Task Graph project file\n");

        Map<String, Object> metaMap = graph.getMetaInformation();

        for (String key : metaMap.keySet()) {
            String value = metaMap.get(key).toString();
            stringBuffer.append(String.format("# %s:\t%s\n", key, value));
        }

        return stringBuffer.toString();
    }
}
