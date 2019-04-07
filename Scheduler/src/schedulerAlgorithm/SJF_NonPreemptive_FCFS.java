package schedulerAlgorithm;

import dataStructure.Node;
import dataStructure.PCB;
import dataStructure.Queue;
import scheduler.SchedulerSimulationController;

public class SJF_NonPreemptive_FCFS extends Queue implements ReadyQueue {

    @Override
    public void insert(PCB newPCB) {
        Node newNo = new Node(newPCB);
        
        if (head == null) {
            head = newNo;
            tail = newNo;
        }
        
        else if(head==tail){
            if(head.getPcb().getArrivalTime() == newNo.getPcb().getArrivalTime()){
                if(head.getPcb().getBurstTime() <= newNo.getPcb().getBurstTime()){
                    enqueue(newPCB);
                }
                else{
                    newNo.setNext(head);
                    head=newNo;
                }
            }
            else if(head.getPcb().getArrivalTime() < newNo.getPcb().getArrivalTime()){
                enqueue(newPCB);
            }
            else{
                newNo.setNext(head);
                head=newNo;
            }
        }
        
    }

    @Override
    public void DrawGanttChart(SchedulerSimulationController ctrl) {
    }
}
