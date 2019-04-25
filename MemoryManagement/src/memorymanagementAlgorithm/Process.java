package memorymanagementAlgorithm;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ahmed
 */

import java.util.*;
import javafx.scene.paint.Color;
//Vector<Double> list = new Vector<Double>();

public class Process {

    /**
     * @param Segment_vector the Segment_vector to set
     */
    public void setSegment_vector(Vector<Segment> Segment_vector) {
        this.Segment_vector = Segment_vector;
    }

    /**
     * @return the Segment_vector
     */
    public Vector<Segment> getSegment_vector() {
        return Segment_vector;
    }

    private Color color;
    private static int Process_ID = 0;
    private int ID;
    private boolean inserted = false;
    private int number_of_segments;
    private Vector<Segment> Segment_vector;
    //====================constructor============================//

    public Process()// just the id
    {
        ID = Process_ID;
        Process_ID++;
        Segment_vector = new Vector<Segment>();
        number_of_segments = Segment_vector.size();
        color = randomColor();
    }

    public Process(Segment input)// single segment
    {
        ID = Process_ID;
        Process_ID++;
        Segment_vector = new Vector<Segment>();
        Segment_vector.add(input);
        number_of_segments = Segment_vector.size();
        color = randomColor();
    }

    public Process(Vector<Segment> input)// vector
    {
        ID = Process_ID;
        Process_ID++;
        Segment_vector = new Vector<Segment>();
        Segment_vector=input;
        number_of_segments = Segment_vector.size();
        color = randomColor();
    }

    //===============set section =============================//
    public void add_Segment(Segment input) {
        getSegment_vector().add(input);
        Collections.sort(getSegment_vector(), (a, b) -> a.get_limit() < b.get_limit() ? -1 : a.get_limit() == b.get_limit() ? 0 : 1);
        number_of_segments = number_of_segments++;

    }

    public void add_Segment_vector(Vector<Segment> input) {
        getSegment_vector().addAll(input);
        Collections.sort(getSegment_vector(), (a, b) -> a.get_limit() < b.get_limit() ? -1 : a.get_limit() == b.get_limit() ? 0 : 1);
        number_of_segments = getSegment_vector().size();
    }

    public void set_inserted(boolean condtion) {
        inserted = condtion;
    }

    //==============get section==============================//
    public int get_number_of_segments() {

        return number_of_segments;
    }

    public int get_id() {

        return ID;
    }

    public Segment get_segemnt_i(int i) {
        return getSegment_vector().get(i);
    }

    public int get_total_size() {
        int size = 0;
        for (int i = 0; i < getSegment_vector().size(); i++) {
            size += getSegment_vector().get(i).get_limit();
        }
        return size;
    }

    public boolean check_all_inserted() {

        for (int i = 0; i < getSegment_vector().size(); i++) {
            if (getSegment_vector().get(i).get_inserted() == false) {
                return false;
            }
        }
        return true;
    }

    public Color getColor() {
        return color;
    }

    public Vector<Segment> get_segment_vector() {
        return getSegment_vector();
    }

    public boolean get_inserted() {

        return inserted;
    }

    //==============copy section==============================//
    public void copy_with_id(Process input) {
        ID = input.get_id();
        for (int i = 0; i > input.get_number_of_segments(); i++) {
            getSegment_vector().add(input.get_segemnt_i(i));
        }
        number_of_segments = input.get_number_of_segments();
        color = input.getColor();
    }

    public void copy_with_out_id(Process input) {
        for (int i = 0; i > input.get_number_of_segments(); i++) {
            getSegment_vector().add(input.get_segemnt_i(i));
        }
        number_of_segments = input.get_number_of_segments();
        color = input.getColor();
    }

    //=============remove section============================//
    public void remove_Segment_i(int i) {
        getSegment_vector().remove(i);
        number_of_segments--;
    }

    public void clear_segment_vector() {
        getSegment_vector().clear();
        number_of_segments = 0;
    }

    //============print section=============================//
    public void print() {
        for (int i = 0; i < getSegment_vector().size(); i++) {
            getSegment_vector().get(i).print();
        }
    }

    public void print(int i) {

        getSegment_vector().get(i).print();

    }

    //============color====================================//
    private Color randomColor() {
        Color newColor;
        newColor = Color.hsb(randomNo(0, 360), randomNo(0.6, 1), randomNo(0.6, 1));
        return newColor;
    }

    private double randomNo(double min, double max) {
        double rand = ((Math.random() * ((max - min) + 1)) + min);
        if (rand > max) {
            rand = max;
        }
        return rand;
    }

}
