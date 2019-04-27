package memorymanagementAlgorithm;

public class Segment {

    private static long SEGMENT_ID = 0;
    private long ID;
    private String name;
    private long base;
    private long limit;
    private boolean inserted;

// ==============constructors ==========================//
    public Segment(long base_, long limit_, String name_, boolean isPartofProcess) {
        if (isPartofProcess) {
            ID = SEGMENT_ID;
            SEGMENT_ID++;
        } else {
            ID = -1;
        }
        name = name_;
        base = base_;
        limit = limit_;
        inserted = false;
    }

    //use this in gui input for process
    public Segment(long limit_, String name_, boolean isPartofProcess) {
        if (isPartofProcess) {
            ID = SEGMENT_ID;
            SEGMENT_ID++;
        } else {
            ID = -1;
        }
        name = name_;
        base = -1;
        limit = limit_;
        inserted = false;
    }

    public Segment(boolean isPartofProcess) {
        if (isPartofProcess) {
            ID = SEGMENT_ID;
            SEGMENT_ID++;
        } else {
            ID = -1;
        }
        name = "";
        base = -1;
        limit = -1;
        inserted = false;
    }

// ==============set section  ==========================//
    /**
     * @param aSEGMENT_ID the SEGMENT_ID to set
     */
    public static void setSEGMENT_ID(long aSEGMENT_ID) {
        SEGMENT_ID = aSEGMENT_ID;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(long limit) {
        this.limit = limit;
    }

    /**
     * @param base the base to set
     */
    public void setBase(long base) {
        this.base = base;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param inserted the inserted to set
     */
    public void setInserted(boolean inserted) {
        this.inserted = inserted;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(long ID) {
        this.ID = ID;
    }

// ==============get section  ==========================//
    /**
     * @return the ID
     */
    public long getID() {
        return ID;
    }

    /**
     * @return the base
     */
    public long getBase() {
        return base;
    }

    /**
     * @return the inserted
     */
    public boolean isInserted() {
        return inserted;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the limit
     */
    public long getLimit() {
        return limit;
    }

// ==============copy section ==========================//
    public void copy_segment_without_id(Segment second) {
        base = second.getBase();
        limit = second.getLimit();
        inserted = second.isInserted();
    }

    public void copy_segment_with_id(Segment second) {
        base = second.getBase();
        limit = second.getLimit();
        setID(second.getID());
        inserted = second.isInserted();
    }

//===============print   ===========================//
    public void print() {
        System.out.print("[ID:" + getID() + ",Name:" + name + ",Base:" + base + ",Limit:" + limit + "]" + "\n");

    }

    public void print_free() {
        System.out.print("[" + "Name:" + name + ",Base:" + base + ",Limit:" + limit + "]" + "\n");

    }

}
