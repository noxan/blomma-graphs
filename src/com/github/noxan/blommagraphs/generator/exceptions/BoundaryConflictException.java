package com.github.noxan.blommagraphs.generator.exceptions;


/**
 * @author Freax
 */
public class BoundaryConflictException extends GeneratorException {
    private static final long serialVersionUID = 8554208185844382115L;

    public BoundaryConflictException() {
        super("Boundary conflict! Wrong Boundary...");
    }
}
