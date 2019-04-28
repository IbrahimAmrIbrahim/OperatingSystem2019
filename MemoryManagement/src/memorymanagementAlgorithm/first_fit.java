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
public class first_fit {

    private Memory my_memory;

    //==========constructor==============//
    public first_fit(long Size_) {
        my_memory = new Memory(Size_);
    }

    //=======================methods ====================//
    public void test() {
        my_memory = new Memory(10000);
        Blank b = new Blank();
        b.add_free_segment(new Segment(100, 100, "Free", false));

        b.add_free_segment(new Segment(400, 100, "Free", false));
        b.add_free_segment(new Segment(600, 100, "Free", false));
        b.add_free_segment(new Segment(800, 100, "Free", false));
        b.add_free_segment(new Segment(1000, 100, "Free", false));

        b.add_free_segment(new Segment(1200, 400, "Free", false));

        b.add_free_segment(new Segment(1800, 50, "Free", false));
        b.add_free_segment(new Segment(2000, 50, "Free", false));
        insert_holes(b);
        Process p0 = new Process();
        p0.add_Segment(new Segment(100, "S11", true));
        p0.add_Segment(new Segment(100, "S21", true));
        p0.add_Segment(new Segment(100, "S31", true));
        p0.add_Segment(new Segment(100, "S41", true));
        p0.add_Segment(new Segment(100, "S51", true));
        Process p1 = new Process();
        p1.add_Segment(new Segment(100, "S12", true));
        p1.add_Segment(new Segment(100, "S22", true));
        p1.add_Segment(new Segment(100, "S32", true));
        p1.add_Segment(new Segment(100, "S42", true));
        p1.add_Segment(new Segment(100, "S52", true));
        Process p2 = new Process();
        p2.add_Segment(new Segment(100, "S13", true));
        p2.add_Segment(new Segment(100, "S23", true));
        p2.add_Segment(new Segment(100, "S33", true));
        p2.add_Segment(new Segment(100, "S43", true));
        p2.add_Segment(new Segment(100, "S53", true));
        Process p3 = new Process();
        p3.add_Segment(new Segment(100, "S14", true));
        p3.add_Segment(new Segment(100, "S24", true));
        p3.add_Segment(new Segment(100, "S34", true));
        p3.add_Segment(new Segment(100, "S44", true));
        p3.add_Segment(new Segment(100, "S54", true));

        allocate_process(p0);
        allocate_process(p1);
        allocate_process(p2);
        allocate_process(p3);
        deallocate_process(p0);
    }

    public void allocate_process(Process input) {
        //add the process on waiting queue
        my_memory.add_waiting_process(input);

        //====================================================================//
        //====================data area==============//
        Vector<Long> base_values = new Vector<Long>();
        Blank clone = new Blank();
        clone.copy(my_memory.get_free_Blank());

        //===================check condtion and insert area ============================//
        if (my_memory.get_free_Blank().get_total_size() >= input.get_total_size()) {
            for (int i = 0; i < input.get_number_of_segments(); i++) {
                // for loop on the number of segement of the process
                for (int j = 0; j < my_memory.get_free_Blank().get_number_of_free_segments(); j++) {
                    // for loop on the number of free locations
                    if (input.get_segemnt_i(i).getLimit() <= my_memory.get_free_Blank().get_segemnt_i(j).getLimit()) {
                        // i can insert this Segment of process x

                        //====================data area =====================================//
                        long new_base = my_memory.get_free_Blank().get_segemnt_i(j).getBase();//process new base = hole base
                        long free_new_base = new_base + input.get_segemnt_i(i).getLimit();//free new base= hole base + segment limit
                        long free_new_limit = my_memory.get_free_Blank().get_segemnt_i(j).getLimit() - input.get_segemnt_i(i).getLimit();
                        // free new limit= hole limit- segment limit 

                        //===================set blank and process base vector=====================================//
                        my_memory.get_free_Blank().get_segemnt_i(j).setBase(free_new_base);
                        my_memory.get_free_Blank().get_segemnt_i(j).setLimit(free_new_limit);

                        // check condtion if i am gonna remove all free area or no 
                        if (my_memory.get_free_Blank().get_segemnt_i(j).getLimit() == 0) {
                            my_memory.get_free_Blank().remove_free_segment_i(j);
                        }

                        base_values.add(new_base);
                        input.get_segemnt_i(i).setInserted(true);// this segment is inserted 

                        //=========================================================//
                        break;// see another segment
                    }//if 

                }//for j

            }// for i

            //===========check process inserted or no area==================//
            if (input.check_all_inserted()) {

                // modfy the input process Segments
                for (int i = 0; i < base_values.size(); i++) {
                    input.get_segemnt_i(i).setBase(base_values.get(i));
                }
                // add the input to running and remove it from waiting
                my_memory.add_runing_process(input);
            } // for loop to change the free 
            else {
                input.set_all_uninserted();
                // can't be inserted 
                // error 
                // still in wait
                // get the last blank agaain
                my_memory.set_free_Blank(clone);

            }
        } else {
            // error msg the process size is bigger than the memory itself
        }
        my_memory.print();
        System.out.println("----------------------------------");

    }

    public void deallocate_process(Process input) {

        my_memory.deallocate_process(input);
        for (int i = 0; i < my_memory.get_waiting_vector().size(); i++) {

            int size = my_memory.get_waiting_vector().size();
            allocate_process(my_memory.get_waiting_vector().get(0));

            my_memory.get_waiting_vector().removeElement(my_memory.get_waiting_vector().get(my_memory.get_waiting_vector().size() - 1));

            if (size > my_memory.get_waiting_vector().size()) {
                i--;
            }
        }
        System.out.println("=====================");
        my_memory.print();
    }

    public void insert_holes(Blank input) {
        // change it with memory.setfree
        my_memory.set_free_Blank(input);
        // free set 
        my_memory.get_free_Blank().sort_on_base();
        //sorted on base
        my_memory.adding_old_process();

        // now the memory have the old process and free location
    }

    public void get_total_free() {

    }
    //==================================//

    public Vector<Process> getAllocatedProcessVector() {
        return my_memory.getRuning_Process();
    }

    public Vector<Process> getWaitingProcessVector() {
        return my_memory.getWaiting_Process();
    }

    public Blank getFreeSpace() {
        return my_memory.getFree();
    }

    public void memoryCompaction() {
        my_memory.compaction_memory();
    }
}
