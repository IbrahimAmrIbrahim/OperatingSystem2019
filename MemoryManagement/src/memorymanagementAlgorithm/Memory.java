package memorymanagementAlgorithm;

import java.util.*;

public class Memory {

    private Vector<Process> waitting_processes;
    private Vector<Segment> allocated_processes;
    private Void free;

    //=============================constructor====================================//
    public Memory() {
        waitting_processes = new Vector<Process>();
        allocated_processes = new Vector<Segment>();
        free = new Void();
    }

    //==================set area ===============================//
    public void add_process(Process input) {
        waitting_processes.add(input);
        allocated_processes.addAll(input.getSegment_vector());
    }

    public void add_process_vector(Vector<Process> input) {
        waitting_processes.addAll(input);
        for (int i = 0; i < input.size(); i++) {
            allocated_processes.addAll(input.get(i).getSegment_vector());
        }
    }

    public void add_free_Segment(Segment input) {
        free.add_free_segment(input);
    }

    public void add_free_process(Process input) {
        free.add_free_segment_vector(input.getSegment_vector());
        waitting_processes.removeElement(input);
    }

    //===========print =================//
    public void print() {
        System.out.println("process allocated ");
        for (int i = 0; i < waitting_processes.size(); i++) {
            waitting_processes.get(i).print();
        }
        System.out.println("free locations ");
        free.print();
    }

    //===========get section=================================//
    public int get_total_free() {
        return free.get_total_size();
    }

    //====================== method sections ====================//
    public void sort_segment_vector(Vector<Segment> input) {
        Collections.sort(input, (a, b) -> a.getBase() < b.getBase() ? -1 : a.getBase() == b.getBase() ? 0 : 1);
    }

    public void swap_base_2_segments(Segment first, Segment second) {
        first.setBase(second.getBase());
        second.setBase(first.getBase() + first.getLimit());

    }

    public void re_arrange_the_memory()// this will collect the free on 1 big segment take care it will take a period of time
    {
        // how to trade ?!! 
        /*
        for example 
        p1 from 0 to 10
        and p2 from 30 to 40
        all i need to do is every waitting_processes base is biger than the free trade it with the free 
        by changing the base 
         */

        sort_segment_vector(allocated_processes);
        for (int i = 0; i < free.get_number_of_free_segments(); i++) {
            // for every single free location

            for (int j = allocated_processes.size() - 1; j >= 0; j--) {
                // check is there any location got base biger than mine 
                if (free.get_segemnt_i(i).getBase() > allocated_processes.get(j).getBase()) {
                    swap_base_2_segments(free.get_segemnt_i(i), allocated_processes.get(j));
                }
            }

        }
        free.resort();
    }
}
