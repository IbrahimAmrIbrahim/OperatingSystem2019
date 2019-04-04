package schedulerAlgorithm;

import dataStructure.PCB;
import dataStructure.Queue;
import scheduler.SchedulerSimulationController;

public class FCFS extends Queue implements ReadyQueue {

    @Override
    public void insert(PCB newPCB) {
        enqueue(newPCB);
    }

    @Override
    public void DrawGanttChart(SchedulerSimulationController ctrl) {
    }

}
