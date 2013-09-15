package com.github.noxan.blommagraphs.utils;


import com.github.noxan.blommagraphs.graphs.TaskGraph;


/**
 * This class is used to create an array of TaskGraphs that can be used for scheduling by a
 * StreamScheduler.
 * 
 * @author namelessvoid
 * 
 */
public class StreamSchedulableArrayGenerator {

    /**
     * This method gets one single TaskGraph as input and multiplies it. It creates as many new
     * graphs as the number of deadlines in the deadLines array. Deadlines for each TaskGraph are
     * reset by the deadLines. The resulting TaskGraph array can now easily be passed to an
     * StreamScheduler which requires an array of TaskGraphs to schedule them.
     * 
     * @param graph One instance of a TaskGraph that is multiplied.
     * @param deadLines Determines a deadline for each new TaskGraph instance and also how many
     *            graphs are generated (deadLines.length).
     * 
     * @return An array that holds newly generated TaskGraphs.
     */
    public static TaskGraph[] generateArray(TaskGraph graph, int[] deadLines) {
        TaskGraph graphs[] = new TaskGraph[deadLines.length];

        // Ugly workaround: TODO use serialize -> deserialize to get new graph instance. That's
        // supposed to be replaced when a deep-copy method for TaskGraph is implemented.

        for (int i = 0; i < deadLines.length; ++i) {
            graphs[i] = graph.clone();
            graphs[i].resetDeadLine(deadLines[i]);
        }

        return graphs;
    }

}
