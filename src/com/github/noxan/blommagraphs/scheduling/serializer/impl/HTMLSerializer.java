package com.github.noxan.blommagraphs.scheduling.serializer.impl;


import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;


/**
 * gets a ScheduledTaskList and builds a HTML-file that displays the tasks on
 * each processor
 * 
 * @author LaPush
 * 
 */
public class HTMLSerializer implements ScheduledTaskListSerializer {
    private final int timePixelFactor = 20;

    @Override
    public String serialize(ScheduledTaskList scheduledTaskList) {
        StringBuilder string = new StringBuilder();
        string.append("     <div id=\"processorWrapper\">\n");

        for (int i = 0; i < scheduledTaskList.getCpuCount(); i++) {
            string.append("         <div class=\"processor").append(i + 1).append("\">\n");
            boolean bitch = true;
            int counti = 1;
            int flummi = 1;
            for (int j = 0; j < scheduledTaskList.getScheduledTasksOnCpu(i).size(); j++) {
                ScheduledTaskList scheduledTaskOnCpuList = scheduledTaskList
                        .getScheduledTasksOnCpu(i);

                if (bitch) {
                    string.append("             <div class=\"header\">\n");
                    string.append("                 <h4>").append(i + 1)
                            .append("<small> Processor</small></h4>\n");
                    string.append("             </div><!-- header -->\n");
                    bitch = false;
                }
                flummi = j;
                if (j == 0 && i != 0) {
                    string.append("         <div class=\"waitTime\" style=\"height: ")
                            .append((scheduledTaskOnCpuList.get(j).getStartTime() - scheduledTaskOnCpuList
                                    .get(j).getCommunicationTime()) * timePixelFactor)
                            .append("px;\"></div><!-- waittime -->\n");
                }
                int waitTime = 0;
                if (j == 0) {
                    flummi = 1;
                }

                waitTime = scheduledTaskOnCpuList.get(j).getStartTime()
                        - (scheduledTaskOnCpuList.get(flummi - 1).getFinishTime() + scheduledTaskOnCpuList
                                .get(j).getCommunicationTime());
                counti += 1;

                if (waitTime > 0) {
                    string.append("             <div class=\"waitTime\" style=\"height: ")
                            .append(waitTime * timePixelFactor).append("px;\">");
                    string.append("             </div><!-- waittime -->\n");
                    waitTime = 0;
                }

                if (scheduledTaskOnCpuList.get(j).getCommunicationTime() != 0) {
                    string.append("         <div class=\"gap\" style=\"height: ")
                            .append(scheduledTaskList.getScheduledTasksOnCpu(i).get(j)
                                    .getCommunicationTime()
                                    * timePixelFactor).append("px;\"></div><!-- gap -->\n");
                }
                string.append("             <div class=\"task\"")
                        .append(" style=\"height: ")
                        .append(scheduledTaskOnCpuList.get(j).getComputationTime()
                                * timePixelFactor).append("px;\">\n");
                string.append("                 <h4><small>")
                        .append(scheduledTaskOnCpuList.get(j).getTaskId())
                        .append("</small></h4>\n");
                string.append("             </div>\n");

            }

            string.append("         </div><!-- processor n -->\n");
        }
        string.append("         <div class=\"clear\"></div><!-- clear -->");
        string.append("     </div><!-- processorWrapper -->\n");

        return string.toString();
    }
}
