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
    private int node_number_2 = 0;
    private Node start_copy = new Node();
    private boolean first_time_use = true;
    private int run_time = 0;
    private float turn_around_avr = 0;
    private float waiting_avr = 0;
    private Queue my_queue = new Queue();
    private Queue final_queue = new Queue();
//==============================================================================//

    public void set_RR_time(int x) {
        RR_time = x;
    }
//==============================================================================//
// main insert  

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

    //===================re insert 2 can change slice time 
    public void reinsert2(int slice) {
        RR_time = slice;
        my_queue.setHead(null);
        my_queue.setTail(null);
        node_number = 0;
        node_number_2 = 0;
        final_queue.setHead(null);
        final_queue.setTail(null);
        Node current;
        current = head;

        while (current != null) {

            insert_method_2(current.getPcb());// now on my_queue is the process 
            current = current.getNext();

        }

    } //====================//

//==============================================================================//
    // insert done 
    @Override
    public void DrawGanttChart(SchedulerSimulationController ctrl) {

        //=================================//
        //===================================//
        int slice;
        slice = ctrl.getTimeSlice();
        set_RR_time(slice);
        reinsert2(slice);
        sort_arrival(0);

        insert_on_final_queue();

     
        //sort(0);
        //sort_2(0);
        time_calculating();
        //time_confg();
        Node temp2 = final_queue.getHead();
        int start_time = 0;
        for (int i = 0; i < node_number_2; i++) {
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
    // insert for second algorthm

    public void insert_method_2(PCB newPCB) {

        PCB pcb_dumy = new PCB(false);

        pcb_dumy.copy(newPCB);
        pcb_dumy.setPriority(0);// all input got proprtiy of 0
        //======================================================================//
        // if the duration is less than or equal the quantim just push it 

        pcb_dumy.setStartofExec(pcb_dumy.getArrivalTime());// set start of the exe is the arrival time 
        pcb_dumy.setEndofExec(pcb_dumy.getArrivalTime() + pcb_dumy.getBurstTime());// set the end is the arrival + brust
        my_queue.enqueue(pcb_dumy);// enqueue it
        node_number++;//node number ++

    }

    //============================================================================//
    public void inset_on_final(PCB newPCB) {

        PCB pcb_dumy = new PCB(false);

        pcb_dumy.copy(newPCB);
        pcb_dumy.setPriority(0);// all input got proprtiy of 0
        //======================================================================//
        // if the duration is less than or equal the quantim just push it 
        pcb_dumy.setStartofExec(newPCB.getStartofExec());
        pcb_dumy.setEndofExec(newPCB.getEndofExec());
        final_queue.enqueue(pcb_dumy);// enqueue it
        node_number_2++;//node number ++

    }

    //============================================================================//
    public void insert_on_final_queue() {
        // it's sorted on arrival 
        int run_time = 0;

        // loop on them all 
        int i = node_number;
        Node current = my_queue.getHead();
        Node prev = my_queue.getHead();
        while (current != null && node_number != 0) {//if node_number==0 that mean there is no elemebts
            // if i will go inside the queue 
            //current now is the first in the array
            PCB new_pcb = new PCB(false);
            new_pcb.copy(current.getPcb()); // new pcb = current now 
            if (current.getPcb().getArrivalTime() <= run_time) {

                // the copy
                new_pcb.setStartofExec(run_time);// set the start first 
                // check do i need to push it back ?
                if (current.getPcb().getBurstTime() > RR_time) {

                    int new_brust = current.getPcb().getBurstTime() - RR_time;
                    new_pcb.setBurstTime(RR_time);
                    new_pcb.setEndofExec(new_pcb.getStartofExec() + new_pcb.getBurstTime());
                    // insert this in final_queue 
                    inset_on_final(new_pcb);

                    Node next = current.getNext();
                    // push back in my_queue 

                    current.getPcb().setBurstTime(new_brust);
                    while (next != null) {
                        // the next node 
                        PCB swap_pcb = new PCB(false);
                        swap_pcb.copy(current.getPcb());

                        //==========================//
                        current.getPcb().copy(next.getPcb());
                        next.getPcb().copy(swap_pcb);
                        // this will move the current node to the end 
                        current = next;
                        next = current.getNext();
                    }

                } // if it's equal or less no need to push back
                else if (new_pcb.getBurstTime() <= RR_time) {
                    Queue trade = new Queue();
                    new_pcb.setBurstTime(new_pcb.getBurstTime());
                    new_pcb.setEndofExec(new_pcb.getStartofExec() + new_pcb.getBurstTime());
                    //insert it in final_queue 
                    // delete it from my_queue by making the head point at the next
                    // and node number is less by 1 now

                    inset_on_final(new_pcb);
                    // push back in my_queue 
                    Node next = current.getNext();
                    while (current.getNext() != null) {
                        // the next node 

                        PCB swap_pcb = new PCB(false);

                        //==========================
                        swap_pcb.copy(new_pcb);
                        current.getPcb().copy(next.getPcb());
                        next.getPcb().copy(swap_pcb);
                        // this will move the current node to the end 
                        prev = current;
                        current = current.getNext();
                        next = current.getNext();
                    }
                    node_number--;
                    Node current_2 = my_queue.getHead();

                    for (int j = 0; j < node_number; j++) {
                        PCB dumy = new PCB(false);
                        dumy.copy(current_2.getPcb());
                        trade.enqueue(dumy);

                        current_2 = current_2.getNext();
                    }

                    for (int s = 0; s < node_number + 1; s++) {
                        my_queue.dequeue();
                    }
                    Node current_3 = trade.getHead();
                    while (current_3 != null) {
                        PCB dumy_2 = new PCB(false);
                        dumy_2.copy(current_3.getPcb());
                        my_queue.enqueue(dumy_2);

                        current_3 = current_3.getNext();

                    }

                }

                run_time = new_pcb.getEndofExec();// the run time equal to run of the exe
                current = my_queue.getHead();
                i = node_number;
                continue;// exit the loop to insert new one 
            }

            // if i am not qualified to start then shall it be someone else 
            current = current.getNext();
            i--;// to make sure that you searched in all the queue
            // if i==0
            if (i == 0) {
                // no process is in the queue in the run time i need to be in ideal case process 
                run_time = my_queue.getHead().getPcb().getArrivalTime();// and it's the time of the first arrival in my_queue
                current = my_queue.getHead();// point at the first agaian
                i = node_number;// re set the iterator

            }

        }

    }

    //=======================================================================//
// inset RR with fair rounds 
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

                // modified 
                //  new_pcb.setPriority(new_pcb.getPriority() - 1);
                //
                //
                new_pcb.setPriority(new_pcb.getPriority() - 1);

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
    // sort on arrive time only
    public void sort_arrival(int mode) {
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

                } else if (current_arrival == next_arrival
                        && current.getPcb().getPID() > next.getPcb().getPID()) {

                    drifter.copy(current.getPcb());
                    current.getPcb().copy(next.getPcb());

                    current.getPcb().setStartofExec(start_next);
                    current.getPcb().setEndofExec(end_next);

                    next.getPcb().copy(drifter);
                    next.getPcb().setStartofExec(start_current);

                    next.getPcb().setEndofExec(end_current);

                }

                current = current.getNext();
                next = current.getNext();
            }

        }
    }
//==============================================================================//
    // sort on arrival time 
    // round robin with fair 

    public void sort(int mode) {
        // puble sort on arrival time only 
        for (int i = 0; i < node_number; i++) {
            Node current = my_queue.getHead();
            Node next = current.getNext();
            PCB drifter = new PCB(false);
            //===============================================//
            while (next != null) {
                int start_current = current.getPcb().getStartofExec();
                int end_current = current.getPcb().getStartofExec() + current.getPcb().getBurstTime();
                int start_next = next.getPcb().getStartofExec();
                int end_next = next.getPcb().getStartofExec() + next.getPcb().getBurstTime();

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
                    // modify   && current.getPcb().getPriority() < next.getPcb().getPriority()) {
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
                    //
                }
                //if i have p0 p0' p1 

                current = current.getNext();
                next = current.getNext();
            }

        }
    }
//==============================================================================//
    // sort on arrival and the priortiy 
// malnash d3wa beha 

    public void sort_2(int mode) {
    
        // i am sure it's sorted  on arrival
        // so all i need is to get the first element and it's arrival time is my start xD
        // issue 
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

                //==============modif  ===================//
                if (next.getPcb().getPID() == current.getPcb().getPID()) {
                    next.getPcb().setPriority(next.getPcb().getPriority() - 1);
                } //================================================================================//
                else if (current_arrival < next_arrival && current.getPcb().getPriority() < next.getPcb().getPriority()) {
                    if (run > next_arrival) {
                        // modified  if (run > next_arrival) {
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
    // -------------------------------------..
    void time_calculating() {
        int node_number = 0;
        Node node_ = head;

        while (node_ != null) {
            node_number++;
            node_ = node_.getNext();
        }

        int total_brust = 0;
        float turn_around_for_proces[] = new float[node_number];
        Node current_queue;
        current_queue = head;
        for (int n = 0; n < node_number; n++) {

            Node current_final;
            current_final = final_queue.getHead();
            for (int i = 0; i < node_number_2; i++) {
                if (current_queue.getPcb().getPID() == current_final.getPcb().getPID()) {
                    turn_around_for_proces[n] = current_final.getPcb().getEndofExec() - current_queue.getPcb().getArrivalTime();
                }
                current_final = current_final.getNext();

            }
            current_queue = current_queue.getNext();
        }

        Node current = head;
        float total_turn_around = 0;
        for (int i = 0; i < node_number; i++) {
            total_brust += current.getPcb().getBurstTime();
            total_turn_around += turn_around_for_proces[i];
            current = current.getNext();
        }
        turn_around_avr = total_turn_around / node_number;
        waiting_avr = (total_turn_around - total_brust) / node_number;

    }
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
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(PCB pcb) {

        Node current = head;

        int number_node_queue = 0;
        Node node_ = head;

        while (node_ != null) {
            number_node_queue++;
            node_ = node_.getNext();
        }

        Queue Queue_trade = new Queue();
        for (int i = 0; i < number_node_queue; i++) {
            if (!pcb.equals(current.getPcb())) {
                Queue_trade.enqueue(current.getPcb());
            }
            dequeue();
            current = current.getNext();
        }
        Node current_trade = Queue_trade.getHead();
        for (int i = 0; i < number_node_queue - 1; i++) {
            enqueue(current_trade.getPcb());

            current_trade = current_trade.getNext();
        }

      
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }
}
