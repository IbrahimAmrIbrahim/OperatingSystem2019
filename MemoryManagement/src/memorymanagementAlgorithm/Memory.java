/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memorymanagementAlgorithm;

import java.util.*;

/**
 *
 * @author ahmed
 */
public class Memory {

    private Vector<Process> Process;// all process
    private Vector<Segment> allocated_segment;// segment inserted on memory
    private Vector<Process> waiting_Process;// process on waiting 
    private Vector<Process> runing_Process;// process on waiting 
    private Blank free;
    private long size;

    //=============================constructor====================================//
    public Memory(long size_) {
        size = size_;
        Process = new Vector<Process>();
        allocated_segment = new Vector<Segment>();
        waiting_Process = new Vector<Process>();
        runing_Process = new Vector<Process>();
        free = new Blank();
    }

    //================== set area ===============================//
    public void add_waiting_process(Process input) {
        waiting_Process.add(input);
    }

    public void add_waiting_process_vector(Vector<Process> input) {
        waiting_Process.addAll(input);
    }

    // remove it, its gonna destroy us 
    public void add_runing_process(Process input) {
        waiting_Process.removeElement(input);
        runing_Process.add(input);
        allocated_segment.addAll(input.getSegment_vector());
    }

    public void set_free_Blank(Blank input) {
        free = input;
    }

    // use it in the algorthm only
    public void deallocate_process(Process input) {
        free.add_free_segment_vector(input.getSegment_vector());
        runing_Process.removeElement(input);
        allocated_segment.removeAll(input.getSegment_vector());
    }

    //===========print =================//
    public void print() {
        System.out.println("process allocated ");
        for (int i = 0; i < runing_Process.size(); i++) {
            runing_Process.get(i).print();
        }
        System.out.println("free locations ");
        free.print();
    }

    //===========get section=================================//
    public int get_total_free() {
        return free.get_total_size();
    }

    public Vector<Process> get_waiting_vector() {
        return waiting_Process;
    }

    public Blank get_free_Blank() {
        return free;
    }

    public long get_utlization() {
        long number = 0;
        for (int i = 0; i < allocated_segment.size(); i++) {
            number += allocated_segment.get(i).getLimit();
        }

        return number;
    }

    //====================== method sections ====================//
    public void adding_old_process() {// the free is sorted on base

        if (free.get_segemnt_i(0).getBase() > 0) {
            long old_base2 = 0;
            long old_limit2 = free.get_segemnt_i(0).getBase() - old_base2;
            Process old_process2 = new Process(new Segment(old_base2, old_limit2, "old process", true));
            runing_Process.add(old_process2);
            allocated_segment.add(old_process2.get_segemnt_i(0));
        }

        //============between 2 free ========================//
        for (int i = 0; i < free.get_number_of_free_segments() - 1; i++) {
            long old_base = free.get_segemnt_i(i).getBase() + free.get_segemnt_i(i).getLimit();
            long old_limit = free.get_segemnt_i(i + 1).getBase() - old_base;
            Process old_process = new Process(new Segment(old_base, old_limit, "old process", true));
            runing_Process.add(old_process);
            allocated_segment.add(old_process.get_segemnt_i(0));
        }
        //==================================================//
        //===============add old process at the end ==========//
        long last_free_address = 0;
        // i have 1 segment only
        if (free.get_number_of_free_segments() > 0) {
            last_free_address = (free.get_segemnt_i(free.get_number_of_free_segments() - 1).getBase() + free.get_segemnt_i(free.get_number_of_free_segments() - 1).getLimit());
            // if the free not at the first location

        }
        //for 1 segment
        if (size != last_free_address && free.get_number_of_free_segments() > 0) {
            long old_base = free.get_segemnt_i(free.get_number_of_free_segments() - 1).getBase() + free.get_segemnt_i(free.get_number_of_free_segments() - 1).getLimit();
            long old_limit = size - old_base;
            Process old_process = new Process(new Segment(old_base, old_limit, "old process", true));
            runing_Process.add(old_process);
            allocated_segment.add(old_process.get_segemnt_i(0));

        }
        // for no segment 
        if (free.get_number_of_free_segments() == 0) {
            long old_base = 0;
            long old_limit = size - old_base;
            Process old_process = new Process(new Segment(old_base, old_limit, "old process", true));
            runing_Process.add(old_process);
            allocated_segment.add(old_process.get_segemnt_i(0));
        }
        //================================================================//

    }

    private void sort_segment_vector(Vector<Segment> input) {
        Collections.sort(input, (a, b) -> a.getBase() < b.getBase() ? -1 : a.getBase() == b.getBase() ? 0 : 1);

    }

    private void swap_base_2_segments(Segment first, Segment second) {
        first.setBase(second.getBase());
        second.setBase(first.getBase() + first.getLimit());

    }

    // use it if total free can fit 
    // this will collect the free on 1 big segment take care it will take a period of time
    public void compaction_memory() {
        // how to trade ?!! 
        /*
        for example 
        p1 from 0 to 10
        and p2 from 30 to 40
        all i need to do is every process base is biger than the free trade it with the free 
        by changing the base 
         */

        sort_segment_vector(allocated_segment);
        for (int i = 0; i < free.get_number_of_free_segments(); i++) {
            // for every single free location

            for (int j = allocated_segment.size() - 1; j >= 0; j--) {
                // check is there any location got base biger than mine 
                if (free.get_segemnt_i(i).getBase() > allocated_segment.get(j).getBase()) {
                    swap_base_2_segments(free.get_segemnt_i(i), allocated_segment.get(j));
                }
            }

        }
        free.resort();

    }

    /**
     * @return the waiting_Process
     */
    public Vector<Process> getWaiting_Process() {
        return waiting_Process;
    }

    /**
     * @return the runing_Process
     */
    public Vector<Process> getRuning_Process() {
        return runing_Process;
    }

    public void clear_waiting_process() {
        waiting_Process.clear();
    }

    /**
     * @return the free
     */
    public Blank getFree() {
        return free;
    }
}
