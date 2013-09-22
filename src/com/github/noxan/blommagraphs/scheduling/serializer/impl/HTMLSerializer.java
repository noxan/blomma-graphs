package com.github.noxan.blommagraphs.scheduling.serializer.impl;


import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;


public class HTMLSerializer implements ScheduledTaskListSerializer {

    @Override
    public String serialize(ScheduledTaskList scheduledTaskList) {
        StringBuilder string = new StringBuilder();
        string.append("     <div id=\"processorWrapper\">\n");

        for (int i = 0; i < scheduledTaskList.getCpuCount(); i++) {
            System.out.println("CPU COUNT" + scheduledTaskList.getCpuCount());

            string.append("         <div class=\"processor\">\n");
            boolean bitch = true;
            int counti = 1;
            int flummi = 1;
            for (int j = 0; j < scheduledTaskList.getScheduledTasksOnCpu(i).size(); j++) {
                ScheduledTaskList scheduledTaskOnCpuList = scheduledTaskList
                        .getScheduledTasksOnCpu(i);

                if (bitch) {
                    string.append("             <div class=\"header\">\n");
                    string.append("                 <h4>").append(i + 1).append("<small> Processor</small></h4>\n");
                    string.append("             </div><!-- header -->\n");
                    bitch = false;
                }
                flummi = j;
                if (j == 0 && i != 0) {
                    string.append("         <div class=\"waitTime\" style=\"height: ").append(scheduledTaskOnCpuList.get(j).getStartTime() * 12).append("px;\"></div><!-- waittime -->\n");
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
                    string.append("             <div class=\"waitTime\" style=\"height: ").append(waitTime * 12).append("px;\">");
                    string.append("             </div><!-- waittime -->\n");
                    waitTime = 0;
                }

                if (scheduledTaskOnCpuList.get(j).getCommunicationTime() != 0) {
                    string.append("         <div class=\"gap\" style=\"height: ").append(scheduledTaskList.getScheduledTasksOnCpu(i).getTaskById(j).getCommunicationTime() * 12).append("px;\"></div><!-- gap -->\n");
                }
                string.append("             <div class=\"task\"").append(" style=\"height: ").append(scheduledTaskOnCpuList.get(j).getComputationTime() * 12).append("px;\">\n");
                string.append("                 <h4><small>").append(scheduledTaskOnCpuList.get(j).getTaskId()).append("</small></h4>\n");
                string.append("             </div>\n");

            }

            string.append("         </div><!-- processor n -->\n");
        }
        string.append("         <div class=\"clear\"></div><!-- clear -->");
        string.append("     </div><!-- processorWrapper -->\n");

        return string.toString();
    }
}
