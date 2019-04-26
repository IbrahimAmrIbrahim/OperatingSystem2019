/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memorymanagementAlgorithm;

import java.util.*;
import javafx.scene.paint.Color;


/*


how it gonna work
we have intialy void (free locations)= base = 0 limit 1000
now when we insert a process we should remove this segment and add for example base =100 limit 1000-100=900 
and if we have for example 
from 0 to 1000 busy
and now we void has nothing = clear
if process will leave
for example process 1 gonna leave 
and it's from  500 to 700
we will add this process to void 
but it will make a 2 segments 500 to 600 from 600 to 700
and this is bad we need to make it only 1 segment
require auto correct size 

but if it's from 500 to 600 and from 900 to 1000
we will add the segments easy as 2 free segments


 */
/**
 *
 * @author ahmed
 */
public class Void {
//   Collections.sort(xx,(a, b) -> a.getBase() < b.getBase() ? -1 : a.getBase() == b.getBase() ? 0 : 1);

    private Color color = Color.BLACK;

    private Vector<Segment> void_vector;
    private char condtion = 'b';// b = sort on base,  h=sort on larger at top , 
    //====================constructor============================//

    public Void() {
        void_vector = new Vector<Segment>();
    }

    public Void(Segment input) {
        void_vector = new Vector<Segment>();
        input.setName("free");
        void_vector.add(input);
    }

    public Void(Vector<Segment> input) {

        for (int i = 0; i < input.size(); i++) {
            input.get(i).setName("free");
        }
        void_vector = input;
        //sort on base default
        sort_on_base();

    }

    //===============set section =============================//
    public void add_free_segment(Segment input) {
        input.setName("free");
        void_vector.add(input);
        // sort on base
        //====================safe section=================================================================================//

        sort_on_base();
        collect_free();
        if (condtion == 'b') {
            ;
        } else if (condtion == 'l') {
            sort_on_limit_large_at_top();
        } else if (condtion == 's') {
            sort_on_limit_small_at_top();
        }

    }

    public void add_free_segment_vector(Vector<Segment> input) {
        for (int i = 0; i < input.size(); i++) {
            input.get(i).setName("free");
        }
        void_vector.addAll(input);
        //====================safe section=================================================================================//
        sort_on_base();
        collect_free();
        if (condtion == 'b') {
            ;
        } else if (condtion == 'l') {
            sort_on_limit_large_at_top();
        } else if (condtion == 's') {
            sort_on_limit_small_at_top();
        }

    }

    //==============get section==============================//
    public int get_number_of_free_segments() {

        return void_vector.size();
    }

    public long get_total_size() {
        long size = 0;
        for (int i = 0; i < void_vector.size(); i++) {
            size += void_vector.get(i).getLimit();
        }
        return size;
    }

    public Segment get_segemnt_i(int i) {
        return void_vector.get(i);
    }

    public Vector<Segment> get_segment_vector() {
        return void_vector;
    }

    public Color getColor() {
        return color;
    }

    //=============remove section============================//
    public void remove_free_segment_i(int i) {
        void_vector.remove(i);

    }

    public void clear_free_segment_vector() {
        void_vector.clear();

    }

    //============print section=============================//
    public void print() {
        for (int i = 0; i < void_vector.size(); i++) {
            void_vector.get(i).print_free();
        }
    }

    public void print(int i) {

        void_vector.get(i).print_free();

    }

    //===============General methods========================//
    //resort the void collect free together
    public void resort() {

        //====================safe section=================================================================================//
        // must sort on base
        Collections.sort(void_vector, (a, b) -> a.getBase() < b.getBase() ? -1 : a.getBase() == b.getBase() ? 0 : 1);
        for (int i = 0; i < void_vector.size() - 1; i++) {
            if (void_vector.get(i).getBase() + void_vector.get(i).getLimit() == void_vector.get(i + 1).getBase()) {
                void_vector.get(i).setLimit(void_vector.get(i).getLimit() + void_vector.get(i + 1).getLimit());
                void_vector.remove(i + 1);
                i--;
            }
        }
    }

    public void sort_on_base() {
        condtion = 'b';
        Collections.sort(void_vector, (a, b) -> a.getBase() < b.getBase() ? -1 : a.getBase() == b.getBase() ? 0 : 1);
    }

    public void sort_on_limit_small_at_top() {
        condtion = 's';
        Collections.sort(void_vector, (a, b) -> a.getLimit() < b.getLimit() ? -1 : a.getLimit() == b.getLimit() ? 0 : 1);
    }

    public void sort_on_limit_large_at_top() {
        condtion = 'l';
        Collections.sort(void_vector, (a, b) -> a.getLimit() > b.getLimit() ? -1 : a.getLimit() == b.getLimit() ? 0 : 1);
    }

    private void collect_free() {
        for (int i = 0; i < void_vector.size() - 1; i++) {
            if (void_vector.get(i).getBase() + void_vector.get(i).getLimit() == void_vector.get(i + 1).getBase()) {
                void_vector.get(i).setLimit(void_vector.get(i).getLimit() + void_vector.get(i + 1).getLimit());
                void_vector.remove(i + 1);
                i--;
            }
        }
    }
}
