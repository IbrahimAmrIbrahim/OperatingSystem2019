package memorymanagementAlgorithm;

import java.util.*;
import javafx.scene.paint.Color;

public class Process {

    private static long PROCESS_ID = 0;
    private Color color;
    private long ID;
    private boolean inserted;
    private Vector<Segment> Segment_vector;

    //====================constructor============================//
    public Process() {
        ID = PROCESS_ID;
        PROCESS_ID++;
        Segment_vector = new Vector<Segment>();
        inserted = false;
        color = randomColor();
    }

    public Process(Segment input) {
        ID = PROCESS_ID;
        PROCESS_ID++;
        Segment_vector = new Vector<Segment>();
        Segment_vector.add(input);
        inserted = false;
        color = randomColor();
    }

    // use this in the gui
    public Process(Vector<Segment> input) {
        ID = PROCESS_ID;
        PROCESS_ID++;
        Segment_vector = input;
        inserted = false;
        color = randomColor();
    }

    //===============set section =============================//
    /**
     * @param aPROCESS_ID the PROCESS_ID to set
     */
    public static void setPROCESS_ID(long aPROCESS_ID) {
        PROCESS_ID = aPROCESS_ID;
    }

    // to add in the gui table 
    public void add_Segment(Segment input) {
        getSegment_vector().add(input);
    }

    // to add more than 1 segment at a time
    public void add_Segment_vector(Vector<Segment> input) {
        getSegment_vector().addAll(input);
    }

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

    public void set_all_uninserted() {
        for (int i = 0; i < getSegment_vector().size(); i++) {
            getSegment_vector().get(i).setInserted(false);
        }

    }

    //==============get section==============================//
    // return the number of segments
    public long get_number_of_segments() {
        return Segment_vector.size();
    }

    //return id
    public long getID() {
        return ID;
    }

    // return the segment number i this will make issue as the segment index changed after sorting
    //beter use name
    public Segment get_segemnt_i(int i) {
        return getSegment_vector().get(i);
    }

    public long get_total_size() {
        int size = 0;
        for (int i = 0; i < getSegment_vector().size(); i++) {
            size += getSegment_vector().get(i).getLimit();
        }
        return size;
    }

    public boolean check_all_inserted() {
        for (int i = 0; i < getSegment_vector().size(); i++) {
            if (getSegment_vector().get(i).isInserted() == false) {
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
     * @return the Segment_vector
     */
    public Vector<Segment> getSegment_vector() {
        return Segment_vector;
    }

    /**
     * @return the inserted
     */
    public boolean isInserted() {
        return inserted;
    }

    //==============copy section==============================//
    public void copy_with_id(Process input) {
        ID = input.getID();
        for (int i = 0; i > input.get_number_of_segments(); i++) {
            getSegment_vector().add(input.get_segemnt_i(i));
        }
        color = input.getColor();
    }

    public void copy_with_out_id(Process input) {
        for (int i = 0; i > input.get_number_of_segments(); i++) {
            getSegment_vector().add(input.get_segemnt_i(i));
        }
        color = input.getColor();
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

    public void print(int i) {
        getSegment_vector().get(i).print();
    }

    //============color====================================//
    private Color randomColor() {
        Color newColor;
        do {
            newColor = Color.hsb(randomNo(0, 360), randomNo(0.6, 1), randomNo(0.6, 1));
        } while ((newColor == color.RED) || (newColor == color.BLUE));
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
