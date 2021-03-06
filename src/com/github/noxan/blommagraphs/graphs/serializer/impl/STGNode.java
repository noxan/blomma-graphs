/**
 * 
 */
package com.github.noxan.blommagraphs.graphs.serializer.impl;


import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;


/**
 * This is a class that holds information about one node (task) in STG format.
 * 
 * In STG format, a node (task) consists of it's ID, computation count, number of dependencies and
 * the dependencies itself. A dependencie saves the predecessor and communication costs. These
 * information are represented in one STGNode.
 * 
 * @author namelessvoid
 * @since 2013-09-01
 */
public class STGNode {
    int id;
    int computationcosts;
    Map<Integer, Integer> dependencies;

    /**
     * Constructor that sets id and computationcosts of the node.
     * 
     * @param id
     * @param computationcosts
     */
    public STGNode(int id, int computationcosts) {
        this.id = id;
        this.computationcosts = computationcosts;
        this.dependencies = new HashMap<Integer, Integer>();
    }

    /**
     * Constructor that uses a node to initialize the STGNode.
     * 
     * @param node A TaskGraphNode which shall be represented by the new STGNode.
     */
    public STGNode(TaskGraphNode node) {
        this(node.getId(), node.getComputationTime());

        for (TaskGraphEdge edge : node.getPrevEdges()) {
            this.addDependency(edge.getPrevNode().getId(), edge.getCommunicationTime());
        }
    }

    /**
     * Standard constructor.
     */
    public STGNode() {
        this(0, 1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0)
            throw new InvalidParameterException("id cannot be negativ: " + id);

        this.id = id;
    }

    public int getComputationcosts() {
        return computationcosts;
    }

    public void setComputationcosts(int computationcosts) {
        if (computationcosts <= 0)
            throw new InvalidParameterException("computation costs must be greater 0: "
                    + computationcosts);

        this.computationcosts = computationcosts;
    }

    public Map<Integer, Integer> getDependencies() {
        return dependencies;
    }

    public int getDependencyCount() {
        return dependencies.size();
    }

    /**
     * Checks if the node has a dependency on node with given id.
     * 
     * @param id The id of the predecessor.
     * @return
     */
    public boolean hasDependencyOn(int id) {
        return dependencies.containsKey(id);
    }

    /**
     * Adds dependency on node with id. The dependency has given communication costs.
     * 
     * @param id The id of the predecessor.
     * @param computationcosts
     */
    public void addDependency(int id, int computationcosts) {
        dependencies.put(id, computationcosts);
    }

    @Override
    public String toString() {
        StringBuffer string = new StringBuffer();
        string.append(String.format("%d %d %d\n", this.id, this.computationcosts,
                getDependencyCount()));

        for (int key : dependencies.keySet()) {
            string.append(String.format("\t%d %d\n", key, dependencies.get(key)));
        }
        return string.toString();
    }
}
