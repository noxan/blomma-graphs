package com.github.noxan.blommagraphs.scheduling.system.impl;


import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class DefaultSystemMetaInformation implements SystemMetaInformation {
    private int cpuCount;

    public DefaultSystemMetaInformation() {
        this(2);
    }

    public DefaultSystemMetaInformation(int cpu) {
        setCpuCount(cpu);
    }

    @Override
    public int getCpuCount() {
        return cpuCount;
    }

    @Override
    public void setCpuCount(int cpuCount) {
        this.cpuCount = cpuCount;
    }
}
