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

        if (head == null) {

            head = newNode;
            tail = newNode;

        } else if (head == tail) {
            if ((head.getPcb().getArrivalTime() < newNode.getPcb().getArrivalTime())) {
                enqueue(newPCB);
            } else if ((head.getPcb().getArrivalTime() > newNode.getPcb().getArrivalTime())) {
                newNode.setNext(head);
                head = newNode;
            } else {
                if (newNode.getPcb().getPriority() < head.getPcb().getPriority()) {
                    newNode.setNext(head);
                    head = newNode;
                } else {
                    enqueue(newPCB);
                }
            }
        } else {
            Node traverse_node = head;
            if (newNode.getPcb().getArrivalTime() < traverse_node.getPcb().getArrivalTime()) {
                newNode.setNext(head);
                head = newNode;
                /////////////////////
            } else if (newNode.getPcb().getArrivalTime() == traverse_node.getPcb().getArrivalTime()) {
                if (newNode.getPcb().getPriority() < traverse_node.getPcb().getPriority()) {
                    newNode.setNext(head);
                    head = newNode;
                }
                /////////////////////
            } else {
                while (traverse_node.getNext() != null) {
                    if (traverse_node.getNext().getPcb().getArrivalTime() >= newNode.getPcb().getArrivalTime()) {
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
        needsPriority.set_head(this.head);
        needsPriority.set_tail(this.tail);
    }

    public Queue Sort_Priotity(Queue Q) {

        if (Q.get_head() == Q.get_tail()) {
            return Q;
        } else {
            boolean Swapped = true;
            while (Swapped == true) {
                Swapped = false;
                Node traverse_Node = Q.get_head();
                while (traverse_Node.getNext() != null) {
                    if (traverse_Node.getPcb().getPriority() > traverse_Node.getNext().getPcb().getPriority()) {
                        Node temp = traverse_Node;
                        traverse_Node.setNext(traverse_Node.getNext().getNext());
                        temp.getNext().setNext(temp);
                        Swapped = true;
                    }
                }
            }
            return Q;
        }
    }

    public void Fix_Priority() {
        Node traverse_Node = needsPriority.get_head();
        while (traverse_Node.getNext() != null) {

            boolean Sort = false;
            Node Starting_Traverse_Node = traverse_Node;
            Node Inner_traverse = traverse_Node.getNext();
            Queue to_be_sorted_by_priority = new Queue();

            while (Inner_traverse.getNext() != null) {
                if ((Inner_traverse.getPcb().getArrivalTime() - traverse_Node.getPcb().getArrivalTime()) < traverse_Node.getPcb().getBurstTime()) {
                    traverse_Node = Inner_traverse;
                    to_be_sorted_by_priority.enqueue(Inner_traverse.getPcb());
                    Sort = true;
                }
            }
            if (Sort == true) {
                Queue temp = Sort_Priotity(to_be_sorted_by_priority);
                traverse_Node = temp.get_tail();
                Starting_Traverse_Node.setNext(temp.get_head());
            } else {
                traverse_Node = traverse_Node.getNext();
            }

        }
    }

    @Override
    public void DrawGanttChart(SchedulerSimulationController ctrl) {
    }

    @Override
    public double getAvgWaitingTime() {
        return 0;
    }

    @Override
    public double getAvgTurnarroundTime() {
        return 0;
    }

}
