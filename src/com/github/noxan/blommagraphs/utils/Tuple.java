package com.github.noxan.blommagraphs.utils;


public class Tuple<A, B> {
    private A a;
    private B b;

    public Tuple() {
    }

    public Tuple(A a, B b) {
        setFirst(a);
        setSecond(b);
    }

    public A getFirst() {
        return a;
    }

    public B getSecond() {
        return b;
    }

    public void setFirst(A a) {
        this.a = a;
    }

    public void setSecond(B b) {
        this.b = b;
    }
}
