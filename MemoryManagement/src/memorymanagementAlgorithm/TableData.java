package memorymanagementAlgorithm;

import javafx.scene.paint.Color;

public class TableData {

    private String ID;
    private String name;
    private String base;
    private String limit;
    private Color color;
    private Process process;

    /**
     * Constructor for process
     *
     * @param _ID
     * @param _color
     * @param _process
     */
    public TableData(String _ID, Color _color, Process _process) {
        ID = _ID;
        name = "";
        base = "";
        limit = "";
        color = _color;
        process = _process;
    }

    /**
     * Constructor for allocated segment
     *
     * @param _ID
     * @param _name
     * @param _base
     * @param _limit
     * @param _color
     */
    public TableData(String _ID, String _name, String _base, String _limit, Color _color) {
        ID = _ID;
        name = _name;
        base = _base;
        limit = _limit;
        color = _color;
        process = null;
    }

    /**
     * Constructor for waiting segments
     *
     * @param _ID
     * @param _name
     * @param _color
     */
    public TableData(String _ID, String _name, Color _color) {
        ID = _ID;
        name = _name;
        base = "";
        limit = "";
        color = _color;
        process = null;
    }

    /**
     * Constructor for free space
     *
     * @param _base
     * @param _limit
     */
    public TableData(String _base, String _limit) {
        ID = "";
        name = "";
        base = _base;
        limit = _limit;
        color = null;
        process = null;
    }

    /**
     * @return the ID
     */
    public String getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the base
     */
    public String getBase() {
        return base;
    }

    /**
     * @param base the base to set
     */
    public void setBase(String base) {
        this.base = base;
    }

    /**
     * @return the limit
     */
    public String getLimit() {
        return limit;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(String limit) {
        this.limit = limit;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the process
     */
    public Process getProcess() {
        return process;
    }

    /**
     * @param process the process to set
     */
    public void setProcess(Process process) {
        this.process = process;
    }

}
