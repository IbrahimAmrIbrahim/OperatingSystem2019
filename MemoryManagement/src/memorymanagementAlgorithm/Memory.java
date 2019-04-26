/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memorymanagementAlgorithm;

import java.util.*;
import javafx.scene.paint.Color;

/**
 *
 * @author ahmed
 */
public class Memory {

    private Vector<Process> Process;// all process
    private Vector<Segment> allocated_segment;// segment inserted on memory
    private Vector<Process> waiting_queue;// process on waiting 
    private Vector<Process> runing_process;// process on waiting 
    private Void free;

    //=============================constructor====================================//
    public Memory() {
        Process = new Vector<Process>();
        allocated_segment = new Vector<Segment>();
        waiting_queue = new Vector<Process>();
        runing_process = new Vector<Process>();
        free = new Void();
    }

    //================== set area ===============================//
    public void add_process(Process input) {
        Process.add(input);
        waiting_queue.add(input);
    }

    public void add_process_vector(Vector<Process> input) {
        Process.addAll(input);
        waiting_queue.addAll(input);
    }

    public void add_free_Segment(Segment input) {
        free.add_free_segment(input);
    }

    // use it in the algorthm only
    public void add_free_process(Process input) {
        free.add_free_segment_vector(input.getSegment_vector());
        Process.removeElement(input);
    }

    //===========print =================//
    public void print() {
        System.out.println("process allocated ");
        for (int i = 0; i < Process.size(); i++) {
            Process.get(i).print();
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

    private void swap_base_2_segments(Segment first, Segment second) {
        first.setBase(second.getBase());
        second.setBase(first.getBase() + first.getLimit());

    }

    // use it if total free can fit 
    public void re_arrange_the_memory()// this will collect the free on 1 big segment take care it will take a period of time
    {
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
}
