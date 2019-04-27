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

    void allocate_process(Process input) {
        //here start the real work
        my_memory.add_waiting_process(input);

        //=============================================//
        //check on all free 
        Vector<Long> base_values = new Vector<Long>();

        Vector<Integer> free_index = new Vector<Integer>();

        Vector<Long> free_new_base_limit = new Vector<Long>();
        if (my_memory.get_free_Blank().get_total_size() >= input.get_total_size()) {
            for (int i = 0; i < input.get_number_of_segments(); i++) {

                for (int j = 0; j < my_memory.get_free_Blank().get_number_of_free_segments(); j++) {
                    if (input.get_segemnt_i(i).getLimit() <= my_memory.get_free_Blank().get_segemnt_i(j).getLimit()) {
                        // INSERTED  //need to change the free

                        //=========================================================//
                        long new_base = my_memory.get_free_Blank().get_segemnt_i(j).getBase();//process new base = hole base
                        long free_new_base = new_base + input.get_segemnt_i(i).getLimit();//free new base= hole base + segment limit
                        long free_new_limit = my_memory.get_free_Blank().get_segemnt_i(j).getLimit() - input.get_segemnt_i(i).getLimit();
                        // free new limit= hole limit- segment limit 
                        //========================================================//

                        free_index.add(j);// i got the right index
                        base_values.add(new_base);
                        free_new_base_limit.add(free_new_base);
                        free_new_base_limit.add(free_new_limit);// see are you gonna remove holes with 0 limit or no
                        input.get_segemnt_i(i).setInserted(true);// this segment is inserted 

                        //=========================================================//
                        break;// see another segment
                    }//if 

                }//for j

            }// for i

            if (input.check_all_inserted()) {
                // all in 
                // for loop to change the base of the input
                for (int i = 0; i < input.get_number_of_segments(); i++) {

                    input.get_segemnt_i(i).setBase(base_values.get(i));
                }
                // add the input to running and remove it from waiting
                my_memory.add_runing_process(input);

                // fore loop to change the free 
                for (int i = 0; i < free_index.size(); i++) {
                    my_memory.get_free_Blank().get_segemnt_i(free_index.get(i)).setBase(free_new_base_limit.get(i));
                    my_memory.get_free_Blank().get_segemnt_i(free_index.get(i)).setLimit(free_new_base_limit.get(i + 1));
                    //==================================================================================================//
                    // if the free segment has limit of zero remove it
                    if (my_memory.get_free_Blank().get_segemnt_i(free_index.get(i)).getLimit() == 0) {
                        my_memory.get_free_Blank().remove_free_segment_i(free_index.get(i));
                        free_index.remove(i);// remove this from the index vector
                        i--;// re order the loop 
                    }

                }// for loop i
            } else {
                // error 
                // still in wait

            }
        } else {
            // error msg
        }
    }

    void deallocate_process(Process input) {

    }

    void insert_holes(Blank input) {
        // change it with memory.setfree
        my_memory.set_free_Blank(input);
        // free set 
        my_memory.get_free_Blank().sort_on_base();
        //sorted on base
        my_memory.adding_old_process();

        // now the memory have the old process and free location
    }

    void get_total_free() {

    }
    //==================================//

}
