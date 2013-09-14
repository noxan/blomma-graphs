package com.github.noxan.blommagraphs.graphs.factories.impl;


import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.exceptions.DuplicateEdgeException;
import com.github.noxan.blommagraphs.graphs.impl.JGraphtTaskGraph;


/**
 * This Factory always creates exactly the same graph.
 * 
 * @author namelessvoid
 * 
 */
public class JGraphtTaskGraphFactory {

    /**
     * Get the serialization of the graph in STG format.
     * 
     * @return String with STG representation of the graph.
     */
    public static String getSTGSerializedGraph() {
        return "10\n0 1 0\n1 10 1\n\t0 1\n2 5 3\n\t0 7\n\t1 5\n"
                + "\t3 5\n3 10 1\n\t0 1\n4 15 1\n\t0 1\n5 15 2\n\t3 6\n\t4 5\n"
                + "6 10 2\n\t2 4\n\t7 4\n7 20 1\n\t1 3\n8 10 2\n\t2 6\n\t5 3\n"
                + "9 1 3\n\t4 8\n\t6 1\n\t8 1\n";
    }

    /**
     * This method returns always the same graph which e.g. can be used for testing.
     * 
     * @return The TaskGraph.
     */
    public static TaskGraph makeGraph() {
        TaskGraph taskGraph = new JGraphtTaskGraph();
        TaskGraphNode nodes[] = new TaskGraphNode[10];

        nodes[0] = taskGraph.getFirstNode();
        nodes[9] = taskGraph.getLastNode();

        nodes[1] = taskGraph.insertNode(nodes[0], 1, nodes[9], 1, 10);
        nodes[2] = taskGraph.insertNode(nodes[1], 5, nodes[9], 4, 5);
        nodes[3] = taskGraph.insertNode(nodes[0], 1, nodes[2], 5, 10);
        nodes[4] = taskGraph.insertNode(nodes[0], 1, nodes[9], 8, 15);
        nodes[5] = taskGraph.insertNode(nodes[3], 6, nodes[9], 3, 15);
        nodes[6] = taskGraph.insertNode(nodes[2], 4, nodes[9], 1, 10);
        nodes[7] = taskGraph.insertNode(nodes[1], 3, nodes[6], 4, 20);
        nodes[8] = taskGraph.insertNode(nodes[5], 3, nodes[9], 1, 10);

        try {
            taskGraph.insertEdge(nodes[0], nodes[2], 7);
            taskGraph.insertEdge(nodes[2], nodes[8], 6);
            taskGraph.insertEdge(nodes[4], nodes[5], 5);
        } catch (DuplicateEdgeException e) {
            e.printStackTrace();
        }

        return taskGraph;
    }
}
