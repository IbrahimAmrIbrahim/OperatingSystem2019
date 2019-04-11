package schedulerAlgorithm;

import dataStructure.PCB;
import dataStructure.Queue;
import dataStructure.Node;

import javafx.scene.paint.Color;
import scheduler.SchedulerSimulationController;

public class RoundRobin extends Queue implements ReadyQueue {

    // in my class priority is the finish of the exe 
//================ private area ================================================//
    private int RR_time = 3;// for now the RR period is 10 unit time 
    private int node_number = 0;
    private Node start_copy = new Node();
    private boolean first_time_use = true;
    private int run_time = 0;
    private float turn_around_avr = 0;
    private float waiting_avr = 0;
    private Queue my_queue = new Queue();
//==============================================================================//

    public void set_RR_time(int x) {
        RR_time = x;
    }
//==============================================================================//

    public void insert(PCB newPCB) {

        enqueue(newPCB);

    }
//==============================================================================//

    public void reinsert(int slice) {
        RR_time = slice;
        if (first_time_use) {
            Node current;
            current = head;

            while (current != null) {
                start_copy = current;

                insert2(current.getPcb());
                current = current.getNext();

            }
            first_time_use = false;
        } //====================//
        else {
            Node dummy = start_copy;;
            start_copy = start_copy.getNext();
            while (start_copy != null) {
                dummy = start_copy;

                insert2(start_copy.getPcb());
                start_copy = start_copy.getNext();

            }
            start_copy = dummy;
        }

    }
//==============================================================================//
    // insert done 

    @Override
    public void DrawGanttChart(SchedulerSimulationController ctrl) {

        //=================================//
        //===================================//
        int slice;
        slice = ctrl.getTimeSlice();
        reinsert(slice);
        Node temp2 = my_queue.getHead();
        sort(0);
        sort_2(0);

        time_confg();
        int start_time = 0;
        for (int i = 0; i < node_number; i++) {
            //fixed area //

            //========================================//
            if (temp2.getPcb().getStartofExec() > start_time) {
                ctrl.drawIdleProcess(temp2.getPcb().getStartofExec() - start_time);

            }
            ctrl.draw(temp2.getPcb().getBurstTime(), temp2.getPcb().getPID(), temp2.getPcb().getColor());
            start_time = temp2.getPcb().getEndofExec();
            temp2 = temp2.getNext();
        }

        ctrl.writeAvgWaitingTime(waiting_avr);
        ctrl.writeAvgTurnarroundTime(turn_around_avr);

    }
//==============================================================================//

    public void insert2(PCB newPCB) {

        PCB pcb_dumy = new PCB(false);

        pcb_dumy.copy(newPCB);
        pcb_dumy.setPriority(0);// all input got proprtiy of 0
        //======================================================================//
        // if the duration is less than or equal the quantim just push it 
        if (pcb_dumy.getBurstTime() <= RR_time) {
            pcb_dumy.setStartofExec(pcb_dumy.getArrivalTime());// set start of the exe is the arrival time 
            pcb_dumy.setEndofExec(pcb_dumy.getArrivalTime() + pcb_dumy.getBurstTime());// set the end is the arrival + brust
            my_queue.enqueue(pcb_dumy);// enqueue it
            node_number++;//node number ++

        } //==========================esle======================================//
        else {
            // the duration is biger than the quantim
            PCB new_pcb = new PCB(false);// a new pcb
            new_pcb.copy(pcb_dumy);// copy the old
            // while the burst time bigger than zero
            while (new_pcb.getBurstTime() > 0) {
                
                int new_pcb_time = new_pcb.getBurstTime();
                PCB next_new = new PCB(false);// fix it
                int put_in;// the duartion in the brust time 
                //======================================//
                if (new_pcb.getBurstTime() > RR_time) {
                   // the brust time is biger than the quantim
                    put_in = RR_time;// the duration of this process equal to the quantim
                } else {
                    put_in = new_pcb.getBurstTime();// the duration of this process equal to it's brust time
                }
                //=================================================//

                new_pcb.setBurstTime(put_in);// change the time to be put in
                //next_new start copy here
                next_new.copy(new_pcb);

                //=========================================================//
                next_new.setStartofExec(new_pcb.getArrivalTime());// start of exe equal to the arrival 
                next_new.setEndofExec(new_pcb.getArrivalTime() + put_in);// the end equal arrival + the put in 
                my_queue.enqueue(next_new);// enqueue it
                node_number++;// node ++
                //====================================//
                //change the burst time from x to x - RR_time 
                // example from 15 to 15  - 10 = 5
                //============================================//
                new_pcb.setPriority(new_pcb.getPriority() - 1);// the next copy gor priority = -1 + it's priority
                new_pcb.setBurstTime(new_pcb_time - RR_time);// the brust time equal to the old time ( the real brust - the quantim)
                new_pcb.setArrivalTime(new_pcb.getArrivalTime() + RR_time);// the new arrival = to the old arrival + the put in
                // but right now the arrival is arrival + RR_time ?
                // now we will go to the second loop if and only if the time i used is the quantim time :)
                // can gives error
                //===========================================//
                // insert it again  
            }

        }

    }
//==============================================================================//
    // sort on arrival time 

    public void sort(int mode) {
        // puble sort on arrival time only 
        for (int i = 0; i < node_number; i++) {
            Node current = my_queue.getHead();
            Node next = current.getNext();
            PCB drifter = new PCB(false);
        //===============================================//
            while (next != null) {
                int start_current = current.getPcb().getStartofExec();
                int end_current = current.getPcb().getStartofExec() + RR_time;
                int start_next = next.getPcb().getStartofExec();
                int end_next = next.getPcb().getStartofExec() + RR_time;

                // iterate till the end 
                int current_arrival = current.getPcb().getArrivalTime();
                int next_arrival = next.getPcb().getArrivalTime();

                //first case normal case done 
                if (current_arrival > next_arrival) {

                    drifter.copy(current.getPcb());
                    current.getPcb().copy(next.getPcb());

                    current.getPcb().setStartofExec(start_next);
                    current.getPcb().setEndofExec(end_next);

                    next.getPcb().copy(drifter);
                    next.getPcb().setStartofExec(start_current);

                    next.getPcb().setEndofExec(end_current);

                } // if i have a redudnt  p0 p0' p1 at same time 
                else if (next_arrival == current_arrival
                        && current.getPcb().getPriority() < next.getPcb().getPriority()) {

                    // trade //
                    drifter.copy(current.getPcb());
                    current.getPcb().copy(next.getPcb());

                    current.getPcb().setStartofExec(start_next);
                    current.getPcb().setEndofExec(end_next);

                    next.getPcb().copy(drifter);
                    next.getPcb().setStartofExec(start_current);

                    next.getPcb().setEndofExec(end_current);
                    //==================================//
                    // change the arrival time depending on the shift  only on premum

                    //====================================//
                } // the 2 are premum
                else if (next_arrival == current_arrival
                        && current.getPcb().getPriority() > next.getPcb().getPriority()) {

                } else if (next_arrival == current_arrival
                        && current.getPcb().getPriority() < 0 && next.getPcb().getPriority() < 0) {

                }
                //if i have p0 p0' p1 

                current = current.getNext();
                next = current.getNext();
            }

        }
    }
//==============================================================================//
    // sort on arrival and the priortiy 

    public void sort_2(int mode) {

        // i am sure it's sorted  on arrival
        // so all i need is to get the first element and it's arrival time is my start xD
        //======= fixed area =======//
        for (int i = 0; i < node_number; i++) {
            int run = 0;
            run = my_queue.getHead().getPcb().getArrivalTime() + my_queue.getHead().getPcb().getBurstTime();// the first node is the start of my code and run = the [  ]

            Node current = my_queue.getHead(); // current point at first node
            Node next = current.getNext();  // next point at second node   
            if (next != null) {

                if (next.getPcb().getArrivalTime() > run) {
                    next.getPcb().setStartofExec(next.getPcb().getArrivalTime());
                    next.getPcb().setEndofExec(next.getPcb().getStartofExec() + next.getPcb().getBurstTime());
                    run = next.getPcb().getEndofExec();
                } else {
                    next.getPcb().setStartofExec(run);
                    next.getPcb().setEndofExec(run + next.getPcb().getBurstTime());
                    run = next.getPcb().getEndofExec();
                }

                current = next;// now next and current point at the second node
                next = current.getNext();// next point at the third  node
            }
            while (current != null && next != null)// because the next we made with out check first time only
            {

                PCB drifter = new PCB(false);

                int start_next = next.getPcb().getStartofExec();
                int end_next = next.getPcb().getStartofExec() + next.getPcb().getBurstTime();

                // iterate till the end 
                int current_arrival = current.getPcb().getArrivalTime();
                int next_arrival = next.getPcb().getArrivalTime();

                if (current_arrival < next_arrival && current.getPcb().getPriority() < next.getPcb().getPriority()) {
                    if (run > next_arrival) {
                        // trade //
                        drifter.copy(current.getPcb());
                        current.getPcb().copy(next.getPcb());

                        current.getPcb().setStartofExec(start_next);
                        current.getPcb().setEndofExec(end_next);
                        // the third node will get the second exe time 
                        // and the third will get the end of the second 
                        next.getPcb().copy(drifter);

                        //-----------------------------------//
                        next.getPcb().setStartofExec(current.getPcb().getEndofExec());

                        next.getPcb().setEndofExec(current.getPcb().getEndofExec() + drifter.getBurstTime());

                    }

                } else {
                    next.getPcb().setStartofExec(run);
                    next.getPcb().setEndofExec(run + next.getPcb().getBurstTime());
                }
                //======================================================//
                //
                run = run + next.getPcb().getBurstTime();

                current = current.getNext();
                next = current.getNext();

            }

        }
    }

    // claclulat turn around time //
//==============================================================================//
    // to get the round robin time 
    public void time_confg() {
        // i am sure it's sorted 
        // so all i need is to get the first element and it's arrival time is my start xD

        //======= fixed area =======//
        float final_value = 0;
        int end_of_exe = 0;
        run_time = 0;
        int arrival_time = 0;
        int brust_time = 0;
        int ideal_time = 0;
        int start_of_exe = 0;
        int time_diff = 0;
        float number_of_pcb = node_number;
        float turn_around_time = 0;
        float avrage_turn_around_time = 0;
        float waiting_time = 0;
        float avrage_waiting_time = 0;
        int turn_around_for_process[] = new int[node_number];
        Node current = my_queue.getHead();

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
                header = my_queue.getHead();
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
        turn_around_avr = avrage_turn_around_time;
        waiting_avr = avrage_waiting_time;
    }
//==============================================================================//
    //just trash

    public void run_real(SchedulerSimulationController ctrl) {

        for (int i = 0; i < 5; i++) {
            //fixed area //

            //========================================//
            ctrl.drawIdleProcess(5);

        }

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
