package com.github.noxan.blommagraphs.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.serializer.TaskGraphSerializer;


public class TaskGraphFileUtils {
    public static void writeFile(String pathname, TaskGraph graph, TaskGraphSerializer serializer)
            throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(pathname));

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
        String graphString = serializer.serialize(graph);

        writer.write(graphString);

        writer.close();
        fos.close();
    }

    public static TaskGraph readFile(String pathname, TaskGraphSerializer deserializer)
            throws IOException {
        FileInputStream fis = new FileInputStream(new File(pathname));

        StringBuilder graphString = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        while ((line = reader.readLine()) != null) {
            graphString.append(line);
        }

        reader.close();
        fis.close();

        return deserializer.deserialize(graphString.toString());
    }
}
