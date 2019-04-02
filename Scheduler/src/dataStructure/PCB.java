package dataStructure;

import javafx.scene.paint.Color;

public class PCB {

    private static int currentPID = 0;

    private int pID;
    private Color color;
    private int burstTime;
    private int arrivalTime;
    private int priority;

    /**
     * Make a new PCB for a process. it set the burstTime = 0,arrivalTime =
     * 0,priority = 0, and assign a unique ID to the process , and a random
     * color.
     */
    public PCB() {
        pID = currentPID;
        currentPID++;
        color = randomColor();
        burstTime = 0;
        arrivalTime = 0;
        priority = 0;
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

}
