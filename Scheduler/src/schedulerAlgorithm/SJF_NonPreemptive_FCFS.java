package schedulerAlgorithm;

import dataStructure.Node;
import dataStructure.PCB;
import dataStructure.Queue;
import scheduler.SchedulerSimulationController;

public class SJF_NonPreemptive_FCFS extends Queue implements ReadyQueue {

    @Override
    public void insert(PCB newPCB) {
        Node newNo = new Node(newPCB);
        Node ptr=head;
        Node pre_ptr=head;
        int flag=0;
        int bru_T=0;
        
        
        if (head == null) {
            head = newNo;
            tail = newNo;
            newNo.setNext(null);
        }
        
        else if(head==tail){
            if(head.getPcb().getArrivalTime() == newNo.getPcb().getArrivalTime()){
                if(head.getPcb().getBurstTime() <= newNo.getPcb().getBurstTime()){
                    enqueue(newPCB);
                    tail.setNext(null);
                }
                else{
                    newNo.setNext(head);
                    head=newNo;
                }
            }
            else if(head.getPcb().getArrivalTime() < newNo.getPcb().getArrivalTime()){
                enqueue(newPCB);
                tail.setNext(null);
            }
            else{
                newNo.setNext(head);
                head=newNo;
            }
        }
        else{
           while(flag==0){
                if(newNo.getPcb().getArrivalTime()<= bru_T){
                   if(ptr.getPcb().getArrivalTime()<= newNo.getPcb().getArrivalTime()){
                       if(newNo.getPcb().getBurstTime() < ptr.getPcb().getBurstTime()){
                           pre_ptr.setNext(newNo);
                           newNo.setNext(ptr);
                           break;
                       }
                       else if(newNo.getPcb().getBurstTime() == ptr.getPcb().getBurstTime()){
                           newNo.setNext(ptr.getNext());
                           ptr.setNext(newNo);
                           break;
                       }
                       else{
                           while(newNo.getPcb().getBurstTime() >= ptr.getPcb().getBurstTime()){
                               pre_ptr=ptr;
                               ptr=ptr.getNext();
                           }
                            newNo.setNext(pre_ptr.getNext());
                            pre_ptr.setNext(newNo);
                            break;
                       }
                   }
                   else{
                        newNo.setNext(ptr.getNext());
                        ptr.setNext(newNo);
                        break;
                   }
                }
                pre_ptr=ptr;
                if(ptr.getNext() == null){
                    enqueue(newPCB);
                    tail.setNext(null);
                    flag=1;
                }
                bru_T = bru_T + ptr.getPcb().getBurstTime();
                ptr=ptr.getNext();
            }
        }
    }

    @Override
    public void DrawGanttChart(SchedulerSimulationController ctrl) {
    }

    @Override
    public void edit(PCB PCB) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(PCB pcb) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

