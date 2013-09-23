package com.github.noxan.blommagraphs;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;

import com.github.noxan.blommagraphs.evaluation.impl.PracticalScheduleSimulator;
import com.github.noxan.blommagraphs.evaluation.impl.TimebasedScheduleSimulationWorker;
import com.github.noxan.blommagraphs.generator.TaskGraphGenerator;
import com.github.noxan.blommagraphs.generator.impl.DefaultTaskGraphGenerator;
import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.serializer.TaskGraphSerializer;
import com.github.noxan.blommagraphs.graphs.serializer.impl.STGSerializer;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.last.LASTScheduler;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.serializer.impl.HTMLSerializer;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;


public class EvaluationBuilder {
    public static void main(String[] args) {
        EvaluationBuilder evaluationBuilder = new EvaluationBuilder();
        evaluationBuilder.start();
    }

    private final String evaluationRootPath = "export/evaluation";

    private final String evaluationTemplatePathname = "ressources/evaluation/index.html";

    public void start() {
        // vm warmup
        for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; i++) {
        }

        new File(evaluationRootPath).mkdir();

        TaskGraphGenerator generator = new DefaultTaskGraphGenerator();

        TaskGraph taskGraph = generator.generator();

        Scheduler scheduler = new LASTScheduler();

        SystemMetaInformation systemMetaInformation = new DefaultSystemMetaInformation(4);

        ScheduledTaskList scheduledTaskList = scheduler.schedule(taskGraph, systemMetaInformation);

        PracticalScheduleSimulator simulator = new PracticalScheduleSimulator();
        ScheduledTaskList evaluatedScheduledTaskList = simulator.simulateExecution(
                scheduledTaskList, TimebasedScheduleSimulationWorker.class);
        Collections.sort(evaluatedScheduledTaskList);

        try {
            String html = readFile(evaluationTemplatePathname);

            // content - visual scheduledTaskList
            ScheduledTaskListSerializer visualSerializer = new HTMLSerializer();

            html = html.replace("{{visualTaskList1}}",
                    visualSerializer.serialize(scheduledTaskList));
            html = html.replace("{{visualTaskList2}}",
                    visualSerializer.serialize(evaluatedScheduledTaskList));

            // content - scheduledTaskList
            html = html.replace("{{scheduledTaskList}}",
                    scheduledTaskListToHTMLTable(scheduledTaskList));

            // content - evaluatedTaskList
            html = html.replace("{{evaluatedTaskList}}",
                    scheduledTaskListToHTMLTable(evaluatedScheduledTaskList));

            TaskGraphSerializer taskGraphSerializer = new STGSerializer();

            html = html.replace("{{taskGraph}}", taskGraphSerializer.serialize(taskGraph));

            // arbor task graph

            html = html.replace("arborTaskGraph", taskGraphToArborGraph(taskGraph));
            // content end

            FileOutputStream fos = new FileOutputStream(new File(evaluationRootPath + "/"
                    + "test.html"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(html);

            writer.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFile(String pathname) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream fis = new FileInputStream(new File(evaluationTemplatePathname));

        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }

        reader.close();
        fis.close();

        return stringBuilder.toString();
    }

    private String taskGraphToArborGraph(TaskGraph taskGraph) {
        StringBuilder arborTaskGraphBuilder = new StringBuilder();

        for (TaskGraphNode node : taskGraph.getNodeSet()) {
            arborTaskGraphBuilder.append("sys.addNode('" + node.getId() + "'");
            if (node == taskGraph.getFirstNode()) {
                arborTaskGraphBuilder
                        .append(", {'first': true, 'fixed': true, 'mass': 20, 'p': {'y': 20, 'x': 'auto'}}");
            } else if (node == taskGraph.getLastNode()) {
                arborTaskGraphBuilder
                        .append(", {'last': true, 'fixed': true, 'mass': 20, 'p': {'y': 780, 'x': 'auto'}}");
            }
            arborTaskGraphBuilder.append(")\n");
        }
        for (TaskGraphEdge edge : taskGraph.getEdgeSet()) {
            arborTaskGraphBuilder.append("sys.addEdge('" + edge.getPrevNode().getId() + "','"
                    + edge.getNextNode().getId() + "')\n");
        }

        return arborTaskGraphBuilder.toString();
    }

    private String scheduledTaskListToHTMLTable(ScheduledTaskList scheduledTaskList) {
        StringBuilder stringBuilder = new StringBuilder();

        for (ScheduledTask scheduledTask : scheduledTaskList) {
            stringBuilder.append("<tr>");
            stringBuilder.append("<td>");
            stringBuilder.append(scheduledTask.getStartTime());
            stringBuilder.append("</td>");
            stringBuilder.append("<td>");
            stringBuilder.append(scheduledTask.getTaskId());
            stringBuilder.append("</td>");
            stringBuilder.append("<td>");
            stringBuilder.append(scheduledTask.getComputationTime());
            stringBuilder.append("</td>");
            stringBuilder.append("<td>");
            stringBuilder.append(scheduledTask.getCommunicationTime());
            stringBuilder.append("</td>");
            stringBuilder.append("<td>");
            stringBuilder.append(scheduledTask.getCpuId());
            stringBuilder.append("</td>");
            stringBuilder.append("</tr>");
        }

        return stringBuilder.toString();
    }
}
