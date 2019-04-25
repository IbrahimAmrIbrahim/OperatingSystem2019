package memorymanagementAlgorithm;

import java.util.*;
import javafx.scene.paint.Color;

public class Process {

    private static int Process_ID = 0;
    private int ID;
    private Color color;
    private boolean inserted = false;
    private Vector<Segment> Segment_vector;

    //====================constructor============================//
    public Process() {
        ID = Process_ID;
        Process_ID++;
        Segment_vector = new Vector<Segment>();
        color = randomColor();
    }

    public Process(Segment input) {
        ID = Process_ID;
        Process_ID++;
        Segment_vector = new Vector<Segment>();
        Segment_vector.add(input);
        color = randomColor();
    }

    public Process(Vector<Segment> input) {
        ID = Process_ID;
        Process_ID++;
        Segment_vector = input;
        color = randomColor();
    }

    //===============set section =============================//
    public void add_Segment(Segment input) {
        Segment_vector.add(input);
        Collections.sort(Segment_vector, (a, b) -> a.getLimit() < b.getLimit() ? -1 : a.getLimit() == b.getLimit() ? 0 : 1);
    }

    public void add_Segment_vector(Vector<Segment> input) {
        Segment_vector.addAll(input);
        Collections.sort(Segment_vector, (a, b) -> a.getLimit() < b.getLimit() ? -1 : a.getLimit() == b.getLimit() ? 0 : 1);
    }

    //==============get section==============================//
    public int get_number_of_segments() {
        return Segment_vector.size();
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    public Segment get_segemnt_i(int i) {
        return Segment_vector.get(i);
    }

    public int get_total_size() {
        int size = 0;
        for (int i = 0; i < Segment_vector.size(); i++) {
            size += Segment_vector.get(i).getLimit();
        }
        return size;
    }

    public boolean check_all_inserted() {
        for (int i = 0; i < Segment_vector.size(); i++) {
            if (Segment_vector.get(i).isInserted() == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the inserted
     */
    public boolean isInserted() {
        return inserted;
    }

    /**
     * @return the Segment_vector
     */
    public Vector<Segment> getSegment_vector() {
        return Segment_vector;
    }

    //==============set section==============================//
    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @param inserted the inserted to set
     */
    public void setInserted(boolean inserted) {
        this.inserted = inserted;
    }

    /**
     * @param Segment_vector the Segment_vector to set
     */
    public void setSegment_vector(Vector<Segment> Segment_vector) {
        this.Segment_vector = Segment_vector;
    }

    //==============copy section==============================//
    public void copy(Process input) {
        ID = input.getID();
        color = input.getColor();
        for (int i = 0; i > input.get_number_of_segments(); i++) {
            Segment_vector.add(input.get_segemnt_i(i));
        }
    }

    //=============remove section============================//
    public void remove_Segment_i(int i) {
        getSegment_vector().remove(i);
    }

    public void clear_segment_vector() {
        getSegment_vector().clear();
    }

    //============print section=============================//
    public void print() {
        for (int i = 0; i < getSegment_vector().size(); i++) {
            getSegment_vector().get(i).print();
        }
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
