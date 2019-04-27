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
    }

    public void deallocate_process(Process input) {

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

}
