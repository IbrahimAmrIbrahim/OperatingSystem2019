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
public class VOID {
//   Collections.sort(xx,(a, b) -> a.get_base() < b.get_base() ? -1 : a.get_base() == b.get_base() ? 0 : 1);

    private Color color = Color.BLACK;
    private int number_of_free_segments = 0;
    private Vector<Segment> void_vector;
    //====================constructor============================//

    public VOID()// just the id
    {
        void_vector = new Vector<Segment>();
        number_of_free_segments = void_vector.size();

    }

    public VOID(Segment input)// single segment
    {
        void_vector = new Vector<Segment>();
        void_vector.add(input);
        number_of_free_segments = void_vector.size();

    }

    public VOID(Vector<Segment> input)// vector
    {
        void_vector = new Vector<Segment>();
        void_vector = input;
        number_of_free_segments = void_vector.size();

    }

    //===============set section =============================//
    public void add_free_segment(Segment input) {
        void_vector.add(input);
        //====================safe section=================================================================================//
        Collections.sort(void_vector, (a, b) -> a.get_base() < b.get_base() ? -1 : a.get_base() == b.get_base() ? 0 : 1);
        for (int i = 0; i < void_vector.size() - 1; i++) {
            if (void_vector.get(i).get_base() + void_vector.get(i).get_limit() == void_vector.get(i + 1).get_base()) {
                void_vector.get(i).set_limit(void_vector.get(i).get_limit() + void_vector.get(i + 1).get_limit());
                void_vector.remove(i + 1);
                i--;
            }
        }
        //=================================================================================================================//
        number_of_free_segments = number_of_free_segments++;

    }

    public void add_free_segment_vector(Vector<Segment> input) {
        void_vector.addAll(input);
        //====================safe section=================================================================================//
        Collections.sort(void_vector, (a, b) -> a.get_base() < b.get_base() ? -1 : a.get_base() == b.get_base() ? 0 : 1);
        for (int i = 0; i < void_vector.size() - 1; i++) {
            if (void_vector.get(i).get_base() + void_vector.get(i).get_limit() == void_vector.get(i + 1).get_base()) {
                void_vector.get(i).set_limit(void_vector.get(i).get_limit() + void_vector.get(i + 1).get_limit());
                void_vector.remove(i + 1);
                i--;
            }
        }
        //=================================================================================================================//
        number_of_free_segments = void_vector.size();
    }

    //==============get section==============================//
    public int get_number_of_free_segments() {

        return number_of_free_segments;
    }

    public int get_total_size() {
        int size = 0;
        for (int i = 0; i < void_vector.size(); i++) {
            size += void_vector.get(i).get_limit();
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
        number_of_free_segments--;
    }

    public void clear_free_segment_vector() {
        void_vector.clear();
        number_of_free_segments = 0;
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
    public void resort() {

        //====================safe section=================================================================================//
        Collections.sort(void_vector, (a, b) -> a.get_base() < b.get_base() ? -1 : a.get_base() == b.get_base() ? 0 : 1);
        for (int i = 0; i < void_vector.size() - 1; i++) {
            if (void_vector.get(i).get_base() + void_vector.get(i).get_limit() == void_vector.get(i + 1).get_base()) {
                void_vector.get(i).set_limit(void_vector.get(i).get_limit() + void_vector.get(i + 1).get_limit());
                void_vector.remove(i + 1);
                i--;
            }
        }
    }

}
