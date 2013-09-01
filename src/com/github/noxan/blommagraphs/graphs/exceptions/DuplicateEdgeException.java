package com.github.noxan.blommagraphs.graphs.exceptions;


/**
 * An exception to be used when an edge should be inserted but there is already
 * an existing one between the two nodes.
 * 
 * @author noxan
 */
public class DuplicateEdgeException extends Exception {
    private static final long serialVersionUID = 3975611637043020044L;
}
