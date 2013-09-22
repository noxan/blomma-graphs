package com.github.noxan.blommagraphs.scheduling.serializer.impl;


import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;


public class HTMLSerializer implements ScheduledTaskListSerializer {

    @Override
    public String serialize(ScheduledTaskList scheduledTaskList) {
        StringBuilder string = new StringBuilder();
        string.append("<html>\n");
        string.append(" <head>\n");
        string.append("     <title>HTML Serializer</title>\n");
        string.append("     <link rel=\"stylesheet\" href=\"styleshit.css\" type=\"text/css\" media=\"screen\" />\n");
        string.append(" </head>\n");
        string.append(" <body>\n");

        for (int i = 0; i < scheduledTaskList.getCpuCount(); i++) {
            System.out.println("CPU COUNT" + scheduledTaskList.getCpuCount());

            string.append("<div id=\"p").append(i + 1).append("\">\n");
            boolean bitch = true;
            int counti = 1;
            int flummi = 1;
            for (int j = 0; j < scheduledTaskList.getScheduledTasksOnCpu(i).size(); j++) {
                ScheduledTaskList scheduledTaskOnCpuList = scheduledTaskList
                        .getScheduledTasksOnCpu(i);

                if (bitch) {
                    string.append("<div class=\"header\">\n");
                    string.append(" <h1>Processor ").append(i + 1).append("</h1>\n");
                    string.append("</div><!-- header -->\n");
                    bitch = false;
                }
                flummi = j;
                if (j == 0 && i != 0) {
                    string.append("<div class=\"waitTime\" style=\"height: ").append(scheduledTaskOnCpuList.get(j).getStartTime() * 10).append("px;\"></div><!-- waittime -->\n");
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
                    string.append("<div class=\"waitTime\" style=\"height: ").append(waitTime * 10).append("px;\"></div><!-- waittime -->\n");
                    waitTime = 0;
                }

                if (scheduledTaskOnCpuList.get(j).getCommunicationTime() != 0) {
                    string.append("<div class=\"gap\" style=\"height: ").append(scheduledTaskList.getScheduledTasksOnCpu(i).getTaskById(j).getCommunicationTime() * 10).append("px;\"></div><!-- gap -->\n");
                }
                string.append("<div class=\"pd").append(i + 1).append("\" style=\" width: 200px; height: ").append(scheduledTaskOnCpuList.get(j).getComputationTime() * 10).append("px;\">\n");
                string.append(" <p>Task ").append(scheduledTaskOnCpuList.get(j).getTaskId()).append("</p>\n");
                string.append("</div>\n");

            }

            string.append("</div><!-- processor n -->\n");
        }

        return string.toString();
    }
}
