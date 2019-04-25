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

    private Vector<Process> Process;
    private Vector<Segment> allocated_vector;
    private VOID free = new VOID();

    private int process_number = 0;
    private int segment_number = 0;
    private int free_segmants = 1;
    //=============================constructor====================================//

    public Memory(Segment input) {
        Process = new Vector<Process>();
        allocated_vector = new Vector<Segment>();
        free.add_free_segment(input);
        process_number = 0;
        free_segmants = 1;
    }

    public Memory(Vector<Process> input) {
        Process = new Vector<Process>();
        allocated_vector = new Vector<Segment>();
        process_number = input.size();
        Process = input;

        free_segmants = 1;
    }

    public Memory() {
        Process = new Vector<Process>();
        allocated_vector = new Vector<Segment>();
        process_number = 0;
        free_segmants = 0;
    }

    //==================set area ===============================//
    public void add_process(Process input) {
        Process.add(input);
        allocated_vector.addAll(input.get_segment_vector());
        segment_number += input.get_number_of_segments();
        process_number++;
    }

    public void add_process_vector(Vector<Process> input) {
        Process.addAll(input);
        for (int i = 0; i < input.size(); i++) {
            segment_number += input.get(i).get_number_of_segments();
            allocated_vector.addAll(input.get(i).get_segment_vector());
        }
        process_number = Process.size();
    }

    public void add_free_Segment(Segment input) {
        free.add_free_segment(input);
        free_segmants = free.get_number_of_free_segments();
    }

    public void add_free_process(Process input) {
        free.add_free_segment_vector(input.get_segment_vector());
        free_segmants = free.get_number_of_free_segments();
        Process.removeElement(input);
        process_number--;
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
        Collections.sort(input, (a, b) -> a.get_base() < b.get_base() ? -1 : a.get_base() == b.get_base() ? 0 : 1);

    }

    public void swap_base_2_segments(Segment first, Segment second) {
        first.set_base(second.get_base());
        second.set_base(first.get_base() + first.get_limit());

    }

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

        sort_segment_vector(allocated_vector);
        for (int i = 0; i < free.get_number_of_free_segments(); i++) {
            // for every single free location

            for (int j = allocated_vector.size() - 1; j >= 0; j--) {
                // check is there any location got base biger than mine 
                if (free.get_segemnt_i(i).get_base() > allocated_vector.get(j).get_base()) {
                    swap_base_2_segments(free.get_segemnt_i(i), allocated_vector.get(j));
                }
            }

        }
        free.resort();

    }
}
