package schedulerAlgorithm;

import dataStructure.PCB;
import scheduler.SchedulerSimulationController;

public interface ReadyQueue {

    /**
     * This method inserts new process in the ready queue in the order of
     * scheduler type.
     *
     * @param newPCB
     */
    void insert(PCB newPCB);

    /**
     * This method edits the given process in the ready queue.
     *
     * @param pcb
     */
    void edit(PCB pcb);

    /**
     * This method deletes the given process from the ready queue.
     *
     * @param pcb
     */
    void delete(PCB pcb);

    /**
     * This method draws the Gantt chart for the process according to the
     * scheduler type.
     *
     * @param ctrl
     */
    void DrawGanttChart(SchedulerSimulationController ctrl);
}
