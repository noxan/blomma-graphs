package com.github.noxan.blommagraphs;


import java.io.IOException;

import com.github.noxan.blommagraphs.generator.TaskGraphGenerator;
import com.github.noxan.blommagraphs.generator.impl.DefaultTaskGraphGenerator;
import com.github.noxan.blommagraphs.serializer.TaskGraphSerializer;
import com.github.noxan.blommagraphs.serializer.impl.STGSerializer;
import com.github.noxan.blommagraphs.utils.TaskGraphFileUtils;


public class Main {
    public static void main(String[] args) {
        System.out.println("blomma-graphs");

        TaskGraphGenerator taskGraphGenerator = new DefaultTaskGraphGenerator();
        TaskGraphSerializer serializer = new STGSerializer();
        try {
            TaskGraphFileUtils.writeFile("graph.stg", taskGraphGenerator.generator(), serializer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
