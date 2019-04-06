package dataStructure;

import javafx.scene.paint.Color;

public class PCB {

    private static int currentPID = 0;

    private int pID;
    private Color color;
    private int burstTime;
    private int arrivalTime;
    private int priority;
    private int startofExec;
    private int endofExec;

    /**
     * Make a new PCB for a process. it set the burstTime = 0,arrivalTime =
     * 0,priority = 0, and assign a unique ID to the process , and a random
     * color.
     *
     * @param isNewProsess
     */
    public PCB(boolean isNewProsess) {
        if (isNewProsess) {
            pID = currentPID;
            currentPID++;
            color = randomColor();
            burstTime = 0;
            arrivalTime = 0;
            priority = 0;
            startofExec = -1;
            endofExec = -1;
        } else {
            pID = 0;
            color = Color.BLACK;
            burstTime = 0;
            arrivalTime = 0;
            priority = 0;
            startofExec = -1;
            endofExec = -1;
        }
    }

    /**
     * This method check if two PCBs are identical or not.
     *
     * @param pcb
     * @return boolean true if parameter PCB equal called PCB and false if not
     * equal.
     */
    public boolean equals(PCB pcb) {
        if (this.pID == pcb.getpID()
                && this.color.equals(pcb.getColor())
                && this.arrivalTime == pcb.getArrivalTime()
                && this.burstTime == pcb.getBurstTime()
                && this.priority == pcb.getPriority()) {
            return true;
        }
        return false;
    }

    /**
     * This method copy the PCB which given in the parameter to the current PCB.
     *
     * @param pcb
     */
    public void copy(PCB pcb) {
        this.pID = pcb.getpID();
        this.color = pcb.getColor();
        this.arrivalTime = pcb.getArrivalTime();
        this.burstTime = pcb.getBurstTime();
        this.priority = pcb.getPriority();
    }

    /**
     * for debugging only. it prints in the console window;
     */
    public void printPCB() {
        System.out.println("{PID: " + this.pID + " ,Color: " + this.color + " ,Arrival Time: " + this.arrivalTime + " ,Burst Time: " + this.burstTime + " ,Priority: " + this.priority + "}");
    }

    /**
     * Generate Random Color.
     */
    private Color randomColor() {
        Color newColor;
        newColor = Color.hsb(randomNo(0, 360), randomNo(0.6, 1), randomNo(0.5, 1));
        return newColor;
    }

    private double randomNo(double min, double max) {
        double rand = ((Math.random() * ((max - min) + 1)) + min);
        if (rand > max) {
            rand = max;
        }
        return rand;
    }

    /**
     * @return the pID
     */
    public int getpID() {
        return pID;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the burstTime
     */
    public int getBurstTime() {
        return burstTime;
    }

    /**
     * @param burstTime the burstTime to set
     */
    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    /**
     * @return the arrivalTime
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @param arrivalTime the arrivalTime to set
     */
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * This method sets the start point of pID sequence generator. WARNING:
     * Don't use this method until you are sure about what are you doing.
     *
     * @param aCurrentPID the currentPID to set
     */
    public static void setCurrentPID(int aCurrentPID) {
        currentPID = aCurrentPID;
    }

    /**
     * @return the startofExec
     */
    public int getStartofExec() {
        return startofExec;
    }

    /**
     * @param startofExec the startofExec to set
     */
    public void setStartofExec(int startofExec) {
        this.startofExec = startofExec;
    }

    /**
     * @return the endofExec
     */
    public int getEndofExec() {
        return endofExec;
    }

    /**
     * @param endofExec the endofExec to set
     */
    public void setEndofExec(int endofExec) {
        this.endofExec = endofExec;
    }

}
