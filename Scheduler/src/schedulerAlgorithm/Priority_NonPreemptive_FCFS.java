package schedulerAlgorithm;

import dataStructure.Node;
import dataStructure.PCB;
import dataStructure.Queue;

import scheduler.SchedulerSimulationController;

public class Priority_NonPreemptive_FCFS extends Queue implements ReadyQueue {

    private static Queue needsPriority = new Queue();

    @Override
    public void insert(PCB newPCB) {

        Node newNode = new Node(newPCB);

        if (getHead() == null) {

            setHead(newNode);
            setTail(newNode);

        } else if (getHead() == getTail()) {
            if ((getHead().getPcb().getArrivalTime() < newNode.getPcb().getArrivalTime())) {
                enqueue(newPCB);
            } else if ((getHead().getPcb().getArrivalTime() > newNode.getPcb().getArrivalTime())) {
                newNode.setNext(getHead());
                setHead(newNode);
            } else {
                if (newNode.getPcb().getPriority() < getHead().getPcb().getPriority()) {
                    newNode.setNext(getHead());
                    setHead(newNode);
                } else {
                    enqueue(newPCB);
                }
            }
        } else {
            Node traverse_node = getHead();
            if (newNode.getPcb().getArrivalTime() < traverse_node.getPcb().getArrivalTime()) {
                newNode.setNext(getHead());
                setHead(newNode);
                /////////////////////
            } else if (newNode.getPcb().getArrivalTime() == traverse_node.getPcb().getArrivalTime()) {
                if (newNode.getPcb().getPriority() < traverse_node.getPcb().getPriority()) {
                    newNode.setNext(getHead());
                    setHead(newNode);
                }
                /////////////////////
            } else {
                while (traverse_node.getNext() != null) {
                    if (traverse_node.getNext().getPcb().getArrivalTime() > newNode.getPcb().getArrivalTime()) {
                        newNode.setNext(traverse_node.getNext());
                        traverse_node.setNext(newNode);
                        break;
                    }
                    /*
                    if ((traverse_node.getPcb().getBurstTime() > (newNode.getPcb().getArrivalTime() - traverse_node.getPcb().getArrivalTime()))
                            && (traverse_node.getPcb().getBurstTime() > (traverse_node.getNext().getPcb().getArrivalTime() - traverse_node.getPcb().getArrivalTime()))) {
                        
                        if (newNode.getPcb().getPriority() < traverse_node.getNext().getPcb().getPriority()) {
                            newNode.setNext(traverse_node.getNext());
                            traverse_node.setNext(newNode);
                            break;
                        }
                    } else if ((traverse_node.getPcb().getBurstTime() > (newNode.getPcb().getArrivalTime() - traverse_node.getPcb().getArrivalTime()))
                            && (traverse_node.getPcb().getBurstTime() < (traverse_node.getNext().getPcb().getArrivalTime() - traverse_node.getPcb().getArrivalTime()))) {
                        newNode.setNext(traverse_node.getNext());
                        traverse_node.setNext(newNode);
                        break;
                    }*/
                    traverse_node = traverse_node.getNext();
                }
                if (traverse_node.getNext() == null) {
                    enqueue(newPCB);
                }
            }
        }
        printQueue();
        needsPriority.setHead(this.getHead());
        needsPriority.setTail(this.getTail());
    }

    public Queue Sort_Priotity(Queue Q) {
        System.out.println("SORT Called");
        if (Q.getHead() == Q.getTail()) {
            return Q;
        } else {
            boolean Swapped = true;
            while (Swapped == true) {
                Swapped = false;
                Node traverse_Node = Q.getHead();
                while (traverse_Node.getNext() != null) {
                    if (traverse_Node.getPcb().getPriority() > traverse_Node.getNext().getPcb().getPriority()) {
                        PCB temp = new PCB(false);
                        temp.copy(traverse_Node.getPcb());
                        traverse_Node.getPcb().copy(traverse_Node.getNext().getPcb());
                        traverse_Node.getNext().getPcb().copy(temp);
                        Swapped = true;
                    }
                }
            }
            return Q;
        }
    }

    public void Fix_Priority() {
        Node traverse_Node = needsPriority.getHead();
        while (traverse_Node.getNext() != null) {
            System.out.println("1While");
            boolean Sort = false;
            Node Starting_Traverse_Node = traverse_Node;
            Node Inner_traverse = traverse_Node.getNext();
            Queue to_be_sorted_by_priority = new Queue();

            while (Inner_traverse.getNext() != null) {
                System.out.println("2While");

                if ((Inner_traverse.getPcb().getArrivalTime() - traverse_Node.getPcb().getArrivalTime()) < traverse_Node.getPcb().getBurstTime()) {
                    traverse_Node = Inner_traverse;
                    to_be_sorted_by_priority.enqueue(Inner_traverse.getPcb());
                    Sort = true;
                }
                Inner_traverse = Inner_traverse.getNext();
            }
            if (Sort == true) {
                System.out.println("True Sort");
                Queue temp = Sort_Priotity(to_be_sorted_by_priority);
                traverse_Node = temp.getTail();
                Starting_Traverse_Node.setNext(temp.getHead());
            } else {
                traverse_Node = traverse_Node.getNext();
            }
            
        }
        needsPriority.printQueue();
        
    }

    @Override
    public void DrawGanttChart(SchedulerSimulationController ctrl) {
        Fix_Priority();
    }
}
