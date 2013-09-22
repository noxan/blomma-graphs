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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.noxan.blommagraphs.evaluation.impl.PracticalScheduleSimulator;
import com.github.noxan.blommagraphs.generator.TaskGraphGenerator;
import com.github.noxan.blommagraphs.generator.impl.DefaultTaskGraphGenerator;
import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.serializer.TaskGraphSerializer;
import com.github.noxan.blommagraphs.graphs.serializer.impl.STGSerializer;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.Scheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.last.LASTScheduler;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;


public class EvaluationBuilder {
    public static void main(String[] args) {
        EvaluationBuilder evaluationBuilder = new EvaluationBuilder();
        evaluationBuilder.start();
    }

    private final String evaluationRootPath = "export/evaulation";

    private final String evaluationTemplatePathname = "ressources/evaluation/index.html";

    public void start() {
        new File(evaluationRootPath).mkdir();

        TaskGraphGenerator generator = new DefaultTaskGraphGenerator();

        TaskGraph taskGraph = generator.generator();

        Scheduler scheduler = new LASTScheduler();

        ScheduledTaskList scheduledTaskList = scheduler.schedule(taskGraph,
                new DefaultSystemMetaInformation(4));

        PracticalScheduleSimulator simulator = new PracticalScheduleSimulator();

        StringBuilder htmlBuilder = new StringBuilder();

        try {
            FileInputStream fis = new FileInputStream(new File(evaluationTemplatePathname));

            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = reader.readLine()) != null) {
                htmlBuilder.append(line + "\n");
            }

            reader.close();
            fis.close();

            String html = htmlBuilder.toString();

            // content - scheduledTaskList
            StringBuilder scheduledTaskListBuilder = new StringBuilder();

            for (ScheduledTask task : scheduledTaskList) {
                scheduledTaskListBuilder.append("<tr>");
                scheduledTaskListBuilder.append("<td>");
                scheduledTaskListBuilder.append(task.getStartTime());
                scheduledTaskListBuilder.append("</td>");
                scheduledTaskListBuilder.append("<td>");
                scheduledTaskListBuilder.append(task.getTaskGraphNode().getId());
                scheduledTaskListBuilder.append("</td>");
                scheduledTaskListBuilder.append("<td>");
                scheduledTaskListBuilder.append(task.getCpuId());
                scheduledTaskListBuilder.append("</td>");
                scheduledTaskListBuilder.append("</tr>");
            }

            html = html.replace("{{scheduledTaskList}}", scheduledTaskListBuilder.toString());

            // content - evaluatedTaskList
            StringBuilder evaluatedTaskListBuilder = new StringBuilder();
            Map<Long, TaskGraphNode> result = simulator.simulateExecution(scheduledTaskList);

            List<Long> timeList = new ArrayList<Long>(result.keySet());
            Collections.sort(timeList);

            for (long time : timeList) {
                evaluatedTaskListBuilder.append("<tr>");
                evaluatedTaskListBuilder.append("<td>");
                evaluatedTaskListBuilder.append(time);
                evaluatedTaskListBuilder.append("</td>");
                evaluatedTaskListBuilder.append("<td>");
                evaluatedTaskListBuilder.append(result.get(time).getId());
                evaluatedTaskListBuilder.append("</td>");
                evaluatedTaskListBuilder.append("<td>");
                evaluatedTaskListBuilder.append("unknown");
                evaluatedTaskListBuilder.append("</td>");
                evaluatedTaskListBuilder.append("</tr>");
            }

            html = html.replace("{{evaluatedTaskList}}", evaluatedTaskListBuilder.toString());

            TaskGraphSerializer taskGraphSerializer = new STGSerializer();

            html = html.replace("{{taskGraph}}", taskGraphSerializer.serialize(taskGraph));
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
}
