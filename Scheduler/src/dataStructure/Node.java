package dataStructure;

public class Node {

    private PCB processPCB;
    private Node next;

    public Node(PCB newPCB) {
        this.processPCB = newPCB;
        next = null;
    }

    /**
     * for debugging only. it prints in the console window;
     */
    public void printNode() {
        processPCB.printPCB();
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
}
