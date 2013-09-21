package com.github.noxan.blommagraphs;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.serializer.TaskGraphSerializer;
import com.github.noxan.blommagraphs.graphs.serializer.impl.STGSerializer;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.impl.dls.DynamicLevelScheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.genetic.GeneticScheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.last.LASTScheduler;
import com.github.noxan.blommagraphs.scheduling.stream.StreamScheduler;
import com.github.noxan.blommagraphs.scheduling.stream.impl.BasicStreamScheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;
import com.github.noxan.blommagraphs.utils.FileUtils;
import com.github.noxan.blommagraphs.utils.TaskGraphFileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class StatisticsBuilder {

    private class Statistic {
        private String filePath;
        private int nodeCount;
        private float edgeCount;
        private float cpDuration;
        private double algorithmDuration;
        private float singleBlockExecutionTime;
        private float scheduleCpRatio;
        private float scheduleCpVariance;
        private float throughput;
        private float averageCommunicationTime;
    };

    private final String statisticsFilePath = "export/statistics/statistics.html";

    // TODO: have to be 4! Just use 3 until CustomStreamscheduler is fixed :]
    private final int schedulerCount = 3;
    private final int taskGraphCount = 500;
    private final int taskGraphGroupSize = 100;
    private final int taskGroupCount = 5;
    private final int cpuCount = 3;
    // Number of TaskGraph copies that are scheduled.
    private final int blockSize = 2;

    private List<List<Statistic>> taskGraphStatistics;
    private List<List<Statistic>> taskGroupStatistics;
    private List<Statistic> schedulerStatistics;

    private StreamScheduler schedulers[];
    private SystemMetaInformation systemMetaInformation;

    private TaskGraphSerializer taskGraphSerializer;

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        StatisticsBuilder statBuilder = new StatisticsBuilder();

        statBuilder.generateHTML();
        statBuilder.buildStatistics();

    }

    private StatisticsBuilder() {
        // Build lists
        taskGraphStatistics = new ArrayList<List<Statistic>>();
        taskGroupStatistics = new ArrayList<List<Statistic>>();
        schedulerStatistics = new ArrayList<Statistic>();

        for (int scheduler = 0; scheduler < schedulerCount; ++scheduler) {
            taskGraphStatistics.add(new ArrayList<Statistic>());
            taskGroupStatistics.add(new ArrayList<Statistic>());
            schedulerStatistics.add(new Statistic());

            for (int taskGraph = 0; taskGraph < taskGraphCount; ++taskGraph) {
                taskGraphStatistics.get(scheduler).add(new Statistic());
            }
            for (int taskGroup = 0; taskGroup < taskGroupCount; ++taskGroup) {
                taskGroupStatistics.get(scheduler).add(new Statistic());
                Statistic stat = taskGroupStatistics.get(scheduler).get(taskGroup);
                
                switch(taskGroup)  {
                case 0:
                    stat.nodeCount = 10;
                    break;
                case 1:
                    stat.nodeCount = 50;
                    break;
                case 2:
                    stat.nodeCount = 100;
                    break;
                case 3:
                    stat.nodeCount = 300;
                    break;
                case 4:
                    stat.nodeCount = 500;
                    break;
                }
            }
            
        }

        // Create Schedulers
        schedulers = new StreamScheduler[schedulerCount];
        schedulers[0] = new BasicStreamScheduler(new LASTScheduler());
        schedulers[1] = new BasicStreamScheduler(new DynamicLevelScheduler());
        schedulers[2] = new BasicStreamScheduler(new GeneticScheduler(new DynamicLevelScheduler()));
        // schedulers[3] = new CustomStreamScheduler();

        systemMetaInformation = new DefaultSystemMetaInformation(cpuCount);

        taskGraphSerializer = new STGSerializer();
    }

    /**
     * Calculate all statistics and build an html document.
     * 
     * @throws IOException
     */
    private void buildStatistics() throws IOException {
        buildTaskGraphStatistics();
        buildTaskGroupStatistics();
        buildSchedulerStatistics();

    }

    /**
     * Generates statistics for the taskGraphStatistics list by deserializing graphs from disk and
     * scheduling it with all schedulers.
     */
    private void buildTaskGraphStatistics() throws IOException {
        File graphGroupsDirectory = new File("export/graphs");
        File[] graphDirectories = graphGroupsDirectory.listFiles();

        TaskGraph graph;
        // Following task graphs are used for scheduling.
        TaskGraph[] taskGraphs;
        ScheduledTaskList scheduledTaskList;

        int taskGroupCounter = 0;

        for (File graphDirectory : graphDirectories) {
            File[] graphFiles = graphDirectory.listFiles();

            for (int graphId = 0; graphId < taskGraphGroupSize; ++graphId) {

                // Get the current graph
                System.out.println("Deserailizing ...");

                graph = TaskGraphFileUtils.readFile(graphFiles[graphId].getAbsolutePath(),
                        taskGraphSerializer);
                System.out.println("Done.\nGenerating task graph array...");

                // Create copies of this graph which are passed to the stream schedulers.
                taskGraphs = new TaskGraph[blockSize];
                for (int i = 0; i < blockSize; ++i)
                    taskGraphs[i] = graph.clone();

                System.out.println("Done.\nscheduling graph with " + graph.getNodeCount()
                        + " nodes...\n");

                for (int schedulerId = 0; schedulerId < schedulerCount; ++schedulerId) {
                    System.out.println("Calculate taskGraphStatistics here!");

                    Statistic currentStatistic = taskGraphStatistics.get(schedulerId).get(
                            taskGroupCounter * taskGraphGroupSize + graphId);

                    // Starttime measurement
                    long currentAlgorithmDuration = System.currentTimeMillis();
                    scheduledTaskList = schedulers[schedulerId].schedule(taskGraphs,
                            systemMetaInformation);
                    currentAlgorithmDuration = (System.currentTimeMillis() - currentAlgorithmDuration);
                    System.out.println("Algorithm duration: " + (double) currentAlgorithmDuration
                            / 1000);

                    // Calculate taskGraphStatistics here.
                    int criticalPathDuration = calcCriticalPathDuration(graph);

                    currentStatistic.filePath = graphFiles[graphId].getAbsolutePath();
                    currentStatistic.nodeCount = (graph.getNodeCount()-2);
                    currentStatistic.edgeCount = graph.getEdgeCount();
                    currentStatistic.cpDuration = criticalPathDuration;
                    currentStatistic.algorithmDuration = (double) currentAlgorithmDuration / 1000;
                    currentStatistic.scheduleCpRatio = (float) scheduledTaskList.getFinishTime()
                            / criticalPathDuration;
                    currentStatistic.scheduleCpVariance = criticalPathDuration
                            - (float) scheduledTaskList.getFinishTime();
                    currentStatistic.averageCommunicationTime = calcAverageCommunicationTime(scheduledTaskList);
                    currentStatistic.throughput = (float) blockSize
                            / scheduledTaskList.getFinishTime();
                    currentStatistic.singleBlockExecutionTime = scheduledTaskList.getFinishTime();
                }
            }

            taskGroupCounter++;
        }
    }

    /**
     * Generates statistics for the taskGroupStatistics list by using taskGraphStatistics.
     */
    private void buildTaskGroupStatistics() {
        List<Statistic> schedulerTaskList = null;
        List<Statistic> schedulerGroupList = null;

        Statistic groupStatistic = null;

        // Sum up all values
        for (int i = 0; i < schedulerCount; ++i) {
            schedulerTaskList = taskGraphStatistics.get(i);
            schedulerGroupList = taskGroupStatistics.get(i);

            for (Statistic taskStatistic : schedulerTaskList) {
                // Get the correct task group list
                switch (taskStatistic.nodeCount) {
                case 10:
                    groupStatistic = schedulerGroupList.get(0);
                    break;
                case 50:
                    groupStatistic = schedulerGroupList.get(1);
                    break;
                case 100:
                    groupStatistic = schedulerGroupList.get(2);
                    break;
                case 300:
                    groupStatistic = schedulerGroupList.get(3);
                    break;
                case 500:
                    groupStatistic = schedulerGroupList.get(4);
                    break;
                }

                groupStatistic.edgeCount += taskStatistic.edgeCount;
                groupStatistic.cpDuration += taskStatistic.cpDuration;
                groupStatistic.algorithmDuration += taskStatistic.algorithmDuration;
                groupStatistic.singleBlockExecutionTime += taskStatistic.singleBlockExecutionTime;
                groupStatistic.scheduleCpRatio += taskStatistic.scheduleCpRatio;
                groupStatistic.scheduleCpVariance += taskStatistic.scheduleCpVariance;
                groupStatistic.throughput += taskStatistic.throughput;
                groupStatistic.averageCommunicationTime += taskStatistic.averageCommunicationTime;
            }
        }
        // Now devide all by taskGraphGroupSize to get
        for (List<Statistic> groupStatisticList : taskGroupStatistics) {
            for (Statistic statistic : groupStatisticList) {
                statistic.edgeCount = statistic.edgeCount / taskGraphGroupSize;
                statistic.cpDuration = statistic.cpDuration / taskGraphGroupSize;
                statistic.algorithmDuration = statistic.algorithmDuration / taskGraphGroupSize;
                statistic.singleBlockExecutionTime = statistic.singleBlockExecutionTime
                        / taskGraphGroupSize;
                statistic.scheduleCpRatio = statistic.scheduleCpRatio / taskGraphGroupSize;
                statistic.scheduleCpVariance = statistic.scheduleCpVariance / taskGraphGroupSize;
                statistic.throughput = statistic.throughput / taskGraphGroupSize;
                statistic.averageCommunicationTime = statistic.averageCommunicationTime
                        / taskGraphGroupSize;
            }
        }
    }

    /**
     * Generates statistics for the schedulerStatistics list by using taskGroupStatistics.
     */
    private void buildSchedulerStatistics() {
        Statistic schedulerStatistic = null;

        // Sum up all statistic values
        for (int i = 0; i < schedulerCount; ++i) {
            schedulerStatistic = schedulerStatistics.get(i);
            for (Statistic groupStatistic : taskGroupStatistics.get(i)) {

                schedulerStatistic.edgeCount += groupStatistic.edgeCount;
                schedulerStatistic.cpDuration += groupStatistic.cpDuration;
                schedulerStatistic.algorithmDuration += groupStatistic.algorithmDuration;
                schedulerStatistic.singleBlockExecutionTime += groupStatistic.singleBlockExecutionTime;
                schedulerStatistic.scheduleCpRatio += groupStatistic.scheduleCpRatio;
                schedulerStatistic.scheduleCpVariance += groupStatistic.scheduleCpVariance;
                schedulerStatistic.throughput += groupStatistic.throughput;
                schedulerStatistic.averageCommunicationTime += groupStatistic.averageCommunicationTime;
            }
        }

        // Now calculate the average by dividing by taskGroupCount
        for (Statistic stat : schedulerStatistics) {
            stat.edgeCount = stat.edgeCount / taskGroupCount;
            stat.cpDuration = stat.cpDuration / taskGroupCount;
            stat.algorithmDuration = stat.algorithmDuration / taskGroupCount;
            stat.singleBlockExecutionTime = stat.singleBlockExecutionTime / taskGroupCount;
            stat.scheduleCpRatio = stat.scheduleCpRatio / taskGroupCount;
            stat.scheduleCpVariance = stat.scheduleCpVariance / taskGroupCount;
            stat.throughput = stat.throughput / taskGroupCount;
            stat.averageCommunicationTime = stat.averageCommunicationTime / taskGroupCount;
        }
    }

    /**
     * Calculates the critical path duration of a task graph.
     * 
     * @param taskGraph
     * @return Duration of the critical path.
     */
    private int calcCriticalPathDuration(TaskGraph taskGraph) {
        List<TaskGraphEdge> cpEdges = taskGraph.getCriticalPath();
        int duration = 0;
        for (TaskGraphEdge edge : cpEdges) {
            duration += edge.getNextNode().getComputationTime();
        }
        duration += cpEdges.get(0).getPrevNode().getComputationTime();
        return duration;
    }

    /**
     * Calculates the average communication time of a scheduled task list.
     * 
     * @param scheduledTaskList
     * @return The average communication time
     */
    private float calcAverageCommunicationTime(ScheduledTaskList scheduledTaskList) {
        int totalCommunicationTime = 0;
        for (ScheduledTask task : scheduledTaskList) {
            totalCommunicationTime += task.getCommunicationTime();
        }
        return (float) totalCommunicationTime / scheduledTaskList.size();
    }

    private String generateTaskGraphStatisticsHTML() {
        String html = "                 <div class=\"tab-pane active\" class=\"panel-group\" id=\"graph\">";

        int currentScheduler = 0;
        for(List<Statistic> scheduler : taskGraphStatistics) {
            html += "         <div class=\"panel panel-default\">" +
                    "             <div class=\"panel-heading\">" +
                    "                 <h4 class=\"panel-title\">" +
                    "                     <a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#graph\" href=\"#" + currentScheduler + "\">" + currentScheduler + "</a>" +
                    "                 </h4>" +
                    "             </div><!-- panel-heading -->" +
                    "             <div id=\"" + currentScheduler + "\" class=\"panel-collapse collapse\">" +
                    "                  <div class=\"panel-body\">" +
                    "                      <table class=\"table table-hover table-condensed\">" +
                    "                          <thead>" +
                    "                              <tr>" +
                    "                                  <th><a class=\"tooltips\" title=\"Graph\" data-placement=\"top\">Graph</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"Nodes\" data-placement=\"top\">Nodes</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"Edges\" data-placement=\"top\">Edges</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"Throughput\" data-placement=\"top\">T</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"CriticalPathDuration\" data-placement=\"top\">CpD</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"AlgorithmDuration\" data-placement=\"top\">AD</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"SingleBlockExecutionTime\" data-placement=\"top\">SBET</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"ScheduleCriticalPathRatio\" data-placement=\"top\">SCpR</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"ScheduleCriticalPathVariance\" data-placement=\"top\">SCpV</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"AverageCommunicationTime\" data-placement=\"top\">ACT</a></th>" +
                    "                              </tr>" +
                    "                          </thead>";

            int currentGraph = 0;
            for(Statistic statistics : scheduler) {
                html += "                       <tbody>" +
                        "                                   <tr>" +
                        "                                       <td>" + currentGraph + "</td>" +
                        "                                       <td><span class=\"badge\">" + statistics.nodeCount + "</span></td>" +
                        "                                       <td>" + statistics.edgeCount +"</td>" +
                        "                                       <td>" + statistics.throughput + "</td>" +
                        "                                       <td>" + statistics.cpDuration + "</td>" +
                        "                                       <td>" + statistics.algorithmDuration + "</td>" +
                        "                                       <td>" + statistics.singleBlockExecutionTime + "</td>" +
                        "                                       <td>" + statistics.scheduleCpRatio + "</td>" +
                        "                                       <td>" + statistics.scheduleCpVariance + "</td>" +
                        "                                       <td>" + statistics.averageCommunicationTime + "</td>" +
                        "                                   </tr>" +
                        "                               </tbody>";
                currentGraph++;
            }

            html += "                     </table>" +
                    "                 </div><!-- panel-body -->" +
                    "            </div><!-- schedulerCount -->" +
                    "        </div><!-- panel panel-default -->";
            currentScheduler++;
        }

        html += "         </div><!-- tab-pane -->";

        return html;
    }

    private String generateTaskGroupStatisticsHTML() {
        String html  = "                 <div class=\"tab-pane\" class=\"panel-group\" id=\"group\">";

        int currentScheduler = 0;
        for(List<Statistic> scheduler : taskGroupStatistics) {
            html += "         <div class=\"panel panel-default\">" +
                    "             <div class=\"panel-heading\">" +
                    "                 <h4 class=\"panel-title\">" +
                    "                     <a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#group\" href=\"#" + currentScheduler + currentScheduler + "\">" + currentScheduler + "</a>" +
                    "                 </h4>" +
                    "             </div><!-- panel-heading -->" +
                    "             <div id=\"" + currentScheduler + currentScheduler + "\" class=\"panel-collapse collapse\">" +
                    "                  <div class=\"panel-body\">" +
                    "                      <table class=\"table table-condensed\">" +
                    "                          <thead>" +
                    "                              <tr>" +
                    "                                  <th><a class=\"tooltips\" title=\"Counter\" data-placement=\"top\">#</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"Nodes\" data-placement=\"top\">Nodes</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"Edges\" data-placement=\"top\">Edges</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"Throughput\" data-placement=\"top\">T</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"CriticalPathDuration\" data-placement=\"top\">CpD</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"AlgorithmDuration\" data-placement=\"top\">AD</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"SingleBlockExecutionTime\" data-placement=\"top\">SBET</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"ScheduleCriticalPathRatio\" data-placement=\"top\">SCpR</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"ScheduleCriticalPathVariance\" data-placement=\"top\">SCpV</a></th>" +
                    "                                  <th><a class=\"tooltips\" title=\"AverageCommunicationTime\" data-placement=\"top\">ACT</a></th>" +
                    "                              </tr>" +
                    "                          </thead>";

            int currentGroup = 1;
            for(Statistic statistics : scheduler) {
                html += "                       <tbody>" +
                        "                                   <tr>" +
                        "                                       <td>" + currentGroup + "</td>" +
                        "                                       <td><span class=\"badge\">" + statistics.nodeCount + "</span></td>" +
                        "                                       <td>" + statistics.edgeCount +"</td>" +
                        "                                       <td>" + statistics.throughput + "</td>" +
                        "                                       <td>" + statistics.cpDuration + "</td>" +
                        "                                       <td>" + statistics.algorithmDuration + "</td>" +
                        "                                       <td>" + statistics.singleBlockExecutionTime + "</td>" +
                        "                                       <td>" + statistics.scheduleCpRatio + "</td>" +
                        "                                       <td>" + statistics.scheduleCpVariance + "</td>" +
                        "                                       <td>" + statistics.averageCommunicationTime + "</td>" +
                        "                                   </tr>" +
                        "                               </tbody>";
                currentGroup++;
            }

            html += "                     </table>" +
                    "                 </div><!-- panel-body -->" +
                    "            </div><!-- schedulerCount -->" +
                    "        </div><!-- panel panel-default -->";
            currentScheduler++;
        }
        html += generateScheduleAlgorithmDurationChart() +
              "         </div><!-- tab-pane -->";
        return html;
    }

    private String generateSchedulerStatisticsHTML() {
        String html  = "                 <div class=\"tab-pane\" class=\"panel-group\" id=\"scheduler\">" +
                       "                    <div class=\"panel-body\">" +
                       "                      <table class=\"table table-condensed\">" +
                       "                          <thead>" +
                       "                              <tr>" +
                       "                                  <th><a class=\"tooltips\" title=\"Scheduler\" data-placement=\"top\">Scheduler</a></th>" +
                       "                                  <th><a class=\"tooltips\" title=\"Edges\" data-placement=\"top\">Edges</a></th>" +
                       "                                  <th><a class=\"tooltips\" title=\"Throughput\" data-placement=\"top\">T</a></th>" +
                       "                                  <th><a class=\"tooltips\" title=\"CriticalPathDuration\" data-placement=\"top\">CpD</a></th>" +
                       "                                  <th><a class=\"tooltips\" title=\"AlgorithmDuration\" data-placement=\"top\">AD</a></th>" +
                       "                                  <th><a class=\"tooltips\" title=\"SingleBlockExecutionTime\" data-placement=\"top\">SBET</a></th>" +
                       "                                  <th><a class=\"tooltips\" title=\"ScheduleCriticalPathRatio\" data-placement=\"top\">SCpR</a></th>" +
                       "                                  <th><a class=\"tooltips\" title=\"ScheduleCriticalPathVariance\" data-placement=\"top\">SCpV</a></th>" +
                       "                                  <th><a class=\"tooltips\" title=\"AverageCommunicationTime\" data-placement=\"top\">ACT</a></th>" +
                       "                              </tr>" +
                       "                          </thead>" +
                       "                          <tbody>";

        int schedulerCounter = 0;
        for(Statistic scheduler : schedulerStatistics) {

            html +=
                    "                               <tr>" +
                    "                                   <td><strong>" + schedulers[schedulerCounter].getName() + "</strong></td>" +
                    "                                   <td>" + scheduler.edgeCount +"</td>" +
                    "                                   <td>" + scheduler.throughput + "</td>" +
                    "                                   <td>" + scheduler.cpDuration + "</td>" +
                    "                                   <td>" + scheduler.algorithmDuration + "</td>" +
                    "                                   <td>" + scheduler.singleBlockExecutionTime + "</td>" +
                    "                                   <td>" + scheduler.scheduleCpRatio + "</td>" +
                    "                                   <td>" + scheduler.scheduleCpVariance + "</td>" +
                    "                                   <td>" + scheduler.averageCommunicationTime + "</td>" +
                    "                               </tr>";
            schedulerCounter++;
        }
        html += "                               </tbody>" +
                "                           </table>" +
                "                      </div><!-- panel-body -->" +
                "                    </div><!-- tab-pane -->";
        return html;
    }


    private void generateHTML() throws IOException {
        System.out.println("Generate statistics.html.");
        
        String html = "<!DOCTYPE HTML>" +
                      "<html>" +
                      "     <head>" +
                      "         <title>BlommaGraphs - statistics.html</title>" +
                      "         <link rel=\"stylesheet\" media=\"screen\" href=\"../../ressources/bootstrap-3.0.0/dist/css/bootstrap.css\">" +
                      "         <style rel=\"stylesheet\">" +
                      "             footer {" +
                      "                 margin: 10px 0 0 0;" +
                      "             }" +

                      "             .panel {" +
                      "                 margin: 10px 0 0 0;" +
                      "             }" +
                      "         </style>" +

                      "         <meta charset=\"utf-8\">" +

                      "         <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->" +
                      "         <script src=\"http://code.jquery.com/jquery.js\"></script>" +

                      "         <!-- Include all compiled plugins (below), or include individual files as needed -->" +
                      "         <script src=\"../../ressources/bootstrap-3.0.0/dist/js/bootstrap.min.js\"></script>" +
                      "         <script src=\"../../ressources/Chart.js\"></script>" +
                      
                      "         <script>" +
                      "             $(function() {" +
                      "                 $('.tooltips').tooltip();" +
                      "             });" +

                      "             $('#graph a').click(function (e) {\n" +
                      "                 e.preventDefault()\n" +
                      "                 $(this).tab('show')\n" +
                      "             });"  +
                      "             $('#group a').click(function (e) {\n" +
                      "                 e.preventDefault()\n" +
                      "                 $(this).tab('show')\n" +
                      "             });"  +
                      "             $('#scheduler a').click(function (e) {\n" +
                      "                 e.preventDefault()\n" +
                      "                 $(this).tab('show')\n" +
                      "             });"  +

                       "            $('.table > tr').click(function() {" +
                       "                $('this').popover()" +
                       "            });" +
                      "         </script>" +

                      "     </head>" +
                      "     <body>" +
                      "         <div class=\"container\">" +
                      "             <div class=\"page-header\">\n" +
                      "                 <h1>BlommaGraphs <small>Statistics</small></h1>" +
                      "             </div>" +
                      "        <div class=\"tabbable\">" +
                      "                 <ul class=\"nav nav-tabs\">" +
                      "                     <li class=\"active\"><a href=\"#graph\" data-toggle=\"tab\">Graph</a></li>" +
                      "                     <li><a href=\"#group\" data-toggle=\"tab\">Group</a></li>" +
                      "                     <li><a href=\"#scheduler\" data-toggle=\"tab\">Scheduler</a></li>" +
                      "                 </ul>" +
                      "                 <div class=\"tab-content\">" +

                      generateTaskGraphStatisticsHTML() +
                      generateTaskGroupStatisticsHTML() +
                      generateSchedulerStatisticsHTML() +

                      "                 </div><!-- tab-content -->" +
                      "             </div><!-- tabable -->" +
                      "         </div><!-- container -->" +

                      "         <footer>" +
                      "             <div class=\"container\">" +
                      "                 <small>BlommaGraphs © Copyright 2013 | Simon Kerler, Richard Stromer, Manuel Oswald, Ziad Nörpel, Benjamin Wöhrl</small>" +
                      "             </div>" +
                      "         </footer>" +
                      "     </body>" +
                      "</html>";


        System.out.println("Done.\nWriting results to file.\n");
        new File("export/statistics").mkdirs();
        FileUtils.writeFile(statisticsFilePath, html);
        System.out.println("Done.\nStatisticsBuilder done.\n");
    }
    
    /**
     * 
     * @return
     */
    private String generateScheduleAlgorithmDurationChart() {
        String html = "<h1><small>Algorithm durations</small></h1>\n" +
                "<canvas id=\"canvas\" width=\"400\" height=\"400\"></canvas>\n" +
                "<script type=\"text/javascript\">\n" +
                "  var ctx = document.getElementById(\"canvas\").getContext(\"2d\");\n" +
                "  var data = {\n" +
                "  labels : [\"10\", \"50\",\"100\",\"300\",\"500\"],\n" +
                "  datasets : [\n";
                
                // Generate data set
                for (int currentScheduler = 0; currentScheduler < schedulerCount; ++currentScheduler) {
                    List<Statistic> statList = taskGroupStatistics.get(currentScheduler);
                    html += "  {\n" +
                    getSchedulerChartJSColors(currentScheduler) + "\n" +
                    "        data : [";
                    
                    for(int currStat = 0; currStat < statList.size(); ++currStat) {
                        html += statList.get(currStat).algorithmDuration;
                        if(currStat < statList.size() - 1)
                            html += ",";
                    }
                        
                    html += "]\n" +
                    "      },\n";
                }
                html += "    ]\n" +
                "  }\n" +
                "  var myNewChart = new Chart(ctx).Line(data);\n" +
                "</script>\n";
       return html;
    }
    
    /**
     * Return the color for schedulers that is used inside the generate chart methods to build Chart.js
     * diagrams.
     * @param i Number of the scheduler.
     * @return String holding js formatted color definitions.
     */
    private String getSchedulerChartJSColors(int i) {
        String[] colors = new String[4];
        colors[0] = "fillColor : \"rgba(89, 107, 205,0.5)\", strokeColor : \"rgba(89, 107, 205,1)\", pointColor : \"rgba(89, 107, 205,1)\",";
        colors[1] = "fillColor : \"rgba(157, 165, 213,0.5)\", strokeColor : \"rgba(157, 165, 213,1)\", pointColor : \"rgba(157, 165, 213,1)\",";
        colors[2] = "fillColor : \"rgba(24, 40, 77, 0.5)\", strokeColor : \"rgba(24, 40, 77,1)\", pointColor : \"rgba(24, 40, 77,1)\",";
        colors[3] = "fillColor : \"rgba(57, 60, 77,0.5)\", strokeColor : \"rgba(57, 60, 77,1)\", pointColor : \"rgba(57, 60, 77,1)\",";
        return colors[i];
    }
}

