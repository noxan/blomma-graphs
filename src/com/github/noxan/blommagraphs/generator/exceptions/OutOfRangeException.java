package com.github.noxan.blommagraphs.generator.exceptions;


public class OutOfRangeException extends GeneratorException {
    private static final long serialVersionUID = 7788733846860090718L;

    public OutOfRangeException() {
        super("Wrong input!! Out of range...");
    }
}
