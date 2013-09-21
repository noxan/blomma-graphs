package com.github.noxan.blommagraphs.scheduling.serializer.impl;


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
            System.out.println("CPU COUNT" + scheduledTaskList.getCpuCount());
            string.append("<div id=p" + "" + (i + 1) + ">" + "\n");
            boolean bitch = true;
            int counti = 1;
            int flummi = 1;
            for (int j = 0; j < scheduledTaskList.getScheduledTasksOnCpu(i).size(); j++) {
                ScheduledTaskList scheduledTaskOnCpuList = scheduledTaskList
                        .getScheduledTasksOnCpu(i);

                if (bitch) {
                    string.append("<div class=header>\n<h1" + ">Processor " + (i + 1) + "</h1"
                            + ">\n</div>\n");
                    bitch = false;
                }
                flummi = j;
                if (j == 0 && i != 0) {
                    string.append("<div class=waitTime " + "style=\"height: "
                            + scheduledTaskOnCpuList.get(j).getStartTime() * 10 + "px;\"></div>\n");

                }
                int waitTime = 0;
                if (j == 0) {
                    flummi = 1;
                }

                waitTime = scheduledTaskOnCpuList.get(j).getStartTime()
                        - (scheduledTaskOnCpuList.get(flummi - 1).getFinishTime() + scheduledTaskOnCpuList
                                .get(j).getCommunicationTime());
                System.out.println("start " + scheduledTaskOnCpuList.get(j).getStartTime());
                System.out.println("finish "
                        + scheduledTaskOnCpuList.get(flummi - 1).getFinishTime());
                System.out.println("ID " + scheduledTaskOnCpuList.get(j).getTaskId());
                System.out.println("waitTime " + (waitTime));
                System.out.println("LOOPREPITITIONS " + counti);
                System.out.println("CPU: " + scheduledTaskOnCpuList.get(j).getCpuId());
                System.out.println("COMMUNICATION TIME: "
                        + scheduledTaskOnCpuList.get(j).getCommunicationTime());
                System.out.println("");
                counti += 1;

                if (waitTime > 0) {
                    string.append("<div class=waitTime " + "style=\"height: " + waitTime * 10
                            + "px;\"></div>\n");
                    waitTime = 0;
                }

                if (scheduledTaskOnCpuList.get(j).getCommunicationTime() != 0) {
                    string.append("<div class=gap "
                            + "style=\"height: "
                            + scheduledTaskList.getScheduledTasksOnCpu(i).getTaskById(j)
                                    .getCommunicationTime() * 10 + "px;\"></div>\n");
                }
                string.append("<div class=pd" + (i + 1) + " style=\" width: 200px; height: "
                        + scheduledTaskOnCpuList.get(j).getComputationTime() * 10 + "px\";" + "\""
                        + ">\n<p>Task " + scheduledTaskOnCpuList.get(j).getTaskId()
                        + "</p>\n</div>" + "\n");

            }

            string.append("</div>" + "\n");
        }

        return string.toString();
    }
}
