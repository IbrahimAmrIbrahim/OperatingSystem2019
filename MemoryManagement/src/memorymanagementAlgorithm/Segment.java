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
public class Segment {

    private static int SEGMENT_ID = 0;
    private int ID;
    private int base;
    private int limit;
    private boolean inserted = false;
// ==============constructors ==========================//

    public Segment(int base_, int limit_) {

        ID = SEGMENT_ID;
        SEGMENT_ID++;
        base = base_;
        limit = limit_;
    }

    public Segment(int limit_) {

        ID = SEGMENT_ID;
        SEGMENT_ID++;
        limit = limit_;
    }

    public Segment() {
        limit=0;
        ID = SEGMENT_ID;
        SEGMENT_ID++;
    }
// ==============set section  ==========================//

    public void set_base(int base_) {
        base = base_;
    }

    public void set_inserted(boolean inserted_) {
        inserted = inserted_;
    }

    public void set_limit(int limit_) {
        limit = limit_;
    }
// ==============get section  ==========================//

    public int get_base() {
        return base;
    }

    public int get_limit() {
        return limit;
    }

    public int get_ID() {
        return ID;
    }

    public boolean get_inserted() {
        return inserted;
    }
// ==============copy section ==========================//

    public void copy_segment_without_id(Segment second) {
        base = second.get_base();
        limit = second.get_limit();
        inserted = second.get_inserted();
    }

    public void copy_segment_with_id(Segment second) {
        base = second.get_base();
        limit = second.get_limit();
        ID = second.get_ID();
        inserted = second.get_inserted();
    }
//===============print   ===========================//

    public void print() {
        System.out.print("[ID:" + ID + ",Base:" + base + ",Limit:" + limit + "]" + "\n");

    }

    public void print_free() {
        System.out.print("[" + "Base:" + base + ",Limit:" + limit + "]" + "\n");

    }

}
