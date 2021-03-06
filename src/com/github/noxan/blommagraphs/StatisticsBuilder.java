package com.github.noxan.blommagraphs;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphEdge;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.graphs.serializer.TaskGraphSerializer;
import com.github.noxan.blommagraphs.graphs.serializer.impl.STGSerializer;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;
import com.github.noxan.blommagraphs.scheduling.basic.impl.dls.DynamicLevelScheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.genetic.GeneticDLSScheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.genetic.GeneticLASTScheduler;
import com.github.noxan.blommagraphs.scheduling.basic.impl.last.LASTScheduler;
import com.github.noxan.blommagraphs.scheduling.serializer.ScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.serializer.impl.ExtendedScheduledTaskListSerializer;
import com.github.noxan.blommagraphs.scheduling.serializer.impl.HTMLSerializer;
import com.github.noxan.blommagraphs.scheduling.stream.StreamScheduler;
import com.github.noxan.blommagraphs.scheduling.stream.impl.BasicStreamScheduler;
import com.github.noxan.blommagraphs.scheduling.stream.impl.CustomStreamScheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;
import com.github.noxan.blommagraphs.scheduling.system.impl.DefaultSystemMetaInformation;
import com.github.noxan.blommagraphs.utils.FileUtils;
import com.github.noxan.blommagraphs.utils.TaskGraphFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StatisticsBuilder {

    private class Statistic {
        private String filePath;
        private String scheduledTaskHTMLFilePath;
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

    // ** Start of user configuration. Feel free to do changes :)
    
    // Set how many task graph groups should be sheduled. 
    private final int taskGroupCount = 5;
    // Set the number of task graphs that should be sheduled for each group.
    private final int taskGraphGroupSize = 100;
    // Set the number of cpus.
    private final int cpuCount = 3;
    // Number of TaskGraph copies that are scheduled.
    private final int blockSize = 2;

    // Instantiate schedulers that are evaluated.
    private StreamScheduler[] schedulers = {
            new BasicStreamScheduler(new LASTScheduler()),
            new BasicStreamScheduler(new DynamicLevelScheduler()),
            new BasicStreamScheduler(new GeneticDLSScheduler()),
            new BasicStreamScheduler(new GeneticLASTScheduler()),
            new CustomStreamScheduler()
    };
    
    // ** End of user configuration. Do not change anything below this point!
    
    // Directories containing the task graphs to schedule. Should not be changed.
    String[] taskGroupDirectories = {
            "export/graphs/10",
            "export/graphs/50",
            "export/graphs/100",
            "export/graphs/300",
            "export/graphs/500"
    };
    
    // The total number of sheduled task graphs. Don't change this!
    private final int taskGraphCount = taskGraphGroupSize * taskGroupCount;
    
    private final String statisticsFilePath = "export/statistics/statistics.html";
    
    private List<List<Statistic>> taskGraphStatistics;
    private List<List<Statistic>> taskGroupStatistics;
    private List<Statistic> schedulerStatistics;

    private SystemMetaInformation systemMetaInformation;

    private TaskGraphSerializer taskGraphSerializer;
    private ScheduledTaskListSerializer scheduledTaskListHTMLSerializer;
    private ScheduledTaskListSerializer scheduledTaskListTextSerializer;

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        StatisticsBuilder statBuilder = new StatisticsBuilder();

        // Create directory where statistics are saved.
        new File("export/statistics/scheduledtasks").mkdirs();
        
        statBuilder.buildStatistics();
        statBuilder.generateHTML();

    }

    private StatisticsBuilder() {
        // Build lists
        taskGraphStatistics = new ArrayList<List<Statistic>>();
        taskGroupStatistics = new ArrayList<List<Statistic>>();
        schedulerStatistics = new ArrayList<Statistic>();

        for (int scheduler = 0; scheduler < schedulers.length; ++scheduler) {
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

        systemMetaInformation = new DefaultSystemMetaInformation(cpuCount);

        // Serializers
        taskGraphSerializer = new STGSerializer();
        scheduledTaskListHTMLSerializer = new HTMLSerializer();
        scheduledTaskListTextSerializer = new ExtendedScheduledTaskListSerializer();
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
//        File[] graphDirectories = graphGroupsDirectory.listFiles();

        TaskGraph graph;
        // Following task graphs are used for scheduling.
        TaskGraph[] taskGraphs;
        ScheduledTaskList scheduledTaskList;

        int taskGroupCounter = 0;

        for (int dirCount = 0; dirCount < taskGroupCount; ++dirCount) {
            File[] graphFiles = new File(taskGroupDirectories[dirCount]).listFiles();

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

                for (int schedulerId = 0; schedulerId < schedulers.length; ++schedulerId) {
                    System.out.println("Calculate taskGraphStatistics here!");

                    Statistic currentStatistic = taskGraphStatistics.get(schedulerId).get(
                            taskGroupCounter * taskGraphGroupSize + graphId);

                    // Starttime measurement
                    long currentAlgorithmDuration = System.currentTimeMillis();
                    // SCHEDULING
                    scheduledTaskList = schedulers[schedulerId].schedule(taskGraphs,
                            systemMetaInformation);
                    currentAlgorithmDuration = (System.currentTimeMillis() - currentAlgorithmDuration);
                    System.out.println("Algorithm duration: " + (double) currentAlgorithmDuration
                            / 1000);
                    
                    // Write scheduledtasklist to file.
                    String fileName = "export/statistics/scheduledtasks/" + schedulerId + "_" +
                            taskGroupCounter + "_" + graphId + ".html";
                    currentStatistic.scheduledTaskHTMLFilePath = "../../" + fileName;
                    generateScheduledTaskHTMLFile(fileName, scheduledTaskList, graph);

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
        for (int i = 0; i < schedulers.length; ++i) {
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
        for (int i = 0; i < schedulers.length; ++i) {
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
                    "                     <a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#graph\" href=\"#" + currentScheduler + "\">" + schedulers[currentScheduler].getName() + "</a>" +
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
                        "                                   <tr data-link=\"" + statistics.scheduledTaskHTMLFilePath  + "\">" +
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
                    "                     <a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#group\" href=\"#" + currentScheduler + currentScheduler + "\">" + schedulers[currentScheduler].getName() + "</a>" +
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
        html += generateGroupAlgorithmDurationChart() +
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
                "                           <div class=\"diagramWrapper\">" +
                generateSchedulerAlgorithmDurationChart() +
                "                           </div>" +
                "                           <div class=\"diagramWrapper\">" +
                generateSchedulerThroughputChart() +
                "                           </div>" +
                "                           <div class=\"diagramWrapper\">" +
                generateSchedulerScheduleDurationChart() +
                "                           </div>" +
                "                           <div class=\"diagramWrapper\">" +
                generateSchedulerCpRatioChart() +
                "                           </div>" +
                "                           <div class=\"clear\"></div><!-- clear -->" +
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
                      "             body, html {" +
                      "             }" +

                      "             .container {" +
                      "             }" +

                      "             footer {" +
                      "                 margin-top: 20px;" +
                      "                 height: 40px;" +
                      "             }" +

                      "             .popover {" +
                      "                 max-width: 1000px;" +
                      "             }" +

                      "             .panel {" +
                      "                 margin: 10px 0 0 0;" +
                      "             }" +

                      "             .diagramWrapper {" +
                      "                 width: 370px;" +
                      "                 float: left;" +
                      "             }" +

                      "             .clear {" +
                      "                 clear: both;" +
                      "             }" +
                      "         </style>" +

                      "         <meta charset=\"utf-8\">" +

                      "         <script src=\"../../ressources/jquery.js\"></script>" +

                      "         <script src=\"../../ressources/bootstrap-3.0.0/dist/js/bootstrap.min.js\"></script>" +
                      "         <script src=\"../../ressources/Chart.js \"></script>" +

                      "         <script>" +
                      "             $(function() {" +
                      "                 $('.tooltips').tooltip();" +

                      "                 $('.table-hover tr').click( function() {\n" +
                      "                     var link = $(this).data(\"link\");" +
                      "                     window.open(link);" +
                      "                 });" +
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
                      "         </script>" +

                      "     </head>" +
                      "     <body>" +
                      "         <div class=\"container\">" +
                      "             <div class=\"page-header\">\n" +
                      "                 <h1><a href=\"statistics.html\">BlommaGraphs</a><small> Statistics</small></h1>" +
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
    private String generateGroupAlgorithmDurationChart() {
        String html = "<h1><small>Algorithm durations</small></h1>\n" +
                "<canvas id=\"canvas\" width=\"400\" height=\"400\"></canvas>\n" +
                generateChartKey() +
                " <script type=\"text/javascript\">\n" +
                "  var ctx = document.getElementById(\"canvas\").getContext(\"2d\");\n" +
                "  var data = {\n" +
                "  labels : ['10', '50','100','300','500'],\n" +
                "  datasets : [\n";
                
                // Generate data set
                for (int currentScheduler = 0; currentScheduler < schedulers.length; ++currentScheduler) {
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
     * 
     */
    private String generateSchedulerAlgorithmDurationChart() {
        String html = "<h1><small>Algorithm durations</small></h1>\n" +
                "<canvas id='schedulerAlgorithmDurationChart' width='370' height='370'></canvas>\n" +
                "<script type='text/javascript'>\n" +
                "  var ctx = document.getElementById('schedulerAlgorithmDurationChart').getContext('2d');\n" +
                "  var data = {\n" +
                generateChartLabels() +
                "  datasets : [\n" +
                "       {\n" +
                getSchedulerChartJSColors(1) + "\n" +
                "        data : [";
        
                // Generate data set
                for (int currentScheduler = 0; currentScheduler < schedulers.length; ++currentScheduler) {
                    Statistic stat = schedulerStatistics.get(currentScheduler);
                    html += stat.algorithmDuration;
                    if(currentScheduler < schedulerStatistics.size() - 1)
                         html += ",";
                }
                html += "    ]\n" +
                "     }\n" +
                "  ]\n" +
                "}\n" +
                "  var myNewChart = new Chart(ctx).Bar(data);\n" +
                "</script>\n";
       return html;
    }
    
    private String generateSchedulerThroughputChart() {
        String html = "<h1><small>Throughput</small></h1>\n" +
                "<canvas id=\"schedulerThroughputChart\" width=\"370\" height=\"370\"></canvas>\n" +
                "<script type=\"text/javascript\">\n" +
                "  var ctx = document.getElementById(\"schedulerThroughputChart\").getContext(\"2d\");\n" +
                "  var data = {\n" +
                generateChartLabels();
                

                
                html += "  datasets : [\n" +
                "       {\n" +
                getSchedulerChartJSColors(1) + "\n" +
                "        data : [";
        
                // Generate data set
                for (int currentScheduler = 0; currentScheduler < schedulers.length; ++currentScheduler) {
                    Statistic stat = schedulerStatistics.get(currentScheduler);
                    html += stat.throughput;
                    if(currentScheduler < schedulerStatistics.size() - 1)
                         html += ",";
                }
                html += "    ]\n" +
                "     }\n" +
                "  ]\n" +
                "}\n" +
                "  var myNewChart = new Chart(ctx).Bar(data);\n" +
                "</script>\n";
        
        return html;
    }
    
    private String generateSchedulerScheduleDurationChart() {
        String html = "<h1><small>Single block execution time</small></h1>\n" +
                "<canvas id=\"schedulerScheduleDurationChart\" width=\"370\" height=\"370\"></canvas>\n" +
                "<script type=\"text/javascript\">\n" +
                "  var ctx = document.getElementById(\"schedulerScheduleDurationChart\").getContext(\"2d\");\n" +
                "  var data = {\n" +
                generateChartLabels();

        html += "  datasets : [\n" +
                "       {\n" +
                getSchedulerChartJSColors(1) + "\n" +
                "        data : [";
        
                // Generate data set
                for (int currentScheduler = 0; currentScheduler < schedulers.length; ++currentScheduler) {
                    Statistic stat = schedulerStatistics.get(currentScheduler);
                    html += stat.singleBlockExecutionTime;
                    if(currentScheduler < schedulers.length - 1)
                         html += ",";
                }
                html += "    ]\n" +
                "     }\n" +
                "  ]\n" +
                "}\n" +
                "  var myNewChart = new Chart(ctx).Bar(data);\n" +
                "</script>\n";
        
        return html;
    }

    private String generateSchedulerCpRatioChart() {
        String html = "<h1><small>Schedule CP ratio</small></h1>\n" +
                "<canvas id=\"schedulerCPRatioChart\" width=\"300\" height=\"300\"></canvas>\n" +
                generateChartKey() +
                "<script type=\"text/javascript\">\n" +
                "  var ctx = document.getElementById(\"schedulerCPRatioChart\").getContext(\"2d\");\n" +
                "  var data = [";
               
                for(int i = 0; i < schedulers.length; ++i) {
                    Statistic stat = schedulerStatistics.get(i);
                    html += "       {\n" +
                            "           value : " + stat.scheduleCpRatio + "," +
                            "           color: \"rgba(" + getSchedulerRGBColors(i) + ", 1)\"\n" +
                            "       }";
                            
                    if (i < schedulers.length - 1)
                        html += ",\n";
                }

                html += "   ];" +
                "  var myNewChart = new Chart(ctx).PolarArea(data);\n" +
                "</script>\n";
        
        return html;
    }
    
    private String generateChartKey() {
        return "<p>Key: " +
        " <span class=\"badge\" style=\"background-color: rgba(" + getSchedulerRGBColors(0) + ",1)\">LAST</span>" +
        " <span class=\"badge\" style=\"background-color: rgba(" + getSchedulerRGBColors(1) + ",1)\">DLS</span>" +
        " <span class=\"badge\" style=\"background-color: rgba(" + getSchedulerRGBColors(2) + ",1)\">Genetic</span>" +
        " <span class=\"badge\" style=\"background-color: rgba(" + getSchedulerRGBColors(3) + ",1)\">Custom</span>";
    }
    
    private String generateChartLabels() {
        String html = "  labels : [";
        // Create labels from scheduler names
        for (int currentScheduler = 0; currentScheduler < schedulers.length; ++currentScheduler) {
            html += "\"" + schedulers[currentScheduler].getName() + "\"";
            if (currentScheduler < schedulers.length - 1)
                html += ",";
        }
        html += " ],\n";
        return html;
    }
    /**
     * Return the color for schedulers that is used inside the generate chart methods to build Chart.js
     * diagrams.
     * @param i Number of the scheduler.
     * @return String holding js formatted color definitions.
     */
    private String getSchedulerChartJSColors(int i) {
        return "fillColor : \"rgba(" + getSchedulerRGBColors(i) + ",0.2)\", strokeColor : \"rgba(" + getSchedulerRGBColors(i) + ",1)\", pointColor : \"rgba(" + getSchedulerRGBColors(i) + ",1)\",";
    }
    
    private String getSchedulerRGBColors(int i) {
        String[] colors = {
            "120, 0, 230",
            "43, 66, 194",
            "24, 40, 77",
            "57, 60, 77",
            "12, 17, 243",
            "243, 12, 214"
        };
        return colors[i];
    }
    
    private void generateScheduledTaskHTMLFile(String filePath, ScheduledTaskList scheduledTaskList,
            TaskGraph graph) throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE HTML>\n");
        html.append("<html>\n");
        html.append("  <head>\n");
        html.append("    <title>blommagraphs - scheduled task</title>\n");

        html.append("    <meta charset=\"utf-8\">");
                
        html.append("    <script src=\"../../../ressources/jquery.js\"></script>");
        html.append("    <script src=\"../../../ressources/bootstrap-3.0.0/dist/js/bootstrap.min.js\"></script>\n");
        html.append("    <script src=\"../../../ressources/arborjs/arbor.js\"></script>\n");
        html.append("    <script src=\"../../../ressources/arborjs/renderer.js\"></script>\n");

        html.append("    <link rel=\"stylesheet\" media=\"screen\" href=\"../../../ressources/bootstrap-3.0.0/dist/css/bootstrap.css\">");
        html.append("    <link rel=\"stylesheet\" media=\"screen\" href=\"../../../ressources/css/GraphVisualizerHTML.css\">");
        html.append("    <style>");
        html.append("       footer {");
        html.append("           height: 40px;");
        html.append("           margin-top: 20px;");
        html.append("       }");
        html.append("    </style>");
        html.append("  </head>\n");
        html.append("  <body>\n");
        html.append("    <div class=\"container\">\n");
        html.append("       <div class=\"page-header\">\n");
        html.append("          <h1>BlommaGraphs<small> Scheduled Task</small></h1>\n");
        html.append("       </div><!-- page-header -->\n");
        
        
        html.append("       <div class=\"row\">\n");
        
        html.append("         <div class=\"col-md-6\" id=\"arborgraph\">\n");
        html.append("               <canvas id=\"taskgraphviewport\" style=\"width:100%;\" width=\"555\" height=\"600\"></canvas> ");
        html.append("    <script>");
        html.append("       $(document).ready(function() {\n");
        html.append("           var sys = arbor.ParticleSystem(1000, 600, 0.5);\n");
        html.append("           sys.renderer = Renderer(\"#taskgraphviewport\")\n");
        html.append("           sys.parameters({gravity:true})\n");

                                // arbor task graph
                                StringBuilder arborTaskGraphBuilder = new StringBuilder();

                                for (TaskGraphNode node : graph.getNodeSet()) {
                                    arborTaskGraphBuilder.append("sys.addNode('" + node.getId() + "'");
                                    if (node == graph.getFirstNode()) {
                                        arborTaskGraphBuilder
                                                .append(", {'first': true, 'fixed': true, 'mass': 20, 'p': {'y': 20, 'x': 'auto'}}");
                                    } else if (node == graph.getLastNode()) {
                                        arborTaskGraphBuilder
                                                .append(", {'last': true, 'fixed': true, 'mass': 20, 'p': {'y': 780, 'x': 'auto'}}");
                                    }
                                    arborTaskGraphBuilder.append(")\n");
                                }
                                for (TaskGraphEdge edge : graph.getEdgeSet()) {
                                    arborTaskGraphBuilder.append("sys.addEdge('" + edge.getPrevNode().getId() + "','"
                                            + edge.getNextNode().getId() + "')\n");
                                }

        html.append(        arborTaskGraphBuilder.toString());
        html.append("      });");
        html.append("    </script>");
        html.append("         </div><!-- arborgraph -->\n");

        html.append("         <div class=\"col-md-6\" id=\"stggraph\">\n");
        html.append("           <p>\n");
        html.append("             <pre>\n");
        html.append(                taskGraphSerializer.serialize(graph));
        html.append("             </pre>\n");
        html.append("           </p>");
        html.append("         </div><!-- stggraph -->\n");

        html.append("       </div><!-- row -->\n");
        
        
        html.append("       <div class=\"row\">\n");
        html.append("         <div class=\"col-md-6\" id=\"visualizedschedule\">\n");
        html.append(            scheduledTaskListHTMLSerializer.serialize(scheduledTaskList));
        html.append("         </div><!-- visualizedschedule -->\n");
        
        html.append("         <div class=\"col-md-6\" id=\"textualschedule\">\n");
        html.append("           <p>\n");
        html.append("              <pre>\n");
        html.append(                 scheduledTaskListTextSerializer.serialize(scheduledTaskList));
        html.append("              </pre>\n");
        html.append("           </p>\n");
        html.append("         </div><!-- textualschedule -->\n");
        
        html.append("       </div><!-- row -->\n");
        
        
        html.append("       <div class=\"clear\"></div>");
        html.append("    </div><!-- container -->\n");
        html.append("         <footer>\n");
        html.append("             <div class=\"container\">\n");
        html.append("                 <small>BlommaGraphs © Copyright 2013 | Simon Kerler, Richard Stromer, Manuel Oswald, Ziad Nörpel, Benjamin Wöhrl</small>\n");
        html.append("             </div>\n");
        html.append("         </footer>\n");
        html.append("  </body>\n");
        html.append("</html>\n");
     
        FileUtils.writeFile(filePath, html.toString());
    }
}

