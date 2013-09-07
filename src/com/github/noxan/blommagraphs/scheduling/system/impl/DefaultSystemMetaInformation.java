package com.github.noxan.blommagraphs.scheduling.system.impl;


import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class DefaultSystemMetaInformation implements SystemMetaInformation {
    private int processorCount;

    public DefaultSystemMetaInformation() {
        this(2);
    }

    public DefaultSystemMetaInformation(int processorCount) {
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
