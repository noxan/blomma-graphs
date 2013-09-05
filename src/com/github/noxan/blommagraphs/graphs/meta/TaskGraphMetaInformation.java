package com.github.noxan.blommagraphs.graphs.meta;


import java.util.Map;


public interface TaskGraphMetaInformation {
    public void setMetaInformation(String key, Object value);

    public Map<String, Object> getMetaInformation();
}
