package dataStructure;

public class Queue{

    protected Node head;
    protected Node tail;

    public Queue() {
        head = null;
        tail = null;
    }

    public void set_head(Node head){
        this.head = head;
    }
    public Node get_head(){
        return head;
    }
    public void set_tail(Node tail){
        this.tail = tail;
    }
    public Node get_tail(){
        return tail;
    }
    /**
     * enqueue a new node
     * @param newPCB
     */
    public void enqueue(PCB newPCB) {
        // Create a new node with given PCB 
        Node newNode = new Node(newPCB);

        // If the Linked List is empty,then make the new node as head 
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
    }

    /**
     * For debugging only. This method prints the queue in the console window.
     */
    public void printQueue() {
        Node currNode = head;

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
