package dataStructure;

public class Node {

    private PCB pcb;
    private Node next;

    public Node(){
        this.pcb = null;
        next = null;
    }
    
    public Node(PCB newPCB) {
        this.pcb = newPCB;
        next = null;
    }

    /**
     * This method check if two PCBs are identical or not.
     *
     * @param node
     * @return boolean true if parameter Node equal called Node and false if not
     * equal.
     */
    public boolean equals(Node node) {
        if (this.pcb.equals(node.getPcb()) && this.next == node.getNext()) {
            return true;
        }
        return false;
    }

    /**
     * This method copy the Node which given in the parameter to the current Node.
     *
     * @param node
     */
    public void copy(Node node) {
        this.pcb.copy(node.getPcb());
        this.next = node.getNext();
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
