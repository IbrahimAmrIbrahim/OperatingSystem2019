package schedulerAlgorithm;

import dataStructure.PCB;
import dataStructure.Queue;
import dataStructure.Node;
import javafx.scene.paint.Color;
import scheduler.SchedulerSimulationController;

public class RoundRobin extends Queue implements ReadyQueue {
    // in my class priority is the finish of the exe 

    private int RR_time = 3;// for now the RR period is 10 unit time 
    private int node_number = 0;
   
    private int last_arrival_time=0;
    private int run_time = 0;
    // run time must be a factor of 10 like 0 10 20 30 etc

    private int waiting_time = 0;
    private PCB new_pcb= new PCB(false);

    
    public void set_RR_time(int x)
    {
        RR_time=x;
    }
    public void insert(PCB newPCB) {

        //==============================//
        // if the time is less than the RR_TIME then just add it in the qq 
        if (newPCB.getBurstTime() <= RR_time) {
            enqueue(newPCB);
            node_number++;
          
        } //======================================================//
        else {
            System.out.println("flag 1");
          
            new_pcb.copy(newPCB);
            // while the burst time bigger than zero
            while (new_pcb.getBurstTime() > 0) {
                int new_pcb_time = new_pcb.getBurstTime();
                PCB next_new = new PCB(false);// fix it

                int put_in;
                //======================================//
                if (new_pcb.getBurstTime() > RR_time) {
                    
                    put_in = RR_time;
                } else {
                    put_in = new_pcb.getBurstTime();
                }
                //=================================================//

                new_pcb.setBurstTime(put_in);// change the time to be as qq time
                //next_new start copy here
                next_new.copy(new_pcb);

                enqueue(next_new);

                node_number++;
                //====================================//
                //change the burst time from x to x - RR_time 
                // example from 15 to 15  - 10 = 5
                //============================================//
                new_pcb.setPriority(-1);
                new_pcb.setBurstTime(new_pcb_time - RR_time);
                new_pcb.setArrivalTime(new_pcb.getArrivalTime() + RR_time);

                //===========================================//
                // insert it again  
            }
        }

        sort(0);
        printQueue();
        time(0);
    }

    // insert done 
    @Override
    public void DrawGanttChart(SchedulerSimulationController ctrl) {
        Node temp = head;
       
        int start_time=0;
        for (int i = 0; i < node_number; i++) {
            //fixed area //

            //========================================//
            if (temp.getPcb().getStartofExec()>start_time)
            {
                 ctrl.drawIdleProcess( temp.getPcb().getStartofExec()-start_time);
               
            }
            ctrl.draw(temp.getPcb().getBurstTime() , temp.getPcb().getPID() , temp.getPcb().getColor());
             start_time=temp.getPcb().getEndofExec();
            temp = temp.getNext();
        }
        ctrl.writeAvgWaitingTime(time(3));
        ctrl.writeAvgTurnarroundTime(time(2));
    }

    public void sort2(int mode) {

        if (mode == 0)// arive time 
        {

            // bubble sort with node 
            Node temp;
            for (int i = 0; i < node_number; i++) {
                temp = head;
                Node current;
                Node next;
                current = temp;
                next = temp.getNext();
                PCB my_pcb = new PCB(false);
                while (current.getNext() != null) {

                    if (current.getPcb().getArrivalTime() > next.getPcb().getArrivalTime()) {
                        //===============//

                        current = next;
                        next.setNext(current);

                        //==================//
                        //=====================================//
                        //==============================//
                        //================================//
                        current = next;

                        //=====================================//
                    } else {
                        //==================================//

                        //==================================//
                        current = next;
                        next = next.getNext();
                    }
                }

            }

        }

    }

    public void sort(int mode) {

        if (mode == 0)// arive time 
        {

            // bubble sort with node 
            Node temp;
            for (int i = 0; i < node_number; i++) {
                temp = head;
                Node current;
                Node next;
                current = temp;
                next = temp.getNext();
                PCB my_pcb = new PCB(false);
                while (current.getNext() != null) {
                    System.out.println(  "current "+current.getPcb().getPriority() + " next "+next.getPcb().getPriority());
                    if (current.getPcb().getArrivalTime() > next.getPcb().getArrivalTime() 
                            ||
                            (current.getPcb().getArrivalTime() == next.getPcb().getArrivalTime()
                            && 
                            (
                            current.getPcb().getPID()< next.getPcb().getPID()
                            &&
                            current.getPcb().getPriority()<next.getPcb().getPriority()
                            )
                            )
                            ) {
                        //===============//

                        my_pcb.copy(next.getPcb());

                        //==================//
                        //=====================================//
                        next.getPcb().copy(current.getPcb());// let the next point at temp      temp ->next and temp<-next 
                        current.getPcb().copy(my_pcb);

                        //==============================//
                        //================================//
                        current = next;

                        //=====================================//
                    } else {
                        //==================================//

                        //==================================//
                        current = next;
                        next = next.getNext();
                    }
                }

            }

        }

    }

    public long time(int mode) {
        // i am sure it's sorted 
        // so all i need is to get the first element and it's arrival time is my start xD

        //======= fixed area =======//
        int final_value = 0;
        int end_of_exe = 0;
        run_time = 0;
        int arrival_time = 0;
        int brust_time = 0;
        int start_time = 0;
        int start_of_exe = 0;
        int time_diff = 0;
        int number_of_pcb = node_number;
        int turn_around_time = 0;
        int avrage_turn_around_time = 0;
        int waiting_time = 0;
        int avrage_waiting_time = 0;
        int turn_around_for_process[] = new int[node_number];
        Node current = head;

        //======================================================//
        for (int i = 0; i < node_number; i++) {
            time_diff = 0;
            // fixed area //

            brust_time = current.getPcb().getBurstTime();
            arrival_time = current.getPcb().getArrivalTime();

            if (i == 0) {

                //===============================//
                end_of_exe = brust_time + arrival_time;

                //=============================//
                start_of_exe = arrival_time;
                //==========set =============================//
                current.getPcb().setStartofExec(arrival_time);

                current.getPcb().setEndofExec(end_of_exe);

            } // if not the first time 
            else {
                // check if i have this pcb before or no ?

                //========= fixed area ==========//
                Node header;
                header = head;
                boolean time_diff_bool = true;

                //======================================//
                // check if i have it before loop //
                for (int j = 0; j < i; j++) {

                    if (current.getPcb().getPID() == header.getPcb().getPID()) {

                        if (time_diff_bool) {
                            time_diff = current.getPcb().getArrivalTime() - header.getPcb().getArrivalTime();
                            time_diff_bool = false;
                            number_of_pcb--;

                        }
                        turn_around_for_process[j] = 0;

                    }
                    header = header.getNext();
                }
                //=======================end of the for loop ======================//

                //================set ==========================//
                current.getPcb().setStartofExec(end_of_exe);// my start is the end of the  first
                if (arrival_time > end_of_exe) {
                    current.getPcb().setStartofExec(arrival_time);// my start is the arrival
                }
                //=============== fixed area ====================//
                start_of_exe = current.getPcb().getStartofExec();
                //==================================//
                //=====================set ========================//
                current.getPcb().setEndofExec(start_of_exe + brust_time);
                //=====================================================//
                //===============fixed area ====================//
                end_of_exe = current.getPcb().getEndofExec();
                //=============================================//
            }

            // after the if and else 
            //===================end of the for loop ====================//
            turn_around_for_process[i] = end_of_exe - arrival_time + time_diff;

            run_time += brust_time;

            System.out.println("==============");
            System.out.println("start = " + current.getPcb().getStartofExec());
            System.out.println("RUN = " + run_time);
            System.out.println("end = " + current.getPcb().getEndofExec());

            System.out.println("==============");
            current = current.getNext();

        }

        // claclulat turn around time //
        for (int i = 0; i < node_number; i++) {

            turn_around_time += turn_around_for_process[i];

        }

        // final area //
        //=================================//
        turn_around_time = turn_around_time;
        waiting_time += turn_around_time - run_time;
        //=================================//
        avrage_turn_around_time = turn_around_time / number_of_pcb;
        avrage_waiting_time = waiting_time / number_of_pcb;
        //================================//

        // system out area //
        System.out.println(" waiting_time final = " + waiting_time);
        System.out.println(" turn around final = " + turn_around_time);
        System.out.println(" number_of_pcb = " + number_of_pcb);
        System.out.println(" avrage turn around final = " + avrage_turn_around_time);

        switch (mode) {
            case 0:
                final_value = turn_around_time;
                break;
            case 1:
                final_value = waiting_time;
                break;
            case 2:
                final_value = avrage_turn_around_time;
                break;
            case 3:
                final_value = avrage_waiting_time;
                break;

        }
     
        return final_value;
    }
}
