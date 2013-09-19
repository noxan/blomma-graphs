package com.github.noxan.blommagraphs.scheduling.serializer.impl;


import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;


public class HTMLSerializer implements ScheduledTaskListSerializer {

    @Override
    public String serialize(ScheduledTaskList scheduledTaskList) {
        StringBuffer string = new StringBuffer();
        string.append("<html>" + "\n" + "<head>" + "<title>" + "\n" + "HTML Serializer" + "\n"
                + "</title>" + "\n" + "</head>" + "\n" + "<body>");
        for (int i = 0; i < scheduledTaskList.getCpuCount(); i++) {
            string.append("<div id=p" + "" + i + 1 + ">" + "\n");

            for (ScheduledTask task : scheduledTaskList) {
                if (task.getCpuId() == i) {
                    string.append("<div" + " height: " + task.getComputationTime() + ";" + ">"
                            + "\n");
                    string.append("<div id=" + task.getTaskId() + " height: "
                            + task.getComputationTime() + ";" + ">" + "\n");
                }
            }

            string.append("</div>");
        }

        return string.toString();
    }
}
