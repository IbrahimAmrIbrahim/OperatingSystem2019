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
    private Queue my_queue = new Queue();
    private int last_arrival_time = 0;
    private int run_time = 0;
    private float turn_around_avr = 0;
    private float waiting_avr = 0;
    // run time must be a factor of 10 like 0 10 20 30 etc

    private int waiting_time = 0;

    public void set_RR_time(int x) {
        RR_time = x;
    }

    public void insert(PCB newPCB) {

        enqueue(newPCB);

    }
//======================================================//

    public void reinsert(int slice) {
        Node current;
        current = head;
        RR_time = slice;
        while (current != null) {

            insert2(current.getPcb());
            current = current.getNext();

        }
    }

    // insert done 
    @Override
    public void DrawGanttChart(SchedulerSimulationController ctrl) {

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

    public void insert2(PCB newPCB) {
        PCB pcb_dumy = new PCB(false);
        int start = pcb_dumy.getArrivalTime();
        int end = pcb_dumy.getArrivalTime() + RR_time;
        pcb_dumy.copy(newPCB);
        //==============================//
        // if the time is less than the RR_TIME then just add it in the qq 
        if (pcb_dumy.getBurstTime() <= RR_time) {
            pcb_dumy.setStartofExec(pcb_dumy.getArrivalTime());
            pcb_dumy.setEndofExec(pcb_dumy.getArrivalTime() + RR_time);
            my_queue.enqueue(pcb_dumy);
            node_number++;

        } //======================================================//
        else {

            PCB new_pcb = new PCB(false);
            new_pcb.copy(pcb_dumy);
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

                //=========================================================//
                next_new.setStartofExec(start);
                next_new.setEndofExec(end);
                my_queue.enqueue(next_new);

                node_number++;
                //====================================//
                //change the burst time from x to x - RR_time 
                // example from 15 to 15  - 10 = 5
                //============================================//
                new_pcb.setPriority(new_pcb.getPriority() - 1);
                new_pcb.setBurstTime(new_pcb_time - RR_time);

                new_pcb.setArrivalTime(new_pcb.getArrivalTime() + RR_time);

                start = start + RR_time;
                end = end + RR_time;

                //===========================================//
                // insert it again  
            }

        }

    }

    public void sort(int mode) {
        for (int i = 0; i < node_number; i++) {
            Node current = my_queue.getHead();
            Node next = current.getNext();
            PCB drifter = new PCB(false);

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
                System.out.println("my queue  on arrival");
                my_queue.printQueue();
                System.out.println("============================");

                current = current.getNext();
                next = current.getNext();
            }

        }
    }

    public void sort_2(int mode) {

        System.out.println("my queue  on arrival final ");
        my_queue.printQueue();
        System.out.println("============================");

        // i am sure it's sorted 
        // so all i need is to get the first element and it's arrival time is my start xD
        //======= fixed area =======//
        for (int i = 0; i < node_number; i++) {
            int run = 0;
            run = my_queue.getHead().getPcb().getArrivalTime() + RR_time;// the first node is the start of my code and run = the [  ]

            Node current = my_queue.getHead(); // current point at first node

            Node next = current.getNext();  // next point at second node 
            next.getPcb().setStartofExec(run);
            next.getPcb().setEndofExec(run + RR_time);
            run = run + RR_time;
            current = next;// now next and current point at the second node
            next = current.getNext();// next point at the third  node
            while (current != null && next != null)// because the next we made with out check first time only
            {

                PCB drifter = new PCB(false);

                int start_current = current.getPcb().getStartofExec();
                int end_current = current.getPcb().getStartofExec() + RR_time;
                int start_next = next.getPcb().getStartofExec();
                int end_next = next.getPcb().getStartofExec() + RR_time;

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
                        next.getPcb().setStartofExec(start_next + RR_time);

                        next.getPcb().setEndofExec(end_next + RR_time);

                    }

                } else {
                    next.getPcb().setStartofExec(run);
                    next.getPcb().setEndofExec(run + RR_time);
                }
                //======================================================//
                //
                run = run + RR_time;
                System.out.println("my queue  on exe");
                my_queue.printQueue();
                System.out.println("============================");
                current = current.getNext();
                next = current.getNext();
            }
        }
    }
    // claclulat turn around time //

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

    public float time2(int mode) {
        // i am sure it's sorted 
        // so all i need is to get the first element and it's arrival time is my start xD
        System.out.println("-------------------------");
        my_queue.printQueue();
        //======= fixed area =======//
        float final_value = 0;
        int end_of_exe = 0;
        run_time = 0;
        int arrival_time = 0;
        int brust_time = 0;
        int ideal_time = 0;
        int start_of_exe = 0;

        int segma_arrival = 0;
        float number_of_pcb = node_number;
        float turn_around_time = 0;
        float avrage_turn_around_time = 0;
        float waiting_time = 0;
        float avrage_waiting_time = 0;

        Node current = my_queue.getHead();

        //======================================================//
        for (int i = 0; i < node_number; i++) {

            // fixed area //
            brust_time = current.getPcb().getBurstTime();
            arrival_time = current.getPcb().getArrivalTime();
            if (current.getPcb().getPriority() >= 0) {

                segma_arrival += arrival_time;
            } else {
                number_of_pcb--;
            }

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

                //================set ==========================//
                current.getPcb().setStartofExec(end_of_exe);// my start is the end of the  first
                if (arrival_time > end_of_exe) {
                    ideal_time += arrival_time - end_of_exe;
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
            run_time += brust_time;

            current = current.getNext();

        }

        // claclulat turn around time //
        System.out.println("end_of_exe " + end_of_exe);
        System.out.println("segma_arrival " + segma_arrival);
        System.out.println("ideal_time " + ideal_time);
        turn_around_time = end_of_exe - segma_arrival + ideal_time;

        // final area //
        //=================================//
        waiting_time += turn_around_time - run_time;
        //=================================//
        avrage_turn_around_time = turn_around_time / number_of_pcb;
        avrage_waiting_time = waiting_time / number_of_pcb;
        //================================//

        // system out area //
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
