package memorymanagementAlgorithm;

import java.util.*;

public class Worst_fit {

    private Memory my_memory;

    //==========constructor==============//
    public Worst_fit(long Size_) {
        my_memory = new Memory(Size_);
    }

    //=======================methods ====================//
    public boolean allocate_process(Process input) {
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
                        my_memory.get_free_Blank().sort_on_limit_large_at_top();
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
                return true;
            } // for loop to change the free 
            else {
                input.set_all_uninserted();
                // can't be inserted 
                // error 
                // still in wait
                // get the last blank again
                my_memory.set_free_Blank(clone);
                return false;
            }
        } else {
            return false;
            // error msg the process size is bigger than the memory itself
        }

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

    }

    public void insert_holes(Blank input) {
        // change it with memory.setfree
        my_memory.set_free_Blank(input);
        // free set 
        my_memory.get_free_Blank().sort_on_base();
        //sorted on base
        my_memory.adding_old_process();

        my_memory.get_free_Blank().sort_on_limit_large_at_top();
        // now the memory have the old process and free location
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
        my_memory.get_free_Blank().sort_on_limit_large_at_top();
        for (int i = 0; i < my_memory.get_waiting_vector().size(); i++) {

            int size = my_memory.get_waiting_vector().size();
            allocate_process(my_memory.get_waiting_vector().get(0));

            my_memory.get_waiting_vector().removeElement(my_memory.get_waiting_vector().get(my_memory.get_waiting_vector().size() - 1));

            if (size > my_memory.get_waiting_vector().size()) {
                i--;
            }
        }
    }

    public void clear_waiting_process() {
        my_memory.clear_waiting_process();
    }

    public void remove_waiting_process(Process input) {
        my_memory.get_waiting_vector().removeElement(input);
    }

    public void remove_all_runing() {
        for (int i = 0; i < my_memory.getRuning_Process().size(); i++) {
            my_memory.deallocate_process(my_memory.getRuning_Process().get(0));
            i = 0;
        }

        for (int i = 0; i < my_memory.get_waiting_vector().size(); i++) {

            int size = my_memory.get_waiting_vector().size();
            allocate_process(my_memory.get_waiting_vector().get(0));

            my_memory.get_waiting_vector().removeElement(my_memory.get_waiting_vector().get(my_memory.get_waiting_vector().size() - 1));

            if (size > my_memory.get_waiting_vector().size()) {
                i--;
            }
        }
    }

    public long get_total_used_size() {
        return my_memory.get_utlization();
    }
}
