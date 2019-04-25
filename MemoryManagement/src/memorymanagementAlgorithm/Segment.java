package memorymanagementAlgorithm;

public class Segment {

    private static int SEGMENT_ID = 0;
    private int ID;
    private String name;
    private int base;
    private int limit;
    private boolean inserted = false;

// ==============constructors ==========================//
    public Segment(int base_, int limit_, String name_, boolean isPartofProcess) {
        if (isPartofProcess) {
            ID = SEGMENT_ID;
            SEGMENT_ID++;
        } else {
            ID = -1;
        }
        name = name_;
        base = base_;
        limit = limit_;
    }

    public Segment(int limit_, String name_, boolean isPartofProcess) {
        if (isPartofProcess) {
            ID = SEGMENT_ID;
            SEGMENT_ID++;
        } else {
            ID = -1;
        }
        name = name_;
        base = -1;
        limit = limit_;
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
    }

// ==============set section  ==========================//
    /**
     * @param limit the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * @param base the base to set
     */
    public void setBase(int base) {
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

// ==============get section  ==========================//
    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @return the base
     */
    public int getBase() {
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
    public int getLimit() {
        return limit;
    }

// ==============copy section ==========================//
    public void copy(Segment second) {
        base = second.getBase();
        limit = second.getLimit();
        ID = second.getID();
        name = second.getName();
        inserted = second.isInserted();
    }

//===============print   ===========================//
    public void print() {
        System.out.print("{ID: " + ID + " ,Name: " + name + " ,Base: " + base + " ,Limit: " + limit + "}" + "\n");
    }
}
