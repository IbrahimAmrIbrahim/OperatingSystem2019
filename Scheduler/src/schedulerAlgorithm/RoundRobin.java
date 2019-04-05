package schedulerAlgorithm;

import dataStructure.PCB;
import dataStructure.Queue;
import dataStructure.Node;
import scheduler.SchedulerSimulationController;

public class RoundRobin extends Queue implements ReadyQueue {

    private int RR_time = 10;// for now the RR period is 10 unit time 
    private int node_number = 0;

    private int waiting_time = 0;
    private PCB new_pcb;

    @Override
    public void insert(PCB newPCB) {

        //        waiting_time+=newPCB.getBurstTime(); // take the waiting time 
        // my qq must be special
        // as if process newpcb period is biger than the qq period it will be inside the qq agian as new processs
        // so the system i should go with is insert first time  
        // p1 p2 p3 p4 then p1 p2 p3 p4 again
        // this is special case of the specials
        // i need 
        //process -> brust time and arrival time .
        // i need first qq then a second qq 
        // i need to know when we call insert .
        //==============================//
        // if the time is less than the RR_TIME then just add it in the qq 
        if (newPCB.getBurstTime() > 0) {
            enqueue(newPCB);
            node_number++;
        } //-------------------------------------------------------//
        // the time is bigger than the qq time, then i will suffer 
        // i need to add it again so i need to change in the schedual itself 
        // chanign it from p1 p2 p3 p4 to be p1 p2 p3 p4 p1 etc
        else {
            // while the burst time bigger than zero
            while (newPCB.getBurstTime() > 0) {
                int new_pcb_time = newPCB.getBurstTime();
                //=================================================//

                newPCB.setBurstTime(RR_time);// change the time to be as qq time
                enqueue(newPCB);
                node_number++;
                //====================================//
                //change the burst time from x to x - RR_time 
                // example from 15 to 15  - 10 = 5
                //============================================//
                newPCB.setBurstTime(new_pcb_time - RR_time);
                newPCB.setArrivalTime(newPCB.getArrivalTime() + RR_time);

                //===========================================//
                // insert it again  
            }
        }

    }

    // insert done 
    @Override
    public void DrawGanttChart(SchedulerSimulationController ctrl) {
        sort(0);
        
    }

   
   

    public void sort(int mode) {

        if (mode == 0)// arive time 
        {

            // bubble sort with node 
            Node temp;
            for (int i = 0; i < node_number; i++) {
                temp = head;

                while (temp.getNext() != null) {
                    if (temp.getPcb().getArrivalTime() >= temp.getNext().getPcb().getArrivalTime()) {
                        //===============//
                        Node dummy;
                        dummy = temp.getNext();// dummy = next of temp
                        //==================//

                        //=====================================//
                        // temp ->next->next of the next
                        temp.getNext().setNext(temp);// let the next point at temp      temp ->next and temp<-next 
                        temp.setNext(dummy.getNext());// let temp point at next of the next  next->temp->next of the next 

                        //=====================================//
                    }
                }

            }

        }

    }

}
