package com.github.noxan.blommagraphs.scheduling.serializer.impl;


import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;


public class HTMLSerializer implements ScheduledTaskListSerializer {

    @Override
    public String serialize(ScheduledTaskList scheduledTaskList) {
        StringBuffer string = new StringBuffer();
        string.append("<html>\n<head><title>\nHTML Serializer"
                + "\n</title>\n<link rel=\"stylesheet\" href=\"styleshit.css\""
                + "type=\"text/css\" media=\"screen\" />\n</head>\n<body>\n");
        for (int i = 0; i < scheduledTaskList.getCpuCount(); i++) {
            string.append("<div id=p" + "" + (i + 1) + ">" + "\n");
            boolean bitch = true;
            for (ScheduledTask task : scheduledTaskList) {
                if (bitch) {
                    string.append("<div class=header>\n<h1" + ">Processor " + (i + 1) + "</h1"
                            + ">\n</div>\n");
                    bitch = false;
                }
                if (task.getCpuId() == i) {
                    string.append("<div class=gap " + "style=\"height: "
                            + task.getCommunicationTime() * 10 + "px;\"></div>\n");
                    string.append("<div class=pd" + (task.getCpuId() + 1)
                            + " style=\" width: 200px; height: " + task.getComputationTime() * 10
                            + "px\";" + "\"" + ">\n<p>Task " + task.getTaskId() + "</p>\n</div>"
                            + "\n");
                }
            }

            string.append("</div>" + "\n");
        }

        return string.toString();
    }
}
