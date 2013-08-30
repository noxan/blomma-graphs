package com.github.noxan.blommagraphs.generator.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: Freax
 * Date: 30.08.13
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public class BoundaryConflictException extends GeneratorException {
    public BoundaryConflictException() {
        super("Boundary conflict! Wrong Boundary...");
    }
}
