package com.github.noxan.blommagraphs.graphs.meta.impl;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.github.noxan.blommagraphs.graphs.meta.TaskGraphMetaInformation;


public class DefaultTaskGraphMetaInformation implements TaskGraphMetaInformation {
    private Map<String, Object> metaInformation;

    public DefaultTaskGraphMetaInformation() {
        metaInformation = new HashMap<String, Object>();
    }

    @Override
    public void setMetaInformation(String key, Object value) {
        metaInformation.put(key, value);
    }

    @Override
    public Map<String, Object> getMetaInformation() {
        return Collections.unmodifiableMap(metaInformation);
    }
}
