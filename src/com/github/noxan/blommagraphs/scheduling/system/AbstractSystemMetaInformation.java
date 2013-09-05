package com.github.noxan.blommagraphs.scheduling.system;


public abstract class AbstractSystemMetaInformation implements SystemMetaInformation {
    private int processorCount;

    public AbstractSystemMetaInformation() {
        this(2);
    }

    public AbstractSystemMetaInformation(int processorCount) {
        setProcessorCount(processorCount);
    }

    @Override
    public int getProcessorCount() {
        return processorCount;
    }

    @Override
    public void setProcessorCount(int processorCount) {
        this.processorCount = processorCount;
    }
}
