package schedulerAlgorithm;

import dataStructure.PCB;
import dataStructure.Queue;
import dataStructure.Node;
import scheduler.SchedulerSimulationController;

public class RoundRobin extends Queue implements ReadyQueue {

    private int RR_time = 10;// for now the RR period is 10 unit time 
    private int node_number = 0;
    private int run_time=0;
    // run time must be a factor of 10 like 0 10 20 30 etc
    
    private int waiting_time = 0;
    private PCB new_pcb;

    @Override
    public void insert(PCB newPCB) {

        //==============================//
        
        
        // if the time is less than the RR_TIME then just add it in the qq 
        if (newPCB.getBurstTime() <= RR_time) {
            enqueue(newPCB);
            node_number++;
        } 
        
        //======================================================//
        
        
        else {
            System.out.println("flag 1");
            // while the burst time bigger than zero
            while (newPCB.getBurstTime() > 0) {
                int new_pcb_time = newPCB.getBurstTime();
                PCB next_new = new PCB(false) ;// fix it
               
                int put_in;
                //======================================//
                if (newPCB.getBurstTime() > RR_time)
                {
                    put_in=RR_time;
                }
                else 
                {
                    put_in=newPCB.getBurstTime();
                }
                //=================================================//
             
                
                newPCB.setBurstTime(put_in);// change the time to be as qq time
                //next_new start copy here
                next_new.copy(newPCB);
               
                enqueue(next_new);
              
                      
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
         
sort(0);
printQueue();
    }

    // insert done 
    @Override
    public void DrawGanttChart(SchedulerSimulationController ctrl) {
        
        
    }

   
   

    public void sort(int mode) {
       
        if (mode == 0)// arive time 
        {
         
            // bubble sort with node 
            Node temp ;
            for (int i = 0; i < node_number; i++) {
                temp = head;
                 Node current;
                 Node next;
                 current=temp;
                 next=temp.getNext();
                 PCB my_pcb = new PCB(false);
                while (current.getNext() != null) {
                   
                    if (current.getPcb().getArrivalTime() > next.getPcb().getArrivalTime()) {
                        //===============//
                      
                       
                       my_pcb.copy(next.getPcb());
                       
                       
                        //==================//

                        //=====================================//
                        // temp ->next->next of the next
                        next.getPcb().copy(current.getPcb());// let the next point at temp      temp ->next and temp<-next 
                        current.getPcb().copy(my_pcb);
                        current=next;
                       
                          
                        //=====================================//
                    }
                    else 
                    {
                        
                        current=next;
                        next=current.getNext();
                    }
                }

            }

        }

    }

}
