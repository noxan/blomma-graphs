
import java.util.List;

import com.github.noxan.blommagraphs.graphs.TaskGraph;
import com.github.noxan.blommagraphs.graphs.TaskGraphNode;
import com.github.noxan.blommagraphs.scheduling.ScheduledTask;
import com.github.noxan.blommagraphs.scheduling.Scheduler;
import com.github.noxan.blommagraphs.scheduling.system.SystemMetaInformation;


public class DynamicLevelSchedulerImpl implements Scheduler {
    private TaskGraph taskGraph;

    @Override
    public List<ScheduledTask> schedule(TaskGraph graph, SystemMetaInformation systemInformation) {
        // compute static level of all nodes
        // initialize ready-pool at first only start node

        // compute the earliest start time for every ready node for each
        // processor LaPush
        // choose the node-processor pair with the biggest dynamic level and
        // comit it to the processor
        // add new ready nodes to the ready node pool
        return null;
    }

    public boolean isReadyNode(TaskGraphNode taskGraphNode) {
        return true;
    }

}
