package dataStructure;

public class Queue {

    private Node Head;
    private Node Tail;

    public Queue() {
        Head = null;
        Tail = null;
    }

    /**
     * enqueue a new node
     */
    public void enqueue(PCB newPCB) {
        // Create a new node with given PCB 
        Node newNode = new Node(newPCB);

        // If the Linked List is empty,then make the new node as head 
        if (Head == null) {
            Head = newNode;
            Tail = newNode;
        } else {
            newNode.setNext(Head);
            Head = newNode;
        }
    }

    /**
     * for debugging only. it prints in the console window;
     */
    public void printQueue() {
        Node currNode = Head;

        System.out.println("Queue: ");

        // Traverse through the LinkedList 
        while (currNode != null) {
            // Print the data at current node 
            currNode.printNode();

            // Go to next node 
            currNode = currNode.getNext();
        }
    }
}
