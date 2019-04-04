package dataStructure;

public class Node {

    private PCB pcb;
    private Node next;

    public Node(PCB newPCB) {
        this.pcb = newPCB;
        next = null;
    }

    /**
     * for debugging only. it prints in the console window;
     */
    public void printNode() {
        getPcb().printPCB();
    }

    /**
     * @return the next
     */
    public Node getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(Node next) {
        this.next = next;
    }

    /**
     * @return the pcb
     */
    public PCB getPcb() {
        return pcb;
    }
}
